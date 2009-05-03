package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.internal.hibernate.HibernateEntityValueEncoder;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.FeaturedEntity;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.Video;
import co.uk.techmeetup.misc.EventEncoder;
import co.uk.techmeetup.misc.EventOptionModel;
import co.uk.techmeetup.misc.VideoEncoder;
import co.uk.techmeetup.misc.VideoOptionModel;

public class SetFeaturedPage extends BasePage {

	private List<Video> allVideos;
	private List<Event> allEvents;

	@InjectPage
	private Index index;

	@Property
	private List<Video> selectedVideo;

	@Property
	private List<Event> selectedEvent;

	public SelectModel getVideoModel() {
		List<VideoOptionModel> optionList = new ArrayList<VideoOptionModel>();
		for (Video video : allVideos) {
			optionList.add(new VideoOptionModel(video));
		}
		return new SelectModelImpl(optionList.toArray(new OptionModel[0]));
	}

	public ValueEncoder getVideoEncoder() {
		return new VideoEncoder(getSession());

	}

	@OnEvent(value = "submit", component = "featuredVideoForm")
	public BasePage setFeaturedVideos() {
		Criteria crit = getSession().createCriteria(FeaturedEntity.class);
		crit.add(Restrictions.eq("type", FeaturedEntity.VIDEO));
		List<FeaturedEntity> featured = crit.list();
		for (FeaturedEntity oldFeatured : featured) {
			getSession().delete(oldFeatured);
		}
		getSession().beginTransaction();
		for (Video video : selectedVideo) {
			FeaturedEntity featuredEntity = new FeaturedEntity();
			featuredEntity.setEntity(video);
			featuredEntity.setFeaturedBy(getLoggedInUser());
			featuredEntity.setType(FeaturedEntity.VIDEO);
			getSession().persist(featuredEntity);
		}
		getSession().getTransaction().commit();
		return index;

	}

	public SelectModel getEventModel() {
		List<EventOptionModel> optionList = new ArrayList<EventOptionModel>();
		for (Event event : allEvents) {
			optionList.add(new EventOptionModel(event));
		}
		return new SelectModelImpl(optionList.toArray(new OptionModel[0]));
	}

	public ValueEncoder<Event> getEventEncoder() {
		return new EventEncoder(getSession());

	}

	@OnEvent(value = "submit", component = "featuredEventForm")
	public BasePage setFeaturedEvent() {
		Criteria crit = getSession().createCriteria(FeaturedEntity.class);
		crit.add(Restrictions.eq("type", FeaturedEntity.EVENT));
		List<FeaturedEntity> featured = crit.list();
		for (FeaturedEntity oldFeatured : featured) {
			getSession().delete(oldFeatured);
		}
		getSession().beginTransaction();
		for (Event event : selectedEvent) {
			FeaturedEntity featuredEntity = new FeaturedEntity();
			featuredEntity.setEntity(event);
			featuredEntity.setFeaturedBy(getLoggedInUser());
			featuredEntity.setType(FeaturedEntity.EVENT);
			getSession().persist(featuredEntity);
		}
		getSession().getTransaction().commit();
		return index;

	}

	@SetupRender
	public void setupRender() {
		Criteria criteria = getSession().createCriteria(Video.class);
		allVideos = criteria.list();

		criteria = getSession().createCriteria(FeaturedEntity.class);
		criteria.add(Restrictions.eq("type", FeaturedEntity.VIDEO));
		selectedVideo = new ArrayList<Video>();
		for (FeaturedEntity featuredEntity : (List<FeaturedEntity>) criteria
				.list()) {
			selectedVideo.add((Video) featuredEntity.getEntity(getSession()));
		}

		criteria = getSession().createCriteria(Event.class);
		criteria.add(Restrictions.gt("date", new Date()));
		allEvents = criteria.list();

		criteria = getSession().createCriteria(FeaturedEntity.class);
		criteria.add(Restrictions.eq("type", FeaturedEntity.EVENT));
		selectedEvent = new ArrayList<Event>();
		for (FeaturedEntity featuredEntity : (List<FeaturedEntity>) criteria
				.list()) {
			selectedEvent.add((Event) featuredEntity.getEntity(getSession()));
		}
	}

}
