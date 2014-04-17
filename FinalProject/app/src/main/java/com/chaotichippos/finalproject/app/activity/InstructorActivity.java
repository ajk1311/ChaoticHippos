package com.chaotichippos.finalproject.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.dialog.YesNoDialogFragment;
import com.chaotichippos.finalproject.app.model.Question;
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
            menu.add(Menu.NONE, R.id.menu_option_create_exam, Menu.NONE, "Publish Exam")
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

	private void ensureCompleteQuestions() {
		int position = 0;
		boolean incomplete = false;
		final List<Question> questions = getQuestionListFragment().getQuestionList();
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
					"You have some incomplete questions.\nDo you wish to continue publishing?");
			dialog.setListener(new YesNoDialogFragment.YesNoListener() {
				@Override
				public void onYes() {
					publishTest();
				}
				@Override
				public void onNo() {
					getMainPane().openPane();
					getQuestionListFragment().scrollToQuestion(incompletePosition);
				}
			});
		} else {
			publishTest();
		}
	}

	private void publishTest() {
		// TODO save test
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
				view = new CreateMatchingView(this);
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
        savePreviousQuestion();
        List<Question> qList = getQuestionListFragment().getQuestionList();
        for(Question question: qList) {
            try {
                question.toParseObject().save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}