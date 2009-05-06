package co.uk.techmeetup.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import co.uk.techmeetup.components.BaseComponent;
import co.uk.techmeetup.components.DiscussionList;
import co.uk.techmeetup.data.Question;
import co.uk.techmeetup.data.Tag;
import co.uk.techmeetup.data.TagInstance;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.SearchResult;
import co.uk.techmeetup.services.InputSanitizer;
import co.uk.techmeetup.services.TagService;

@IncludeJavaScriptLibrary("context:js/validation_fix.js") 
public class DiscussionPage extends SearchablePage {

	@Property
	private String title;

	@Property
	private String body;

	@Property
	private String tags;

	@Component(parameters = { "AutocompleteNoProgress.minChars=2" })
	@Mixins( { "AutocompleteNoProgress" })
	private TextField tagsField;

	@InjectComponent
	private DiscussionList discussionList;

	@Inject
	private TagService tagService;

	@Inject
	private InputSanitizer inputSanitizer;
	
	private String thisSearchString;

	@OnEvent(value = "submit", component = "postQuestion")
	public void postQuestion() {
		if (!this.isLoggedIn()) {
			// TODO deal with not logged in users
			return;
		}
		//body=inputSanitizer.sanitizeWithBreaks(body);
		Session session = getSession();
		session.beginTransaction();
		Set<Tag> tagSet = findOrCreateTags(tags);
		User loggedInUser = getLoggedInUser();
		Question question = new Question();

		question.setTitle(title);
		question.setBody(body);
		question.setOwner(loggedInUser);
		Set<TagInstance> tags = tag(tagSet, loggedInUser, question);
		question.getTags().addAll(tags);
		getSession().persist(question);

		session.getTransaction().commit();
	}

	@SetupRender
	public void setupRender() throws ParseException {
		search(null, 0);
	}

	@Override
	public SearchResult doSearch(String searchString, int page)
			throws ParseException {
		thisSearchString = searchString;
		return getContentService().findEntity(Question.class, searchString,
				null, null, ControlVars.DISCUSSION_PER_PAGE,
				ControlVars.DISCUSSION_PER_PAGE * page);

	}

	@Override
	protected BaseComponent getMainContentComponent() {
		return discussionList;
	}

	@Override
	protected int getMaxResult() {
		return ControlVars.DISCUSSION_PER_PAGE;
	}

	public Class getTagClass() {
		return Question.class;
	}

	String[] onProvideCompletionsFromTagsField(String input) {
		return tagService.autocompleTags(input);
	}

	public boolean getUserSearch() {
		if (isLoggedIn() && thisSearchString != null) {
			String userName = getLoggedInUser().getName();
			return userName.equals(thisSearchString.trim());
		} else {
			return false;
		}
	}
}
