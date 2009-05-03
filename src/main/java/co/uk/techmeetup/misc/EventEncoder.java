package co.uk.techmeetup.misc;

import org.apache.tapestry5.ValueEncoder;
import org.hibernate.Session;

import co.uk.techmeetup.data.Event;

public class EventEncoder implements ValueEncoder<Event> {

	private Session session;

	public EventEncoder(Session session) {
		this.session = session;
	}

	@Override
	public String toClient(Event entity) {
		return String.valueOf(entity.getId());

	}

	@Override
	public Event toValue(String arg0) {
		return (Event) session.load(Event.class, Long.parseLong(arg0));
	}

}
