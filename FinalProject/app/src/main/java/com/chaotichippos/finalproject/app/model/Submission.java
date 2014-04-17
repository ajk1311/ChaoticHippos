package com.chaotichippos.finalproject.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chaotichippos.finalproject.app.util.DebugLog;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * POJO for a {@link com.chaotichippos.finalproject.app.model.Test} submission from a student
 */
public class Submission implements Parcelable {

	public static final String TAG = "Submission";

	// Key names used by the internal ParseObject
	public static final String KEY_ANSWERS = "answers";
	public static final String KEY_PARENT_EXAM = "parentExam";
	public static final String KEY_READY = "ready";
	public static final String KEY_GRADE = "grade";

	private String mId;
	private JSONObject mAnswers;
	private String mParentExam;
	private boolean mIsReady;
	private double mGrade;

	public Submission(ParseObject submission) {
		mId = submission.getObjectId();
		mAnswers = submission.getJSONObject(KEY_ANSWERS);
		if (mAnswers == null) {
			mAnswers = new JSONObject();
		}
		mParentExam = submission.getString(KEY_PARENT_EXAM);
		mIsReady = submission.getBoolean(KEY_READY);
		mGrade = submission.getDouble(KEY_GRADE);
	}

	private Submission(Parcel source) {
		try {
			mId = source.readString();
			mAnswers = new JSONObject(source.readString());
			mParentExam = source.readString();
			mIsReady = source.readInt() == 1;
			mGrade = source.readDouble();
		} catch (JSONException e) {
			e.printStackTrace();
			DebugLog.w(TAG, "Shit got fucked up creating from parcel");
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mAnswers.toString());
		dest.writeString(mParentExam);
		dest.writeInt(mIsReady ? 1 : 0);
		dest.writeDouble(mGrade);
	}

	public static final Parcelable.Creator<Submission> CREATOR =
			new Parcelable.Creator<Submission>() {
				@Override
				public Submission createFromParcel(Parcel source) {
					return null;
				}

				@Override
				public Submission[] newArray(int size) {
					return new Submission[size];
				}
			};

	public String getObjectId() {
		return mId;
	}

	public void setAnswers(JSONObject answers) {
		mAnswers = answers;
	}

	public JSONObject getAnswers() {
		return mAnswers;
	}

	public void setAnswer(String questionId, String answerText) {
		try {
			mAnswers.put(questionId, answerText);
		} catch (JSONException e) {
			e.printStackTrace();
			DebugLog.w(TAG, "Error adding answer " + answerText + " for question " + questionId);
		}
	}

	public void putAnswer(Answer answer) {
		try {
			mAnswers.put(answer.getQuestionId(), answer.getAnswerText());
		} catch (JSONException e) {
			e.printStackTrace();
			DebugLog.w(TAG, "Error adding answer " + answer.getAnswerText() +
					" for question " + answer.getQuestionId());
		}
	}

	public String getAnswer(String questionId) {
		return mAnswers.has(questionId) ? mAnswers.optString(questionId) : null;
	}

	public void setParentExam(String parentExamId) {
		mParentExam = parentExamId;
	}

	public String getParentExamId() {
		return mParentExam;
	}

	public void setReady(boolean isReady) {
		mIsReady = isReady;
	}

	public boolean isReady() {
		return mIsReady;
	}

	public void setGrade(double grade) {
		mGrade = grade;
	}

	public double getGrade() {
		return mGrade;
	}

	public ParseObject toParseObject() {
		final ParseObject submission = new ParseObject(TAG);
		submission.setObjectId(mId);
		submission.put(KEY_ANSWERS, mAnswers);
		submission.put(KEY_PARENT_EXAM, mParentExam);
		submission.put(KEY_READY, mIsReady);
		submission.put(KEY_GRADE, mGrade);
		return submission;
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
