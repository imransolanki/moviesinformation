package com.themoviedb.model.webservice;

import com.google.gson.annotations.Expose;

/**
 * @author Imran
 *
 */
public class Genres {

	@Expose
	public int id;
	@Expose
	public String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
