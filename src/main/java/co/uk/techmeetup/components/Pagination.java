package co.uk.techmeetup.components;

import org.apache.lucene.queryParser.ParseException;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import co.uk.techmeetup.pages.SearchablePage;

/**
 * This component is responsible for rendering the pageniation on
 * {@link SearchablePage}s. It persists the page the user is on and is
 * responsible for refreshing the content when the page number are clicked.
 * 
 * @author andrey
 * 
 */
public class Pagination extends BaseComponent {

	public static final String PAGINATION_ZONE = "paginationZone";

	@Persist
	private int page;

	@Property
	private int thisPage;

	/**
	 * Called when the 'next' page link is clicked. Increments the current
	 * {@link #page} and returns the updated content to be rendered.
	 * 
	 * @param page
	 *            -ignored
	 * @return
	 * @throws ParseException
	 */
	@OnEvent(value = "action", component = "next")
	public Object onNext(int page) throws ParseException {
		this.page++;
		return getPageUpdate();
	}

	/**
	 * Called when the 'prev' page link is clicked. Decrements the current
	 * {@link #page} and returns the updated content to be rendered.
	 * 
	 * @param page
	 *            -ignored
	 * @return
	 * @throws ParseException
	 */
	@OnEvent(value = "action", component = "prev")
	public Object onPrev(int page) throws ParseException {
		if (this.page > 0) {
			this.page = page--;
		}
		return getPageUpdate();
	}

	/**
	 * Called when a specific page number is clicked.
	 * 
	 * @param page
	 *            The page to navigate to.
	 * @return
	 * @throws ParseException
	 */
	@OnEvent(value = "action", component = "pageselect")
	public Object onDirectPageLink(int page) throws ParseException {
		this.page = page;
		return getPageUpdate();
	}

	/**
	 * This method updates the webpage whenever the page number changes.
	 * 
	 * @return The MultiZoneUpdate containing the components that will render
	 *         themselfs thus updating the webpage to the new page.
	 * @throws ParseException
	 */
	private MultiZoneUpdate getPageUpdate() throws ParseException {
		String searchString = ((SearchablePage) getPage()).getSearchString();

		BaseComponent mainContent = ((SearchablePage) getPage()).search(
				searchString, page);
		Pagination paginationComponent = ((SearchablePage) getPage())
				.getPaginationComponent();
		MultiZoneUpdate out = new MultiZoneUpdate(
				SearchablePage.MAIN_CONTENT_ZONE, mainContent);
		out = out.add(PAGINATION_ZONE, paginationComponent);
		return out;
	}

	/**
	 * Returns the array of avalible page numbers -from page 0 to the last page.
	 * Used to render the page number navigation links.
	 * 
	 * @return
	 */
	public int[] getPages() {
		int pageCount = ((SearchablePage) getPage()).getPageCount();
		int[] pageNo = new int[pageCount];
		for (int i = 0; i < pageCount; i++) {
			pageNo[i] = i;
		}
		return pageNo;
	}

	/**
	 * Returns the previus page.
	 * 
	 * @return
	 */
	public int getPrevPage() {
		return page - 1;
	}

	/**
	 * Returns the next page.
	 * 
	 * @return
	 */
	public int getNextPage() {
		return page + 1;
	}

	/**
	 * Returns the css class for the page number currently being rendered. Used
	 * to highlight the page the user is on.
	 * 
	 * @return
	 */
	public String getPageNumberClass() {
		if (page == thisPage) {
			return "this_page";
		} else {
			return "";
		}
	}

	/**
	 * Get the current page number.
	 * 
	 * @return
	 */
	public int getPageNo() {
		return page;
	}

	/**
	 * Set the current page number.
	 * 
	 * @param page
	 */
	public void setPageNo(int page) {
		this.page = page;
	}

	/**
	 * 
	 * @return true if this is NOT the first page (0).
	 */
	public boolean isNotFirst() {
		return page != 0;
	}

	/**
	 * 
	 * @return true if this is NOT the last page.
	 */
	public boolean isNotLast() {
		int pageCount = ((SearchablePage) getPage()).getPageCount();
		if (Math.max(pageCount - 1, 0) == page) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Test if pagination should be shown. If there is only 1 page avalible then
	 * pagination need no be shown.
	 * 
	 * @return true if pagination does not need to be displayed.
	 */
	public boolean getShowPagination() {
		return !(!isNotLast() && !isNotFirst());
	}

	/**
	 * Returns the current page number +1. Used to make visible pagination start
	 * at 1 instead of 0.
	 * 
	 * @return
	 */
	public int getThisPageNice() {
		return thisPage + 1;
	}
}
