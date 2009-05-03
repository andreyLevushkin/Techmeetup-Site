package co.uk.techmeetup.data;

import org.hibernate.search.annotations.Indexed;

@Indexed
public class Comment extends TmuEntity {

	private TmuEntity commentOn;
	private String body;
	private boolean removed;

	public TmuEntity getCommentOn() {
		return commentOn;
	}

	public void setCommentOn(TmuEntity commentOn) {
		this.commentOn = commentOn;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public int compareTo(Object o) {
		if (this.equals(o)) {
			return 0;
		}
		Comment otherComment = (Comment) o;
		return getCreated().compareTo(otherComment.getCreated());
	}

}
