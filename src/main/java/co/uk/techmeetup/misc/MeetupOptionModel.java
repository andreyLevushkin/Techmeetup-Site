package co.uk.techmeetup.misc;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.OptionModel;

import co.uk.techmeetup.data.Meetup;


public class MeetupOptionModel implements OptionModel, Comparable {

	private final Meetup meetup;

	public MeetupOptionModel(Meetup video) {
		this.meetup = video;
	}

	@Override
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>();
	}

	@Override
	public String getLabel() {
		return meetup.getName();
	}

	@Override
	public Object getValue() {
		return meetup;
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public int compareTo(Object arg0) {
		return this.compareTo(arg0);
	}

}










