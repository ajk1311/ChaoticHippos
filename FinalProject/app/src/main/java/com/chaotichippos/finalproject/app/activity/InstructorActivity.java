package com.chaotichippos.finalproject.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.view.CreateFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.CreateMultipleChoiceView;
import com.chaotichippos.finalproject.app.view.TrueFalseCreateView;

public class InstructorActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
				view = new CreateFillInTheBlankView(this);
				break;

			case MULTIPLE_CHOICE:
				view = new CreateMultipleChoiceView(this);
				break;

			case MATCHING:

				break;

			case TRUE_OR_FALSE:
				view = new TrueFalseCreateView(this);
				break;
		}
//		((QuestionViewer) view).setQuestion(question);
		getContentContainer().removeAllViews();
		getContentContainer().addView(view);
	}
}