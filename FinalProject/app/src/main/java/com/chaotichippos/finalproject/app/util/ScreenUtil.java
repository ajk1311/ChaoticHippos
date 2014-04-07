package com.chaotichippos.finalproject.app.util;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.chaotichippos.finalproject.app.App;

public class ScreenUtil {

	private static DisplayMetrics sDisplayMetrics = App.getContext()
			.getResources().getDisplayMetrics();

	public static int getDimensionPixelSize(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, sDisplayMetrics);
	}
}
