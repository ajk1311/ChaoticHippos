package com.chaotichippos.finalproject.app.model;

public class TrueOrFalseQuestion extends Question {

	private static final String KEY_QUESTION_TEXT = "questionText";

	private static final String KEY_ANSWER = "answer";

	public TrueOrFalseQuestion() {
		setType(Type.TRUE_OR_FALSE);
	}

	public void setQuestionText(String questionText) {
		getData().put(KEY_QUESTION_TEXT, questionText);
	}

	public String getQuestionText() {
		return getData().getString(KEY_QUESTION_TEXT);
	}

	public void setAnswer(boolean answer) {
		getData().put(KEY_ANSWER, answer);
	}

	public boolean getAnswer() {
		return getData().getBoolean(KEY_ANSWER);
	}
}
