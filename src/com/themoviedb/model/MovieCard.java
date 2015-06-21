package com.themoviedb.model;

/**
 * Model class for managing cards on Home screen
 * 
 * @author Imran
 * 
 */
public class MovieCard {

	private int _id;
	private String _title;
	private String _posterPath;

	/**
	 * Default constructor
	 */
	public MovieCard() {
	}

	/**
	 * Parameterized Constructor
	 * 
	 * @param id
	 *            movie id
	 * @param title
	 *            movie title
	 * @param posterPath
	 *            poster path for given movie
	 */
	public MovieCard(int id, String title, String posterPath) {
		super();
		this._id = id;
		this._title = title;
		this._posterPath = posterPath;
	}

	/**
	 * Function returning title
	 * 
	 * @return Title
	 */
	public String getTitle() {
		return _title;
	}

	/**
	 * Function sets the title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this._title = title;
	}

	/**
	 * Function returning movie-id
	 * 
	 * @return
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Function sets the movie-id
	 * 
	 * @param id
	 *            movie-id
	 */
	public void setId(int id) {
		this._id = id;
	}

	/**
	 * Function return poster path which is a file name present on a server
	 * 
	 * @return poster path
	 */
	public String getPosterPath() {
		return _posterPath;
	}

	/**
	 * Function sets the poster path which is a file name present on a server
	 * 
	 * @param posterPath
	 *            poster path
	 */
	public void setPosterPath(String posterPath) {
		this._posterPath = posterPath;
	}

}
