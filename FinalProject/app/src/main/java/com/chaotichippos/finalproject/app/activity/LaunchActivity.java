package com.chaotichippos.finalproject.app.activity;

import android.app.Activity;
import android.content.Intent;

import com.chaotichippos.finalproject.app.util.PreferenceHelper;

/**
 * Empty {@link android.app.Activity} that determines which Activity with content to show
 */
public class LaunchActivity extends Activity {

	@Override
	protected void onResume() {
		super.onResume();
		final PreferenceHelper.UserType currentUser = PreferenceHelper.get().getUserType();
		startActivity(new Intent(this, currentUser.getActivityClass())
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}
}
