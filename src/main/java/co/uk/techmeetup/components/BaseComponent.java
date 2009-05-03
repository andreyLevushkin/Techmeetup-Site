package co.uk.techmeetup.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.UserSession;
import co.uk.techmeetup.pages.BasePage;

/**
 * It is expected that all componenets will extend this abstract component. It
 * is intended to provide functionality that is going to be requered by all
 * components.
 * 
 * @author andrey
 * 
 */
public abstract class BaseComponent {

	/**
	 * Injected Hibernate session.
	 */
	@Inject
	private Session session;

	@Inject
	private ComponentResources resources;

	/**
	 * Persisted user state
	 */
	@ApplicationState
	private UserSession userSession;

	private boolean userSessionExists;

	/**
	 * Returns the hibernate session for this request
	 * 
	 * @return Hibernate session.
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Returns the page that contains this component
	 * 
	 * @return
	 */
	protected BasePage getPage() {
		return (BasePage) resources.getPage();
	}

	/**
	 * Test if the user accessing the site has logged in.
	 * 
	 * @return Returns true if the user has logged in.
	 */
	public boolean getIsLoggedIn() {
		return userSessionExists && userSession.getUserId() != null;
	}

	/**
	 * Returns the {@link UserSession} object for this session if it exists.
	 * 
	 * @return
	 */
	public UserSession getUserSession() {
		return userSession;
	}

	/**
	 * 
	 * @return true if the logged in user is a meta admin.
	 */
	public boolean getIsMetaAdmin() {
		return (userSessionExists && getLoggedInUser().getType() == User.META_ADMIN);
	}

	/**
	 * Returns the currently logged in user.
	 * 
	 * @return Returns the currently logged in user. Or null if the user is not
	 *         logged in.
	 */
	public User getLoggedInUser() {
		if (getIsLoggedIn()) {
			return (User) getSession().load(User.class,
					getUserSession().getUserId());
		} else {
			return null;
		}
	}
}
