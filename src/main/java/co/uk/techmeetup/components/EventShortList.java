package co.uk.techmeetup.components;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.Meetup;

/**
 * Renders upcoming events for the the specified {@link Meetup} or for all
 * meetups if the {@link #meetup} parameter is null.
 * 
 * @author andrey
 * 
 */
public class EventShortList extends BaseComponent {

	/**
	 * The number of {@link Event}s to render.
	 */
	public static final int MAX_SHORT_EVENT_RESULTS = 4;

	@Parameter
	private Meetup meetup;

	@SuppressWarnings("unused")
	@Property
	private Event event;

	@Property
	private int loopIndex;

	/**
	 * Returns the {@link Event}s that are to be rendered.
	 * @return List of events to be rendered
	 */
	@SuppressWarnings("unchecked")
	public List<Event> getEvents() {
		Criteria criteria = getSession().createCriteria(Event.class);
		criteria.add(Restrictions.gt("date", new Date()));
		if (meetup != null) {
			criteria.add(Restrictions.eq("meetup", meetup));
		}
		criteria.setMaxResults(MAX_SHORT_EVENT_RESULTS);
		criteria.addOrder(Order.asc("date"));
		return criteria.list();
	}

	/**
	 * The css class of the event currently being rendered. 
	 * @return The css class of the event currently being rendered. 
	 */
	public String getEventClass() {
		if (loopIndex % 2 == 0) {
			return "dark";
		} else {
			return "";
		}
	}
}
