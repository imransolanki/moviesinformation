package com.themoviedb;

import android.app.Application;
import android.content.Context;

/**
 * Application class for {@link MovieInformationApp} application
 * 
 * @author Imran
 * 
 */
public class MovieInformationApp extends Application {

	/**
	 * Static variable which stores application context
	 */
	public static Context sApplicationCtx = null;

	/**
	 * Static variable which stores activity context
	 */
	public static Context sActivityCtx = null;

	public static final String TAG = "MoviesDemo";

	@Override
	public void onCreate() {
		super.onCreate();
		/**
		 * Initialize application context. This context is available throughout
		 * out the applications life cycle
		 */
		sApplicationCtx = this;
	}
}
