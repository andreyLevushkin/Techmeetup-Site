package co.uk.techmeetup.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Comment;
import co.uk.techmeetup.data.Notification;
import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.DescendingDateComparator;

@EagerLoad
public class NotificationServiceImpl extends Thread implements
		NotificationService {

	public static final long WAIT_INTERVAL = 1000 * 30;
	public static final int SUBJECT_LENGTH = 60;

	private boolean run = true;
	private Session session;
	private Date lastChecked;

	public NotificationServiceImpl(Session session) {
		this.session = session;
		lastChecked = new Date();
		start();
	}

	@Override
	public void startNotifications() {
		this.run = true;
	}

	@Override
	public void stopNotifications() {
		this.run = false;
	}

	@Override
	public void run() {
		this.setPriority(MIN_PRIORITY);

		while (true) {
			while (run) {
				Set<Question> toBeNotified = new HashSet<Question>();

				/* Get new questions */
				Criteria criteria = session.createCriteria(Question.class);
				criteria.add(Restrictions.gt("created", lastChecked));
				toBeNotified.addAll(criteria.list());

				/* Get new Comments */
				criteria = session.createCriteria(Comment.class);
				criteria.add(Restrictions.gt("created", lastChecked));

				/* Extract the origianl question from the comments */
				for (Comment comment : (List<Comment>) criteria.list()) {
					toBeNotified.add((Question) session.load(Question.class,
							comment.getCommentOn().getId()));
				}

				/* Send notifications */
				for (Question question : toBeNotified) {
					List<User> usersToBeNotified = getUsers(question);
					sendNotifications(question, usersToBeNotified);
				}

				lastChecked = new Date();

				try {
					Thread.sleep(WAIT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(WAIT_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastChecked = new Date();
		}
	}

	private List<User> getUsers(Question question) {
		List<User> out = new ArrayList<User>();
		Criteria criteria = session.createCriteria(User.class);

		for (User user : (List<User>) criteria.list()) {
			if (!isMuted(user, question)) {
				out.add(user);
			}
		}
		return out;
	}

	private void sendNotifications(Question question, List<User> users) {
		if (users == null || users.size() == 0) {
			return;
		}

		String subject = "[Techmeetup] " + question.getTitle();
		String tags = "";
		for (TagInstance tag : question.getTags()) {
			tags += tag.getTag().getTagString() + " ";
		}

		String replyUrl = "http://" + ControlVars.SERVER_HOST
				+ "/discussiondetailpage/" + question.getId();
		for (User user : users) {
			Criteria criteria = session.createCriteria(Notification.class);
			criteria.add(Restrictions.eq("user", user));
			criteria.add(Restrictions.eq("entity", question));
			Notification notification = (Notification) criteria.uniqueResult();
			if (notification == null) {
				notification = createNewNotification(user, question, false);
			}

			String muteUrl = "http://" + ControlVars.SERVER_HOST + "/mute/"
					+ notification.getHash();

			SortedSet<Comment> comments = new TreeSet<Comment>(
					new DescendingDateComparator());
			comments.addAll(question.getComments());

			String message = String.format(ControlVars.NOTIFICATION_HEADER,
					muteUrl, replyUrl);
			for (Comment comment : comments) {
				message += String.format(
						ControlVars.QUESTION_NOTIFICATION_TEMPLATE, comment
								.getOwner().getName(), comment.getBody());
			}

			message += String.format(
					ControlVars.QUESTION_NOTIFICATION_TEMPLATE, user.getName(),
					question.getBody());
			message += String.format(ControlVars.NOTIFICATION_FOOTER, muteUrl,
					replyUrl);
			sendEmail(message, subject, user);
		}

	}

	private boolean sendEmail(String message, String subject, User user) {
		System.out.print(message);
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(ControlVars.SMTP_HOST);
			email.setFrom("noreply@techmeetup.co.uk", "Techmeetup");
			email.addTo(user.getEmail(), user.getName());
			email.setSubject(subject);
			email.setHtmlMsg(message);
			email.send();
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Notification createNewNotification(User user, TmuEntity entity,
			boolean muted) {
		Notification notification = new Notification();
		notification.setEntity(entity);
		notification.setUser(user);
		session.save(notification);
		return notification;
	}

	@Override
	public boolean isMuted(User user, TmuEntity entity) {
		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("entity", entity));
		Notification notification = (Notification) criteria.uniqueResult();
		if (notification == null) {
			notification = createNewNotification(user, entity, false);
		}
		return notification.isBlock();
	}

	@Override
	public void mute(User user, TmuEntity entity) {
		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("entity", entity));
		Notification notification = (Notification) criteria.uniqueResult();
		if (notification != null) {
			Transaction tx = session.beginTransaction();
			notification.setBlock(true);
			tx.commit();
		} else {
			createNewNotification(user, entity, true);
		}
	}

	@Override
	public void unMute(User user, TmuEntity entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void mute(String hash) {
		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.eq("hash", hash));
		Notification notification = (Notification) criteria.uniqueResult();
		if (notification != null) {
			Transaction tx = session.beginTransaction();
			notification.setBlock(true);
			tx.commit();
		}
	}

	@Override
	public void unMute(User user, String hash) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
