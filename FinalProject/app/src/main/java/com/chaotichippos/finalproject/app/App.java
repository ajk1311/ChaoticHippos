package com.chaotichippos.finalproject.app;

import android.app.Application;
import android.content.Context;

public class App extends Application {

	private static Context sAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sAppContext = getApplicationContext();
	}

	public static Context getContext() {
		return sAppContext;
	}
}
