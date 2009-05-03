package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Transaction;

import co.uk.techmeetup.data.BlogPost;
import co.uk.techmeetup.data.Comment;

public class ArticleDetailsPage extends BasePage {

	@Property
	@Persist
	private BlogPost article;

	@Property
	private Comment comment;

	@Property
	private int commentIndex;

	@Property
	private String newComment;

	@InjectPage
	private Index index;

	public Object onActivate(long questionId) {
		try {
			article = (BlogPost) getSession().load(BlogPost.class, questionId);
		} catch (ObjectNotFoundException ex) {
			return index;
		}
		return null;

	}

	public String getCommentClass() {
		if (commentIndex % 2 == 0) {
			return "dark";
		} else {
			return "";
		}
	}

	@OnEvent(component = "newCommentForm", value = "submit")
	public void onCommentForm() {
		Comment comment = new Comment();
		comment.setOwner(getLoggedInUser());
		comment.setBody(newComment);
		comment.setMeetup(article.getMeetup());
		comment.setCommentOn(article);
		Transaction tx = getSession().beginTransaction();
		getSession().persist(comment);
		article.getComments().add(comment);
		tx.commit();

	}
}
