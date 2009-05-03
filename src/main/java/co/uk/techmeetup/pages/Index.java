package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import co.uk.techmeetup.components.BaseComponent;
import co.uk.techmeetup.components.FrontPageListing;
import co.uk.techmeetup.components.Layout;
import co.uk.techmeetup.data.BlogPost;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.data.Video;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.SearchResult;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Start page of application Techmeetup.
 */
public class Index extends SearchablePage {

	@InjectComponent
	private FrontPageListing frontPageListing;

	@Property
	private TmuEntity thisEntity;

	@Override
	public SearchResult doSearch(String searchString, int page)
			throws ParseException {
		return getContentService().findFrontPageContent(null, null, null, ControlVars.FRONTPAGE_CONTENT_COUNT,
				page * ControlVars.FRONTPAGE_CONTENT_COUNT);

	}

	@SetupRender
	public void setupRender() throws ParseException {
		this.search(null, 0);
	}

	@Override
	protected BaseComponent getMainContentComponent() {
		return frontPageListing;
	}

	@Override
	protected int getMaxResult() {
		return ControlVars.FRONTPAGE_CONTENT_COUNT;
	}

}
