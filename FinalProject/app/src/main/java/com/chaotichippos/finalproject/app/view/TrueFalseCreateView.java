package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This view displays the interface for creating a true or false question
 */
public class TrueFalseCreateView extends TrueFalseView {

	public TrueFalseCreateView(Context context) {
		super(context);
	}

	@Override
	protected TextView getTextDisplayView(Context context) {
		return new EditText(context);
	}
}
