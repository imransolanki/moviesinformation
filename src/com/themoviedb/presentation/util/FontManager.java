package com.themoviedb.presentation.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Manager class which handles fonts used in application
 * 
 * @author Imran
 * 
 */
public class FontManager {

	private ArrayList<String> _names = null;
	private ArrayList<Typeface> _typefaces = null;
	private Context _context = null;
	private static FontManager _font = null;

	private FontManager(Context context) {
		_context = context;
		_names = new ArrayList<String>();
		_typefaces = new ArrayList<Typeface>();

	}

	public static FontManager getInstance(Context context) {
		if (_font == null) {
			_font = new FontManager(context);
		}

		return _font;
	}

	/**
	 * Method to get the typeface object for a specific asset name supplied
	 * 
	 * @param assetName
	 * @return
	 */
	public Typeface getTypeface(String assetName) {
		if (assetName == null) {
			return null;
		}

		Typeface tf = null;
		assetName = assetName.trim();

		int assetNamePosition = _names.indexOf(assetName);

		// font name is present in our array list
		if (assetNamePosition != -1) {

			if (assetNamePosition < _typefaces.size()) {
				// get the typeface object at the same index
				tf = _typefaces.get(assetNamePosition);
			}

			if (tf != null) {
				// Log.i( "Font", "Found "+ assetName + " at position "+
				// assetNamePosition );
				// we have already loaded this font, so return it.
				return tf;
			} else {

				// try to load it from assets
				tf = _createTypefaceFromAsset(assetName);
				if (tf != null) {
					_typefaces.add(assetNamePosition, tf);
				}

				return tf;
			}

		} else {
			// some new font is added in the application. try to load that font.
			// if found, add both the name and the typeface to the respective
			// lists
			tf = _createTypefaceFromAsset(assetName);
			if (tf != null) {
				_typefaces.add(tf);
				_names.add(assetName);
				// Log.i( "Font", "Added "+ assetName);
			}
			return tf;
		}

	}

	/**
	 * Create typeface from asset file
	 * 
	 * @param assetName
	 * @return typeface
	 */
	private Typeface _createTypefaceFromAsset(String assetName) {
		try {
			// otherwise, load it, save it same position in typeface list
			// and return
			Typeface tf = Typeface.createFromAsset(_context.getAssets(),
					"fonts/" + assetName);
			// Log.i("Font","Creating "+ assetName+" font from asset");
			return tf;
		} catch (Exception e) {
			return null;
		}
	}

}
