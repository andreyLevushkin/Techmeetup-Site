package co.uk.techmeetup.components;

import java.util.SortedSet;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.Comment;

/**
 * This component renders the {@link Comment}s provided to it. It requeres a
 * {@link Set} of comments as a parameter.
 * 
 * @author andrey
 * 
 */
public class CommentListing extends BaseComponent {

	@Parameter(required = true)
	private SortedSet<Comment> comments;

	@SuppressWarnings("unused")
	@Property
	private Comment thisComment;

	/**
	 * Returns the comments to be rendered
	 * @return
	 */
	public SortedSet<Comment> getComments() {
		return comments;
	}

}
