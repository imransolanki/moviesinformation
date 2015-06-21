package com.themoviedb.presentation;

import java.io.IOException;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.themoviedb.MovieInformationApp;
import com.themoviedb.R;
import com.themoviedb.model.webservice.MovieDetailedInformation;
import com.themoviedb.model.webservice.ProductionCompanies;
import com.themoviedb.presentation.util.ProgressDialog;
import com.themoviedb.utils.WebServiceUtils;

/**
 * Fragment class to display Movie details
 * 
 * @author Imran
 * 
 */
public class MovieDetailFragment extends Fragment {

	/**
	 * Constant which is used to specify movie-id key when starting a fragment
	 */
	public static final String KEY_MOVIE_DETAIL_ID = "keyMovieDetails";

	private View _view;

	/**
	 * Movie Poster
	 */
	private ImageView _relativeLayoutImage;
	/**
	 * Movie Information (title,home page,overview,production companies)
	 */
	private TextView _tvTitle = null, _tvHomePageUrl = null,
			_tvOverview = null, _tvProductionCompanies = null;
	/**
	 * CopyRight, Adult Symobols
	 */
	private TextView _tvAdultState = null, _tvCopyrightSymbol = null;

	/**
	 * Movie Rating
	 */
	private RatingBar _movieRating = null;

	private MovieDetailedInformation _movieDetailedInformation;

	private int _movieDetails = -1;

	/**
	 * Default Constructor
	 */
	public MovieDetailFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_view = inflater.inflate(R.layout.fragment_movie_details, container,
				false);
		_initUi();

		/**
		 * Retrieve basic movie details
		 */
		Bundle bundle = getArguments();
		if (bundle != null) {
			_movieDetails = bundle.getInt(KEY_MOVIE_DETAIL_ID);

			if (_movieDetails != -1) {
				// user has selected card from auto complete list
				Log.i(MovieInformationApp.TAG, "movie-details=" + _movieDetails);
				final String url = WebServiceUtils
						.getMoviesDetailUrl(_movieDetails);
				MovieDetailedInformationFetcher movieFetcher = new MovieDetailedInformationFetcher();
				movieFetcher.execute(url);
			}
		}

