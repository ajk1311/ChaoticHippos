package com.chaotichippos.finalproject.app.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * POJO for a {@link com.chaotichippos.finalproject.app.model.Test} submission from a student
 */
@ParseClassName("Submission")
public class Submission extends ParseObject {

	// Key names used by the internal ParseObject
	private static final String KEY_ANSWERS = "answers";
	private static final String KEY_PARENT_EXAM = "parentExam";

	/** Holds the answers */
	private JSONArray mAnswers = new JSONArray();


	// Getters/setters
	// ====================================================================

	public void setParentExam(String parentExamId) {
		put(KEY_PARENT_EXAM, parentExamId);
	}

	public String getParentExamId() {
		return getString(KEY_PARENT_EXAM);
	}

	public void addAnswers(Answer[] answers) throws JSONException {
		for (Answer answer : answers) {
			mAnswers.put(new JSONObject().put(
					answer.getQuestionId(), answer.getAnswerText()));
		}
	}

	public Submission generate() {
		put(KEY_ANSWERS, mAnswers);
		return this;
	}
}
