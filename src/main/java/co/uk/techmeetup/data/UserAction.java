package co.uk.techmeetup.data;

import java.util.Date;

public class UserAction {

	public static final short VIEW = 1;
	public static final short MARK_SPAM = 2;
	public static final short QUESTION = 3;
	public static final short LOGIN = 4;
	public static final short COMMENT = 5;

	private long id;
	private User user;
	private TmuEntity entity;
	private Date timestamp = new Date();;
	private short type;
	
	
	public long getId() {
		return id;
	}
	private void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public TmuEntity getEntity() {
		return entity;
	}
	public void setEntity(TmuEntity entity) {
		this.entity = entity;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
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
		UserAction other = (UserAction) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
