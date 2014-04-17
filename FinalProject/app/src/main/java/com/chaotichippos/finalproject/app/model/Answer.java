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

	public static float checkAnswer(Question question, Answer answer) {
		switch (question.getType()) {
			case TRUE_OR_FALSE:
				return checkTrueFalseAnswer(question, answer);

			case FILL_IN_THE_BLANK:
				return checkFillInTheBlankAnswer(question, answer);

			case MULTIPLE_CHOICE:
				return checkMultipleChoiceAnswer(question, answer);

			case MATCHING:
				return checkMatchingAnswer(question, answer);

			default:
				throw new IllegalArgumentException("Trying to check question of unknown type");
		}
	}

	private static float checkTrueFalseAnswer(Question question, Answer answer) {
		int provided = Integer.parseInt(answer.getAnswerText());
		int correct = new TrueOrFalseQuestionWrapper(question).getAnswer();
		return provided == correct ? 1 : 0;
	}

	private static float checkFillInTheBlankAnswer(Question question, Answer answer) {
		return 0.0f;
	}

	private static float checkMultipleChoiceAnswer(Question question, Answer answer) {
		return 0.0f;
	}

	private static float checkMatchingAnswer(Question question, Answer answer) {
		return 0.0f;
	}
}
