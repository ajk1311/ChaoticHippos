package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.model.TrueOrFalseQuestion;
import com.chaotichippos.finalproject.app.util.ScreenUtil;

/**
 * This view displays the interface for creating a true or false question
 */
public class TrueFalseCreateView extends LinearLayout implements QuestionViewer {

	private static final int ANSWER_INVALID = -1;

	private static final int ANSWER_TRUE = 0;

	private static final int ANSWER_FALSE = 1;


	private EditText mQuestionText;
	private TrueFalseAnswerGroup mAnswer;

	private TrueOrFalseQuestion mQuestion;


	public TrueFalseCreateView(Context context) {
		super(context);
		setOrientation(VERTICAL);

		final int marginSmall = ScreenUtil.getDimensionPixelSize(8);
		final int marginMedium = ScreenUtil.getDimensionPixelSize(16);

		final LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, // Width
				ViewGroup.LayoutParams.WRAP_CONTENT); // Height
		params.setMargins(marginMedium, marginMedium, marginMedium, marginSmall);

		final LayoutParams params2 = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, // Width
				0,  // Height
				1.0f); // Weight
		params2.setMargins(marginMedium, marginSmall, marginMedium, marginMedium);

		mQuestionText = new EditText(context);
		addView(mQuestionText, params);

		mAnswer = new TrueFalseAnswerGroup(context);
		addView(mAnswer, params2);
	}

	@Override
	public void setQuestion(Question question) {
		mQuestion = (TrueOrFalseQuestion) question;
		mQuestionText.setText(mQuestion.getQuestionText());
		mAnswer.setSelectedIndex(mQuestion.getAnswer() ? ANSWER_TRUE : ANSWER_FALSE);
	}

	@Override
	public Question getQuestion() {
		mQuestion.setQuestionText(mQuestionText.getText().toString().trim());
		mQuestion.setAnswer(mAnswer.getSelectedAnswerIndex() == ANSWER_TRUE);
		return mQuestion;
	}

	@Override
	public Answer getAnswer() {
		return null;
	}

	/** Layout that holds the two choices for a true or false question */
	private static class TrueFalseAnswerGroup extends LinearLayout {

		/** Each of the choices will have these parameters */
		private static final LayoutParams PARAMS = new LayoutParams(
				0, // Width
				ViewGroup.LayoutParams.WRAP_CONTENT, // Height
				1.0f); // Weight

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

		private OnClickListener mSelectionListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewSelected(v);
			}
		};
	}
}
