package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.User;
import co.uk.techmeetup.services.PasswordService;

@IncludeJavaScriptLibrary("context:js/validation_fix.js") 
public class LoginPage extends BasePage {

	@Inject
	private PasswordService passwordService;

	@Property
	private String username;

	@Property
	private String password;

	@InjectComponent
	private Form loginForm;

	@OnEvent(value = "submit", component = "loginForm")
	public Object onLogin() {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("name", username));
		User user = (User) criteria.uniqueResult();
		if (user != null && passwordService.checkPassword(user, password)) {
			getUserSession().setUserId(user.getId());
			Object nextPage = getUserSession().getNextPage();
			if (nextPage != null) {
				getUserSession().setNextPage(null);
				return nextPage;
			} else {
				return Index.class;
			}
		} else {
			loginForm.recordError("Username and password do not match");
			return null;
		}
	}

}
