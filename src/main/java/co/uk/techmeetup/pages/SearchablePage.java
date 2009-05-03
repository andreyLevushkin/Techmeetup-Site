package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import co.uk.techmeetup.components.BaseComponent;
import co.uk.techmeetup.components.Pagination;
import co.uk.techmeetup.components.TagCloudSearch;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.SearchResult;
import co.uk.techmeetup.misc.WeightedString;

@IncludeJavaScriptLibrary( { "context:js/tag_cloud.js" })
public abstract class SearchablePage extends BasePage {

	public static final String MAIN_CONTENT_ZONE = "mainContentZone";
	public static final String TAG_CLOUD_ZONE = "tagCloudZone";

	@InjectComponent
	private TagCloudSearch tagCloudSearch;

	@InjectComponent
	private Pagination pagination;

	private List<Object> entities;
	private Collection<WeightedString> cloudTags = new HashSet<WeightedString>();

	@Persist(value = PersistenceConstants.FLASH)
	private String oldSearchString;

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	private Request request;

	@Inject
	private ComponentResources componentResources;

	private Integer pageCount;

	public List<Object> getEntities() {
		return entities;
	}

	public Pagination getPaginationComponent() {
		return pagination;
	}

	public void setEntities(List<Object> entities) {
		this.entities = entities;
		if (entities.size() > 0) {
			setCloudTags(entities.get(0).getClass());
		}
	}

	public Collection<WeightedString> getCloudTags() {
		return cloudTags;
	}

	private void setCloudTags(Class type) {
		if (getSearchString() == null || entities == null
				|| entities.size() == 0) {
			cloudTags = new HashSet<WeightedString>();
		} else if (entities.get(0).getClass() == User.class) {
			List<User> users = new ArrayList<User>();
			for (Object o : entities) {
				User user = (User) o;
				users.add(user);
			}
			cloudTags = this.getTagService().getUserTags(users);
		} else {
			List<TmuEntity> es = new ArrayList<TmuEntity>();
			for (Object o : entities) {
				TmuEntity user = (TmuEntity) o;
				es.add(user);
			}
			cloudTags = this.getTagService().getEntityTags(es);
		}
	}

	public String getSearchString() {
		return tagCloudSearch.getSearchStringValue();
	}

	public int getPageNo() {
		return pagination.getPageNo();
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public abstract SearchResult doSearch(String searchString, int page)
			throws ParseException;

	protected abstract int getMaxResult();

	protected abstract BaseComponent getMainContentComponent();

	public BaseComponent search(String searchString, int page)
			throws ParseException {

		if (request.isXHR()) {
			if (page == -1) {
				page = 0;
				pagination.setPageNo(0);
				tagCloudSearch.setSearchStringValue(searchString);
			} else {
				searchString = tagCloudSearch.getSearchStringValue();
			}
		} else {
			if (tagCloudSearch.getSearchStringValue() != null
					|| pagination.getPageNo() != 0) {
				searchString = tagCloudSearch.getSearchStringValue();
				page = pagination.getPageNo();
			} else {
				componentResources.discardPersistentFieldChanges();
			}
		}

		SearchResult result = (SearchResult) doSearch(searchString, page);
		setEntities(result.getResults());
		setPageCount((int) Math
				.ceil(((float) result.getResultCount() / (float) getMaxResult())));
		return getMainContentComponent();
	}

	@SetupRender
	public void setupLayoutRender() {
		if (!request.isXHR()) {
			if (oldSearchString != null) {
				tagCloudSearch.setSearchStringValue(oldSearchString);
			}
		}
		renderSupport.addScript("initTagCloud();");
		renderSupport.addScript("refreshHack();");
	}
}
