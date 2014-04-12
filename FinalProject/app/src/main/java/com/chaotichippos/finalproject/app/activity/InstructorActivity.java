package com.chaotichippos.finalproject.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.fragment.QuestionListFragment;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.view.CompleteFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.CreateFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.CreateMatchingView;
import com.chaotichippos.finalproject.app.view.CreateMultipleChoiceView;
import com.chaotichippos.finalproject.app.view.QuestionViewer;
import com.chaotichippos.finalproject.app.view.TrueFalseCreateView;
import com.parse.ParseException;

import java.util.List;

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
		if (!open) {
			menu.add(Menu.NONE, R.id.menu_option_switch, Menu.NONE, "Switch to Student")
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.add(Menu.NONE, R.id.menu_option_create_exam, Menu.NONE, "Create Exam")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		} else {
			menu.clear();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_option_switch:
				startActivity(new Intent(this, StudentActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
				return true;
            case R.id.menu_option_create_exam:
                createExam();
                return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void showViewForQuestion(Question question) {
		View view = null;
		switch (question.getType()) {
			case FILL_IN_THE_BLANK:
				view = new CreateFillInTheBlankView(this);
                ((CreateFillInTheBlankView) view).setQuestion(question);
				break;

			case MULTIPLE_CHOICE:
				view = new CreateMultipleChoiceView(this);
				break;

			case MATCHING:
				view = new CreateMatchingView(this);
                ((CreateMatchingView) view).setQuestion(question);
				break;

			case TRUE_OR_FALSE:
				view = new TrueFalseCreateView(this);
				break;
		}
		getContentContainer().removeAllViews();
		getContentContainer().addView(view);
		((QuestionViewer) view).setQuestion(question);
	}

    public void createExam() {
        List<Question> qList = getQuestionListFragment().getQuestionList();
        QuestionViewer qv = null;
        for(Question question: qList) {
            switch (question.getType()) {
                case FILL_IN_THE_BLANK:
                    qv = new CreateFillInTheBlankView(this);
                    break;

                case MULTIPLE_CHOICE:
                    qv = new CreateMultipleChoiceView(this);
                    break;

                case MATCHING:
                    qv = new CreateMatchingView(this);
                    break;

                case TRUE_OR_FALSE:
                    qv = new TrueFalseCreateView(this);
                    break;
            }
            qv.setQuestion(question);
            question = qv.getQuestion();
            try {
                question.toParseObject().save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}