package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;

public class EditableQuestionListItemView extends LinearLayout {

	private TextView mTextView;

	private View mRemoveBtnView;

	public EditableQuestionListItemView(Context context) {
		this(context, null, 0);
	}

	public EditableQuestionListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EditableQuestionListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOrientation(HORIZONTAL);
		setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
		setBackgroundResource(R.drawable.list_item_bg);
		setMinimumHeight(context.getResources()
				.getDimensionPixelSize(R.dimen.list_item_height));
		TypedValue listPreferredHeight = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight,
				listPreferredHeight, true);
		setMinimumHeight((int) listPreferredHeight
				.getDimension(context.getResources().getDisplayMetrics()));
		setDividerDrawable(context.getResources().getDrawable(R.drawable.divider_vertical));
		setShowDividers(SHOW_DIVIDER_MIDDLE);
		setDividerPadding(context.getResources()
				.getDimensionPixelSize(R.dimen.activity_vertical_margin));
		LayoutInflater.from(context).inflate(R.layout.question_list_item_editable, this, true);
		mTextView = (TextView) findViewById(R.id.text);
		mRemoveBtnView = findViewById(R.id.btn_remove);
	}

	public TextView getTextView() {
		return mTextView;
	}

	public View getRemoveButton() {
		return mRemoveBtnView;
	}
}
