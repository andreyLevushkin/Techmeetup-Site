package co.uk.techmeetup.misc;

import org.apache.tapestry5.Link;

public class UserSession {
	
	private Integer userId;
	private Link nextPage;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Link getNextPage() {
		return nextPage;
	}

	public void setNextPage(Link nextPage) {
		this.nextPage = nextPage;
	}
	

}
