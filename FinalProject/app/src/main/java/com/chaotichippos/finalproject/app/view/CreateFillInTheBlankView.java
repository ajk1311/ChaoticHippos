package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;
import com.parse.ParseObject;

/**
 *
 */
public class CreateFillInTheBlankView extends LinearLayout implements QuestionViewer {

    private Question question;
    private Button insertBlankButton;
    private Button createButton;
    private EditText questionTitleEditText;
    private EditText questionTextEditText;
    private EditText blank1EditText;
    private ImageView blank1Image;
    private EditText blank2EditText;
    private ImageView blank2Image;
    private EditText blank3EditText;
    private ImageView blank3Image;
    private int numBlanks = 0;

    public CreateFillInTheBlankView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.create_fill_in_the_blank_base_view, this, true);
        question = new Question();
        question.setType(Question.Type.FILL_IN_THE_BLANK);
        numBlanks = 0;
        insertBlankButton = (Button) findViewById(R.id.fitb_insertblank_button);
        createButton = (Button) findViewById(R.id.fitb_create_button);
        questionTitleEditText = (EditText) findViewById(R.id.fitb_title_edittext);
        questionTextEditText = (EditText) findViewById(R.id.fitb_qtext_edittext);
        blank1EditText = (EditText) findViewById(R.id.fitb_blank1_edittext);
        blank1Image = (ImageView) findViewById(R.id.fitb_blank1_img);
        blank2EditText = (EditText) findViewById(R.id.fitb_blank2_edittext);
        blank2Image = (ImageView) findViewById(R.id.fitb_blank2_img);
        blank3EditText = (EditText) findViewById(R.id.fitb_blank3_edittext);
        blank3Image = (ImageView) findViewById(R.id.fitb_blank3_img);

        for(int i = 1; i <= 3; i++) {
            hideBlank(i);
        }

        questionTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (after == (count - 1) && charSequence.charAt(start) == (char) 1) {
                    hideBlank(numBlanks--);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        insertBlankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertBlank();
            }
        });
    }
    private void hideBlank(int blank) {
        if(blank == 1) {
            blank1EditText.setVisibility(View.GONE);
            blank1Image.setVisibility(View.GONE);
            blank1EditText.setText(null);
        }
        else if(blank == 2) {
            blank2Image.setVisibility(View.GONE);
            blank2EditText.setVisibility(View.GONE);
            blank2EditText.setText(null);
        }
        else if(blank == 3) {
            blank3Image.setVisibility(View.GONE);
            blank3EditText.setVisibility(View.GONE);
            blank3EditText.setText(null);
            insertBlankButton.setEnabled(true);
        }
    }

    private void unhideBlank(int blank) {
        if(blank == 1) {
            blank1EditText.setVisibility(View.VISIBLE);
            blank1Image.setVisibility(View.VISIBLE);
        }
        else if(blank == 2) {
            blank2EditText.setVisibility(View.VISIBLE);
            blank2Image.setVisibility(View.VISIBLE);
        }
        else if(blank == 3) {
            blank3EditText.setVisibility(View.VISIBLE);
            blank3Image.setVisibility(View.VISIBLE);
            insertBlankButton.setEnabled(false);
        }
    }

    private void insertBlank() {
        ++numBlanks;
        String temp = "";
        temp += (char) 1;
        SpannableString s = new SpannableString(temp);
        Drawable d = null;
        if(numBlanks == 1) {
            d = getResources().getDrawable(R.drawable.blank1);
        }
        else if(numBlanks == 2) {
            d = getResources().getDrawable(R.drawable.blank2);
        }
        else if(numBlanks == 3) {
            d = getResources().getDrawable(R.drawable.blank3);
        }
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        int cPos = questionTextEditText.getSelectionStart();
        ImageSpan span = new ImageSpan(d);
        s.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        questionTextEditText.append(s);
        questionTextEditText.setSelection(cPos + 1);
        unhideBlank(numBlanks);
    }
    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public Answer getAnswer() {
        return null;
    }
}
