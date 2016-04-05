/**
 * 
 */
package com.enixta.platform.buysmaart.model;

/**
 * @author nbattula
 *
 */
public class SearchInput {
	
	private String query;
	private long maxResults;
	private boolean isFuzzySearch;
	private boolean isAllWordsMust;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public long getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(long maxResults) {
		this.maxResults = maxResults;
	}
	public boolean isFuzzySearch() {
		return isFuzzySearch;
	}
	public void setFuzzySearch(boolean isFuzzySearch) {
		this.isFuzzySearch = isFuzzySearch;
	}
	public boolean isAllWordsMust() {
		return isAllWordsMust;
	}
	public void setAllWordsMust(boolean isAllWordsMust) {
		this.isAllWordsMust = isAllWordsMust;
	}
	
	
}
