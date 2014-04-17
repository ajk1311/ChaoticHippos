package com.chaotichippos.finalproject.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Submission;
import com.chaotichippos.finalproject.app.model.Test;
import com.chaotichippos.finalproject.app.view.CompleteFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.MultipleChoiceAnswerView;
import com.chaotichippos.finalproject.app.view.QuestionViewer;
import com.chaotichippos.finalproject.app.view.StudentMatchingView;
import com.chaotichippos.finalproject.app.view.TrueFalseCreateView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class StudentActivity extends MainActivity {

	private static final String TAG = StudentActivity.class.getSimpleName();

	private static final String KEY_SAVE_SUBMISSION = TAG + ".saveSubmission";

	private Submission mCurrentSubmission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mCurrentSubmission = savedInstanceState.getParcelable(KEY_SAVE_SUBMISSION);
		}
	}

	@Override
	protected void onTestLoaded(final Test currentTest) {
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
					if (mCurrentSubmission == null || mCurrentSubmission.isReady()) {
						startNewSubmission(currentTest);
					}
				} else {
					// TODO handle error
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getQuestionListFragment().setQuestionListEditable(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// TODO save submission as not ready
		List<Question> questions = getQuestionListFragment().getQuestionList();
		for (Question question : questions) {

		}
	}

	@Override
	protected void setupDualPaneMenu(MenuInflater inflater, Menu menu) {

	}

	@Override
	protected void setupSinglePaneMenu(MenuInflater inflater, Menu menu, boolean open) {
		if (!open) {
			menu.add(Menu.NONE, R.id.menu_option_switch, Menu.NONE, "Switch to Instructor")
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			menu.add(Menu.NONE, R.id.menu_option_submit_answers, Menu.NONE, "Submit answers")
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		} else {
			menu.clear();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_option_switch:
				startActivity(new Intent(this, InstructorActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
				return true;

			case R.id.menu_option_submit_answers:
				
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void showViewForQuestion(Question question) {
		View view = null;
		switch (question.getType()) {
			case FILL_IN_THE_BLANK:
				view = new CompleteFillInTheBlankView(this);
				break;

			case MULTIPLE_CHOICE:
                view = new MultipleChoiceAnswerView(this);
				break;

			case MATCHING:
				view = new StudentMatchingView(this);
				break;

			case TRUE_OR_FALSE:
                view = new TrueFalseCreateView(this);
				break;
		}
		savePreviousAnswer();
		getContentContainer().removeAllViews();
		getContentContainer().addView(view);
		((QuestionViewer) view).setQuestion(question);
		((QuestionViewer) view).setAnswer(mCurrentSubmission.getAnswer(question.getObjectId()));
	}

	private void savePreviousAnswer() {
		if (getContentContainer().getChildCount() == 0) {
			return;
		}
		final View view = getContentContainer().getChildAt(0);
		if (view instanceof QuestionViewer) {
			Answer answer = ((QuestionViewer) view).getAnswer();
			mCurrentSubmission.putAnswer(answer);
		}
	}
}