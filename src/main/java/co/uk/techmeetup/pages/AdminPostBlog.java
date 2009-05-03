package co.uk.techmeetup.pages;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Session;

import co.uk.techmeetup.data.BlogPost;
import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.services.ImageService;

public class AdminPostBlog extends BasePage {

	@Property
	private UploadedFile mainImage;

	@Property
	private String title;

	@Property
	private String body;

	@Property
	private String selectedMeetup;

	@Persist
	private Map<String, Meetup> meetupMap;

	@Inject
	private ImageService imageService;

	@OnEvent(value = "submit", component = "postBlogForm")
	public void postBlog() throws IOException {
		User admin = getLoggedInUser();
		Meetup meetup = meetupMap.get(selectedMeetup);
		Session session = getSession();
		session.beginTransaction();
		/* Set up the main image */
		Image image = null;
		if (mainImage != null) {
			image =imageService.createImage(mainImage, admin, meetup, ImageService.BLOG_THUMB);
		}

		BlogPost blogPost = new BlogPost();
		blogPost.setApprovedBy(admin);
		blogPost.setTitle(title);
		blogPost.setMainImage(image);
		blogPost.setMeetup(meetup);
		blogPost.setBody(body);
		blogPost.setOwner(admin);
		session.save(blogPost);
		session.getTransaction().commit();
	}

	public String[] getMeetupOptions() {
		User loggedInUser =getLoggedInUser();
		Set<Meetup> meetupSet = loggedInUser.getAdminFor();
		String[] meetups = new String[meetupSet.size()];
		int i = 0;
		meetupMap = new HashMap<String, Meetup>();
		for (Meetup meetup : meetupSet) {
			meetups[i] = meetup.getName();
			meetupMap.put(meetup.getName(), meetup);
			i++;
		}
		return meetups;
	}
}
