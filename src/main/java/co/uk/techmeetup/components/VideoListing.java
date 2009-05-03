package co.uk.techmeetup.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.Video;

/**
 * Renders a list of videos provided using the {@link #videos} parameter.
 * 
 * @author andrey
 * 
 */
public class VideoListing extends BaseComponent {

	public static final String RIGHT = "float_right";
	public static final String LEFT = "float_left";

	@Parameter(required = true)
	private List<Video> videos;

	@SuppressWarnings("unused")
	@Property
	private Video thisVideo;

	@Property
	private int loopIndex;

	/**
	 * Returns the List of {@link Video}s that will be rendered.
	 * 
	 * @return
	 */
	public List<Video> getVideoList() {
		return videos;
	}

	/**
	 * Returns the CSS class to apply to the video currently being rendered.
	 * 
	 * @return
	 */
	public String getVideoClass() {
		if (loopIndex % 2 == 0) {
			return RIGHT;
		} else {
			return LEFT;
		}
	}
}
