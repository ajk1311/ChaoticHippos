package com.chaotichippos.finalproject.app.model;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.R;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * POJO for a question inside a {@link com.chaotichippos.finalproject.app.model.Test}
 */
@ParseClassName("Question")
public class Question extends ParseObject {

	// Key names used by the internal ParseObject
	private static final String KEY_TYPE = "questionType";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_DATA = "data";

	/** Represents the different types of Questions someone can make or answer */
	public static enum Type {
		MULTIPLE_CHOICE		(App.getContext().getString(R.string.question_type_multiple_choice)),
		TRUE_OR_FALSE		(App.getContext().getString(R.string.question_type_true_or_false)),
		MATCHING			(App.getContext().getString(R.string.question_type_matching)),
		FILL_IN_THE_BLANK	(App.getContext().getString(R.string.question_type_fill_in_the_blank));

		private final String mTitle;

		private Type(String title) {
			mTitle = title;
		}

		/** @return The text to display in the dialog's list  */
		public String getTitle() {
			return mTitle;
		}
	}


	// Getters/setters
	// ====================================================================

	public void setType(Type type) {
		put(KEY_TYPE, type.ordinal());
	}

	public Type getType() {
		int value = getInt(KEY_TYPE);
		return Type.values()[value];
	}

	public void setImageUrl(String imageUrl) {
		put(KEY_IMAGE, imageUrl);
	}

	public String getImageUrl() {
		return getString(KEY_IMAGE);
	}

	public void setData(ParseObject data) {
		put(KEY_DATA, data);
	}

	public ParseObject getData() {
		ParseObject data = getParseObject(KEY_DATA);
		if (data == null) {
			data = new ParseObject("data");
			put(KEY_DATA, data);
		}
		return data;
	}
}
