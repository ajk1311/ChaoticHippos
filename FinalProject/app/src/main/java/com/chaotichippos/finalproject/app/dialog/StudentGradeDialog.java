package com.chaotichippos.finalproject.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;

public class StudentGradeDialog extends DialogFragment {

	private static final String KEY_MAX = "max";
	private static final String KEY_GRADE = "grade";

	public interface OnDoneListener {
		public void onDone();
	}

	public static StudentGradeDialog create(double grade, double max) {
		final StudentGradeDialog dialog = new StudentGradeDialog();
		final Bundle args = new Bundle();
		args.putDouble(KEY_GRADE, grade);
		args.putDouble(KEY_MAX, max);
		dialog.setArguments(args);
		return dialog;
	}

	private OnDoneListener mListener;

	public void setOnDoneListener(OnDoneListener listener) {
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final View view = LayoutInflater.from(getActivity())
				.inflate(R.layout.dialog_student_grade, null);

		final TextView grade = (TextView) view.findViewById(R.id.student_grade_text);
		grade.setText(String.format("%.2f", getArguments().getDouble(KEY_GRADE)) + "/"
				+ (int) getArguments().getDouble(KEY_MAX));

		return new AlertDialog.Builder(getActivity())
				.setView(view)
				.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) mListener.onDone();
					}
				})
				.create();
	}
}
