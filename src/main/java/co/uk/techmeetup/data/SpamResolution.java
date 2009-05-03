package co.uk.techmeetup.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpamResolution {

	public static final short NEW = 0;
	public static final short UNDER_REVIEW = 1;
	public static final short APPROVED = 2;
	public static final short REMOVED = 3;

	private int id;
	private TmuEntity content;
	private User markedBy;
	private User resolvedBy;
	private Date markDate;
	private Date resolveDate;
	private short resolveOutcome;
	private Set<Message> messages = new HashSet<Message>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TmuEntity getContent() {
		return content;
	}

	public void setContent(TmuEntity conent) {
		this.content = conent;
	}

	public User getMarkedBy() {
		return markedBy;
	}

	public void setMarkedBy(User markedBy) {
		this.markedBy = markedBy;
	}

	public User getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(User resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

	public Date getMarkDate() {
		return markDate;
	}

	public void setMarkDate(Date markDate) {
		this.markDate = markDate;
	}

	public Date getResolveDate() {
		return resolveDate;
	}

	public void setResolveDate(Date resolveDate) {
		this.resolveDate = resolveDate;
	}

	public short getResolveOutcome() {
		return resolveOutcome;
	}

	public void setResolveOutcome(short resolveOutcome) {
		this.resolveOutcome = resolveOutcome;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
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
		SpamResolution other = (SpamResolution) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
