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
import android.widget.Toast;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.event.DisplayQuestionEvent;
import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.activity.InstructorActivity;
import com.chaotichippos.finalproject.app.activity.MainActivity;
import com.chaotichippos.finalproject.app.dialog.ProgressDialogFragment;
import com.chaotichippos.finalproject.app.dialog.YesNoDialogFragment;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Submission;
import com.chaotichippos.finalproject.app.model.Test;
import com.chaotichippos.finalproject.app.view.CompleteFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.MultipleChoiceAnswerView;
import com.chaotichippos.finalproject.app.view.QuestionViewer;
import com.chaotichippos.finalproject.app.view.StudentMatchingView;
import com.chaotichippos.finalproject.app.view.TrueFalseCompleteView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.util.List;

public class StudentTestFragment extends Fragment {

	public static final String TAG = StudentEmptyFragment.class.getSimpleName();

	private static final String KEY_SAVE_SUBMISSION = TAG + ".saveSubmission";

	private Submission mCurrentSubmission;

	private MainActivity mMainActivity;

	private FrameLayout mContainer;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof  MainActivity) {
			mMainActivity = ((MainActivity) activity);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			mCurrentSubmission = savedInstanceState.getParcelable(KEY_SAVE_SUBMISSION);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(KEY_SAVE_SUBMISSION, mCurrentSubmission);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.add(Menu.NONE, R.id.menu_option_switch, Menu.NONE, "Switch to Instructor")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(Menu.NONE, R.id.menu_option_submit_answers, Menu.NONE, "Submit answers")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_option_switch:
				startActivity(new Intent(getActivity(), InstructorActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
				return true;

			case R.id.menu_option_submit_answers:
				ensureCompleteAnswers();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void ensureCompleteAnswers() {
		int position = 0;
		boolean incomplete = false;
		final List<Question> questions = mMainActivity.getQuestionListFragment().getQuestionList();
		for (int i = 0, sz = questions.size(); i < sz; i++) {
			if (!questions.get(i).isComplete()) {
				incomplete = true;
				position = i;
				break;
			}
		}
		if (incomplete) {
			final int incompletePosition = position;
			final YesNoDialogFragment dialog = YesNoDialogFragment.create(
					"You have some unanswered questions. Do you wish to continue with submission?");
			dialog.setListener(new YesNoDialogFragment.YesNoListener() {
				@Override
				public void onYes() {
					gradeAndSubmitAnswers();
				}
				@Override
				public void onNo() {
					mMainActivity.getMainPane().openPane();
					mMainActivity.getQuestionListFragment().scrollToQuestion(incompletePosition);
				}
			});
			dialog.show(getFragmentManager(), "yesNo");
		} else {
			gradeAndSubmitAnswers();
		}
	}

	private void gradeAndSubmitAnswers() {
		double grade = 0.0f;
		for (Question question : mMainActivity.getQuestionListFragment().getQuestionList()) {
			final Answer.Results results = Answer.checkAnswer(question,
					mCurrentSubmission.getAnswer(question.getObjectId()));
			grade += results.score;
			mCurrentSubmission.setAnswer(question.getObjectId(), results.data);
		}
		final double finalGrade = grade;
		mCurrentSubmission.setGrade(grade);
        mCurrentSubmission.setReady(true);
		final ProgressDialogFragment dialog = ProgressDialogFragment.create("Submitting test...");
		dialog.show(getFragmentManager(), null);
		mCurrentSubmission.toParseObject().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				dialog.dismiss();
				if (e == null) {
					// TODO show grade popup
					Toast.makeText(mMainActivity.getApplicationContext(),
							"Thank you! Your answers have been submitted",
							Toast.LENGTH_LONG)
							.show();
					if (isAdded()) mMainActivity.finish();
				} else {
					Toast.makeText(App.getContext(),
							"Sorry, there was an error processing your request: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
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
		if (savedInstanceState == null) {
			loadSubmission();
		}
		App.getEventBus().register(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		App.getEventBus().unregister(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (!mCurrentSubmission.isReady()) {
			savePreviousAnswer();
			mCurrentSubmission.toParseObject().saveInBackground();
		}
	}

	private void loadSubmission() {
		final Test currentTest = mMainActivity.getCurrentTest();
		ParseQuery<ParseObject> submissionQuery = ParseQuery.getQuery(Submission.TAG);
		submissionQuery.orderByDescending("createdAt");
		submissionQuery.whereEqualTo("ready", false);
		submissionQuery.whereEqualTo("parentExam", currentTest.getObjectId());
		submissionQuery.setLimit(1);
		submissionQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject submission, ParseException e) {
				if (e == null) {
					mCurrentSubmission = new Submission(submission);
					if (mCurrentSubmission.isReady()) {
						startNewSubmission(currentTest);
					}
				} else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
					startNewSubmission(currentTest);
				} else {
					Toast.makeText(App.getContext(),
							"Sorry, there was an error processing your request: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private void startNewSubmission(Test currentTest) {
		final ParseObject submission = new ParseObject(Submission.TAG);
		submission.put(Submission.KEY_READY, false);
		submission.put(Submission.KEY_PARENT_EXAM, currentTest.getObjectId());
		submission.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				mCurrentSubmission = new Submission(submission);
			}
		});
	}

	@Subscribe
	public void onQuestionSelected(DisplayQuestionEvent event) {
		View view = null;
		final Question question = event.getQuestion();
		switch (question.getType()) {
			case FILL_IN_THE_BLANK:
				view = new CompleteFillInTheBlankView(getActivity());
				break;

			case MULTIPLE_CHOICE:
				view = new MultipleChoiceAnswerView(getActivity());
				break;

			case MATCHING:
				view = new StudentMatchingView(getActivity());
				break;

			case TRUE_OR_FALSE:
				view = new TrueFalseCompleteView(getActivity());
				break;
		}
		savePreviousAnswer();
		mContainer.removeAllViews();
		mContainer.addView(view);
		((QuestionViewer) view).setQuestion(event.getIndex(), question);
		((QuestionViewer) view).setAnswer(mCurrentSubmission.getAnswer(question.getObjectId()));
	}


	private void savePreviousAnswer() {
		if (mContainer.getChildCount() == 0) {
			return;
		}
		final View view = mContainer.getChildAt(0);
		if (view instanceof QuestionViewer) {
			Answer answer = ((QuestionViewer) view).getAnswer();
			mCurrentSubmission.putAnswer(answer);
		}
	}
}