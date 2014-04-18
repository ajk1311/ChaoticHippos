package com.chaotichippos.finalproject.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.chaotichippos.finalproject.app.R;

import java.util.Calendar;

public class TestInfoDialog extends DialogFragment {

	private static final int MAX_DURATION = 180; // minutes
	private static final int TIME_STEP = 5; // minutes

	private static String[] sDurationTimes;
	static {
		final int size = MAX_DURATION / TIME_STEP;
		sDurationTimes = new String[size];
		for (int i = 1; i <= size; i++) {
			sDurationTimes[i] = String.valueOf(i * TIME_STEP);
		}
	}

	public interface OnInfoSubmittedListener {
		public void onInfoSubmitted(String name, long duration, long expiration);
	}

	private EditText mNameField;
	private NumberPicker mDurationPicker;
	private DatePicker mExpirationPicker;

	private OnInfoSubmittedListener mListener;

	public void setOnInfoSubmittedListener(OnInfoSubmittedListener listener) {
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final View customView = LayoutInflater.from(getActivity())
				.inflate(R.layout.dialog_test_info, null);

		mNameField = (EditText) customView.findViewById(R.id.test_info_name);

		mDurationPicker = (NumberPicker) customView.findViewById(R.id.test_info_duration);
		setupDurationPicker();

		mExpirationPicker = (DatePicker) customView.findViewById(R.id.test_info_expiration);
		setupExpirationPicker();

		return new AlertDialog.Builder(getActivity())
				.setView(customView)
				.setCancelable(false)
				.setTitle("Enter your test info")
				.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						collectAndCheckInfo();
					}
				})
				.create();
	}

	private void setupDurationPicker() {
		mDurationPicker.setMaxValue(sDurationTimes.length - 1);
		mDurationPicker.setMinValue(0);
		mDurationPicker.setWrapSelectorWheel(false);
		mDurationPicker.setDisplayedValues(sDurationTimes);
	}

	private void setupExpirationPicker() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis() * 1000L);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		mExpirationPicker.init(year, month, day, null);
	}

	private void collectAndCheckInfo() {
		if (mNameField.getText().length() == 0) {
			mNameField.setError("Please enter a test name");
			return;
		}

		if (mListener == null) {
			return;
		}

		final String name = mNameField.getText().toString().trim();

		final int duration = Integer.parseInt(sDurationTimes[mDurationPicker.getValue()]);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, mExpirationPicker.getDayOfMonth());
		c.set(Calendar.MONTH, mExpirationPicker.getMonth());
		c.set(Calendar.YEAR, mExpirationPicker.getYear());
		final long expiration = c.getTimeInMillis() / 1000L;

		mListener.onInfoSubmitted(name, duration, expiration);
	}
}
