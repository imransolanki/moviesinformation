package com.themoviedb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.themoviedb.MovieInformationApp;
import com.themoviedb.R;

import android.util.Log;

/**
 * Utility class to make web-service calls
 * 
 * @author Imran
 * 
 */
public class WebServiceUtils {

	private static final String sAPI_KEY = "api_key=64a71ec25b99ea56d6184f31d8070b80";

	/** A method to download json data from url */
	public static String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an HTTP connection to communicate with URL
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to URL
			urlConnection.connect();

			// Reading data from URL
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.e(MovieInformationApp.TAG, "Exception while downloading url"
					+ e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/**
	 * Function which returns the URL for retrieving suggestions for movies
	 * 
	 * @param qry
	 *            query string
	 * @return
	 */
	public static String getMoviesSuggestionUrl(String qry) {
		/**
		 * Querying Movies based on user string
		 */
		try {
			qry = "query=" + URLEncoder.encode(qry, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// Building the parameters to the web service
		String parameters = qry + "&" + sAPI_KEY;

		// Building the URL to the web service
		String baseUrl = MovieInformationApp.sApplicationCtx.getResources()
				.getString(R.string.base_url);
		String url = baseUrl + "/search/movie" + "?" + parameters;
		return url;

	}

	/**
	 * Function which returns the URL for retrieving Movies Details URL
	 * 
	 * @param id
	 *            movie-id
	 * @return
	 */
	public static String getMoviesDetailUrl(int id) {
		/**
		 * Querying Movies based on user string
		 */
		String movie_id = "" + id;
		// Building the parameters to the web service
		String parameters = sAPI_KEY;

		// Building the URL to the web service
		String baseUrl = MovieInformationApp.sApplicationCtx.getResources()
				.getString(R.string.base_url);
		String url = baseUrl + "/movie/" + movie_id + "?" + parameters;
		return url;

	}

	public static String getMovieImageDownloadUrl(String filePath) {
		String BASE_URL_FOR_IMAGE = MovieInformationApp.sApplicationCtx
				.getResources().getString(R.string.base_image_url);
		String size = "original";
		final String url = BASE_URL_FOR_IMAGE + size + filePath;
		return url;
	}
}
