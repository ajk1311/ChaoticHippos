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
import android.widget.TimePicker;

import com.chaotichippos.finalproject.app.R;

import java.util.Calendar;

public class TestInfoDialog extends DialogFragment {

	private static final int TIME_STEP = 5; // minutes

	private static String[] DISPLAY_TIMES = new String[] {
			"5 minutes",
			"10 minutes",
			"15 minutes",
			"20 minutes",
			"30 minutes",
			"35 minutes",
			"40 minutes",
			"45 minutes",
			"50 minutes",
			"55 minutes",
			"1 hour",
			"1 hour, 5 minutes",
			"1 hour, 20 minutes",
			"1 hour, 15 minutes",
			"1 hour, 20 minutes",
			"1 hour, 25 minutes",
			"1 hour, 30 minutes",
			"1 hour, 35 minutes",
			"1 hour, 40 minutes",
			"1 hour, 45 minutes",
			"1 hour, 50 minutes",
			"1 hour, 55 minutes",
			"2 hours",
			"2 hours, 5 minutes",
			"2 hours, 20 minutes",
			"2 hours, 15 minutes",
			"2 hours, 20 minutes",
			"2 hours, 25 minutes",
			"2 hours, 30 minutes",
			"2 hours, 35 minutes",
			"2 hours, 40 minutes",
			"2 hours, 45 minutes",
			"2 hours, 50 minutes",
			"2 hours, 55 minutes",
			"3 hours",
			"3 hours, 5 minutes",
			"3 hours, 20 minutes",
			"3 hours, 15 minutes",
			"3 hours, 20 minutes",
			"3 hours, 25 minutes",
			"3 hours, 30 minutes",
			"3 hours, 35 minutes",
			"3 hours, 40 minutes",
			"3 hours, 45 minutes",
			"3 hours, 50 minutes",
			"3 hours, 55 minutes"
	};

	public interface OnInfoSubmittedListener {
		public void onInfoSubmitted(String name, long duration, long expiration);
	}

	private EditText mNameField;
	private NumberPicker mDurationPicker;
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;

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

		mDatePicker = (DatePicker) customView.findViewById(R.id.test_info_date);
		setupExpirationPicker();

		mTimePicker = (TimePicker) customView.findViewById(R.id.test_info_time);
		setupTimePicker();

		return new AlertDialog.Builder(getActivity())
				.setView(customView)
				.setCancelable(false)
				.setTitle(R.string.test_info_title)
				.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						collectAndCheckInfo();
					}
				})
				.create();
	}

	private void setupDurationPicker() {
		mDurationPicker.setMaxValue(DISPLAY_TIMES.length - 1);
		mDurationPicker.setMinValue(0);
		mDurationPicker.setWrapSelectorWheel(false);
		mDurationPicker.setDisplayedValues(DISPLAY_TIMES);
	}

	private void setupExpirationPicker() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		mDatePicker.init(year, month, day, null);
	}

	private void setupTimePicker() {
		mTimePicker.setIs24HourView(false);
	}

	private void collectAndCheckInfo() {
		if (mNameField.getText().length() == 0) {
			mNameField.setError(getString(R.string.test_info_name_error));
			return;
		}

		if (mListener == null) {
			return;
		}

		final String name = mNameField.getText().toString().trim();

		final int duration = (mDurationPicker.getValue() + 1) * TIME_STEP;

		final Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
		c.set(Calendar.MONTH, mDatePicker.getMonth());
		c.set(Calendar.YEAR, mDatePicker.getYear());

		c.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
		c.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());

		System.currentTimeMillis();
		final long expiration = c.getTimeInMillis() / 1000L;
		mListener.onInfoSubmitted(name, duration, expiration);
	}
}
