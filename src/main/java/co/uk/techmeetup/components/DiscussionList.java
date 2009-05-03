package co.uk.techmeetup.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.TagInstance;

/**
 * Renders the {@link List} of {@link Question}s provided to it trough the
 * {@link #questions} parameter. The {@link #userSearch} parameter should be
 * true if the current search is based on a user name (ie. searching for all
 * questions by a single user).
 * 
 * @author andrey
 * 
 */
public class DiscussionList extends BaseComponent {

	@Parameter(required = true)
	private List<Question> questions;

	@Parameter(required = false)
	private boolean userSearch;

	@Property
	private Question thisQuestion;

	@Property
	private int loopIndex;

	@SuppressWarnings("unused")
	@Property
	private TagInstance thisTag;

	/**
	 * Returns the questions to be rendred.
	 * 
	 * @return A list of questions to be rendred.
	 */
	public List<Question> getQuestionList() {
		return questions;
	}

	/**
	 * Returns the CSS class that should be apllied to the question that is
	 * currently being rendred.
	 * 
	 * @return CSS class to be applied
	 */
	public String getQuestionClass() {
		if (loopIndex % 2 == 0) {
			return "dark";
		} else {
			return "";
		}
	}

	/**
	 * Returns the total number of comments for the question currently being
	 * rendered.
	 * 
	 * @return
	 */
	public int getCommentCount() {
		return thisQuestion.getComments().size();
	}

	/**
	 * 
	 * @return Returns true if the size of the provided questions List is 0.
	 */
	public boolean getHasNoResults() {
		return questions.size() == 0 && !isEmptyUserSearch();
	}

	/**
	 * 
	 * @return Returns true if {@link #userSearch} is true and the size of the
	 *         provided question list is 0.
	 */
	public boolean isEmptyUserSearch() {
		return userSearch && questions.size() == 0;
	}
}
