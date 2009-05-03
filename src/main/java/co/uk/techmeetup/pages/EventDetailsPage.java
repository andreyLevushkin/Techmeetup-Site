package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.Event;

public class EventDetailsPage extends BasePage {

	@Property
	private Event event;
	
	public void onActivate(long questionId) {
		event= (Event) getSession().load(Event.class, questionId);

		if (event == null) {
			throw new RuntimeException("Event with id " + questionId
					+ " does not exists");
		}
	}
	
}
