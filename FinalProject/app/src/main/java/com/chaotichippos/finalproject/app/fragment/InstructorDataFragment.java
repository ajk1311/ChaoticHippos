package com.chaotichippos.finalproject.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.activity.InstructorActivity;
import com.chaotichippos.finalproject.app.activity.MainActivity;
import com.chaotichippos.finalproject.app.activity.StudentActivity;
import com.chaotichippos.finalproject.app.dialog.ProgressDialogFragment;
import com.chaotichippos.finalproject.app.dialog.YesNoDialogFragment;
import com.chaotichippos.finalproject.app.event.DisplayQuestionEvent;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Submission;
import com.chaotichippos.finalproject.app.model.Test;
import com.chaotichippos.finalproject.app.view.ExamScoresBarGraph;
import com.chaotichippos.finalproject.app.view.MatchingBlankPieGraph;
import com.chaotichippos.finalproject.app.view.TrueFalseMultiChoicePieGraph;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class InstructorDataFragment extends Fragment {

	private static final String KEY_SAVE_SUBMISSIONS = "saveSubmissions";

	private FrameLayout mContainer;
	private MainActivity mMainActivity;
	private TextView mOverallHeaderView;
	private ArrayList<Submission> mSubmissions;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			mMainActivity = (MainActivity) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContainer = (FrameLayout) inflater
				.inflate(R.layout.fragment_student_test, container, false);
		return mContainer;
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
		getActivity().getActionBar().setTitle(mMainActivity.getCurrentTest().getName());
		for (Question question : mMainActivity.getQuestionListFragment().getQuestionList()) {
			question.setComplete(true);
		}
		if (savedInstanceState == null) {
			loadSubmissions();
		} else {
			final ArrayList<Submission> submissions = savedInstanceState
					.getParcelableArrayList(KEY_SAVE_SUBMISSIONS);
			mSubmissions = submissions;
			showOverallStats(true);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(KEY_SAVE_SUBMISSIONS, mSubmissions);
	}

	private void loadSubmissions() {
		final Test currentTest = mMainActivity.getCurrentTest();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Submission.TAG);
		query.whereEqualTo(Submission.KEY_PARENT_EXAM, currentTest.getObjectId());
		query.whereEqualTo(Submission.KEY_READY, true);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				if (e == null || e.getCode() == ParseException.OBJECT_NOT_FOUND) {
					mSubmissions = new ArrayList<Submission>();
					for (ParseObject object : parseObjects) {
						mSubmissions.add(new Submission(object));
					}
					showOverallStats(true);
				} else {
					Toast.makeText(App.getContext(),
							"Sorry, there was an error with your request: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private void showOverallStats(boolean init) {
		if (init) {
			final ListView questionListView = mMainActivity.getQuestionListFragment().getListView();
			final ListAdapter adapter = questionListView.getAdapter();
			questionListView.setAdapter(null);

			mOverallHeaderView = (TextView) LayoutInflater.from(getActivity())
					.inflate(R.layout.question_list_item_textview, questionListView, false);
			mOverallHeaderView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mMainActivity.getMainPane().closePane();
					mMainActivity.getQuestionListFragment().clearSelection();
					mOverallHeaderView.setActivated(true);
					showOverallStats(false);
				}
			});
			mOverallHeaderView.setText("Overall stats");
			questionListView.addHeaderView(mOverallHeaderView);
			questionListView.setAdapter(adapter);
			mOverallHeaderView.setActivated(true);
		}

		final int max = mMainActivity.getQuestionListFragment().getQuestionList().size();
		final List<Float> scores = new ArrayList<Float>();
		for (Submission submission : mSubmissions) {
			scores.add((float) (submission.getGrade() / max) * 100.0f);
		}
		mContainer.removeAllViews();
		mContainer.addView(new ExamScoresBarGraph(getActivity(), scores));
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
	public void onQuestionSelected(DisplayQuestionEvent event) {
		mOverallHeaderView.setActivated(false);
		final Question question = event.getQuestion();
		switch (question.getType()) {
			case TRUE_OR_FALSE:
			case MULTIPLE_CHOICE:
				showSinglePartQuestionGraph(event.getIndex(), question);
				break;

			case MATCHING:
			case FILL_IN_THE_BLANK:
				showMultiPartQuestionGraph(event.getIndex(), question);
				break;
		}
	}

	private void showSinglePartQuestionGraph(int index, Question question) {
		int correct, incorrect, unanswered;
		correct = incorrect = unanswered = 0;
		for (Submission submission : mSubmissions) {
			final String score = submission.getAnswer(question.getObjectId());
			switch ((int) Float.parseFloat(score)) {
				case 0:
					incorrect++;
					break;

				case 1:
					correct++;
					break;

				case -1:
					unanswered++;
					break;
			}
		}
		mContainer.removeAllViews();
		mContainer.addView(new TrueFalseMultiChoicePieGraph(getActivity(), index,
				correct, incorrect, unanswered));
	}

	private void showMultiPartQuestionGraph(int index, Question question) {
		int max = 0;
		final List<Float> scores = new ArrayList<Float>();
		for (Submission submission : mSubmissions) {
			final String answer = submission.getAnswer(question.getObjectId());
			final String[] scoreMax = answer.split("/");
			scores.add(Float.parseFloat(scoreMax[0]));
			if (max == 0) {
				max = Integer.parseInt(scoreMax[1]);
			}
		}
		mContainer.removeAllViews();
		mContainer.addView(new MatchingBlankPieGraph(getActivity(), index, max, scores));
	}
}
