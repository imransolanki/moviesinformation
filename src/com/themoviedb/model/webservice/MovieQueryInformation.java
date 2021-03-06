package com.themoviedb.model.webservice;

import com.google.gson.annotations.Expose;

/**
 * @author Imran
 *
 */
public class MovieQueryInformation {

	@Expose
	public int page;

	@Expose
	public MovieSuggestionInformation[] results;

	@Expose
	public int total_pages;

	@Expose
	public int total_results;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public MovieSuggestionInformation[] getResults() {
		return results;
	}

	public void setResults(MovieSuggestionInformation[] results) {
		this.results = results;
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}

	public int getTotal_results() {
		return total_results;
	}

	public void setTotal_results(int total_results) {
		this.total_results = total_results;
	}
}
