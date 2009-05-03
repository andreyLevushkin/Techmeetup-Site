package co.uk.techmeetup.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.FieldBridge;

import co.uk.techmeetup.misc.TagInstanceSetBridge;

public abstract class TmuEntity implements Comparable {

	@DocumentId
	private long id;

	private Meetup meetup;

	public static  final SimpleDateFormat sdf=new SimpleDateFormat("EEE, MMM d, yy");
	
	@IndexedEmbedded
	private User owner;
	private Date created = new Date();

	@FieldBridge(impl = TagInstanceSetBridge.class)
	@Field
	@Analyzer(impl = org.apache.lucene.analysis.WhitespaceAnalyzer.class)
	private Set<TagInstance> tags = new HashSet<TagInstance>();

	private SortedSet<Comment> comments = new TreeSet<Comment>();

	private Set<Image> images = new HashSet<Image>();

	public int getCommentCount() {
		int count = comments.size();
		return count;
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Meetup getMeetup() {
		return meetup;
	}

	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<TagInstance> getTags() {
		return tags;
	}

	public void setTags(Set<TagInstance> tags) {
		this.tags = tags;
	}

	public SortedSet<Comment> getComments() {
		return comments;
	}

	public void setComments(SortedSet<Comment> comments) {
		this.comments = comments;
	}

	public String getNiceCreated(){
		return sdf.format(this.getCreated());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmuEntity other = (TmuEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		TmuEntity entity = (TmuEntity) o;
		return -1 * getCreated().compareTo(entity.getCreated());
	}

}
