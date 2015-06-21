package com.themoviedb.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.themoviedb.MovieInformationApp;
import com.themoviedb.R;
import com.themoviedb.model.MovieCard;
import com.themoviedb.presentation.util.ProgressDialog;
import com.themoviedb.provider.MovieInformationProvider;

/**
 * Launcher Activity of MovieInformation application
 * 
 * @author Imran
 * 
 */
public class ScrMain extends Activity implements LoaderCallbacks<Cursor> {

	private LoaderCallbacks<Cursor> _callbacks = null;

	private HomeFragment _homeFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scr_main);

		/**
		 * Initialize activity context which can be referred through out the
		 * application. We have only single activity but (in future) we may have
		 * to initialize this on start of every new activity.
		 */
		MovieInformationApp.sActivityCtx = this;

		/**
		 * Set the current activity to listen to Loader's callbacks
		 */
		_callbacks = this;

		if (savedInstanceState == null) {
			_homeFragment = new HomeFragment();
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();

			fragmentTransaction.add(R.id.frame_layout_fragment_container,
					_homeFragment);
			fragmentTransaction.commit();
		}

		_handleIntent(getIntent());

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		_handleIntent(intent);
	}

	/**
	 * Function handles all the intent which are received by the activity
	 * 
	 * @param intent
	 */
	private void _handleIntent(Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
			// Start Search
			String query = intent.getStringExtra(SearchManager.QUERY);
			if (query != null && !query.isEmpty()) {
				Log.d(MovieInformationApp.TAG, "Search has started.data="
						+ query);
				ProgressDialog.getInstance().showSpinner();
				doSearch(query);
			}
		} else if (intent.getAction().equals(Intent.ACTION_VIEW)) {
			// Display detail of selected movie
			String selectedData = intent
					.getStringExtra(SearchManager.EXTRA_DATA_KEY);
			Log.i(MovieInformationApp.TAG, "selectedMovie=" + selectedData);

			JSONObject json;
			Integer id = -1;
			try {
				json = new JSONObject(selectedData);
				id = (Integer) json.get("id");
			} catch (JSONException e) {
				Log.e(MovieInformationApp.TAG, e.getMessage());
				e.printStackTrace();
				return;
			}

			/**
			 * Start MovieDetailFragment and pass movie-id as parameter. Each
			 * movie has unique movie-id
			 */
			_startMovieDetailsFragment(id);
		}
	}

	/**
	 * Functions accepts movie-id as a parameter and starts new Fragment for
	 * showing details of a movie. The movie-id is used inside
	 * {@link MovieDetailFragment} to retrieve movie details from web server
	 * 
	 * @param id
	 */
	private void _startMovieDetailsFragment(Integer id) {
		Fragment movieDetailsFragment = new MovieDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(MovieDetailFragment.KEY_MOVIE_DETAIL_ID, id);
		movieDetailsFragment.setArguments(bundle);

		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout_fragment_container,
				movieDetailsFragment, MovieDetailFragment.class.getName());
		fragmentTransaction.commit();
	}

	/**
	 * Starts the new search for a given query
	 * 
	 * @param query
	 *            query
	 */
	private void doSearch(String query) {
		Bundle data = new Bundle();
		data.putString("query", query);
		getLoaderManager().restartLoader(0, data, _callbacks);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_options, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();

		// Current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
		CursorLoader cLoader = null;
		switch (arg0) {
		case 0:
			cLoader = new CursorLoader(getBaseContext(),
					MovieInformationProvider.SEARCH_URI, null, null,
					new String[] { query.getString("query") }, null);
			break;
		default:
			break;
		}
		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		ProgressDialog.getInstance().dismissSpinner();
		Log.d(MovieInformationApp.TAG, c.toString());
		showMovieCards(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// Do Nothing
	}

	/**
	 * Populate {@link HomeFragment} with cards which have movie details in it
	 * 
	 * @param c
	 *            cursor
	 */
	private void showMovieCards(Cursor c) {
		List<MovieCard> movieCardsList = new ArrayList<MovieCard>();

		int id = 0;
		String title = "", posterPath = "";
		MovieCard card;
		// populate list which contains movies details
		while (c.moveToNext()) {
			id = c.getInt(c.getColumnIndex("_id"));
			title = c.getString(c
					.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
			posterPath = c.getString(c
					.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2));
			card = new MovieCard(id, title, posterPath);
			movieCardsList.add(card);
		}

		final List<MovieCard> cardsOnHomeFragment = movieCardsList;

		/**
		 * Start HomeFragment and pass cards data to it
		 */
		Handler handler = new Handler(getMainLooper());
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				bundle.putSerializable(HomeFragment.CARDS_KEY,
						(Serializable) cardsOnHomeFragment);

				FragmentTransaction fragmentTransaction = getFragmentManager()
						.beginTransaction();

				HomeFragment homeFragment = new HomeFragment();
				homeFragment.setArguments(bundle);
				fragmentTransaction.replace(
						R.id.frame_layout_fragment_container, homeFragment,
						MovieDetailFragment.class.getName());
				fragmentTransaction.commit();
			}
		}, 0);
	}
}