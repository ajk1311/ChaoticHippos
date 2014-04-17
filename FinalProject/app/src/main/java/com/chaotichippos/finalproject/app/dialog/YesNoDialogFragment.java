package com.chaotichippos.finalproject.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class YesNoDialogFragment extends DialogFragment {

	public interface YesNoListener {
		public void onYes();

		public void onNo();
	}

	public static YesNoDialogFragment create(String message) {
		final YesNoDialogFragment dialog = new YesNoDialogFragment();
		final Bundle args = new Bundle();
		args.putString("msg", message);
		dialog.setArguments(args);
		return dialog;
	}

	private YesNoListener mListener;

	public void setListener(YesNoListener listener) {
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setMessage(getArguments().getString("msg"))
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) mListener.onNo();
					}
				}).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) mListener.onYes();
					}
				}).setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if (mListener != null) mListener.onNo();
					}
				})
				.setCancelable(true)
				.create();
	}
}
