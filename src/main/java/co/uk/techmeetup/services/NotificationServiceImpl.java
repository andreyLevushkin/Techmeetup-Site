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
public class NotificationServiceImpl implements NotificationService {

	public static final long WAIT_INTERVAL = 1000 * 5;
	public static final int SUBJECT_LENGTH = 60;

	private boolean run = true;
	private Session session;
	private NotifierThread notifier;
	private static volatile short count = 0;

	public NotificationServiceImpl(Session session) {
		this.session = session;
		if (run) {
			notifier = new NotifierThread();
			notifier.start();

		}
	}

	@Override
	public void startNotifications() {
		throw new UnsupportedOperationException();
		// TODO Write code to start the notifications
	}

	@Override
	public void stopNotifications() {
		throw new UnsupportedOperationException();
		// TODO Write code to stop the notifications
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

	private void sendNotifications(Question question, List<User> usersToNotify) {
		if (usersToNotify == null || usersToNotify.size() == 0) {
			return;
		}

		String subject = "[Techmeetup] " + question.getTitle();
		String tags = "";
		for (TagInstance tag : question.getTags()) {
			tags += tag.getTag().getTagString() + " ";
		}

		String replyUrl = "http://" + ControlVars.SERVER_HOST
				+ "/discussiondetailpage/" + question.getId();
		for (User userToNotify : usersToNotify) {
			Criteria criteria = session.createCriteria(Notification.class);
			criteria.add(Restrictions.eq("user", userToNotify));
			criteria.add(Restrictions.eq("entity", question));
			Notification notification = (Notification) criteria.uniqueResult();
			if (notification == null) {
				notification = createNewNotification(userToNotify, question,
						false);
			}

			String muteUrl = "http://" + ControlVars.SERVER_HOST + "/mute/"
					+ notification.getHash();

			SortedSet<Comment> comments = new TreeSet<Comment>(
					new DescendingDateComparator());
			session.refresh(question);
			comments.addAll(question.getComments());

			String message = String.format(ControlVars.NOTIFICATION_HEADER,
					muteUrl, replyUrl, replyUrl);
			for (Comment comment : comments) {
				String messageFragment = String.format(
						ControlVars.QUESTION_NOTIFICATION_TEMPLATE, comment
								.getOwner().getName(), comment.getBody());
				message = message.concat(messageFragment);
			}

			message += String.format(
					ControlVars.QUESTION_NOTIFICATION_TEMPLATE, question
							.getOwner().getName(), question.getBody());
			message += String.format(ControlVars.NOTIFICATION_FOOTER, muteUrl,
					replyUrl);
			sendEmail(message, subject, userToNotify);
		}

	}

	private boolean sendEmail(String message, String subject, User user) {
		System.out.print("-------------NOTIFICATION-----------------\n"
				+ user.getName() + " :" + subject + "\n" + message
				+ "\n------------------------END----------------------\n");
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
		} else {
			session.refresh(notification);
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
		// TODO Implement unMute
		throw new UnsupportedOperationException();
	}

	@Override
	public Notification mute(String hash) {
		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.eq("hash", hash));
		Notification notification = (Notification) criteria.uniqueResult();
		if (notification != null) {
			Transaction tx = session.beginTransaction();
			notification.setBlock(true);
			tx.commit();
		}
		return notification;
	}

	@Override
	public void unMute(User user, String hash) {
		// TODO Implement unMute
		throw new UnsupportedOperationException();
	}

	private class NotifierThread extends Thread {

		private boolean run = true;
		private Date lastChecked = new Date();

		public void run() {
			this.setPriority(MIN_PRIORITY);
			count++;
			while (run) {
				Set<Question> toBeNotified = new HashSet<Question>();
				System.out.print("Checking for new stuff since " + lastChecked);

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

				lastChecked = new Date();

				/* Send notifications */
				for (Question question : toBeNotified) {
					System.out.println("Notifying " + question.getId() + " "
							+ question.getTitle());
					List<User> usersToBeNotified = getUsers(question);
					sendNotifications(question, usersToBeNotified);
				}

				try {
					Thread.sleep(WAIT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		/**
		 * @return the run
		 */
		public boolean isRun() {
			return run;
		}

		/**
		 * @param run
		 *            the run to set
		 */
		public void setRun(boolean run) {
			this.run = run;
		}

		/**
		 * @return the lastChecked
		 */
		public Date getLastChecked() {
			return lastChecked;
		}

		/**
		 * @param lastChecked
		 *            the lastChecked to set
		 */
		public void setLastChecked(Date lastChecked) {
			this.lastChecked = lastChecked;
		}

	}
}
