package com.chaotichippos.finalproject.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.activity.InstructorActivity;
import com.chaotichippos.finalproject.app.activity.StudentActivity;
import com.chaotichippos.finalproject.app.dialog.ProgressDialogFragment;
import com.chaotichippos.finalproject.app.dialog.YesNoDialogFragment;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Test;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

public class InstructorDataFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		App.getEventBus().register(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		App.getEventBus().unregister(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO fetch submissions
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.add(Menu.NONE, R.id.menu_option_switch, Menu.NONE, "Switch to Student")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(Menu.NONE, R.id.menu_option_new_exam, Menu.NONE, "Start new Test")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_option_switch:
				startActivity(new Intent(getActivity(), StudentActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
				return true;

			case R.id.menu_option_new_exam:
				startNewTest();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void ensureStartNewTest() {
		final YesNoDialogFragment dialog = YesNoDialogFragment.create(
				"Are you sure you want to start a new test? " +
						"The old test will no longer be available to students.");
		dialog.setListener(new YesNoDialogFragment.YesNoListener() {
			@Override
			public void onYes() {
				startNewTest();
			}
			@Override
			public void onNo() {
			}
		});
		dialog.show(getFragmentManager(), null);
	}

	private void startNewTest() {
		final ProgressDialogFragment dialog = ProgressDialogFragment.create("Creating new test...");
		dialog.show(getFragmentManager(), "progress");
		ParseObject test = new ParseObject(Test.TAG);
		test.put(Test.KEY_READY, false);
		test.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				dialog.dismiss();
				if (e == null) {
					if (isAdded()) {
						final Intent intent = new Intent(getActivity(), InstructorActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				} else {
					Toast.makeText(App.getContext(),
							"Sorry, there was an error processing your request: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	@Subscribe
	public void onQuestionSelected(Question question) {
		// TODO show appropriate graph
	}
}
