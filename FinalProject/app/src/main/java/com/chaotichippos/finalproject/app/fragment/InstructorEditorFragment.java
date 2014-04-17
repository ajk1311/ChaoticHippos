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
import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.activity.MainActivity;
import com.chaotichippos.finalproject.app.activity.StudentActivity;
import com.chaotichippos.finalproject.app.dialog.ProgressDialogFragment;
import com.chaotichippos.finalproject.app.dialog.YesNoDialogFragment;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Test;
import com.chaotichippos.finalproject.app.view.CreateFillInTheBlankView;
import com.chaotichippos.finalproject.app.view.CreateMatchingView;
import com.chaotichippos.finalproject.app.view.CreateMultipleChoiceView;
import com.chaotichippos.finalproject.app.view.QuestionViewer;
import com.chaotichippos.finalproject.app.view.TrueFalseCreateView;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.util.List;

public class InstructorEditorFragment extends Fragment {

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
		if (!mMainActivity.getCurrentTest().isReady()) {
			mMainActivity.savePreviousQuestion(mContainer);
			for(Question question: mMainActivity.getQuestionListFragment().getQuestionList()) {
				question.toParseObject().saveInBackground();
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.add(Menu.NONE, R.id.menu_option_switch, Menu.NONE, "Switch to Student")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(Menu.NONE, R.id.menu_option_create_exam, Menu.NONE, "Publish Exam")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_option_switch:
				startActivity(new Intent(getActivity(), StudentActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
				return true;
			case R.id.menu_option_create_exam:
				ensureCompleteQuestions();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void ensureCompleteQuestions() {
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
					"You have some incomplete questions.\nDo you wish to continue publishing?");
			dialog.setListener(new YesNoDialogFragment.YesNoListener() {
				@Override
				public void onYes() {
					publishTest();
				}
				@Override
				public void onNo() {
					mMainActivity.getMainPane().openPane();
					mMainActivity.getQuestionListFragment().scrollToQuestion(incompletePosition);
				}
			});
			dialog.show(getFragmentManager(), "yesNo");
		} else {
			publishTest();
		}
	}

	private void publishTest() {
		// TODO dialog for duration, etc.
		ProgressDialogFragment.create("Publishing test...").show(getFragmentManager(), null);
		mMainActivity.savePreviousQuestion(mContainer);
		for(Question question: mMainActivity.getQuestionListFragment().getQuestionList()) {
			if (question.isComplete()) {
				question.toParseObject().saveInBackground();
			} else {
				question.toParseObject().deleteInBackground();
			}
		}
		final Test test = mMainActivity.getCurrentTest();
		test.setReady(true);
		test.toParseObject().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (!isAdded()) {
					return;
				}
				Toast.makeText(getActivity(),
						"Thank you! Your test has been published", Toast.LENGTH_LONG).show();
				// TODO restart activity with graphs showing
				mMainActivity.finish();
			}
		});
	}

	@Subscribe
	public void onQuestionSelected(Question question) {
		View view = null;
		switch (question.getType()) {
			case FILL_IN_THE_BLANK:
				view = new CreateFillInTheBlankView(getActivity());
				break;

			case MULTIPLE_CHOICE:
				view = new CreateMultipleChoiceView(getActivity());
				break;

			case MATCHING:
				view = new CreateMatchingView(getActivity());
				break;

			case TRUE_OR_FALSE:
				view = new TrueFalseCreateView(getActivity());
				break;
		}
		mMainActivity.savePreviousQuestion(mContainer);
		mContainer.removeAllViews();
		mContainer.addView(view);
		((QuestionViewer) view).setQuestion(question);
	}
}