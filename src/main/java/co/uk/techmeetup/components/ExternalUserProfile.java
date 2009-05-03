package co.uk.techmeetup.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.User;

/**
 * Renders the public profile of the user when the user's name is clicked. This
 * is expected to be displayed inside a modal dialog.
 * 
 * @author andrey
 * 
 */
public class ExternalUserProfile extends BaseComponent {

	/**
	 * If the user's website URLs will be truncated to this value.
	 */
	public static final int MAX_WEBSITE_LENGTH = 20;

	@Parameter
	private int id;

	@SuppressWarnings("unused")
	@Property
	private Tag thisTag;

	@SuppressWarnings("unused")
	@Property
	private Meetup meetup;

	@Property
	private String website;

	/**
	 * Set the ID of the user who's profile will be rendered.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the ID of the profile of the user whose profile will be rendered.
	 */
	public int getId() {
		return id;
	}

	/**
	 * The user whose profile will be rendered.
	 * 
	 * @return
	 */
	public User getUser() {
		return (User) getSession().load(User.class, id);
	}

	public boolean getUserExists() {
		// TODO what is this? I think this may need to be removed.
		return true;
	}

	/**
	 * Returns the truncated version of the {@link #website} belonging to the
	 * user currently being rendered.
	 * 
	 * @return
	 */
	public String getWebsiteNice() {
		if (website.length() < MAX_WEBSITE_LENGTH) {
			return website;
		} else {
			return website.substring(0, MAX_WEBSITE_LENGTH) + "...";
		}
	}
}
