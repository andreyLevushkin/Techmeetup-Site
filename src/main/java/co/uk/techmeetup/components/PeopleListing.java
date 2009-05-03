package co.uk.techmeetup.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.User;

/**
 * Rendres the list of people provided using the {@link #users} parameter.
 * 
 * @author andrey
 * 
 */
public class PeopleListing extends BaseComponent {

	@Parameter(required = true)
	private List<User> users;

	@SuppressWarnings("unused")
	@Parameter
	private Integer page;

	@SuppressWarnings("unused")
	@Property
	private User user;

	@SuppressWarnings("unused")
	@Property
	private int loopIndex;

	/**
	 * 
	 * @return The list of {@link User}s to be rendered.
	 */
	public List<User> getUsers() {
		return users;
	}

}
