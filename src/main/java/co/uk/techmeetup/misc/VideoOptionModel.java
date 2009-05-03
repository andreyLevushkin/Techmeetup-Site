package co.uk.techmeetup.misc;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.OptionModel;

import co.uk.techmeetup.data.Video;

public class VideoOptionModel implements OptionModel, Comparable {

	private final Video video;

	public VideoOptionModel(Video video) {
		this.video = video;
	}

	@Override
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>();
	}

	@Override
	public String getLabel() {
		return video.getName();
	}

	@Override
	public Object getValue() {
		return video;
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
