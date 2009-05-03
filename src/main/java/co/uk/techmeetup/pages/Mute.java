package co.uk.techmeetup.pages;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Notification;
import co.uk.techmeetup.services.NotificationService;

public class Mute extends BasePage {

	@Inject
	private Request request;

	@Inject
	private NotificationService notificationService;
	
	@InjectPage
	private Index index;

	public Object onActivate(String notificationHash)
			throws MalformedURLException {
		notificationService.mute(notificationHash);
		String referer = request.getHeader("Referer");
		if (referer != null) {
			URL cameFrom = new URL(referer);
			return cameFrom;
		}
		return index;
	}

	public Object onActivate() {
		return index;
	}
}
