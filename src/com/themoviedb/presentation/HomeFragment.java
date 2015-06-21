package com.themoviedb.presentation;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.themoviedb.R;
import com.themoviedb.model.MovieCard;

/**
 * Fragment to display Home fragment screen of application
 * 
 * @author Imran
 * 
 */
public class HomeFragment extends Fragment {

	public static final String CARDS_KEY = "cardsKey";

	private ArrayList<MovieCard> _cards = null;

	private View _view;
	private RecyclerView _recyclerView;
	private RecyclerView.Adapter<MovieCardAdapter.MovieViewHolder> _adapter;
	private RecyclerView.LayoutManager _layoutManager;

	/**
	 * Default constructor
	 */
	public HomeFragment() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_view = inflater.inflate(R.layout.fragment_scr_main, container, false);

		/**
		 * Initializes UI and view objects
		 */
		_initUi();

		Bundle bundle = getArguments();
		if (bundle != null) {
			_cards = (ArrayList<MovieCard>) bundle.getSerializable(CARDS_KEY);
			bundle.remove(CARDS_KEY);
			_populateCards(_cards);
		}
		return _view;
	}

	/**
	 * Initializes UI
	 */
	private void _initUi() {
		/**
		 * Initializing Card view and related components(LayoutManager,Adapter
		 * etc)
		 */
		_recyclerView = (RecyclerView) _view
				.findViewById(R.id.home_screen_recycler_view);
		_recyclerView.setHasFixedSize(true);

		/**
		 * Using GridLayout to display cards in a grid of column 2.
		 */
		_layoutManager = new GridLayoutManager(getActivity()
				.getApplicationContext(), 2);

		_recyclerView.setLayoutManager(_layoutManager);
		_recyclerView.setAdapter(null);
	}

	/**
	 * Function to populate cards based on the input
	 * 
	 * @param cards
	 */
	private void _populateCards(ArrayList<MovieCard> cards) {
		/**
		 * Set the adapter
		 */
		_adapter = new MovieCardAdapter(cards);
		_recyclerView.setAdapter(_adapter);
	}

}
