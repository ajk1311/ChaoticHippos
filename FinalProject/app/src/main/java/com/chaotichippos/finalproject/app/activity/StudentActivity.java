package com.chaotichippos.finalproject.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.view.CompleteFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.StudentMatchingView;

public class StudentActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getQuestionListFragment().setQuestionListEditable(false);
	}

	@Override
	protected void setupDualPaneMenu(MenuInflater inflater, Menu menu) {

	}

	@Override
	protected void setupSinglePaneMenu(MenuInflater inflater, Menu menu, boolean open) {

	}

	@Override
	protected void showViewForQuestion(Question question) {
		View view = null;
		switch (question.getType()) {
			case FILL_IN_THE_BLANK:
				view = new CompleteFillInTheBlankView(this);
				break;

			case MULTIPLE_CHOICE:

				break;

			case MATCHING:
				view = new StudentMatchingView(this);
				break;

			case TRUE_OR_FALSE:

				break;
		}
		getContentContainer().removeAllViews();
		getContentContainer().addView(view);
	}
}