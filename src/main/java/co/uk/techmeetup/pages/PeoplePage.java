package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;

import co.uk.techmeetup.components.BaseComponent;
import co.uk.techmeetup.components.Pagination;
import co.uk.techmeetup.components.PeopleListing;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.SearchResult;

public class PeoplePage extends SearchablePage {

	@InjectComponent
	private PeopleListing peopleListing;

	@Override
	public SearchResult doSearch(String searchString, int page)
			throws ParseException {
		return getContentService()
				.findUser(searchString, null, ControlVars.PEOPLE_PER_PAGE,
						page * ControlVars.PEOPLE_PER_PAGE);

	}

	@SetupRender
	public void setupRender() throws ParseException {
		search(null, 0);
	}

	@Override
	protected BaseComponent getMainContentComponent() {
		return peopleListing;
	}

	@Override
	protected int getMaxResult() {
		return ControlVars.PEOPLE_PER_PAGE;
	}

}
