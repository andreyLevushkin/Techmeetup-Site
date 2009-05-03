package co.uk.techmeetup.components;

import org.apache.tapestry5.annotations.Parameter;

import co.uk.techmeetup.data.User;

/**
 * Responsible for rendering a clickable link to the users profile. The
 * {@link #user} parameter specifies what user should be rendered.
 * 
 * @author andrey
 * 
 */
public class UserName {

	@Parameter(required = true, allowNull = false)
	private User user;

	/**
	 * Returns the user whose name will be rendered.
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

}
