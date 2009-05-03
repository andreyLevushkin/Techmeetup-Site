package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.components.BaseComponent;
import co.uk.techmeetup.components.EventListing;
import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.FeaturedEntity;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.SearchResult;

public class EventPage extends SearchablePage {

	public static final int MAX_FEATURED_EVENTS = 3;

	@InjectComponent
	private EventListing eventListing;

	@Property
	private Event thisEvent;

	public List<Event> getFeaturedEvents() {
		Criteria criteria = getSession().createCriteria(FeaturedEntity.class);
		criteria.add(Restrictions.eq("type", FeaturedEntity.EVENT));
		criteria.setMaxResults(MAX_FEATURED_EVENTS);
		List<Event> out = new ArrayList<Event>();
		List<FeaturedEntity> featuredEntities = (List<FeaturedEntity>) criteria
				.list();
		for (FeaturedEntity featuredEntity : featuredEntities) {
			out.add((Event) featuredEntity.getEntity(getSession()));
		}
		return out;
	}

	@Override
	public SearchResult doSearch(String searchString, int page)
			throws ParseException {
		return getContentService().findEntity(Event.class, searchString, null,
				null, new Date(), ControlVars.EVENTS_PER_PAGE, page * getMaxResult());
	}

	@SetupRender
	public void setupRender() throws ParseException {
		this.search(null, 0);
	}

	@Override
	protected BaseComponent getMainContentComponent() {
		return eventListing;
	}

	@Override
	protected int getMaxResult() {
		return ControlVars.EVENTS_PER_PAGE;
	}
	
	public Class getTagClass(){
		return Event.class;
	}

}
