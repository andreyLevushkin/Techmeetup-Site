package co.uk.techmeetup.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.TagInstance;

/**
 * Renders the {@link Event}s provided to it in the {@link #events} parameter.
 * 
 * @author andrey
 * 
 */
public class EventListing extends BaseComponent {

	@Parameter(required = true)
	private List<Event> events;

	@Property
	private Event event;

	@Property
	private int loopIndex;

	@SuppressWarnings("unused")
	@Property
	private TagInstance thisTag;

	/**
	 * Returns the list of {@link Event}s to be rendered.
	 * 
	 * @return
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * The CSS class to be applied to the event currently being rendered. Allows
	 * alternating CSS classes to be applied to the list of events.
	 * 
	 * @return The CSS class to be applied to the event currently being
	 *         rendered.
	 */
	public String getQuestionClass() {
		if (loopIndex % 2 == 0) {
			return "dark";
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @return The number of comments on the event currently being rendered.
	 */
	public int getCommentCount() {
		return event.getComments().size();
	}
}
