package co.uk.techmeetup.services;

import java.util.Date;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import co.uk.techmeetup.data.Event;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.SearchResult;

/**
 * This service enables the search facilities avalible on the site. This is done
 * mostly throgh the hibernate-search (apache lucene).
 * 
 * @author andrey
 * 
 */
public interface ContentService {

	/**
	 * 
	 * @param type
	 * @param query
	 * @param meetup
	 * @param user
	 * @param count
	 * @param offset
	 * @return
	 * @throws ParseException
	 */
	public SearchResult findEntity(Class type, String query, Meetup meetup,
			User user, int count, int offset) throws ParseException;

	public SearchResult findEntity(Class type, String query, Meetup meetup,
			User user, Date from, int count, int offset) throws ParseException;

	public List<TmuEntity> getFeatured(Class type);

	public SearchResult findUser(String query, Meetup meetup, int count,
			int offset) throws ParseException;

	public SearchResult findFrontPageContent(String query, Meetup meetup,
			User user, int count, int offset) throws ParseException;
}
