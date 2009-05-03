package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import co.uk.techmeetup.data.BlogPost;
import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.data.Video;

public class SiteAdminPage extends BasePage {

	public static final int BATCH_SIZE = 20;
	public static final Class[] INDEXED_CLASSES = { Question.class, 
			BlogPost.class, Video.class };

	@Property
	private String newTechmeetup;

	@OnEvent(component = "reindex", value = "action")
	public void reindex() {
		Session session = getSession();

		for (Class indexedClass : INDEXED_CLASSES) {
			/* Purge index */
			FullTextSession fullTextSession = Search
					.getFullTextSession(session);
			Transaction tx = fullTextSession.beginTransaction();
			fullTextSession.purgeAll(indexedClass);
			tx.commit();

			/* Rebuild it */
			fullTextSession.setFlushMode(FlushMode.MANUAL);
			fullTextSession.setCacheMode(CacheMode.IGNORE);
			tx = fullTextSession.beginTransaction();
			// Scrollable results will avoid loading too many objects in memory
			ScrollableResults results = fullTextSession.createCriteria(
					indexedClass).setFetchSize(BATCH_SIZE).scroll(
					ScrollMode.FORWARD_ONLY);
			int index = 0;
			while (results.next()) {
				index++;
				fullTextSession.index(results.get(0)); // index each element
				if (index % BATCH_SIZE == 0) {
					fullTextSession.flushToIndexes(); // apply changes to
														// indexes
					fullTextSession.clear(); // clear since the queue is
												// processed
				}
			}
			tx.commit();
		}

	}
}
