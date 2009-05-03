package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.components.CommentListing;
import co.uk.techmeetup.data.Comment;
import co.uk.techmeetup.data.Video;
import org.apache.tapestry5.annotations.OnEvent;

public class VideoDetailsPage extends BasePage {

	@Property
	private long videoId;

	@Property
	private Comment thisComment;

	@Property
	private String newComment;

	@Persist
	private Video video;
	
	@InjectComponent
	private CommentListing commentListing;

	public void onActivate(long videoId) {
		this.videoId = videoId;
		video = (Video) getSession().load(Video.class, videoId);
		if (video == null) {
			throw new RuntimeException("Video with id " + videoId
					+ " does not exists");
		}
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	@OnEvent(value = "submit", component = "postComment")
	public Object commentSubmit(int videoId) {
		getSession().beginTransaction();
		Comment comment=new Comment();
		comment.setBody(newComment);
		comment.setOwner(getLoggedInUser());
		comment.setMeetup(video.getMeetup());
		comment.setCommentOn(video);
		getSession().save(comment);
		video.getComments().add(comment);
		getSession().getTransaction().commit();
		return commentListing;
	}

}
