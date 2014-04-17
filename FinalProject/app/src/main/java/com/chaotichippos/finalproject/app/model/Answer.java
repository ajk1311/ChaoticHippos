package com.chaotichippos.finalproject.app.model;

import org.json.JSONException;

public class Answer {

	/**
	 * Represents the score the question received, along with
	 * how the data should be represented in the graph view
	 */
	public static class Results {
		public double score;
		public String data;
	}

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

	public static Results checkAnswer(Question question, String answerText) {
		switch (question.getType()) {
			case TRUE_OR_FALSE:
				return checkTrueFalseAnswer(question, answerText);

			case FILL_IN_THE_BLANK:
				return checkFillInTheBlankAnswer(question, answerText);

			case MULTIPLE_CHOICE:
				return checkMultipleChoiceAnswer(question, answerText);

			case MATCHING:
				return checkMatchingAnswer(question, answerText);

			default:
				throw new IllegalArgumentException("Trying to check question of unknown type");
		}
	}

	private static Results checkTrueFalseAnswer(Question question, String answerText) {
		int provided = Integer.parseInt(answerText);
		int correct = new TrueOrFalseQuestionWrapper(question).getAnswer();
		final Results results = new Results();
		results.score = provided == correct ? 1 : 0;
		results.data = String.valueOf(results.score);
		return results;
	}

	private static Results checkFillInTheBlankAnswer(Question question, String answerText) {
		String provided[] = answerText.split(";");
        String correct1 = null, correct2 = null, correct3 = null;
        int numBlanks = 0, correct = 0;
        try {
            correct1 = question.getData().getString("blank1");
            correct2 = question.getData().getString("blank2");
            correct3 = question.getData().getString("blank3");
            numBlanks = question.getData().getInt("numBlanks");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(correct1 != null) {
            if(correct1.compareTo(provided[0]) == 0) {
                correct++;
            }
        }
        if(correct2 != null) {
            if(correct2.compareTo(provided[1]) == 0) {
                correct++;
            }
        }
        if(correct1 != null) {
            if(correct3.compareTo(provided[2]) == 0) {
                correct++;
            }
        }
		final Results results = new Results();
		results.score = (double) correct / numBlanks;
		results.data = String.valueOf(correct) + "/" + String.valueOf(numBlanks);
        return results;
	}

	private static Results checkMultipleChoiceAnswer(Question question, String answerText) {
		return null;
	}

	private static Results checkMatchingAnswer(Question question, String answerText) {
		return null;
	}
}
