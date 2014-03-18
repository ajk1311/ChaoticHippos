package com.chaotichippos.finalproject.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Question;

/**
 * Dialog that allows the user to select a new type of question to add to a test
 */
public class QuestionAdditionDialogFragment extends DialogFragment
		implements
		AdapterView.OnItemClickListener {

	/**
	 * Interface that listeners can implement to respond
	 * to the user selecting the type of question to add
	 */
	public interface OnQuestionTypeSelectedListener {
		public void onQuestionTypeSelected(Question.Type type);
	}

	private OnQuestionTypeSelectedListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final ListView list = new ListView(getActivity());
		list.setAdapter(new QuestionTypeAdapter());
		list.setOnItemClickListener(this);
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.question_type_dialog_title)
				.setView(list)
				.create();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		mListener = null;
	}

	/** Sets a listener to respond to the user's selection */
	public void setOnQuestionTypeSelectedListener(OnQuestionTypeSelectedListener listener) {
		mListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mListener != null) {
			mListener.onQuestionTypeSelected((Question.Type) parent.getAdapter().getItem(position));
		}
	}

	/** Provides views to display in the dialog's list based on the different question types */
	private class QuestionTypeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return Question.Type.values().length;
		}

		@Override
		public Object getItem(int position) {
			return Question.Type.values()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity())
						.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			((TextView) convertView.findViewById(android.R.id.text1))
					.setText(Question.Type.values()[position].getTitle());
			return convertView;
		}
	}
}
