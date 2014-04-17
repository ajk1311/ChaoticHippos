package com.chaotichippos.finalproject.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.dialog.QuestionAdditionDialogFragment;
import com.chaotichippos.finalproject.app.fragment.QuestionListFragment;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Test;
import com.chaotichippos.finalproject.app.view.QuestionViewer;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Base class for out application's two main {@link android.app.Activity}s.
 * All common behavior and functions should go in this class and subclasses
 * will implement the abstract methods defined here.
 */
public abstract class MainActivity extends Activity
		implements
		QuestionListFragment.OnQuestionSelectedListener,
		QuestionAdditionDialogFragment.OnQuestionTypeSelectedListener {

	/** Used for constants and logging */
	private static final String TAG = MainActivity.class.getSimpleName();

	/** Key for retrieving a visible dialog, if there is one */
	private static final String KEY_DIALOG_ADD_QUESTION = TAG + ".DialogAddQuestion";

	private static final String KEY_SAVE_TEST = TAG + ".Test";


	// Fields
	//================================================================

	/** The current test from the instructor */
	private Test mCurrentTest;

	/** The main layout for the content of the Activity */
	private SlidingPaneLayout mMainPane;

	/** A task to be run when the pane is done sliding, if it is indeed slidable */
	private static Runnable sPendingOperation = null;


	// Methods
	//================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMainPane = (SlidingPaneLayout) findViewById(R.id.main_pane);
		mMainPane.setPanelSlideListener(new MainPanelSlideListener());
		mMainPane.setShadowResource(R.drawable.pane_shadow);
		mMainPane.setParallaxDistance(getResources()
				.getDimensionPixelSize(R.dimen.question_list_parallax));
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.replace(R.id.list_fragment_container, new QuestionListFragment()).commit();
			loadTestFromServer();
			mMainPane.openPane();
		} else {
			mCurrentTest = savedInstanceState.getParcelable(KEY_SAVE_TEST);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		QuestionAdditionDialogFragment dialog = (QuestionAdditionDialogFragment)
				getFragmentManager().findFragmentByTag(KEY_DIALOG_ADD_QUESTION);
		if (dialog != null) {
			dialog.setOnQuestionTypeSelectedListener(this);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(KEY_SAVE_TEST, mCurrentTest);
	}

	public Test getCurrentTest() {
		return mCurrentTest;
	}

	protected void setCurrentTest(Test test) {
		mCurrentTest = test;
		getQuestionListFragment().onTestLoaded(mCurrentTest);
	}

	/**
	 * @return the {@link android.support.v4.widget.SlidingPaneLayout}
	 * that contains the list and content of the current user's view
	 */
	public SlidingPaneLayout getMainPane() {
		return mMainPane;
	}

	/** @return The {@link com.chaotichippos.finalproject.app.fragment.QuestionListFragment}
	 * containing the questions
	 */
	public QuestionListFragment getQuestionListFragment() {
		return (QuestionListFragment) getFragmentManager()
				.findFragmentById(R.id.list_fragment_container);
	}

	@Override
	public void onAddQuestionRequested() {
		final QuestionAdditionDialogFragment dialog = new QuestionAdditionDialogFragment();
		dialog.setOnQuestionTypeSelectedListener(this);
		dialog.show(getFragmentManager(), KEY_DIALOG_ADD_QUESTION);
	}

	@Override
	public void onQuestionSelected(final Question question) {
		showViewWhenAppropriate(question);
	}

	@Override
	public void onQuestionTypeSelected(final Question.Type type) {
		final ParseObject newQuestion = new ParseObject(Question.TAG);
		newQuestion.put(Question.KEY_TYPE, type.ordinal());
		newQuestion.put(Question.KEY_PARENT_TEST, mCurrentTest.getObjectId());
		newQuestion.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Question question = new Question(newQuestion);
					getQuestionListFragment().addQuestion(question);
					showViewWhenAppropriate(question);
				} else {
					// TODO handle error
				}
			}
		});
	}

	private void loadTestFromServer() {
		ParseQuery<ParseObject> testQuery = ParseQuery.getQuery(Test.TAG);
		testQuery.orderByDescending("createdAt");
		testQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject test, ParseException e) {
				if (e == null) {
					mCurrentTest = new Test(test);
					getQuestionListFragment().onTestLoaded(mCurrentTest);
					onTestLoaded(mCurrentTest);
				} else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
					mCurrentTest = null;
					getQuestionListFragment().onTestLoaded(mCurrentTest);
					onTestLoaded(mCurrentTest);
				} else {
					Toast.makeText(MainActivity.this, "Error loading test: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	protected void onTestLoaded(Test currentTest) {
		/* STUB */
	}

	/** Either shows a view for the given question immediately,
	 * or queues up an task that will show the view when the panel is closed
	 *
	 * @param question The question for which to display the view
	 */
	private void showViewWhenAppropriate(final Question question) {
		if (mMainPane.isSlideable() && mMainPane.isOpen()) {
			sPendingOperation = new Runnable() {
				@Override
				public void run() {
					App.getEventBus().post(question);
				}
			};
			mMainPane.closePane();
		} else {
			App.getEventBus().post(question);
		}
	}

	public void savePreviousQuestion(ViewGroup container) {
		if (container.getChildCount() == 0) {
			return;
		}
		final View current = container.getChildAt(0);
		if (current instanceof QuestionViewer) {
			final Question question = ((QuestionViewer) current).getQuestion();
            question.setComplete(((QuestionViewer) current).isQuestionComplete());
			getQuestionListFragment().updateQuestion(question);
		}
	}

	/** Responds to changes in the main sliding panel */
	private class MainPanelSlideListener extends SlidingPaneLayout.SimplePanelSlideListener {
		@Override
		public void onPanelOpened(View panel) {
		}

		@Override
		public void onPanelClosed(View panel) {
			if (sPendingOperation != null) {
				mMainPane.post(sPendingOperation);
				sPendingOperation = null;
			}
		}
	}
}
