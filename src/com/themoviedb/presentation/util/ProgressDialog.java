package com.themoviedb.presentation.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.Spinner;

import com.themoviedb.MovieInformationApp;
import com.themoviedb.R;

/**
 * Utility class to display custom {@link ProgressDialog} and {@link Spinner}
 * 
 * @author Imran
 * 
 */
public class ProgressDialog {
	private static ProgressDialog _progressDialogBase = null;
	private static Dialog _dialog = null;
	private static Dialog spinner = null;

	/**
	 * Default constructor
	 */
	private ProgressDialog() {
	}

	/**
	 * For making this class singleton
	 * 
	 * @return Progress _dialog instance
	 */
	public static ProgressDialog getInstance() {
		if (_progressDialogBase == null) {
			_progressDialogBase = new ProgressDialog();
		}
		return _progressDialogBase;
	}

	/**
	 * Function starts the {@link ProgressDialog}
	 * 
	 */
	public void show() {
		if (_dialog != null && _dialog.isShowing()) {
			Log.i(MovieInformationApp.TAG,
					"No need to show again.Progress dialog is already visible");
			return;
		}
		_dialog = _showDialog(MovieInformationApp.sActivityCtx);
		Log.i(MovieInformationApp.TAG, "Progress dialog is visible");
	}

	/**
	 * Function starts the {@link Spinner}
	 */
	public void showSpinner() {
		spinner = _showSpinnerDialog(MovieInformationApp.sActivityCtx);
	}

	/**
	 * Function Dismisses {@link ProgressDialog}
	 */
	public void dismiss() {
		if (_dialog != null && _dialog.isShowing()) {
			Log.i(MovieInformationApp.TAG, "Dismissing Progress dialog.");
			_dialog.dismiss();
			_dialog = null;
		}
	}

	/**
	 * Function dismisses {@link Spinner}
	 */
	public void dismissSpinner() {
		if (spinner != null && spinner.isShowing()) {
			spinner.dismiss();
			spinner = null;
		}
	}

	/**
	 * Private function to create custom dialog
	 * 
	 * @param context
	 * @return
	 */
	private Dialog _showDialog(Context context) {
		if (context == null) {
			return null;
		}
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.movie_details_loading);
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}

	/**
	 * Function creates custom spinner
	 * 
	 * @param context
	 * @return
	 */
	private Dialog _showSpinnerDialog(Context context) {
		if (context == null) {
			return null;
		}
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progress_dialog);
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}

}
