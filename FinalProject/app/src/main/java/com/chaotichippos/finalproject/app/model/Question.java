package com.chaotichippos.finalproject.app.model;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.R;

public class Question {

	public static enum Type {
		MULTIPLE_CHOICE (App.getContext().getString(R.string.question_type_multiple_choice)),
		TRUE_OR_FALSE (App.getContext().getString(R.string.question_type_true_or_false)),
		MATCHING (App.getContext().getString(R.string.question_type_matching)),
		FILL_IN_THE_BLANK (App.getContext().getString(R.string.question_type_fill_in_the_blank));

		private final String mTitle;

		private Type(String title) {
			mTitle = title;
		}

		/** @return The text to display in the dialog's list  */
		public String getTitle() {
			return mTitle;
		}
	}

	private Type mType;

	public void setType(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}
}
