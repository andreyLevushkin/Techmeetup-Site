package co.uk.techmeetup.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.hibernate.Session;

import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.UserSession;

/**
 * This Dispatcher is responsible for access control for the whole site. All
 * requests must pass through it.
 * 
 * @author andrey
 * 
 */
public class AccessControlDispatcher implements Dispatcher {
	/**
	 * Pages that can only be accessed by logged in users.
	 */
	public static final Set<String> loggedInOnly;

	/**
	 * Pages that can only be access by administrators
	 */
	public static final Set<String> adminOnly;

	/**
	 * Pages that can only be accessed by meta administrators.
	 */
	public static final Set<String> metaAdminOnly;

	static {
		Set<String> tmp = new HashSet<String>();
		tmp.add("/createeventpage");
		tmp.add("/adminpostblog");
		tmp.add("/adminpostvideo");
		tmp.add("/setfeaturedpage");
		adminOnly = tmp;

		tmp = new HashSet<String>();
		tmp.add("/edituserprofilepage");
		loggedInOnly = tmp;

		tmp = new HashSet<String>();
		tmp.add("/siteadminpage");
		metaAdminOnly = tmp;

	}

	@Inject
	private ApplicationStateManager applicationStateManager;

	@Inject
	private Session session;

	@Override
	public boolean dispatch(Request req, Response res) throws IOException {

		Integer userId = null;
		String path = req.getPath().toLowerCase();


		if (applicationStateManager.exists(UserSession.class)) {
			userId = applicationStateManager.get(UserSession.class).getUserId();
		}
		boolean block = false;
		for (String page : adminOnly) {
			if (path.startsWith(page)) {
				if (userId != null) {
					User user = (User) session.load(User.class, userId);
					if (user != null
							&& (user.getType() == User.ADMIN || user.getType() == User.META_ADMIN)) {
						block = false;
						break;
					} else {
						block = true;
						break;
					}
				} else {
					block = true;
					break;
				}
			}
		}
		if (!block) {
			for (String page : loggedInOnly) {
				if (path.startsWith(page)) {
					if (userId == null) {
						block = true;
						break;
					} else {
						break;
					}
				}
			}
		}

		if (!block) {
			for (String page : metaAdminOnly) {
				if (path.startsWith(page)) {
					if (userId != null) {
						User user = (User) session.load(User.class, userId);
						if (user.getType() != User.META_ADMIN) {
							block = true;
							break;
						}
					} else {
						block = true;
						break;
					}
				}
			}
		}

		return block;
	}

}
