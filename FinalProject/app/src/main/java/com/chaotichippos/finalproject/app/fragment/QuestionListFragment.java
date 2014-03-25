package com.chaotichippos.finalproject.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.view.EditableQuestionListItemView;

import java.util.List;

/**
 * Fragment that displays a list of Question objects. Each main view has this list, so the base class
 * {@link com.chaotichippos.finalproject.app.activity.MainActivity} contains this
 * {@link android.app.Fragment} and provides service methods for accessing the correct aspects of the list.
 */
public class QuestionListFragment extends Fragment implements AdapterView.OnItemClickListener {

	/** Used for constants and logging */
	private static final String TAG = QuestionListFragment.class.getSimpleName();

	/** Key for saving and retrieving the editable property of the list */
	private static final String KEY_IS_EDITABLE = TAG + ".isEditable";

	/**
	 * Interface providing methods for the controlling {@link android.app.Activity}
	 * to perform actions to manipulate the data backing the list
	 */
	public interface OnQuestionSelectedListener {

		/** Called when the user presses the "Add Question" button */
		public void onAddQuestionRequested();

		/** Called when the user selects a question from the list */
		public void onQuestionSelected(Question question);
	}


	// Fields
	//================================================================

	private ListView mListView;

	/** {@code true} if the user can add or remove questions from the list */
	private boolean mIsEditable = true;

	private OnQuestionSelectedListener mListener;

	private View mAddQuestionListFooterView = null;


	// Methods
	//================================================================

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnQuestionSelectedListener) {
			mListener = (OnQuestionSelectedListener) activity;
		} else {
			throw new IllegalArgumentException("Activity must implement " +
					"OnQuestionSelectedListener interface!");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_question_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mIsEditable = savedInstanceState.getBoolean(KEY_IS_EDITABLE);
		}
		mListView = (ListView) view.findViewById(android.R.id.list);
		mAddQuestionListFooterView = LayoutInflater.from(getActivity())
				.inflate(R.layout.question_list_add, mListView, false);
		if (mIsEditable) {
			mListView.addFooterView(mAddQuestionListFooterView);
		}
		mListView.setAdapter(new QuestionListAdapter());
	}

	@Override
	public void onResume() {
		super.onResume();
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_IS_EDITABLE, mIsEditable);
	}

	@Override
	public void onPause() {
		super.onPause();
		mListView.setOnItemClickListener(null);
	}

	/** @return the {@link android.widget.ListView} that displays the questions in the test */
	public ListView getListView() {
		return mListView;
	}

	/** @return whether or not the user can add or remove questions from the list */
	public boolean isQuestionListEditable() {
		return mIsEditable;
	}

	/**
	 * Sets whether the user is allowed to add or remove questions
	 * from the list. This is {@code true} by default.
	 *
	 * @param editable {@code true} if the user can edit
	 */
	public void setQuestionListEditable(boolean editable) {
		if (editable == mIsEditable) {
			return;
		}
		mIsEditable = editable;
		// Safely adds or removes the footer by making sure it is called when no adapter is set
		ListAdapter adapter = mListView.getAdapter();
		mListView.setAdapter(null);
		if (mIsEditable) {
			mListView.addFooterView(mAddQuestionListFooterView);
		} else {
			mListView.removeFooterView(mAddQuestionListFooterView);
		}
		mListView.setAdapter(adapter);
		((BaseAdapter) adapter).notifyDataSetChanged();
	}

	/**
	 * Adds a question to display in the list
	 *
	 * @param question The question to display
	 */
	public void addQuestion(Question question) {
		final QuestionListAdapter adapter = (QuestionListAdapter) mListView.getAdapter();
		adapter.addQuestion(question);
		adapter.setQuestionSelected(adapter.getCount() - 1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Adapter adapter = parent.getAdapter();
		if (position == adapter.getCount()) {
			// The "add question" footer was selected
			mListener.onAddQuestionRequested();
		} else {
			// A question was selected
			((QuestionListAdapter) mListView.getAdapter()).setQuestionSelected(position);
			mListener.onQuestionSelected((Question) adapter.getItem(position));
		}
	}

	/** Adapter that provides Question objects for our list to display */
	private class QuestionListAdapter extends BaseAdapter {

		private static final int INVALID_POSITION = -1;

		/** The backing data for the adapter */
		private List<Question> mList;

		/**
		 * Keeps track of the selected position in the list. We aren't using
		 * {@link android.widget.ListView#CHOICE_MODE_SINGLE} because we don't
		 * want the footer we add to be highlighted ever.
		 */
		private int mSelectedPosition = INVALID_POSITION;

		/**
		 * Adds a new question to the list
		 *
		 * @param question The new question
		 */
		public void addQuestion(Question question) {
			if (mList == null) {
				return;
			}
			mList.add(question);
			notifyDataSetChanged();
		}

		/**
		 * Displays views for the given list of questions
		 *
		 * @param list The new list to be displayed
		 */
		public void swapQuestions(List<Question> list) {
			if (list != mList) {
				mList = list;
				notifyDataSetChanged();
			}
		}

		/**
		 * Selects the question at the given position.
		 * If any question was previously selected, it will be unselected.
		 *
		 * @param position The position of the question to be selected
		 */
		public void setQuestionSelected(int position) {
			if (position == mSelectedPosition) {
				return;
			}
			mSelectedPosition = position;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList == null ? null : mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mIsEditable ? new EditableQuestionListItemView(getActivity()) :
						LayoutInflater.from(getActivity())
								.inflate(R.layout.question_list_item_textview, parent, false);
			}

			final String text = getString(R.string.question_prefix) + " " + (position + 1);

			if (mIsEditable) {
				final EditableQuestionListItemView view = (EditableQuestionListItemView) convertView;
				view.getTextView().setText(text);
				view.getRemoveButton().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO remove question
					}
				});
			} else {
				((TextView) convertView).setText(text);
			}

			convertView.setActivated(position == mSelectedPosition);

			return convertView;
		}
	}
}