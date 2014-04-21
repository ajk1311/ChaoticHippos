package com.chaotichippos.finalproject.app.model;

import com.chaotichippos.finalproject.app.util.DebugLog;

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
        final Results results = new Results();
        if(answerText != null) {
			DebugLog.w("TF", "answerText=" + answerText);
			int correct = new TrueOrFalseQuestionWrapper(question).getAnswer();
			DebugLog.w("TF", "correct=" + correct);
            int provided = Integer.parseInt(answerText);
            results.score = provided == correct ? 1 : 0;
            results.data = String.valueOf(results.score);
        } else {
            results.score = 0;
            results.data = "0";
        }
		return results;
	}

	private static Results checkFillInTheBlankAnswer(Question question, String answerText) {
        String provided[] = null;
        if(answerText != null) {
            provided = answerText.split(";");
        }
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
        if(provided != null) {
            if (correct1 != null && provided.length >=1) {
                if (correct1.equalsIgnoreCase(provided[0])) {
                    correct++;
                }
            }
            if (correct2 != null && provided.length >= 2) {
                if (correct2.equalsIgnoreCase(provided[1])) {
                    correct++;
                }
            }
            if (correct3 != null && provided.length >= 3) {
                if (correct3.equalsIgnoreCase(provided[2])) {
                    correct++;
                }
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
        if(answerText != null && correct.equalsIgnoreCase(answerText)) {
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
            if(answerText != null) {
                strArray = answerText.split(delimiter);
                for (int i = 0; i < right.length(); i++) {
                    if (strArray[i].equalsIgnoreCase(right.getString(i))) {
                        correct++;
                    }
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
