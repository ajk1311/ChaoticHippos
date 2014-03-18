package com.chaotichippos.finalproject.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.util.PreferenceHelper;

/**
 * {@link android.app.Activity} that lets the user select which type of user he or she is
 */
public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

		findViewById(R.id.start_btn_student).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						showContent(PreferenceHelper.UserType.STUDENT);
					}
				});

		findViewById(R.id.start_btn_instructor).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						showContent(PreferenceHelper.UserType.INSTRUCTOR);
					}
				});
    }

	/** Shows the correct {@link android.app.Activity} based on the user type */
	private void showContent(PreferenceHelper.UserType userType) {
		PreferenceHelper.get().setUserType(userType);
		startActivity(new Intent(this, userType.getActivityClass())
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}
}
