package com.chaotichippos.finalproject.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SebastianMartinez on 4/7/14.
 */
public class MultipleChoiceQuestion extends Question {

    private static final String KEY_QUESTION_TEXT = "questionText";
    private List<String> choices;

    public MultipleChoiceQuestion() {
        setType(Type.MULTIPLE_CHOICE);
        if (containsKey("choices")) {
            choices = getList("choices");
        } else {
            choices = new ArrayList<String>();
            put("choices", choices);
        }
    }

    public void setQuestionText(String questionText) {
        put(KEY_QUESTION_TEXT, questionText);
    }

    public String getQuestionText() {
        return getString(KEY_QUESTION_TEXT);
    }

    public void addChoice(String answerText) {
        choices.add(answerText);
    }
    public List<String> getChoices() {
        return choices;
    }
}

