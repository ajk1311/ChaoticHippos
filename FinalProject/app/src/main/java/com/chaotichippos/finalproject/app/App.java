package com.chaotichippos.finalproject.app;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

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
		Parse.initialize(sAppContext, PARSE_APP_ID, PARSE_CLIENT_KEY);
	}

	public static Context getContext() {
		return sAppContext;
	}
}
