package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;

/**
 * Created by SebastianMartinez on 4/10/14.
 */
public class MultipleChoiceAnswerView extends RelativeLayout implements QuestionViewer {
    ListView listView;

    public MultipleChoiceAnswerView(Context context)  {
        this(context, null, 0);
    }

    public MultipleChoiceAnswerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleChoiceAnswerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.create_multiple_choice_base_view, this, true);

        listView = (ListView) findViewById(R.id.listview);
    }

    public Question getQuestion() {
        return null;
    }

    public Answer getAnswer() {
        return null;
    }

    public void setQuestion(Question question) {

    }

}
