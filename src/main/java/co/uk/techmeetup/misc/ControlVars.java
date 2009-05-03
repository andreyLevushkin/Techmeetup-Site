package co.uk.techmeetup.misc;

import java.util.HashSet;
import java.util.Set;

public class ControlVars {

	public static final String SERVER_HOST = "87.246.111.22";
	public static final String IMAGE_FILE_PATH = "/usr/share/jetty6/static/";
	// public static final String IMAGE_FILE_PATH =
	// "/home/andrey/ILCK/workspace_ilck/Techmeetup/src/main/webapp/dynamic_images/";
	public static final String IMAGE_URL_PREFIX = "/dynamic_images/";
	public static final String DEFAULT_PROFILE_IMAGE_URL = "/images/default_profile.jpg";
	public static final String SANITIZER_POLICY_FILE = "/usr/share/jetty6/sanitizer.policy";
	public static final int SALT_LENGTH = 10;
	public static final String SALT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-+!#$%^&*()";
	public static final int DEFAULT_PASSWORD_LENGTH = 10;

	public static final int PROFILE_IMAGE_HEIGHT = 100;
	public static final int PROFILE_IMAGE_WIDTH = 100;
	public static final int THUMBNAIL_IMAGE_WIDTH = 100;
	public static final int THUMBNAIL_IMAGE_HEIGHT = 100;
	public static final int BLOG_IMAGE_HEIGHT = 300;
	public static final int BLOG_IMAGE_WIDTH = 300;
	public static final int EVENT_THUMB_HEIGHT = 100;
	public static final int EVENT_THUMB_WIDTH = 100;

	public static final Set<String> SUPPORTED_IMAGE_MIME;

	public static final int DISCUSSION_PER_PAGE = 10;
	public static final int PEOPLE_PER_PAGE = 20;
	public static final int VIDEOS_PER_PAGE = 10;
	public static final int EVENTS_PER_PAGE = 3;
	public static final int FRONTPAGE_CONTENT_COUNT = 60;

	public static final int FEATURED_VIDEOS = 3;

	public static final String SMTP_HOST = "localhost";

	static {
		SUPPORTED_IMAGE_MIME = new HashSet<String>();
		SUPPORTED_IMAGE_MIME.add("image/png");
		SUPPORTED_IMAGE_MIME.add("image/jpeg");
		SUPPORTED_IMAGE_MIME.add("image/bmp");
		SUPPORTED_IMAGE_MIME.add("image/gif");

	}

	/**
	 * The order of substitution is mute_link,reply_link
	 */
	public static final String NOTIFICATION_HEADER = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
			+ "<html lang=\"en\">\n"
			+ "<head>\n"
			+ "<title>Techmeetup</title>\n"
			+ "</head>\n"
			+ "<body>\n<div>\n"
			+ "<a href=\"%s\">Mute</a> any further notifications on this topic<br/>"
			+ "<a href=\"%s\">Reply</a> to this message<br/><br/><br/>";

	/**
	 * The order of substitution is name,body
	 */
	public static final String QUESTION_NOTIFICATION_TEMPLATE = "<p><br/>%s posted:<br/>"
			+ "%s\n\n\n\n<br/></p>";

	/**
	 * The order of substitution is mute_link,reply_link
	 */
	public static final String NOTIFICATION_FOOTER = "-----------------------------------------------------------\n"
			+ "<p><a href=\"%s\">Mute</a> any further notifications on this topic<br/>"
			+ "<a href=\"%s\">Reply</a> to this message<br/>"
			+ "You received this message because you care about tech (and your notifications are enabled at <a href=\"http://www.techmeetup.co.uk\">www.TechMeetup.co.uk</a>).<br/>"
			+ "If you don&rsquo;t want these timely notifications of what&rsquo;s happening around you, click here to remove your account from the service, or here to remain on the service but disable notifications<br/>"
			+ "</p></div>\n</body>\n</html>";
}
