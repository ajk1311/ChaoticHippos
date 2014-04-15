package com.chaotichippos.finalproject.app.model;

import com.chaotichippos.finalproject.app.view.TrueFalseView;

import org.json.JSONException;
import org.json.JSONObject;

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
		final JSONObject data = mWrapped.getData();
		return data.has(KEY_QUESTION_TEXT) ?
				data.optString(KEY_QUESTION_TEXT) : null;
    }

	public void setAnswer(int answer) {
		try {
			mWrapped.getData().put(KEY_ANSWER, answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public int getAnswer() {
		final JSONObject data = mWrapped.getData();
		return data.has(KEY_ANSWER) ?
				data.optInt(KEY_ANSWER) : TrueFalseView.ANSWER_INVALID;
    }

	public Question get() {
		return mWrapped;
	}
}
