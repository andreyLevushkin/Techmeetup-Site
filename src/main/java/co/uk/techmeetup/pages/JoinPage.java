package co.uk.techmeetup.pages;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.PersistenceConstants;
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
import org.apache.tapestry5.annotations.Persist;
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
import org.hibernate.ReplicationMode;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.MeetupEncoder;
import co.uk.techmeetup.misc.MeetupOptionModel;
import co.uk.techmeetup.misc.TmpImage;
import co.uk.techmeetup.services.ImageService;
import co.uk.techmeetup.services.PasswordService;
import co.uk.techmeetup.services.TagService;

@IncludeJavaScriptLibrary("context:js/join_page_form.js")
public class JoinPage extends BasePage {

	@Property
	private BasePage nextPage;

	@InjectPage
	private Index indexPage;

	@Property
	@Persist
	private String username;

	@Property
	@Persist
	private String email;

	@Property
	@Persist
	private String expertTags;

	@Property
	@Persist
	private String interestTags;

	@Persist
	private String facebookProfile;

	@Persist
	private String twitterProfile;

	@Property
	@Persist
	private String linkedinProfile;

	@Property
	private String password;

	@Property
	private String passwordConfirm;

	@Property
	@Persist(PersistenceConstants.SESSION)
	private UploadedFile profilePicture;

	@Inject
	private ImageService imageService;

	@InjectComponent
	private Form joinForm;

	@InjectComponent
	private TextField usernameField;

	@InjectComponent
	private TextField emailField;

	@InjectComponent
	private PasswordField passwordField;

	@InjectComponent
	private PasswordField passwordConfirmField;

	@InjectComponent
	private Upload profilePictureField;

	@Persist
	private List<Meetup> selectedMeetups;

	@Persist
	private TmpImage tmpImage;

	@Persist
	private Set<String> websites;

	@Persist
	@Property
	private String aboutMe;

	@Inject
	private ComponentResources componentResources;

	@Component(parameters = { "AutocompleteNoProgress.minChars=2" })
	@Mixins( { "AutocompleteNoProgress" })
	private TextField interestTagsField;

	@Component(parameters = { "AutocompleteNoProgress.minChars=2" })
	@Mixins( { "AutocompleteNoProgress" })
	private TextField expertTagsField;

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	private TagService tagService;

	@Inject
	@Path("context:images/select.png")
	@Property
	private Asset selectImage;

	@Inject
	@Path("context:images/deselect.png")
	@Property
	private Asset deselectImage;

	@Inject
	private PasswordService passwordService;

	/**
	 * @return the websites
	 */
	public Set<String> getWebsites() {
		if (this.websites != null && this.websites.size() > 0) {
			return websites;
		} else {
			Set<String> out = new HashSet<String>();
			out.add("http://");
			return out;
		}
	}

	/**
	 * @param websites
	 *            the websites to set
	 */
	public void setWebsites(Set<String> websites) {
		this.websites = websites;
	}

	public String getTwitterProfile() {
		if (this.twitterProfile == null) {
			return "@";
		} else {
			return twitterProfile;
		}
	}

	public void setTwitterProfile(String twitterProfile) {
		this.twitterProfile = twitterProfile;
	}

	public List<Meetup> getSelectedMeetups() {
		if (selectedMeetups != null) {
			for (Meetup meetup : selectedMeetups) {
				if (!getSession().contains(meetup)) {
					getSession().replicate(meetup,
							ReplicationMode.LATEST_VERSION);
				}
			}
		}
		return selectedMeetups;
	}

	public void setSelectedMeetups(List<Meetup> selectedMeetups) {
		this.selectedMeetups = selectedMeetups;
	}

	/**
	 * @return the website
	 */
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

	private String website;

