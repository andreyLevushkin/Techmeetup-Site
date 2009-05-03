package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.hibernate.Criteria;
import org.hibernate.annotations.Parameter;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.components.BaseComponent;
import co.uk.techmeetup.components.VideoListing;
import co.uk.techmeetup.data.FeaturedEntity;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.Video;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.SearchResult;

public class VideoPage extends SearchablePage {

	@Property
	private Video thisVideo;

	@InjectComponent
	private VideoListing videoListing;

	public List<Video> getFeaturedVideoList() {
		Criteria criteria = getSession().createCriteria(FeaturedEntity.class);
		criteria.add(Restrictions.eq("type", FeaturedEntity.VIDEO));
		criteria.setMaxResults(ControlVars.FEATURED_VIDEOS);
		List<Video> out = new ArrayList<Video>();
		List<FeaturedEntity> featured = criteria.list();
		for (FeaturedEntity entity : featured) {
			out.add((Video) entity.getEntity(getSession()));
		}
		return out;
	}

	@Override
	public SearchResult doSearch(String searchString, int page)
			throws ParseException {
		return getContentService().findEntity(Video.class, searchString, null,
				getLoggedInUser(), ControlVars.VIDEOS_PER_PAGE,
				ControlVars.VIDEOS_PER_PAGE * this.getPageNo());

	}

	@SetupRender
	public void setupRender() throws ParseException {
		this.search(null, 0);
	}

	@Override
	protected BaseComponent getMainContentComponent() {
		return videoListing;
	}

	@Override
	protected int getMaxResult() {
		return ControlVars.VIDEOS_PER_PAGE;
	}
}
