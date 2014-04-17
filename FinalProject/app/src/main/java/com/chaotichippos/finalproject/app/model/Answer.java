package com.chaotichippos.finalproject.app.model;

import org.json.JSONArray;
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
        if(correct3 != null) {
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
		String correct = null;
        try {
            correct = question.getData().getString("correctAnswer");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        int score = 0;
        if(correct.compareTo(answerText) == 0) {
            score = 1;
        }
        final Results results = new Results();
        results.score = (double) score;
        results.data = String.valueOf(score);
        return results;
	}

	private static Results checkMatchingAnswer(Question question, String answerText) {
        JSONArray right;
        int numPairs = 0, correct = 0;
        try {
            right = question.getData().getJSONArray("rightSide");
            numPairs = right.length();
            String[] strArray;
            String delimiter = ";";
            strArray = answerText.split(delimiter);
            for(int i = 0; i <  right.length(); i++)
            {
                if(strArray[i].equals(right.get(i)))
                {
                    correct++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Results results = new Results();
        results.score = (double) correct / numPairs;
        results.data = String.valueOf(correct) + "/" + String.valueOf(numPairs);
        return results;
	}
}
