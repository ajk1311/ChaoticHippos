package com.chaotichippos.finalproject.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.chaotichippos.finalproject.app.model.Question;

public class StudentActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	}
}