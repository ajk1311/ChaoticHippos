package com.chaotichippos.finalproject.app.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * POJO model for a test from the server
 */
@ParseClassName("Test")
public class Test extends ParseObject {

	// Key names used by the internal ParseObject
	private static final String KEY_DURATION = "duration";
	private static final String KEY_EXPIRATION = "expiration";
	private static final String KEY_NAME = "name";
	private static final String KEY_QUESTIONS = "questions";


	// Getters/setters
	// ====================================================================

	public long getDuration() {
		return getLong(KEY_DURATION);
	}

	public void setDuration(long duration) {
		put(KEY_DURATION, duration);
	}

	public long getExpiration() {
		return getLong(KEY_EXPIRATION);
	}

	public void setExpiration(long expiration) {
		put(KEY_EXPIRATION, expiration);
	}

	public String getName() {
		return getString(KEY_NAME);
	}

	public void setName(String name) {
		put(KEY_NAME, name);
	}

	public List<Question> getQuestions() {
		return getList(KEY_QUESTIONS);
	}

	public void setQuestions(List<Question> questions) {
		put(KEY_QUESTIONS, questions);
	}
}
