package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.ObjectNotFoundException;

import co.uk.techmeetup.data.Comment;
import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.services.InputSanitizer;

public class DiscussionDetailPage extends BasePage {

	@Persist
	private Question question;

	@Property
	private TagInstance tag;

	@Property
	private Comment comment;

	@Property
	private String newCommentBody;

	@Property
	private int commentIndex;

	@InjectPage
	private DiscussionPage discussionPage;

	@Inject
	private InputSanitizer inputSanitizer;

	@OnEvent(value = "submit", component = "newCommentForm")
	public void postComment() {
		if (isLoggedIn()) {
			//newCommentBody = inputSanitizer.sanitizeWithBreaks(newCommentBody);
			Comment newComment = new Comment();
			newComment.setOwner(getLoggedInUser());
			newComment.setMeetup(question.getMeetup());
			newComment.setBody(newCommentBody);
			if (!getSession().contains(question)) {
				question = (Question) getSession().load(Question.class,
						question.getId());
				// TODO This is a hack that should not be needed - not sure why
				// it does not work without this
			}

			newComment.setCommentOn(question);
			getSession().beginTransaction();
			getSession().persist(newComment);
			question.getComments().add(newComment);
			getSession().getTransaction().commit();

		}
	}

	public Object onActivate(long questionId) {
		try {
			question = (Question) getSession().load(Question.class, questionId);
		} catch (ObjectNotFoundException ex) {
			return discussionPage;
		}
		return null;
	}

	public int getCommentCount() {
		int count = question.getComments().size();
		return count;
	}

	public Question getQuestion() {
		return question;
	}

	public String getCommentClass() {
		if (commentIndex % 2 == 0) {
			return "dark";
		} else {
			return "";
		}
	}
}
