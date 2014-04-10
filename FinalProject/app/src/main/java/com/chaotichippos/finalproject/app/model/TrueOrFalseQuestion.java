package com.chaotichippos.finalproject.app.model;

import com.parse.ParseClassName;

import org.json.JSONException;

@ParseClassName("Question")
public class TrueOrFalseQuestion extends Question {

	private static final String KEY_QUESTION_TEXT = "questionText";

	private static final String KEY_ANSWER = "answer";

	public TrueOrFalseQuestion() {
		setType(Type.TRUE_OR_FALSE);
	}

	public void setQuestionText(String questionText) {
        try {
            getData().put(KEY_QUESTION_TEXT, questionText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public String getQuestionText() {
        try {
            return getData().getString(KEY_QUESTION_TEXT);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

	public void setAnswer(boolean answer) {
        try {
            getData().put(KEY_ANSWER, answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public boolean getAnswer() {
        try {
            return getData().getBoolean(KEY_ANSWER);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
