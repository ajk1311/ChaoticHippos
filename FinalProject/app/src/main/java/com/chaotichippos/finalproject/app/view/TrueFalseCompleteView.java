package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.model.Question;

/**
 * View that displays the interface for answering a true or false question
 */
public class TrueFalseCompleteView extends TrueFalseView {

	public TrueFalseCompleteView(Context context) {
		super(context);
	}

	@Override
	public Question getQuestion() {
		return mQuestionWrapper.get();
	}

	@Override
	protected TextView getTextDisplayView(Context context) {
		final TextView questionText = new TextView(context);
		questionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		return questionText;
	}
}
