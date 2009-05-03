package co.uk.techmeetup.components;

import java.util.Collection;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import co.uk.techmeetup.misc.WeightedString;
import co.uk.techmeetup.pages.SearchablePage;
import co.uk.techmeetup.services.TagService;

/**
 * Renders the search box that usually goes next to the tag cloud. When any tags
 * on the page are clicked they will appear in the search box rendered by this
 * component. This component MUST appear within an instance of
 * {@link SearchablePage}. It expects the {@link #entityTags} parameter to
 * contain a collection of {@link WeightedString}s representing the relevant
 * tags to render - normally based on the content that currently being displayed
 * on the page.
 * 
 * @author andrey
 * 
 */
public class TagCloudSearch extends BaseComponent {

	@Parameter(required = true)
	private Collection<WeightedString> entityTags;

	@InjectComponent
	private TagCloud tagCloud;

	@SuppressWarnings("unused")
	@Component(parameters = { "AutocompleteNoProgress.minChars=2" })
	@Mixins( { "AutocompleteNoProgress" })
	private TextField searchString;

	@Persist
	private String searchStringValue;

	@Inject
	private ComponentResources resources;

	@Inject
	private TagService tagService;

	/**
	 * This is called when the search form is submited. It is responsible for
	 * searching the database for the {@link #searchStringValue} provided by the
	 * user.
	 * 
	 * @return MultiZoneUpdate that will update the page with the content
	 *         returned by the search.
	 * @throws ParseException
	 */
	@OnEvent(value = "submit", component = "tagSearchForm")
	public MultiZoneUpdate onTagSearchForm() throws ParseException {
		if (searchStringValue != null) {
			searchStringValue = searchStringValue.toLowerCase().replace(",",
					" ").trim();
		}
		SearchablePage containgPage = (SearchablePage) resources.getPage();
		Object mainContent = containgPage.search(searchStringValue, -1);
		Pagination pagination = containgPage.getPaginationComponent();
		MultiZoneUpdate out = new MultiZoneUpdate(
				SearchablePage.MAIN_CONTENT_ZONE, mainContent);
		out = out.add(Pagination.PAGINATION_ZONE, pagination);
		out = out.add(SearchablePage.TAG_CLOUD_ZONE, tagCloud);

		return out;
	}

	/**
	 * 
	 * @return Provided relevant tags.
	 */
	public Collection<WeightedString> getEntityTags() {
		return entityTags;
	}

	/**
	 * 
	 * @return returns the searchStringValue
	 */
	public String getSearchStringValue() {
		return searchStringValue;
	}

	/**
	 * Sets the {@link #searchStringValue}
	 * 
	 * @param searchString
	 */
	public void setSearchStringValue(String searchString) {
		this.searchStringValue = searchString;
	}

	/**
	 * 
	 * @return true if there are not relevant tags
	 */
	public boolean getHasTags() {
		return entityTags.size() != 0;
	}

	/**
	 * Used to autocomplete the search string entered by the user with tags
	 * avalible in the database.
	 * 
	 * @param input
	 *            Search string entered by the user
	 * @return Array containing possible completions.
	 */
	public String[] onProvideCompletionsFromSearchString(String input) {
		return tagService.autocompleTags(input);
	}

}
