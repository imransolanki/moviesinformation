package com.themoviedb.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.themoviedb.MovieInformationApp;
import com.themoviedb.model.webservice.MovieQueryInformation;
import com.themoviedb.model.webservice.MovieSuggestionInformation;
import com.themoviedb.utils.WebServiceUtils;

/**
 * Content Provider to manager queries/suggestion for user given queries
 * 
 * @author Imran
 * 
 */
public class MovieInformationProvider extends ContentProvider {

	public static final String AUTHORITY = "com.themoviedb.MovieInformationProvider";

	public static final Uri SEARCH_URI = Uri.parse("content://" + AUTHORITY
			+ "/search");

	public static final Uri DETAILS_URI = Uri.parse("content://" + AUTHORITY
			+ "/details");

	private static final int SEARCH = 1;
	private static final int SUGGESTIONS = 2;

	// Defines a set of uris allowed with this content provider
	private static final UriMatcher mUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {

		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		// URI for "Go" button
		uriMatcher.addURI(AUTHORITY, "search", SEARCH);

		// URI for suggestions in Search Dialog
		uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,
				SUGGESTIONS);

		return uriMatcher;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;

		String jsonString = "";
		MatrixCursor mCursor = null;

		switch (mUriMatcher.match(uri)) {
		case SEARCH:
			/**
			 * When user presses search button to find movies which are similar
			 * to his input
			 */

			mCursor = new MatrixCursor(new String[] { "_id",
					SearchManager.SUGGEST_COLUMN_TEXT_1,
					SearchManager.SUGGEST_COLUMN_TEXT_2 });

			jsonString = getMovies(selectionArgs);

			// use GSON to build Java model objects
			JsonElement jsonElement = new JsonParser().parse(jsonString);
			Gson gson = new Gson();
			MovieQueryInformation movieQueryInfo = gson.fromJson(jsonElement,
					MovieQueryInformation.class);
			// Retrieve movies list
			MovieSuggestionInformation[] movies = movieQueryInfo.getResults();
			for (int i = 0; i < movies.length; i++) {
				int movie_id = movies[i].getId();
				String title = movies[i].getTitle();
				String posterPath = movies[i].getPoster_path();
				mCursor.addRow(new String[] { Integer.toString(movie_id),
						title, posterPath });
			}

			cursor = mCursor;
			break;

		case SUGGESTIONS:
			/**
			 * Find Suggestions
			 */
			mCursor = new MatrixCursor(new String[] { "_id",
					SearchManager.SUGGEST_COLUMN_TEXT_1,
					SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA });

			// Get suggestions
			jsonString = getMovies(selectionArgs);

			// use GSON to build Java model objects
			jsonElement = new JsonParser().parse(jsonString);
			gson = new Gson();
			movieQueryInfo = gson.fromJson(jsonElement,
					MovieQueryInformation.class);
			// Retrieve movies list
			if (movieQueryInfo == null) {
				break;
			}
			movies = movieQueryInfo.getResults();
			for (int i = 0; i < movies.length; i++) {
				Gson movieGson = new Gson();

				int movie_id = movies[i].getId();
				String suggestion_title = movies[i].getTitle();
				String movieDetails = movieGson.toJson(movies[i]);
				mCursor.addRow(new String[] { Integer.toString(movie_id),
						suggestion_title, movieDetails });
			}

			cursor = mCursor;
			break;

		}

		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Do Nothing
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// Do Nothing
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Do Nothing
		return null;
	}

	@Override
	public boolean onCreate() {
		// Do Nothing
		return false;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// Do Nothing
		return 0;
	}

	/**
	 * Function sends the suggestion / data as received from the server for
	 * given query
	 * 
	 * @param params
	 * @return
	 */
	private String getMovies(String[] params) {

		String data = "";

		String url = WebServiceUtils.getMoviesSuggestionUrl(params[0]);

		try {
			// Fetching the data from web service in background
			data = WebServiceUtils.downloadUrl(url);
		} catch (Exception e) {
			Log.d(MovieInformationApp.TAG, e.toString());
		}
		return data;
	}
}