package co.uk.techmeetup.misc;

import java.util.List;

public final class SearchResult {

	private final int resultCount;
	private final List<Object> results;

	public SearchResult(List<Object> results, int resultCount) {
		this.resultCount = resultCount;
		this.results = results;
	}

	public int getResultCount() {
		return resultCount;
	}

	public List<Object> getResults() {
		return results;
	}

}
