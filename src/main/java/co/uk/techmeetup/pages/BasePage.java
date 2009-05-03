package co.uk.techmeetup.pages;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.uk.techmeetup.components.ExternalUserProfile;
import co.uk.techmeetup.components.Layout;
import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.UserSession;
import co.uk.techmeetup.services.ContentService;
import co.uk.techmeetup.services.TagService;

public abstract class BasePage {

	@Inject
	private HibernateSessionManager sessionManager;

	@Inject
	private ContentService contentService;

	@Inject
	private TagService tagService;
	
	@ApplicationState
	private UserSession userSession;

	private boolean userSessionExists;

	private Session session;

	public boolean getUserSessionExists() {
		return userSessionExists;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public boolean isLoggedIn() {
		if (userSessionExists && userSession.getUserId() != null) {
			return true;
		} else {
			return false;
		}
	}

	public User getLoggedInUser() {
		if (isLoggedIn()) {
			return (User) getSession().load(User.class,
					getUserSession().getUserId());
		} else {
			return null;
		}
	}

	public Session getSession() {
		if (session == null) {
			session = sessionManager.getSession();
		}
		return session;
	}

	public ContentService getContentService() {
		return contentService;
	}

	public Set<TagInstance> tag(Set<Tag> tags, User tagger, TmuEntity entity) {
		return tagService.tag(tags, tagger, entity);
	}

	public User findByName(String name) {
		return (User) getSession().createQuery(
				"from User as user where user.name = ?").setString(0, name)
				.uniqueResult();
	}

	public Tag findOrCreateTag(String tagName) {
		return tagService.findOrCreateTag(tagName);
	}

	public Set<Tag> findOrCreateTags(String tagNames) {
		return tagService.findOrCreateTags(tagNames);
	}
	
	public TagService getTagService(){
		return tagService;
	}

	public Image getDefaultProfileImage() {
		Image defaultProfileImage = (Image) getSession().createQuery(
				"From Image as image where image.url = ? ").setString(0,
				ControlVars.DEFAULT_PROFILE_IMAGE_URL).uniqueResult();
		if (defaultProfileImage == null) {
			defaultProfileImage = new Image();
			defaultProfileImage.setUrl(ControlVars.DEFAULT_PROFILE_IMAGE_URL);
			boolean hasTransaction = true;
			getSession().save(defaultProfileImage);
		}
		return defaultProfileImage;
	}
	
	public Format getStringFormat(){
		return  new StringFormat();
	}
	
	class StringFormat extends Format{

		private static final long serialVersionUID = 1L;

		@Override
		public StringBuffer format(Object arg0, StringBuffer appendTo,
				FieldPosition position) {
			String in=(String) arg0;
			appendTo.append(in);
			return appendTo;
		}

		@Override
		public Object parseObject(String arg0, ParsePosition arg1) {
			return null;
		}
		
	}
}
