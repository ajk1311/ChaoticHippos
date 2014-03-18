package com.chaotichippos.finalproject.app.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.activity.InstructorActivity;
import com.chaotichippos.finalproject.app.activity.StartActivity;
import com.chaotichippos.finalproject.app.activity.StudentActivity;

/**
 * Singleton class that provides an easier interface to the
 * {@link android.content.SharedPreferences} of our app
 */
public class PreferenceHelper {

	private static final String TAG = PreferenceHelper.class.getSimpleName();

	private static final String KEY_USER_TYPE = TAG + ".USER_TYPE";

	/** Enum type that represents whether the user is a student or a professor */
	public static enum UserType {
		STUDENT (StudentActivity.class),
		INSTRUCTOR (InstructorActivity.class),
		UNSPECIFIED (StartActivity.class);

		private final Class<?> mActivityClass;

		private UserType(Class<?> activityClass) {
			mActivityClass = activityClass;
		}

		/** @return The {@link android.app.Activity} subclass to start for the selected UserType */
		public Class<?> getActivityClass() {
			return mActivityClass;
		}
	}

	/** The Singleton instance */
	private static PreferenceHelper sInstance;

	/**
	 * @return the Singleton instance of
	 * {@link com.chaotichippos.finalproject.app.util.PreferenceHelper}
	 */
	public static PreferenceHelper get() {
		if (sInstance == null) {
			synchronized (PreferenceHelper.class) {
				if (sInstance == null) {
					sInstance = new PreferenceHelper();
				}
			}
		}
		return sInstance;
	}

	/** Contains all of the application's preferences */
	private SharedPreferences mPrefs;

	/** Initializes the {@link android.content.SharedPreferences} member*/
	private PreferenceHelper() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
	}

	/** @return the type of user currently using the app, or UNSPECIFIED if not known */
	public UserType getUserType() {
		final int userTypeValue = mPrefs.getInt(KEY_USER_TYPE, UserType.UNSPECIFIED.ordinal());
		return UserType.values()[userTypeValue];
	}

	/** Saves which kind of user is currently using the app */
	public void setUserType(UserType userType) {
		mPrefs.edit().putInt(KEY_USER_TYPE, userType.ordinal()).apply();
	}
}
