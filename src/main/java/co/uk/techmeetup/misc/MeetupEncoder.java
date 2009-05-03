package co.uk.techmeetup.misc;

import org.apache.tapestry5.ValueEncoder;
import org.hibernate.Session;

import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.Video;

public class MeetupEncoder implements ValueEncoder<Meetup> {

	private Session session;

	public MeetupEncoder(Session session) {
		this.session = session;
	}

	@Override
	public String toClient(Meetup entity) {
		return String.valueOf(entity.getId());
	}

	@Override
	public Meetup toValue(String arg0) {
		return (Meetup) session
				.load(Meetup.class, Integer.parseInt(arg0));

	}

}
