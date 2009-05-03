package co.uk.techmeetup.services;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.WeightedString;


public interface TagService {
	public static final int MAX_RESULTS = 150;
	public static final int MAX_TAGS = 20;
	
	public Set<TagInstance> tag(Set<Tag> tags, User tagger, TmuEntity entity) ;
	
	public Collection<WeightedString> getPopularTags(Class type);
	
	public Tag findOrCreateTag(String tagName);
	
	public Set<Tag> findOrCreateTags(String tagNames);
	
	public Collection<WeightedString> getUserTags(Collection<User> users) ;
	
	public Collection<WeightedString> getEntityTags(Collection<TmuEntity> entities) ;
	
	public String[] autocompleTags(String input);
}
