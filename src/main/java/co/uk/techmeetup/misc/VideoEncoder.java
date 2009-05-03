package co.uk.techmeetup.misc;

import org.apache.tapestry5.ValueEncoder;
import org.hibernate.Session;

import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.Video;

public class VideoEncoder implements ValueEncoder<Video> {

	private Session session;

	public VideoEncoder(Session session) {
		this.session = session;
	}

	@Override
	public String toClient(Video entity) {
		return String.valueOf(entity.getId());
	}

	@Override
	public Video toValue(String arg0) {
		return (Video) session
				.load(Video.class, Long.parseLong(arg0));

	}

}
