package co.uk.techmeetup.pages;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Criteria;

import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.services.ImageService;

public class CreateEventPage extends BasePage {

	@Property
	private Event event;

	@Property
	private String name;

	@Property
	private String description;

	@Property
	private String venue;

	@Property
	private String tags;

	@Property
	private String time;

	@Property
	private String website;

	@Property
	private String meetup;

	@Property
	private Integer cost;

	@Property
	private Date date;

	@Property
	private UploadedFile eventImage;

	@InjectPage
	private Index index;

	@Inject
	private ImageService imageService;

	@Persist
	private Map<String, Meetup> meetupMap;
	
	@InjectComponent
	private Field eventImageField;
	
	@InjectComponent
	private Form createEventForm;

	@OnEvent(value = "submit", component = "createEventForm")
	public BasePage submitEvent() throws IOException {
		if(createEventForm.getHasErrors()){
			return null;
		}
		
		if (event == null) {
			event = new Event();
			event.setOwner(getLoggedInUser());
		}
		event.setTitle(name);
		event.setDescription(description);
		event.setLocation(venue);
		if (cost != null) {
			event.setCost(cost);
		} else {
			event.setCost(0);
		}
		event.setDate(date);
		event.setWebsite(website);
		Meetup eventMeetup = meetupMap.get(meetup);
		event.setMeetup(eventMeetup);
		event.setTime(time);

		getSession().beginTransaction();
		Image image = imageService.createImage(eventImage, getLoggedInUser(),
				eventMeetup, ImageService.EVENT_THUMB);
		event.setImage(image);
		
		if (!getSession().contains(event)) {
			getSession().persist(event);
		} else {
			event.setLastChanged(new Date());
		}

		Set<Tag> tagSet = findOrCreateTags(tags);
		Set<TagInstance> tagInstances=tag(tagSet, getLoggedInUser(), event);
		event.getTags().addAll(tagInstances);
		
		getSession().getTransaction().commit();

		return index;
	}
	
	@OnEvent(value = "success", component = "createEventForm")
	public void onSuccess(){
		if(eventImage==null){
			createEventForm.recordError(eventImageField, "You must provide an image for this event");
		}
	}

	public boolean getAdminControl() {
		if (event != null
				&& getLoggedInUser().getAdminFor().contains(event.getMeetup())) {
			return true;
		} else {
			return false;
		}
	}

	void onActivate(Long eventId) {
		// TODO access control for event editing
		if (eventId != null) {
			event = (Event) getSession().load(Event.class, eventId);
		}
	}

	public String[] getMeetupOptions() {
		Criteria criteria = getSession().createCriteria(Meetup.class);
		List<Meetup> meetupList = criteria.list();
		String[] meetups = new String[meetupList.size()];
		int i = 0;
		meetupMap = new HashMap<String, Meetup>();
		for (Meetup meetup : meetupList) {
			meetups[i] = meetup.getName();
			meetupMap.put(meetup.getName(), meetup);
			i++;
		}
		return meetups;
	}

}
