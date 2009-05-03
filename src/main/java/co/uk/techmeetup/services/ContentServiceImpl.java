package co.uk.techmeetup.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import co.uk.techmeetup.data.BlogPost;
import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.data.Video;
import co.uk.techmeetup.misc.SearchResult;

public class ContentServiceImpl implements ContentService {
	public static final String[] QUESTION_QUERY_FIELDS = { "title",
			"owner.name", "body", "tags" };
	public static final String[] PEOPLE_QUERY_FIELDDS = { "name",
			"interestTags", "expertTags" };
	public static final String[] VIDEO_QUERY_FIELDS = { "name", "description",
			"speakerDescription", "speaker.name" };
	public static final String[] EVENT_QUERY_FIELDS = { "title", "description",
			"location", "website", "tags" };

	private HibernateSessionManager sessionManager;

	public ContentServiceImpl(HibernateSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public SearchResult findEntity(Class type, String query, Meetup meetup,
			User user, int count, int offset) throws ParseException {
		SearchResult out = null;
		if (query == null) {
			Session session = sessionManager.getSession();
			Criteria criteria = session.createCriteria(type);
			final Integer resultCount = (Integer) criteria.setProjection(
					Projections.count("id")).uniqueResult();

			criteria = session.createCriteria(type);
			criteria.addOrder(Order.desc("created"));
			criteria.setMaxResults(count);
			criteria.setFirstResult(offset);
			out = new SearchResult(criteria.list(), resultCount);

		} else {
			out = searchWithQuery(query, type, count, offset);

		}
		return out;
	}

	@Override
	public SearchResult findEntity(Class type, String query, Meetup meetup,
			User user, Date from, int count, int offset) throws ParseException {
		// TODO event search
		return findEntity(type, query, meetup, user, count, offset);
	}

	@Override
	public SearchResult findUser(String query, Meetup meetup, int count,
			int offset) throws ParseException {

		SearchResult out = null;
		if (query == null) {
			Criteria criteria = sessionManager.getSession().createCriteria(
					User.class);
			final Integer resultCount = (Integer) criteria.setProjection(
					Projections.count("id")).uniqueResult();
			criteria = sessionManager.getSession().createCriteria(User.class);
			criteria.setMaxResults(count);
			criteria.setFirstResult(offset);
			out = new SearchResult(criteria.list(), resultCount);

		} else {
			FullTextSession fullTextSession = Search
					.getFullTextSession(sessionManager.getSession());

			MultiFieldQueryParser parser = new MultiFieldQueryParser(
					PEOPLE_QUERY_FIELDDS, new WhitespaceAnalyzer());

			org.apache.lucene.search.Query luceneQuery = parser.parse(query);
			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
					luceneQuery, User.class);

			fullTextQuery.setMaxResults(count);
			fullTextQuery.setFirstResult(offset);
			out = new SearchResult(fullTextQuery.list(), fullTextQuery
					.getResultSize());
		}
		return out;
	}

	@Override
	public List<TmuEntity> getFeatured(Class type) {
		// TODO featredSearch
		return new ArrayList<TmuEntity>();
	}

	@Override
	public SearchResult findFrontPageContent(String query, Meetup meetup,
			User user, int count, int offset) throws ParseException {

		SortedSet<Object> outSet = new TreeSet<Object>();

		/* Deal with blogs */
		SearchResult blogs = findEntity(BlogPost.class, query, meetup, user,
				count, offset);

		/* Deal with videos */
		SearchResult videos = findEntity(Video.class, query, meetup, user,
				count, offset);

		int i = 0;
		List<Object> out = new ArrayList<Object>();
		outSet.addAll(videos.getResults());
		outSet.addAll(blogs.getResults());
		Iterator<Object> iter = outSet.iterator();
		while (i < count && iter.hasNext()) {
			out.add(iter.next());
			i++;
		}
		return new SearchResult(out, blogs.getResultCount()
				+ videos.getResultCount());
	}

	private SearchResult searchWithQuery(String searchString, Class type,
			int count, int offset) throws ParseException {
		FullTextSession fullTextSession = Search
				.getFullTextSession(sessionManager.getSession());
		MultiFieldQueryParser parser = null;

		if (type == Question.class) {
			parser = new MultiFieldQueryParser(QUESTION_QUERY_FIELDS,
					new WhitespaceAnalyzer());
		} else if (type == Video.class) {
			parser = new MultiFieldQueryParser(VIDEO_QUERY_FIELDS,
					new WhitespaceAnalyzer());
		} else if (type == Event.class) {
			parser = new MultiFieldQueryParser(EVENT_QUERY_FIELDS,
					new WhitespaceAnalyzer());
		} else {

		}

		org.apache.lucene.search.Query luceneQuery = parser.parse(searchString);
		FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
				luceneQuery, type);
		
		fullTextQuery.setMaxResults(count);
		fullTextQuery.setFirstResult(offset);
		return new SearchResult(fullTextQuery.list(), fullTextQuery
				.getResultSize());
	}
}
