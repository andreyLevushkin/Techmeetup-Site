package co.uk.techmeetup.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.WeightedString;

public class TagServiceImpl implements TagService {

	private Session session;
	private TagCache tagCache;

	public TagServiceImpl(Session session, TagCache tagCache) {
		this.session = session;
		this.tagCache = tagCache;
	}

	@Override
	public Tag findOrCreateTag(String tagName) {
		if (tagName == null || tagName.trim().length() == 0) {
			return null;
		}else{
			tagName=tagName.trim().toLowerCase();
		}
		Tag tag = (Tag) session.createQuery(
				"from Tag as tag where tag.tagString= ?").setString(0, tagName)
				.uniqueResult();
		if (tag == null) {
			tag = new Tag();
			tag.setTagString(tagName);

			boolean hadTransaction = true;
			if (session.getTransaction() == null) {
				session.beginTransaction();
				hadTransaction = false;
			}
			session.save(tag);
			if (!hadTransaction) {
				session.getTransaction().commit();
			}
		}
		return tag;
	}

	@Override
	public Set<Tag> findOrCreateTags(String tagNames) {
		Set<Tag> out = new HashSet<Tag>();
		if (tagNames != null && tagNames.length() > 0) {
			for (String tag : tagNames.replace(",", " ").split(" ")) {
				if (tag.trim().length() != 0) {
					out.add(findOrCreateTag(tag));
				}
			}
		}
		return out;
	}

	@Override
	public Collection<WeightedString> getPopularTags(Class type) {
		SortedSet<WeightedString> out = tagCache.getPopularTags(type);
		if (out == null) {
			Criteria criteria = session.createCriteria(type);
			criteria.setMaxResults(MAX_RESULTS);

			if (type == User.class) {
				criteria.addOrder(Order.desc("joined"));
				List<User> users = criteria.list();
				out = new TreeSet<WeightedString>();
				out.addAll(getUserTags(users));
			} else {
				criteria.addOrder(Order.desc("created"));
				List<TmuEntity> entities = criteria.list();
				out = new TreeSet<WeightedString>();
				out.addAll(getEntityTags(entities));
			}

			while (out.size() > MAX_TAGS) {
				out.remove(out.last());
			}

			tagCache.setPopularTags(type, out);
		}
		return out;
	}

	@Override
	public Set<TagInstance> tag(Set<Tag> tags, User tagger, TmuEntity entity) {
		boolean hadTransaction = false;

		Transaction transaction = session.getTransaction();
		if (transaction == null) {
			transaction = session.beginTransaction();
		} else {
			hadTransaction = true;
		}

		Set<TagInstance> tagSet = new HashSet<TagInstance>();
		for (Tag tag : tags) {
			TagInstance thisTag = new TagInstance();
			thisTag.setTagged(entity);
			thisTag.setTagger(tagger);
			thisTag.setTag(tag);
			session.persist(thisTag);
			tagSet.add(thisTag);
		}
		if (!hadTransaction) {
			session.getTransaction().commit();
		}
		return tagSet;
	}

	@Override
	public Collection<WeightedString> getUserTags(Collection<User> users) {
		Map<String, WeightedString> tags = new HashMap<String, WeightedString>();
		for (User user : users) {

			for (Tag tag : user.getInterestTags()) {
				String tagString = tag.getTagString();
				if (tags.containsKey(tagString)) {
					WeightedString oldTag = tags.get(tagString);
					oldTag.setWeight(oldTag.getWeight() + 1);
				} else {
					tags.put(tagString, new WeightedString(tagString, 1));
				}
			}
			for (Tag tag : user.getExpertTags()) {
				String tagString = tag.getTagString();
				if (tags.containsKey(tagString)) {
					WeightedString oldTag = tags.get(tagString);
					oldTag.setWeight(oldTag.getWeight() + 1);
				} else {
					tags.put(tagString, new WeightedString(tagString, 1));
				}
			}
		}
		return (Collection<WeightedString>) tags.values();
	}

	@Override
	public Collection<WeightedString> getEntityTags(
			Collection<TmuEntity> entities) {
		Map<String, WeightedString> tags = new HashMap<String, WeightedString>();

		for (TmuEntity entity : entities) {
			for (TagInstance tag : entity.getTags()) {
				String tagString = tag.getTag().getTagString();
				if (tags.containsKey(tagString)) {
					WeightedString oldTag = tags.get(tagString);
					oldTag.setWeight(oldTag.getWeight() + 1);
				} else {
					tags.put(tagString, new WeightedString(tagString, 1));
				}
			}
		}
		return (Collection<WeightedString>) tags.values();
	}

	@Override
	public String[] autocompleTags(String input) {
		if(input.endsWith(" ")||input.endsWith(",")){
			return new String[0];
		}
		String[] split = input.replace(",", " ").split(" ");
		String thisTag = split[split.length - 1];
		String prefix = input.substring(0, input.length() - thisTag.length());
		
		Criteria criteria = session.createCriteria(Tag.class);
		criteria.add(Restrictions.like("tagString", thisTag + "%").ignoreCase());
		List<Tag> tags = criteria.list();
		
		List<String> out = new ArrayList<String>();
		for (int i = 0; i < tags.size(); i++) {
			String thisTagString = tags.get(i).getTagString();
			if (!input.contains(thisTagString)) {
				out.add(prefix + thisTagString);
			}
		}
		return out.toArray(new String[0]);
	}

}
