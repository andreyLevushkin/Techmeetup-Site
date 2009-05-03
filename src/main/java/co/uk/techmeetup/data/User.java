package co.uk.techmeetup.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import co.uk.techmeetup.misc.TagSetBridge;

@Indexed
public class User {

	public static final int NORMAL = 0;
	public static final int ADMIN = 1;
	public static final int META_ADMIN = 2;
	public static final String TWITTER_PREFIX = "http://twitter.com/";

	@DocumentId
	private int id;
	@Field
	private String name;
	private String passwordHash;
	private String passwordSalt;
	private Date joined = new Date();
	private Date LastLogin;
	private List<UserAction> actions = new ArrayList<UserAction>();
	@ContainedIn
	private List<TmuEntity> owned = new ArrayList<TmuEntity>();

	private Set<Meetup> meetups = new HashSet<Meetup>();
	private Set<Meetup> adminFor = new HashSet<Meetup>();
	private Integer type = NORMAL;
	private String email;
	private String aboutMe;

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	private boolean notificationsEnabed = true;

	@FieldBridge(impl = TagSetBridge.class)
	@Field
	@Analyzer(impl = org.apache.lucene.analysis.WhitespaceAnalyzer.class)
	private Set<Tag> expertTags = new HashSet<Tag>();

	@FieldBridge(impl = TagSetBridge.class)
	@Field
	@Analyzer(impl = org.apache.lucene.analysis.WhitespaceAnalyzer.class)
	private Set<Tag> interestTags = new HashSet<Tag>();

	private String facebookProfile;
	private String twitterProfile;
	private String linkedinProfile;
	private Set<String> websites = new HashSet<String>();

	/**
	 * @return the websites
	 */
	public Set<String> getWebsites() {
		return websites;
	}

	public Set<String> getWebsitesNice() {
		Set<String> out = new HashSet<String>();
		for (String website : websites) {
			if (website.length() > 20) {
				out.add(website.substring(0, 20) + "...");
			} else {
				out.add(website);
			}
		}
		return out;
	}

	/**
	 * @param websites
	 *            the websites to set
	 */
	public void setWebsites(Set<String> websites) {
		this.websites = websites;
	}

	private Image profileImage;

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<UserAction> getActions() {
		return actions;
	}

	public void setactions(List<UserAction> actions) {
		this.actions = actions;
	}

	public List<TmuEntity> getOwned() {
		return owned;
	}

	public void setOwned(List<TmuEntity> owned) {
		this.owned = owned;
	}

	public Set<Meetup> getAdminFor() {
		return adminFor;
	}

	public void setAdminFor(Set<Meetup> adminFor) {
		this.adminFor = adminFor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public Date getLastLogin() {
		return LastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		LastLogin = lastLogin;
	}

	public Image getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(Image profileImage) {
		this.profileImage = profileImage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Tag> getExpertTags() {
		return expertTags;
	}

	public void setExpertTags(Set<Tag> expertTags) {
		this.expertTags = expertTags;
	}

	public Set<Tag> getInterestTags() {
		return interestTags;
	}

	public void setInterestTags(Set<Tag> interestTags) {
		this.interestTags = interestTags;
	}

	public String getFacebookProfile() {
		return facebookProfile;
	}

	public void setFacebookProfile(String facebookProfile) {
		this.facebookProfile = facebookProfile;
	}

	public String getTwitterProfile() {
		return twitterProfile;
	}

	public boolean getHasTwitter(){
		return twitterProfile!=null;
	}
	public void setTwitterProfile(String twitterProfile) {
		this.twitterProfile = twitterProfile;
	}

	public String getTwitterProfileLink() {
		return TWITTER_PREFIX + twitterProfile.substring(1);
	}

	public String getLinkedinProfile() {
		return linkedinProfile;
	}

	public void setLinkedinProfile(String linkedinProfile) {
		this.linkedinProfile = linkedinProfile;
	}

	public void setActions(List<UserAction> actions) {
		this.actions = actions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setMeetups(Set<Meetup> meetups) {
		this.meetups = meetups;
	}

	public Set<Meetup> getMeetups() {
		return meetups;
	}

	public boolean isNotificationsEnabed() {
		return notificationsEnabed;
	}

	public void setNotificationsEnabed(boolean notificationsEnabed) {
		this.notificationsEnabed = notificationsEnabed;
	}

}
