package co.uk.techmeetup.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.components.Upload;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.MeetupEncoder;
import co.uk.techmeetup.misc.MeetupOptionModel;
import co.uk.techmeetup.services.ImageService;
import co.uk.techmeetup.services.PasswordService;
import co.uk.techmeetup.services.TagService;

@IncludeJavaScriptLibrary( { "context:js/fillpassword.js" })
public class EditUserProfilePage extends BasePage {

	public static final String DEFAULT_PASS = "qwertyuiopasdfghjkl";

	@InjectPage
	private Index indexPage;

	@Property
	private String email;

	@Property
	private String expertTags;

	@Property
	private String interestTags;

	@Property
	private String facebookProfile;

	@Property
	private String twitterProfile = "@";

	@Property
	private String linkedinProfile;

	@Property
	private String password = DEFAULT_PASS;

	@Property
	private String passwordConfirm = DEFAULT_PASS;

	@Property
	private UploadedFile profilePicture;

	@Inject
	private ImageService imageService;

	@InjectComponent
	private Form updateProfileForm;

	@InjectComponent
	private TextField emailField;

	@InjectComponent
	private PasswordField passwordField;

	@InjectComponent
	private PasswordField passwordConfirmField;

	@InjectComponent
	private Upload profilePictureField;

	@Property
	private List<Meetup> selectedMeetups;

	@Property
	private User user;

	@Property
	private Set<String> websites;

	@Property
	private String aboutMe;

	@Inject
	private PasswordService passwordService;

	@Inject
	private TagService tagService;

	@Component(parameters = { "AutocompleteNoProgress.minChars=2" })
	@Mixins( { "AutocompleteNoProgress" })
	private TextField interestTagsField;

	@Component(parameters = { "AutocompleteNoProgress.minChars=2" })
	@Mixins( { "AutocompleteNoProgress" })
	private TextField expertTagsField;

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	@Path("context:images/select.png")
	private Asset selectImage;

	@Inject
	@Path("context:images/deselect.png")
	private Asset deselectImage;

	private String website;

	public enum NotificationSetting {
		ENABLED, DISABLED
	}

	@Property
	private NotificationSetting notifications;

	public NotificationSetting getEnabled() {
		return NotificationSetting.ENABLED;
	}

	public NotificationSetting getDisabled() {
		return NotificationSetting.DISABLED;
	}

	public ValueEncoder<String> getStringValueEncoder() {
		return new StringValueEncoder();
	}

	public ValueEncoder<Meetup> getMeetupEncoder() {
		return new MeetupEncoder(getSession());
	}

	public Asset getSelectImage() {
		return selectImage;
	}

	public Asset getDeselectImage() {
		return deselectImage;
	}

	public SelectModel getMeetupModel() {
		Criteria criteria = getSession().createCriteria(Meetup.class);
		List<Meetup> meetups = criteria.list();
		List<MeetupOptionModel> optionList = new ArrayList<MeetupOptionModel>();
		for (Meetup meetup : meetups) {
			optionList.add(new MeetupOptionModel(meetup));
		}
		return new SelectModelImpl(optionList.toArray(new OptionModel[0]));

	}

	public String getWebsite() {
		return website;
	}

	/**
	 * @param website
	 *            the website to set
	 */
	public void setWebsite(String website) {
		if (website != null && website.trim().length() > 0
				&& !website.trim().equals("http://")) {
			if (websites == null) {
				websites = new HashSet<String>();
			}
			websites.add(website);
		}
		this.website = website;
	}

	@OnEvent(component = "websites", value = "addRow")
	public Object onAddRowFromPhones() {
		return "http://";
	}

	@SetupRender
	public void setupRender() {

		renderSupport.addScript("initFillPassword();");
		user = getLoggedInUser();
		email = user.getEmail();

		expertTags = "";
		for (Tag tag : user.getExpertTags()) {
			expertTags += tag.getTagString() + " ";
		}

		interestTags = "";
		for (Tag tag : user.getInterestTags()) {
			interestTags += tag.getTagString() + " ";
		}
		facebookProfile = user.getFacebookProfile();
		if (user.getTwitterProfile() != null) {
			twitterProfile = user.getTwitterProfile();
		}
		linkedinProfile = user.getLinkedinProfile();

		selectedMeetups = new ArrayList<Meetup>();
		selectedMeetups.addAll(user.getMeetups());

		websites = new HashSet<String>();
		websites.addAll(user.getWebsites());
		if (websites.size() == 0) {
			websites.add("http://");
		}
		if (user.isNotificationsEnabed()) {
			notifications = NotificationSetting.ENABLED;
		} else {
			notifications = NotificationSetting.DISABLED;
		}
		aboutMe = user.getAboutMe();
	}

