package com.chaotichippos.finalproject.app.view;

import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;

/**
 * Every {@link android.view.View} that displays a {@link com.chaotichippos.finalproject.app.model.Question}
 * must implement this fat interface. getQuestion() will only be called on create views, and
 * getAnswers() will only be called on answering views. setQuestion() could be called on either.
 */
public interface QuestionViewer {

	/**
	 * @return A {@link com.chaotichippos.finalproject.app.model.Question} to be added
	 * to a {@link com.chaotichippos.finalproject.app.model.Test}
	 */
	public Question getQuestion();

	/**
	 * @return An {@link com.chaotichippos.finalproject.app.model.Answer} object to be
	 * added to a {@link com.chaotichippos.finalproject.app.model.Submission}
	 */
	public Answer getAnswers();

	/**
	 * Tells the {@link android.view.View} to display this {@link com.chaotichippos.finalproject.app.model.Question}.
	 * Allows for View recycling instead of constant instantiation.
	 *
	 * @param question The Question to display
	 */
	public void setQuestion(Question question);
}
