package com.chaotichippos.finalproject.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.Test;
import com.chaotichippos.finalproject.app.view.EditableQuestionListItemView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
	private QuestionListAdapter mListAdapter;

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
		mListAdapter = new QuestionListAdapter();
		mListView.setAdapter(mListAdapter);
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

	public void updateQuestion(Question question) {
		Question old = null;
		final List<Question> questions = mListAdapter.mList;
		for (int i = 0, sz = questions.size(); i < sz; i++) {
			old = questions.get(i);
			if (question.getObjectId().equals(old.getObjectId())) {
				questions.set(i, question);
				break;
			}
		}
		mListAdapter.notifyDataSetChanged();
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
		mListView.setAdapter(null);
		if (mIsEditable) {
			mListView.addFooterView(mAddQuestionListFooterView);
		} else {
			mListView.removeFooterView(mAddQuestionListFooterView);
		}
		mListView.setAdapter(mListAdapter);
		mListAdapter.notifyDataSetChanged();
	}

	/**
	 * Called when the current {@link com.chaotichippos.finalproject.app.model.Test} is
	 * loaded in the {@link com.chaotichippos.finalproject.app.activity.MainActivity}
	 *
	 * @param test The current Test from the instructor
	 */
	public void onTestLoaded(Test test) {
		ParseQuery questionQuery = ParseQuery.getQuery(Question.TAG);
		questionQuery.whereEqualTo("parentExam", test.getObjectId());
		questionQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> questions, ParseException e) {
				if (!isAdded()) {
					return;
				}
				if (e == null) {
					mListAdapter.swapQuestions(Question.fromParseList(questions));
				} else {
					Toast.makeText(getActivity(), "Error loading test questions: " + e.getMessage(),
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	/**
	 * Adds a question to display in the list
	 *
	 * @param question The question to display
	 */
	public void addQuestion(Question question) {
		mListAdapter.addQuestion(question);
		mListAdapter.setQuestionSelected(mListAdapter.getCount() - 1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == mListAdapter.getCount()) {
			// The "add question" footer was selected
			mListener.onAddQuestionRequested();
		} else {
			// A question was selected
			mListAdapter.setQuestionSelected(position);
			mListener.onQuestionSelected((Question) mListAdapter.getItem(position));
		}
	}

    public List<Question> getQuestionList() {
        return mListAdapter.mList;
    }

	/** Adapter that provides Question objects for our list to display */
	private class QuestionListAdapter extends BaseAdapter {

		private static final int INVALID_POSITION = -1;

		/** The backing data for the adapter */
		List<Question> mList;

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