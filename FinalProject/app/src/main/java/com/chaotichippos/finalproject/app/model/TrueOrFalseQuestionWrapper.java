package com.chaotichippos.finalproject.app.model;

import org.json.JSONException;

public class TrueOrFalseQuestionWrapper {

	private static final String KEY_QUESTION_TEXT = "questionText";

	private static final String KEY_ANSWER = "answer";

	private Question mWrapped;

	public TrueOrFalseQuestionWrapper(Question wrapped) {
		mWrapped = wrapped;
		mWrapped.setType(Question.Type.TRUE_OR_FALSE);
	}

	public void setQuestionText(String questionText) {
        try {
            mWrapped.getData().put(KEY_QUESTION_TEXT, questionText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public String getQuestionText() {
        try {
            return mWrapped.getData().getString(KEY_QUESTION_TEXT);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

	public void setAnswer(boolean answer) {
        try {
			mWrapped.getData().put(KEY_ANSWER, answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public boolean getAnswer() {
        try {
            return mWrapped.getData().getBoolean(KEY_ANSWER);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

	public Question get() {
		return mWrapped;
	}
}
