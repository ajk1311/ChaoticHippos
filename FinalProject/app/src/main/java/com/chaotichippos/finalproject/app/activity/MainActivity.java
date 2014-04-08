package com.chaotichippos.finalproject.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.dialog.QuestionAdditionDialogFragment;
import com.chaotichippos.finalproject.app.fragment.QuestionListFragment;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Test;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

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

	// Abstract methods
	//================================================================

	/**
	 * Called when both the list and content are visible to the user
	 *
	 * @param inflater The {@link android.view.MenuInflater} to inflate the desired menu
	 * @param menu The {@link android.view.Menu} to hold the menu options
	 */
	protected abstract void setupDualPaneMenu(MenuInflater inflater, Menu menu);

	/**
	 * Called when only either the list or content is visible to the user
	 *
	 * @param inflater The {@link android.view.MenuInflater} to inflate the desired menu
	 * @param menu The {@link android.view.Menu} to hold the menu options
	 * @param open {@code true} if the list is visible, {@code false} if the content is visible
	 */
	protected abstract void setupSinglePaneMenu(MenuInflater inflater, Menu menu, boolean open);

	/**
	 * Called when the user selects or adds a new question
	 *
	 * @param question The selected or added question
	 */
	protected abstract void showViewForQuestion(Question question);


	// Fields
	//================================================================

	/** The current test from the instructor */
	private Test mCurrentTest;

	/** The main layout for the content of the Activity */
	private SlidingPaneLayout mMainPane;

	/** The container layout where the content for each question goes */
	private FrameLayout mContentContainer;

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
		mContentContainer = (FrameLayout) findViewById(R.id.content_container);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.replace(R.id.list_fragment_container, new QuestionListFragment()).commit();
			loadTestFromServer();
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
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (mMainPane.isSlideable()) {
			setupSinglePaneMenu(getMenuInflater(), menu, mMainPane.isOpen());
		} else {
			setupDualPaneMenu(getMenuInflater(), menu);
		}
		return true;
	}

	/**
	 * @return the {@link android.support.v4.widget.SlidingPaneLayout}
	 * that contains the list and content of the current user's view
	 */
	public SlidingPaneLayout getMainPane() {
		return mMainPane;
	}

	/** @return the {@link android.widget.FrameLayout} that holds question contents */
	public FrameLayout getContentContainer() {
		return mContentContainer;
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
	public void onQuestionTypeSelected(Question.Type type) {
		Question question = new Question();
		question.setType(type);
		((QuestionListFragment) getFragmentManager()
				.findFragmentById(R.id.list_fragment_container)).addQuestion(question);
		showViewWhenAppropriate(question);
	}

	private void loadTestFromServer() {
		ParseQuery<Test> testQuery = ParseQuery.getQuery(Test.class);
		testQuery.orderByDescending("createdAt");
		testQuery.getFirstInBackground(new GetCallback<Test>() {
			@Override
			public void done(Test test, ParseException e) {
				if (e == null) {
					mCurrentTest = test;
					getQuestionListFragment().onTestLoaded(mCurrentTest);
				} else {
					Toast.makeText(MainActivity.this, "Error loading test: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
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
					showViewForQuestion(question);
				}
			};
			mMainPane.closePane();
		} else {
			showViewForQuestion(question);
		}
	}

	/** Responds to changes in the main sliding panel */
	private class MainPanelSlideListener extends SlidingPaneLayout.SimplePanelSlideListener {
		@Override
		public void onPanelOpened(View panel) {
			invalidateOptionsMenu();
		}

		@Override
		public void onPanelClosed(View panel) {
			invalidateOptionsMenu();
			if (sPendingOperation != null) {
				mMainPane.post(sPendingOperation);
				sPendingOperation = null;
			}
		}
	}
}
