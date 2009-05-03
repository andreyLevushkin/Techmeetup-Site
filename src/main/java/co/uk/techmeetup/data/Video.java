package co.uk.techmeetup.data;

import java.util.Date;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;


@Indexed
public class Video extends TmuEntity {

	private long vimeoId;
	@Field
	private String name;
	@Field
	private String description;
	@Field
	private String speakerDescription;
	@IndexedEmbedded
	private User speaker;
	private Image thumbnail;
	
	
	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSpeakerDescription() {
		return speakerDescription;
	}

	public void setSpeakerDescription(String speakerDescription) {
		this.speakerDescription = speakerDescription;
	}

	public long getVimeoId() {
		return vimeoId;
	}

	public void setVimeoId(long vimeoId) {
		this.vimeoId = vimeoId;
	}

	public User getSpeaker() {
		return speaker;
	}

	public void setSpeaker(User speaker) {
		this.speaker = speaker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