	@OnEvent(value = "success", component = "joinForm")
	public String onSuccess() throws IOException {
		if (profilePicture != null && imageService.isSupported(profilePicture)) {
			tmpImage = imageService.createTmpImage(profilePicture, null, null,
					ImageService.PROFILE);
		}

		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("name", username));
		User user = (User) criteria.uniqueResult();
		if (user != null) {
			joinForm.recordError(usernameField,
					"Sorry but this username is already in use");
		}
		user = null;
		criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email));
		user = (User) criteria.uniqueResult();
		if (user != null) {
			joinForm.recordError(emailField,
					"Sory but this email is already in use");
		}

		if (!password.equals(passwordConfirm)) {
			joinForm.recordError(passwordField,
					"The passwords you entered are not the same.");
			joinForm.recordError(passwordConfirmField, " ");
		}

		if (!imageService.isSupported(profilePicture)) {
			String error = "Usupported image format. Supported formats";
			for (String mime : ControlVars.SUPPORTED_IMAGE_MIME) {
				error += " " + mime;
			}
			joinForm.recordError(profilePictureField, error);
		}
		return null;
	}

	@OnEvent(value = "submit", component = "joinForm")
	public BasePage onJoinForm() throws NoSuchAlgorithmException, IOException {

		if (joinForm.getHasErrors()) {
			return null;
		}
		User user = new User();

		user.setName(username);
		user.setEmail(email);

		if (twitterProfile != null && !twitterProfile.equals("@")) {
			user.setTwitterProfile(twitterProfile);
		}

		/*
		 * if (this.facebookProfile != null) {
		 * user.setFacebookProfile(facebookProfile); }
		 * 
		 * if (this.linkedinProfile != null) {
		 * user.setLinkedinProfile(linkedinProfile); }
		 */

		getSession().beginTransaction();

		Set<Tag> persistedExpertTags = findOrCreateTags(expertTags);
		Set<Tag> persistedInterestTags = findOrCreateTags(interestTags);

		user.getExpertTags().addAll(persistedExpertTags);
		user.getInterestTags().addAll(persistedInterestTags);

		user.getMeetups().addAll(selectedMeetups);
		if (websites != null) {
			for (String site : websites) {
				if (!site.startsWith("http://")) {
					site = "http://" + site;
				}
				user.getWebsites().add(site);
			}
		}
		getSession().persist(user);
		passwordService.updatePassword(user, password);

		if (profilePicture != null && imageService.isSupported(profilePicture)) {
			Image userProfileImage = imageService.createImage(profilePicture,
					user, null, ImageService.PROFILE);
			user.setProfileImage(userProfileImage);

		} else if (tmpImage != null) {
			user.setProfileImage(tmpImage.keep(getSession(), user));
		} else {
			user.setProfileImage(getDefaultProfileImage());
		}
		user.setAboutMe(aboutMe);

		getSession().getTransaction().commit();
		getUserSession().setUserId(user.getId());
		componentResources.discardPersistentFieldChanges();
		if (nextPage != null) {
			return nextPage;
		} else {
			return indexPage;
		}

	}

	public ValueEncoder<String> getStringValueEncoder() {
		return new StringValueEncoder();
	}

	public ValueEncoder<Meetup> getMeetupEncoder() {
		return new MeetupEncoder(getSession());
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

	@OnEvent(component = "websites", value = "addRow")
	public Object onAddRowFromPhones() {
		return "http://";
	}

	private String getPasswordSalt() {
		Random random = new Random();
		String out = "";
		for (int i = 0; i < ControlVars.SALT_LENGTH; i++) {
			int rand = random.nextInt(ControlVars.SALT_ALPHABET.length());
			out += ControlVars.SALT_ALPHABET.charAt(rand);
		}
		return out;
	}

	String[] onProvideCompletionsFromInterestTagsField(String input) {
		return tagService.autocompleTags(input);
	}

	public String[] onProvideCompletionsFromExpertTagsField(String input) {
		return tagService.autocompleTags(input);
	}

	@SetupRender
	public void setupRender() {
		renderSupport.addScript("initJoinForm();");
	}

}
