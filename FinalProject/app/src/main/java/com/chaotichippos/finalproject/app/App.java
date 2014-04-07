package com.chaotichippos.finalproject.app;

import android.app.Application;
import android.content.Context;

import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Submission;
import com.chaotichippos.finalproject.app.model.Test;
import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

	private static final String PARSE_APP_ID = "gQ1wC5X8U9pNxljJ3L0bnAxWrf2ws7vBnFAc4tSu";
	private static final String PARSE_CLIENT_KEY = "QEcM6ovzHkPbIK1GF0ugUy2LXeMb8IwQmNSTt1JV";

	private static Context sAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sAppContext = getApplicationContext();
		setupParse();
	}

	private void setupParse() {
		ParseObject.registerSubclass(Test.class);
		ParseObject.registerSubclass(Question.class);
		ParseObject.registerSubclass(Submission.class);
		Parse.initialize(sAppContext, PARSE_APP_ID, PARSE_CLIENT_KEY);
	}

	public static Context getContext() {
		return sAppContext;
	}
}
