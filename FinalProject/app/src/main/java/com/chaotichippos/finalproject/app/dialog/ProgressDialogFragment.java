package com.chaotichippos.finalproject.app.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class ProgressDialogFragment extends DialogFragment {

	public static ProgressDialogFragment create(String message) {
		final ProgressDialogFragment dialog = new ProgressDialogFragment();
		final Bundle args = new Bundle();
		args.putString("msg", message);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage(getArguments().getString("msg"));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}
}
