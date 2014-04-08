package com.chaotichippos.finalproject.app.model;

import com.parse.ParseClassName;

@ParseClassName("Question")
public class FillInTheBlankQuestion extends Question {

    private static final String KEY_ANSWER1 = "answer1";
    private static final String KEY_ANSWER2 = "answer2";
    private static final String KEY_ANSWER3 = "answer3";
    private static final String KEY_QUESTION_TEXT = "questionText";

    public FillInTheBlankQuestion() {
        setType(Type.FILL_IN_THE_BLANK);
    }

    public void setQuestionText(String questionText) {
        put(KEY_QUESTION_TEXT, questionText);
    }

    public String getQuestionText() {
        return getString(KEY_QUESTION_TEXT);
    }

    public void setAnswer1(String answer) {
        put(KEY_ANSWER1, answer);
    }

    public String getAnswer1() {
        return getString(KEY_ANSWER1);
    }

    public void setAnswer2(String answer) {
        put(KEY_ANSWER2, answer);
    }

    public String getAnswer2() {
        return getString(KEY_ANSWER2);
    }

    public void setAnswer3(String answer) {
        put(KEY_ANSWER3, answer);
    }

    public String getAnswer3() {
        return getString(KEY_ANSWER3);
    }
}
