package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;

/**
 * Created by David on 4/6/2014.
 */
public class CompleteFillInTheBlankView extends LinearLayout implements QuestionViewer {

    private TextView questionTitleTextView;
    private TextView questionTextTextView;
    private EditText blank1EditText;
    private EditText blank2EditText;
    private EditText blank3EditText;
    private ImageView blank1Image;
    private ImageView blank2Image;
    private ImageView blank3Image;

    private Question question;
    private int numBlanks = 0;

    public CompleteFillInTheBlankView(Context context) {
        super(context);
        questionTitleTextView = (TextView) findViewById(R.id.fitb_title_textview);
        questionTextTextView = (TextView) findViewById(R.id.fitb_qtext_textview);
        blank1EditText = (EditText) findViewById(R.id.fitb_blank1_answer_edittext);
        blank2EditText = (EditText) findViewById(R.id.fitb_blank2_answer_edittext);
        blank3EditText = (EditText) findViewById(R.id.fitb_blank3_answer_edittext);
        blank1Image = (ImageView) findViewById(R.id.fitb_blank1_answer_img);
        blank2Image = (ImageView) findViewById(R.id.fitb_blank2_answer_img);
        blank3Image = (ImageView) findViewById(R.id.fitb_blank3_answer_img);
        for(int i = 1; i <= 3; i++) {
            hideBlank(i);
        }

        questionTitleTextView.setText(question.getType().getTitle());
        setQuestionText();
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
        }
    }

    private void setQuestionText() {
        String qtext = question.getData().getString("questionType");
        for(int i = 0; i < qtext.length(); i++) {
            if(qtext.charAt(i) == (char) 1) {
                insertBlank();
                numBlanks++;
                unhideBlank(numBlanks);
            }
            else {
                questionTextTextView.append(Character.toString(qtext.charAt(i)));
            }
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
        ImageSpan span = new ImageSpan(d);
        s.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        questionTextTextView.append(s);
        unhideBlank(numBlanks);
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public Answer getAnswers() {
        String answerText = blank1EditText.getText().toString() + ";" + blank2EditText.getText().toString() + ";" + blank3EditText.getText().toString() + ";";
        Answer answer = new Answer(question.getObjectId(), answerText);
        return answer;
    }
}
