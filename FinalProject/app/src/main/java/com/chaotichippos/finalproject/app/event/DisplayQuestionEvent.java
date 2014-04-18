package com.chaotichippos.finalproject.app.event;

import com.chaotichippos.finalproject.app.model.Question;

/**
 * Event to be published when a different question is selected from the list
 */
public class DisplayQuestionEvent {

	private final int mIndex;

	private final Question mQuestion;

	public DisplayQuestionEvent(int index, Question question) {
		mIndex = index;
		mQuestion = question;
	}

	public int getIndex() {
		return mIndex;
	}

	public Question getQuestion() {
		return mQuestion;
	}
}