		return _view;
	}

	@Override
	public void onResume() {
		_hideKeyboard();
		super.onResume();
	}

	/**
	 * Function hides the soft keyboard
	 */
	private void _hideKeyboard() {
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * Function populates the screen with movie-details
	 */
	private void _populateMovieDetails() {

		/**
		 * Set Title
		 */
		String title = _movieDetailedInformation.getTitle() + " ("
				+ _movieDetailedInformation.getRelease_date() + ")";
		setMovieTitle(title);

		/**
		 * Set Rating
		 */
		setMovieRating((int) _movieDetailedInformation.getPopularity());

		/**
		 * Set Adult Symbol(if required)
		 */
		enableAdultSymbol(_movieDetailedInformation.isAdult());

		/**
		 * Display Home Page URL
		 */
		String homePage = _movieDetailedInformation.getHomepage();
		if (homePage != null && !homePage.isEmpty()) {
			setMovieHomePage(_movieDetailedInformation.getHomepage());
		}

		/**
		 * Display Movie Overview
		 */
		setMovieOverviewDetail(_movieDetailedInformation.getOverview());

		/**
		 * Display Production companies names
		 */
		ProductionCompanies[] productionCompanies = _movieDetailedInformation
				.getProduction_companies();
		String productionCompanyNames = "";
		for (ProductionCompanies productionCompanies2 : productionCompanies) {
			productionCompanyNames = productionCompanyNames + ","
					+ productionCompanies2.getName();
		}
		if (!productionCompanyNames.isEmpty()) {
			/**
			 * Enable copyright symbols as there are production companies
			 * associated with the movie
			 */
			enableCopyrightSymbol(true);
			productionCompanyNames = productionCompanyNames
					.substring(productionCompanyNames.indexOf(',') + 1);
			setMovieProductionCompanies(productionCompanyNames);
		}

	}

	/**
	 * Function to Set Movie Title
	 * 
	 * @param movieTitle
	 *            title of a movie
	 */
	public void setMovieTitle(final String movieTitle) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (_tvTitle != null) {
					_tvTitle.setText(movieTitle);
				}
			}
		});
	}

	/**
	 * Function to Set Rating of given Movie
	 * 
	 * @param rating
	 *            rating
	 */
	public void setMovieRating(final float rating) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (_movieRating != null) {
					_movieRating.setProgress((int) rating);
				}
			}
		});
	}

	/**
	 * Function to set Home Page(website link) of Movie
	 * 
	 * @param homePageUrl
	 */
	public void setMovieHomePage(final String homePageUrl) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (_tvHomePageUrl != null) {
					String text = "<a href='" + homePageUrl + "'>HomePage</a>";
					_tvHomePageUrl.setText(Html.fromHtml(text));
				}
			}
		});
	}

	/**
	 * Function to set details of Movie
	 * 
	 * @param movieOverview
	 *            details
	 */
	public void setMovieOverviewDetail(final String movieOverview) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (_tvOverview != null) {
					_tvOverview.setText(movieOverview);
				}
			}
		});
	}

	/**
	 * Function to set production companies of a Movie
	 * 
	 * @param productionCompanies
	 *            product companies names separated by comma
	 */
	public void setMovieProductionCompanies(final String productionCompanies) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (_tvProductionCompanies != null) {
					_tvProductionCompanies.setText(productionCompanies);
				}
			}
		});
	}

	/**
	 * Function to display copyright symbol
	 * 
	 * @param enable
	 *            true to enable copyright symbol, else false
	 */
	public void enableCopyrightSymbol(final boolean enable) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (enable) {
					_tvCopyrightSymbol.setVisibility(View.VISIBLE);
				} else {
					_tvCopyrightSymbol.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * Function to display Adult symbol
	 * 
	 * @param enable
	 *            true to enable adult symbol, else false
	 */
	public void enableAdultSymbol(final boolean enable) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (enable) {
					_tvAdultState.setVisibility(View.VISIBLE);
				} else {
					_tvAdultState.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * Initializes UI
	 */
	private void _initUi() {
		_relativeLayoutImage = (ImageView) _view
				.findViewById(R.id.rlMovieImage);
		_tvTitle = (TextView) _view.findViewById(R.id.tv_movie_title);
		_tvHomePageUrl = (TextView) _view.findViewById(R.id.tv_movie_home_page);
		_tvOverview = (TextView) _view.findViewById(R.id.tv_movie_overview);
		_tvProductionCompanies = (TextView) _view
				.findViewById(R.id.tv_movie_production_companies);
		_movieRating = (RatingBar) _view.findViewById(R.id.movie_rating);
		_tvAdultState = (TextView) _view.findViewById(R.id.tv_adult_symbol);
		_tvCopyrightSymbol = (TextView) _view
				.findViewById(R.id.tv_copyright_symbol);

		/**
		 * Linkify Home Page
		 */
		_tvHomePageUrl.setClickable(true);
		_tvHomePageUrl.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private class MovieDetailedInformationFetcher extends
			AsyncTask<String, Void, String> {

		private String _downloadedData = "";
		private DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.app_icon)
				.showImageOnFail(R.drawable.app_icon)
				.showImageForEmptyUri(R.drawable.app_icon).cacheInMemory(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		private ImageLoaderConfiguration _configuration = new ImageLoaderConfiguration.Builder(
				MovieInformationApp.sApplicationCtx)
				.defaultDisplayImageOptions(imageOptions).build();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/**
			 * Initializes ImageLoader class so that it is available to render
			 * images
			 */
			ImageLoader.getInstance().init(_configuration);
			// Start progress dialog
			ProgressDialog.getInstance().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String downloadedData = "";
			try {
				_downloadedData = WebServiceUtils.downloadUrl(params[0]);
			} catch (IOException e) {
				Log.e(MovieInformationApp.TAG, e.getMessage());
				e.printStackTrace();
			}
			return downloadedData;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (_downloadedData != null && !_downloadedData.isEmpty()) {
				Log.i(MovieInformationApp.TAG, "movieDetailedData="
						+ _downloadedData);
				Gson gson = new Gson();
				JsonElement jsonElement = new JsonParser()
						.parse(_downloadedData);
				_movieDetailedInformation = gson.fromJson(jsonElement,
						MovieDetailedInformation.class);

				_populateMovieDetails();
				String posterPath = _movieDetailedInformation.getPoster_path();
				if (posterPath != null && !posterPath.isEmpty()) {
					final String urlImage = WebServiceUtils
							.getMovieImageDownloadUrl(posterPath);
					Log.d(MovieInformationApp.TAG,
							"Starting image download.url=" + urlImage);

					ImageLoader.getInstance().displayImage(urlImage,
							_relativeLayoutImage);
				}
			}
			// Dismiss dialog
			ProgressDialog.getInstance().dismiss();
		}
	}

}
