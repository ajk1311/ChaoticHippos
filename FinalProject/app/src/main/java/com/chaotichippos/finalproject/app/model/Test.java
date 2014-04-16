package com.chaotichippos.finalproject.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

/**
 * POJO model for a test from the server
 */
public class Test implements Parcelable {

	public static final String TAG = "Test";

	// Key names used by the internal ParseObject
	private static final String KEY_DURATION = "duration";
	private static final String KEY_EXPIRATION = "expiration";
	private static final String KEY_NAME = "name";

	private String mId;
	private long mDuration;
	private long mExpiration;
	private String mName;
	private boolean mIsReady;

	public Test(ParseObject test) {
		mId = test.getObjectId();
		mDuration = test.getLong(KEY_DURATION);
		mExpiration = test.getLong(KEY_EXPIRATION);
		mName = test.getString(KEY_NAME);
//		mIsReady = test.getBoolean(KEY_READY);
	}

	private Test(Parcel source) {
		mId = source.readString();
		mDuration = source.readLong();
		mExpiration = source.readLong();
		mName = source.readString();
//		mIsReady = source.readInt() == 1;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeLong(mDuration);
		dest.writeLong(mExpiration);
		dest.writeString(mName);
//		dest.writeInt(mIsReady ? 1 : 0);
	}

	public static final Parcelable.Creator<Test> CREATOR = new Parcelable.Creator<Test>() {
		@Override
		public Test createFromParcel(Parcel source) {
			return new Test(source);
		}

		@Override
		public Test[] newArray(int size) {
			return new Test[size];
		}
	};

	public String getObjectId() {
		return mId;
	}

	public long getDuration() {
		return mDuration;
	}

	public void setDuration(long duration) {
		mDuration = duration;
	}

	public long getExpiration() {
		return mExpiration;
	}

	public void setExpiration(long expiration) {
		mExpiration = expiration;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public boolean isReady() {
		return mIsReady;
	}

	public void setReady(boolean isReady) {
		mIsReady = isReady;
	}

	public ParseObject toParseObject() {
		final ParseObject test = new ParseObject(TAG);
		test.setObjectId(mId);
		test.put(KEY_DURATION, mDuration);
		test.put(KEY_EXPIRATION, mExpiration);
		test.put(KEY_NAME, mName);
//		test.put(KEY_READY, mIsReady);
		return test;
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
