package co.uk.techmeetup.components;

import java.util.List;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.hibernate.Criteria;

import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.User;

/**
 * This component is responsible for rendering the page content that is common
 * to all pages. For example the header and the footer.
 * 
 * @author andrey
 * 
 */
@IncludeStylesheet( { "context://css/full.css", "context://css/modalbox.css" })
@IncludeJavaScriptLibrary( { "context:js/dialog.js",
		"${tapestry.scriptaculous}/effects.js", "context:js/misc.js" })
public class Layout extends BaseComponent {

	public static final String VIDEO_PAGE = "video";
	public static final String HOME_PAGE = "home";
	public static final String EVENT_PAGE = "event";
	public static final String PEOPLE_PAGE = "people";
	public static final String DISCUSSION_PAGE = "discussion";
	public static final String SELECTED_CLASS = "selected";

	@Parameter
	private String page;

	public String getNavHomeClass() {
		if (page != null && page.equals(HOME_PAGE)) {
			return SELECTED_CLASS;
		} else {
			return "";
		}
	}

	public String getNavVideosClass() {
		if (page != null && page.equals(VIDEO_PAGE)) {
			return SELECTED_CLASS;
		} else {
			return "";
		}
	}

	public String getNavEventsClass() {
		if (page != null && page.equals(EVENT_PAGE)) {
			return SELECTED_CLASS;
		} else {
			return "";
		}
	}

	public String getNavPeopleClass() {
		if (page != null && page.equals(PEOPLE_PAGE)) {
			return SELECTED_CLASS;
		} else {
			return "";
		}
	}

	public String getNavDiscussionClass() {
		if (page != null && page.equals(DISCUSSION_PAGE)) {
			return SELECTED_CLASS;
		} else {
			return "";
		}
	}

	public boolean isAdmin() {
		if (isLoggedIn() && getLoggedInUser().getAdminFor().size() > 0
				&& getLoggedInUser().getType() != User.NORMAL) {
			return true;
		} else if (getLoggedInUser().getType() == User.META_ADMIN) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Called when the user clicks the 'logout' link. Responsible for terminting
	 * the users. Sesssion.
	 * 
	 * @return
	 */
	@OnEvent(value = "action", component = "logout")
	public Object logout() {
		getUserSession().setUserId(null);

		return "Index";
	}

	/**
	 * Returns true if the user viewing this page is logged in.
	 * 
	 * @return
	 */
	public boolean isLoggedIn() {
		return this.getIsLoggedIn();
	}

	/**
	 * Returns the currently logged in user. Or null if the user is not logged
	 * in.
	 * 
	 * @return
	 */
	public User getLoggedInUser() {
		if (isLoggedIn()) {
			return (User) getSession().load(User.class,
					getUserSession().getUserId());
		} else {
			return null;
		}
	}

	/**
	 * Returns the list of {@link Meetup}s present in the database. Used for
	 * navigating to the Meetup home pages.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meetup> getMeetups() {
		Criteria crit = getSession().createCriteria(Meetup.class);
		return crit.list();
	}

}
