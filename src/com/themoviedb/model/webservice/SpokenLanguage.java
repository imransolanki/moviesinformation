package com.themoviedb.model.webservice;

import com.google.gson.annotations.Expose;

/**
 * @author Imran
 *
 */
public class SpokenLanguage {

	@Expose
	public String iso_639_1;
	@Expose
	public String name;

	public String getIso_639_1() {
		return iso_639_1;
	}

	public void setIso_639_1(String iso_639_1) {
		this.iso_639_1 = iso_639_1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
