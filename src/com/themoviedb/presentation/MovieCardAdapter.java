package com.themoviedb.presentation;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.themoviedb.MovieInformationApp;
import com.themoviedb.R;
import com.themoviedb.model.MovieCard;
import com.themoviedb.utils.WebServiceUtils;

/**
 * Adapter class which is used to populate {@link HomeFragment} cards of movies
 * 
 * @author Imran
 * 
 */
public class MovieCardAdapter extends
		RecyclerView.Adapter<MovieCardAdapter.MovieViewHolder> {

	private List<MovieCard> _list;
	private DisplayImageOptions imageOptions;
	private ImageLoaderConfiguration _configuration;

	public MovieCardAdapter(List<MovieCard> cardsData) {
		this._list = cardsData;
		imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.app_icon)
				.showImageOnFail(R.drawable.app_icon)
				.showImageForEmptyUri(R.drawable.app_icon).cacheInMemory(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		_configuration = new ImageLoaderConfiguration.Builder(
				MovieInformationApp.sApplicationCtx)
				.defaultDisplayImageOptions(imageOptions).build();
		ImageLoader.getInstance().init(_configuration);
	}

	public static class MovieViewHolder extends RecyclerView.ViewHolder {
		CardView cardView;
		TextView movieTitle;
		ImageView movieLogo;

		public MovieViewHolder(View view) {
			super(view);

			cardView = (CardView) view.findViewById(R.id.movie_card);
			movieLogo = (ImageView) view.findViewById(R.id.image_movie_logo);
			movieTitle = (TextView) view.findViewById(R.id.movie_title);
			cardView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Integer movieCard = (Integer) view.getTag();
					_startMovieDetailFragment(cardView, movieCard);
				}

				private void _startMovieDetailFragment(final CardView cardView,
						final Integer movieCard) {

					Fragment movieDetailsFragment = new MovieDetailFragment();
					Bundle bundle = new Bundle();

					bundle.putInt(MovieDetailFragment.KEY_MOVIE_DETAIL_ID,
							movieCard);
					movieDetailsFragment.setArguments(bundle);

					FragmentTransaction fragmentTransaction = ((Activity) cardView
							.getContext()).getFragmentManager()
							.beginTransaction();

					fragmentTransaction.replace(
							R.id.frame_layout_fragment_container,
							movieDetailsFragment,
							MovieDetailFragment.class.getName());
					fragmentTransaction.commit();
				}

			});

		}
	}

	@Override
	public int getItemCount() {
		return _list.size();
	}

	@Override
	public void onBindViewHolder(MovieViewHolder movieHolder, int position) {
		String title = _list.get(position).getTitle();
		String posterPath = _list.get(position).getPosterPath();
		String url = WebServiceUtils.getMovieImageDownloadUrl(posterPath);

		/**
		 * Start Image Download. Image will be downloaded in background and
		 * would be set to ImageView after download is finished
		 */
		ImageLoader.getInstance().displayImage(url, movieHolder.movieLogo);
		/**
		 * Set title for movie
		 */
		movieHolder.movieTitle.setText(title);
		/**
		 * Set Tag, this data is required when we handle click or any other
		 * action of specific card
		 */
		movieHolder.cardView.setTag(_list.get(position).getId());
	}

	@Override
	public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.card, viewGroup, false);
		MovieViewHolder viewHolder = new MovieViewHolder(view);

		return viewHolder;
	}
}
