package co.uk.techmeetup.data;

import java.util.Date;

import org.hibernate.Session;

public class FeaturedEntity {

	public static final int VIDEO = 1;
	public static final int EVENT = 2;
	public static final int BLOG = 3;
	public static final int QUESTION = 4;

	private int id;
	private long entity_id;
	private User featuredBy;
	private Date created = new Date();
	private int type;
	private Meetup featuredIn;

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public TmuEntity getEntity(Session session) {
		switch (type) {
		case VIDEO:
			return (Video) session.load(Video.class, entity_id);
		case BLOG:
			return (BlogPost) session.load(BlogPost.class, entity_id);
		case EVENT:
			return (Event) session.load(Event.class, entity_id);
		case QUESTION:
			return (Question) session.load(Question.class, entity_id);
		default:
			throw new RuntimeException("Invalid type " + type);
		}

	}

	public void setEntity(TmuEntity entity) {
		if (entity.getClass() == Event.class) {
			this.setType(EVENT);
		} else if (entity.getClass() == Video.class) {
			this.setType(VIDEO);
		} else if (entity.getClass() == BlogPost.class) {
			this.setType(BLOG);
		} else if (entity.getClass() == Question.class) {
			this.setType(QUESTION);
		}
		this.entity_id = entity.getId();
	}

	public long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(long entity_id) {
		this.entity_id = entity_id;
	}

	public User getFeaturedBy() {
		return featuredBy;
	}

	public void setFeaturedBy(User featuredBy) {
		this.featuredBy = featuredBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Meetup getFeaturedIn() {
		return featuredIn;
	}

	public void setFeaturedIn(Meetup featuredIn) {
		this.featuredIn = featuredIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		FeaturedEntity other = (FeaturedEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
