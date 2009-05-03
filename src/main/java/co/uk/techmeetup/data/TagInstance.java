package co.uk.techmeetup.data;

import java.util.Date;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.FieldBridge;

import co.uk.techmeetup.misc.TagStringBridge;

@Indexed
public class TagInstance {
	
	@DocumentId
	private int id;

	
	private User tagger;

	@ContainedIn
	private TmuEntity tagged;


	@IndexedEmbedded
	private Tag tag;

	private Date timestamp = new Date();

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public User getTagger() {
		return tagger;
	}

	public void setTagger(User tagger) {
		this.tagger = tagger;
	}

	public TmuEntity getTagged() {
		return tagged;
	}

	public void setTagged(TmuEntity tagged) {
		this.tagged = tagged;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
