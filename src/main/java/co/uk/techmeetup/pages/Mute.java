package co.uk.techmeetup.pages;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Notification;
import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.services.NotificationService;

public class Mute extends BasePage {

	@Inject
	private Request request;

	@Inject
	private NotificationService notificationService;

	@InjectPage
	private Index index;

	private Notification notification;

	@Property
	private String referer;

	public Object onActivate(String notificationHash)
			throws MalformedURLException {
		notification = notificationService.mute(notificationHash);
		referer = request.getHeader("Referer");
		return null;
	}

	public Object onActivate() {
		return null;
	}

	public String getUsername() {
		if (isMuted()) {
			return notification.getUser().getName();
		} else {
			return "";
		}
	}

	public String getTitle() {
		if (isMuted()) {
			TmuEntity entity = notification.getEntity();
			// TODO make sure this deals with things other then questions
			return ((Question) getSession()
					.load(Question.class, entity.getId())).getTitle();
		} else {
			return "";
		}
	}

	public boolean isMuted() {
		return notification != null;
	}

	public boolean getHasReferer() {
		return referer != null;
	}
}
