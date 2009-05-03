package co.uk.techmeetup.misc;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.OptionModel;

import co.uk.techmeetup.data.Event;

public class EventOptionModel implements OptionModel,
		Comparable<EventOptionModel> {

	private final Event event;

	public EventOptionModel(Event event) {
		this.event = event;
	}

	@Override
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>();
	}

	@Override
	public String getLabel() {
		return event.getTitle();
	}

	@Override
	public Object getValue() {
		return event;
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public int compareTo(EventOptionModel arg0) {
		return event.compareTo(arg0.event);
	}

}
