package com.chaotichippos.finalproject.app;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.squareup.otto.Bus;

public class App extends Application {

	private static final String PARSE_APP_ID = "gQ1wC5X8U9pNxljJ3L0bnAxWrf2ws7vBnFAc4tSu";
	private static final String PARSE_CLIENT_KEY = "QEcM6ovzHkPbIK1GF0ugUy2LXeMb8IwQmNSTt1JV";

	private static Context sAppContext;
	private static Bus sEventBus;

	@Override
	public void onCreate() {
		super.onCreate();
		sAppContext = getApplicationContext();
		sEventBus = new Bus();
		setupParse();
	}

	private void setupParse() {
		Parse.initialize(sAppContext, PARSE_APP_ID, PARSE_CLIENT_KEY);
	}

	public static Context getContext() {
		return sAppContext;
	}

	public static Bus getEventBus() {
		return sEventBus;
	}
}
