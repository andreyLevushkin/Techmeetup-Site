package co.uk.techmeetup.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Session;

import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.data.Video;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.services.ImageService;

public class AdminPostVideo extends BasePage {

	@Property
	private String selectedMeetup;

	@Property
	private String videoName;

	@Property
	private String aboutVideo;

	@Property
	private String aboutSpeaker;

	@Property
	private String speakerName;

	@Property
	private String tags;

	@Property
	private Integer vimeoId;

	@Property
	private UploadedFile thumbnail;

	@Persist
	private Map<String, Meetup> meetupMap;

	@Inject
	private ImageService imageService;

	@OnEvent(value = "submit", component = "postVideoForm")
	public void postVideo() throws IOException {

		Session session = getSession();
		session.beginTransaction();
		User admin = getLoggedInUser();
		User speaker = findByName(speakerName);
		Meetup meetup = meetupMap.get(selectedMeetup);

		Image image = imageService.createImage(thumbnail, speaker, meetup,
				ImageService.VIDEO_THUMB);

		/* Setup the video database entry */
		Video video = new Video();
		video.setOwner(admin);
		video.setDescription(aboutVideo);
		video.setMeetup(meetup);
		video.setSpeakerDescription(aboutSpeaker);
		video.setSpeaker(speaker);
		video.setVimeoId(vimeoId);

		video.setThumbnail(image);
		video.setName(videoName);
		getSession().persist(video);
		tag(findOrCreateTags(tags), admin, video);
		getSession().getTransaction().commit();
	}

	public String[] getMeetupOptions() {
		Set<Meetup> meetupSet = getLoggedInUser().getAdminFor();
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
