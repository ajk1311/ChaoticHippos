package com.chaotichippos.finalproject.app.activity;

import android.app.FragmentTransaction;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.fragment.InstructorDataFragment;
import com.chaotichippos.finalproject.app.fragment.InstructorEditorFragment;
import com.chaotichippos.finalproject.app.model.Test;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class InstructorActivity extends MainActivity {

	@Override
	protected void onTestLoaded(Test currentTest) {
		if (currentTest == null) {
			final ParseObject test = new ParseObject(Test.TAG);
			test.put(Test.KEY_READY, false);
			test.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						setCurrentTest(new Test(test));
						showEditor();
					} else {
						// TODO handle errors
					}
				}
			});
		} else if (currentTest.isReady()) {
			getQuestionListFragment().setQuestionListEditable(false);
			showGraphs();
		} else {
			showEditor();
		}
	}

	private void showEditor() {
		getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.replace(R.id.content_container, new InstructorEditorFragment())
				.commit();
	}

	private void showGraphs() {
		getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.replace(R.id.content_container, new InstructorDataFragment())
				.commit();
	}
}