package com.chaotichippos.finalproject.app.activity;

import android.app.FragmentTransaction;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.fragment.StudentEmptyFragment;
import com.chaotichippos.finalproject.app.fragment.StudentTestFragment;
import com.chaotichippos.finalproject.app.model.Test;

public class StudentActivity extends MainActivity {

	@Override
	protected void onTestLoaded(final Test currentTest) {
		if (currentTest == null || !currentTest.isReady()) {
			getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.replace(R.id.content_container, new StudentEmptyFragment())
					.commit();
		} else {
			getFragmentManager().beginTransaction()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.replace(R.id.content_container, new StudentTestFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getQuestionListFragment().setQuestionListEditable(false);
	}
}