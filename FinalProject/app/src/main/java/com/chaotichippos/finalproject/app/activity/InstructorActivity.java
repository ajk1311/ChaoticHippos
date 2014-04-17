package com.chaotichippos.finalproject.app.activity;

import com.chaotichippos.finalproject.app.model.Test;

public class InstructorActivity extends MainActivity {

	@Override
	protected void onTestLoaded(Test currentTest) {
		if (currentTest == null) {
			// TODO create new test
		} else if (currentTest.isReady()) {
			// TODO view stats or start new test
		} else {
			// TODO show current test
		}
	}
}