	@OnEvent(value = "success", component = "updateProfileForm")
	public String onSuccess() {
		user = getLoggedInUser();
		if (email != null) {
			Criteria criteria = getSession().createCriteria(User.class);
			criteria.add(Restrictions.eq("email", email));
			User userMatchingEmail = (User) criteria.uniqueResult();
			if (userMatchingEmail != null
					&& userMatchingEmail.getId() != user.getId()) {
				updateProfileForm.recordError(emailField,
						"Sorry but this email is already in user");
			}
		}

		if (password != null && !password.equals(passwordConfirm)) {
			updateProfileForm.recordError(passwordField,
					"The passwords you entered are not the same.");
			updateProfileForm.recordError(passwordConfirmField,
					"The passwords you entered are not the same.");
		}

		if (profilePicture != null) {
			if (imageService.isSupported(profilePicture)) {
				String error = "Usupported image format. Supported formats";
				for (String mime : ControlVars.SUPPORTED_IMAGE_MIME) {
					error += " " + mime;
				}
				updateProfileForm.recordError(profilePictureField, error);
			}
		}

		return null;
	}

	@OnEvent(component = "updateProfileForm", value = "submit")
	public void onFormSubmit() throws IOException {
		if (updateProfileForm.getHasErrors()) {
			return;
		}
		Session session = getSession();
		session.beginTransaction();
		Set<String> cleanWebsites = null;
		if (websites != null) {
			cleanWebsites = new HashSet<String>();
			for (String site : websites) {
				if (!site.startsWith("http://")) {
					cleanWebsites.add("http://" + site);
				} else {
					cleanWebsites.add(site);
				}
			}
		} else {
			cleanWebsites = new HashSet<String>();
		}
		if ((cleanWebsites == null && user.getWebsites().size() == 0)
				|| !(user.getWebsites().equals(cleanWebsites) && cleanWebsites
						.equals(user.getWebsites()))) {
			user.getWebsites().clear();
			user.getWebsites().addAll(cleanWebsites);
		}

		if (selectedMeetups != null
				&& !(user.getMeetups().equals(selectedMeetups) && selectedMeetups
						.equals(user.getMeetups()))) {

			user.getMeetups().clear();
			user.getMeetups().addAll(selectedMeetups);

		}

		if (profilePicture != null) {
			Image newProfileImage = imageService.createImage(profilePicture,
					user, null, ImageService.PROFILE);
			user.setProfileImage(newProfileImage);
		}

		Set<Tag> expertTagsProccessed = getTagService().findOrCreateTags(
				expertTags);
		user.setExpertTags(expertTagsProccessed);

		Set<Tag> interestTagsProccessed = getTagService().findOrCreateTags(
				interestTags);
		user.setInterestTags(interestTagsProccessed);

		if (email != null) {
			user.setEmail(email);
		}

		if (password != null && !password.equals(DEFAULT_PASS)) {
			passwordService.updatePassword(user, password);
		}

		// user.setFacebookProfile(facebookProfile);
		if (twitterProfile != null && !twitterProfile.equals("@")) {
			user.setTwitterProfile(twitterProfile);
		}
		// user.setLinkedinProfile(linkedinProfile);
		user.setAboutMe(aboutMe);
		if (notifications.equals(NotificationSetting.ENABLED)) {
			user.setNotificationsEnabed(true);
		} else {
			user.setNotificationsEnabed(false);
		}

		session.getTransaction().commit();
	}

	String[] onProvideCompletionsFromInterestTagsField(String input) {
		return tagService.autocompleTags(input);
	}

	public String[] onProvideCompletionsFromExpertTagsField(String input) {
		return tagService.autocompleTags(input);
	}

}
