package co.uk.techmeetup.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.BlogPost;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.Video;

/**
 * This component is responsible for rendereing the content on the home of the
 * site. It requires the {@link #listedEntities} parameter to specified pointing
 * to the List of blogs and videos to be displayed on the front page.
 * 
 * @author andrey
 * 
 */
public class FrontPageListing extends BaseComponent {

	public static final int BLOG = 1;
	public static final int VIDEO = 2;

	@Parameter(required = true)
	private List<TmuEntity> listedEntities;

	@Property
	private TmuEntity thisEntity;

	@SuppressWarnings("unused")
	@Property
	private int loopIndex;

	@SuppressWarnings("unused")
	@Property
	private TagInstance thisTag;

	/**
	 * Returns a list of {@link BlogPost}s and {@link Video}s to be rendered.
	 * 
	 * @return
	 */
	public List<TmuEntity> getListedEntities() {
		return listedEntities;
	}

	/**
	 * Returns the number of comments for the entity currently being rendered.
	 * 
	 * @return
	 */
	public int getCommentCount() {
		return thisEntity.getComments().size();
	}

	/**
	 * 
	 * @return true if entity being rendered is a {@link BlogPost}
	 */
	public boolean getIsBlog() {
		if (thisEntity.getClass() == BlogPost.class) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return true if entity being rendered is a {@link Video}
	 */
	public boolean getIsVideo() {
		if (thisEntity.getClass() == Video.class) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the {@link TmuEntity} being rendered cast to a {@link BlogPost}.
	 * 
	 * @return
	 */
	public BlogPost getBlogPost() {
		return (BlogPost) thisEntity;
	}

	/**
	 * Returns the {@link TmuEntity} being rendered cast to a {@link Video}.
	 * 
	 * @return
	 */
	public Video getVideo() {
		return (Video) thisEntity;
	}

}
