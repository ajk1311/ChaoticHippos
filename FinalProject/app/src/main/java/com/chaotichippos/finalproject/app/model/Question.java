package com.chaotichippos.finalproject.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chaotichippos.finalproject.app.App;
import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.util.DebugLog;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO for a question inside a {@link com.chaotichippos.finalproject.app.model.Test}
 */
public class Question implements Parcelable {

	public static final String TAG = "Question";

	// Key names used by the internal ParseObject
	public static final String KEY_TYPE = "questionType";
	public static final String KEY_PARENT_TEST = "parentExam";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_DATA = "data";

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

	private String mId;
	private Type mType;
	private String mImageUrl;
	private JSONObject mData;

	public static List<Question> fromParseList(List<ParseObject> fromParse) {
		final ArrayList<Question> questions = new ArrayList<Question>();
		for (int i = 0, sz = fromParse.size(); i < sz; i++) {
			questions.add(new Question(fromParse.get(i)));
		}
		return questions;
	}

	public Question() {
		mData = new JSONObject();
	}

	public Question(ParseObject parseQuestion) {
		mId = parseQuestion.getObjectId();
		mType = Type.values()[parseQuestion.getInt(KEY_TYPE)];
		mImageUrl = parseQuestion.getString(KEY_IMAGE);
		mData = parseQuestion.getJSONObject(KEY_DATA);
		if (mData == null) {
			mData = new JSONObject();
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeInt(mType.ordinal());
		dest.writeString(mImageUrl);
		dest.writeString(mData.toString());
	}

	public static final Parcelable.Creator<Question> CREATOR =
			new Parcelable.Creator<Question>() {
				@Override
				public Question createFromParcel(Parcel source) {
					try {
						final Question q = new Question();
						q.mId = source.readString();
						q.mType = Type.values()[source.readInt()];
						q.mImageUrl = source.readString();
						q.mData = new JSONObject(source.readString());
						return q;
					} catch (JSONException je) {
						je.printStackTrace();
						DebugLog.w(TAG, "Shit got fucked up creating from parcel");
						return null;
					}
				}

				@Override
				public Question[] newArray(int size) {
					return new Question[size];
				}
			};

	public String getObjectId() {
		return mId;
	}

	public void setType(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}

	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setData(JSONObject data) {
		mData = data;
	}

	public JSONObject getData() {
		return mData;
	}

	public ParseObject toParseObject() {
		final ParseObject parseObject = new ParseObject(TAG);
		parseObject.setObjectId(mId);
		parseObject.put(KEY_TYPE, mType.ordinal());
        if(mImageUrl != null){
            parseObject.put(KEY_IMAGE, mImageUrl);
        }
		parseObject.put(KEY_DATA, mData);
		return parseObject;
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
