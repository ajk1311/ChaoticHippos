package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.TrueOrFalseQuestionWrapper;
import com.chaotichippos.finalproject.app.util.ScreenUtil;

/**
 * Since the functionality of each true/false view is so similar, abstract out the common stuff
 * into this base class.
 */
public abstract class TrueFalseView extends ScrollView implements QuestionViewer {

	protected abstract TextView getTextDisplayView(Context context);

	public static final int ANSWER_INVALID = -1;

	public static final int ANSWER_TRUE = 0;

	public static final int ANSWER_FALSE = 1;


	private TextView mQuestionText;
	private TrueFalseAnswerGroup mAnswer;

	private TrueOrFalseQuestionWrapper mQuestionWrapper;

	public TrueFalseView(Context context) {
		super(context);

		final LinearLayout container = new LinearLayout(context);
		container.setOrientation(LinearLayout.VERTICAL);

		final int marginSmall = ScreenUtil.getDimensionPixelSize(8);
		final int marginMedium = ScreenUtil.getDimensionPixelSize(16);

		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(marginMedium, marginMedium, marginMedium, marginSmall);

		final LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
		params2.setMargins(marginMedium, marginSmall, marginMedium, marginMedium);

		mQuestionText = getTextDisplayView(context);
		container.addView(mQuestionText, params);

		mAnswer = new TrueFalseAnswerGroup(context);
		container.addView(mAnswer, params2);

		addView(container);
	}

	@Override
	public void setQuestion(Question question) {
		mQuestionWrapper = new TrueOrFalseQuestionWrapper(question);
		mQuestionText.setText(mQuestionWrapper.getQuestionText());
		mAnswer.setSelectedIndex(mQuestionWrapper.getAnswer());
	}

	@Override
	public Question getQuestion() {
		mQuestionWrapper.setQuestionText(mQuestionText.getText().toString().trim());
		mQuestionWrapper.setAnswer(mAnswer.getSelectedAnswerIndex());
		return mQuestionWrapper.get();
	}

	@Override
	public Answer getAnswer() {
		return new Answer(mQuestionWrapper.get().getObjectId(),
				String.valueOf(mAnswer.getSelectedAnswerIndex() == ANSWER_TRUE));
	}

	@Override
	public boolean isQuestionComplete() {
		boolean complete = true;
		if (mQuestionText instanceof EditText) {
			complete = ((EditText) mQuestionText).getText().length() > 0;
		}
		return mAnswer.getSelectedAnswerIndex() != ANSWER_INVALID && complete;
	}

	@Override
	public void setAnswer(String answerText) {
		final int selectedAnswer = Integer.parseInt(answerText);
		mAnswer.setSelectedIndex(selectedAnswer);
	}

	/** Layout that holds the two choices for a true or false question */
	private static class TrueFalseAnswerGroup extends LinearLayout {

		/** Each of the choices will have these parameters */
		private static final LayoutParams PARAMS = new LayoutParams(0,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

		/** The currently selected answer. Should be ANSWER_TRUE, _FALSE, or _INVALID */
		private int mSelectedIndex = ANSWER_INVALID;

		public TrueFalseAnswerGroup(Context context) {
			super(context);
			setOrientation(HORIZONTAL);
			addView(createAnswerView(context, ANSWER_TRUE));
			addView(createAnswerView(context, ANSWER_FALSE));
		}

		/** @return Either ANSWER_TRUE, _FALSE, or _INVALID */
		public int getSelectedAnswerIndex() {
			return mSelectedIndex;
		}

		public void setSelectedIndex(int index) {
			mSelectedIndex = index;
			for (int i = 0, sz = getChildCount(); i < sz; i++) {
				getChildAt(i).setActivated(i == mSelectedIndex);
			}
		}

		/** Build a {@link android.widget.TextView} that displays the answer option */
		private View createAnswerView(Context context, int answer) {
			final TextView textView = new TextView(context);
			textView.setTextColor(context.getResources()
					.getColorStateList(R.drawable.true_false_answer_text_color));
			textView.setOnClickListener(mSelectionListener);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
			textView.setText(answer == ANSWER_TRUE ? "T" : "F");
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(PARAMS);
			textView.setClickable(true);
			return textView;
		}

		/** Maintains a single selection and keeps track of it */
		private void setViewSelected(View v) {
			View child;
			for (int i = 0, sz = getChildCount(); i < sz; i++) {
				child = getChildAt(i);
				if (child == v) {
					child.setActivated(true);
					mSelectedIndex = i;
				} else {
					child.setActivated(false);
				}
			}
		}

		/** Handles clicks on the true or false options */
		private OnClickListener mSelectionListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewSelected(v);
			}
		};
	}
}
