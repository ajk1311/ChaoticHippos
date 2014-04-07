package com.chaotichippos.finalproject.app.model;

public class Answer {

	private String mQuestionId;

	private String mAnswerText;

	public Answer(String questionId, String answerText) {
		mQuestionId = questionId;
		mAnswerText = answerText;
	}

	public String getQuestionId() {
		return mQuestionId;
	}

	public void setQuestionId(String questionId) {
		mQuestionId = questionId;
	}

	public String getAnswerText() {
		return mAnswerText;
	}

	public void setAnswerText(String answerText) {
		mAnswerText = answerText;
	}
}
