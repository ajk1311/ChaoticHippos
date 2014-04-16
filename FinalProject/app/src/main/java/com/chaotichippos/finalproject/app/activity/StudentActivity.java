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
import com.chaotichippos.finalproject.app.view.CompleteFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.MultipleChoiceAnswerView;
import com.chaotichippos.finalproject.app.view.QuestionViewer;
import com.chaotichippos.finalproject.app.view.StudentMatchingView;
import com.chaotichippos.finalproject.app.view.TrueFalseCreateView;

import java.util.List;

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
		if (!open) {
			menu.add(Menu.NONE, R.id.menu_option_switch, Menu.NONE, "Switch to Instructor")
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
		getContentContainer().removeAllViews();
		getContentContainer().addView(view);
		((QuestionViewer) view).setQuestion(question);
	}
}