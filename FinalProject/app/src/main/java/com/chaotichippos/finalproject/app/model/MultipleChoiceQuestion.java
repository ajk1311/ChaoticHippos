package com.chaotichippos.finalproject.app.model;

import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Question")
public class MultipleChoiceQuestion extends Question {

    private static final String KEY_QUESTION_TEXT = "questionText";
    private List<String> choices;

    public MultipleChoiceQuestion() {
        setType(Type.MULTIPLE_CHOICE);
        if (toParseObject().containsKey("choices")) {
            choices = toParseObject().getList("choices");
        } else {
            choices = new ArrayList<String>();
            toParseObject().put("choices", choices);
        }
    }

    public void setQuestionText(String questionText) {
        toParseObject().put(KEY_QUESTION_TEXT, questionText);
    }

    public String getQuestionText() {
        return toParseObject().getString(KEY_QUESTION_TEXT);
    }

    public void addChoice(String answerText) {
        choices.add(answerText);
    }
    public List<String> getChoices() {
        return choices;
    }
}

