package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;

import org.json.JSONException;

/**
 * Created by David on 4/6/2014.
 */
public class CompleteFillInTheBlankView extends LinearLayout implements QuestionViewer {

    private TextView questionTitleTextView;
    private TextView questionTextTextView;
    private EditText blank1EditText;
    private EditText blank2EditText;
    private EditText blank3EditText;

    private Question question;
    private String answerText;
    private int numBlanks = 0;

    public CompleteFillInTheBlankView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.complete_fill_in_the_blank_base_view, this, true);
        questionTitleTextView = (TextView) findViewById(R.id.fitb_complete_title);
        questionTextTextView = (TextView) findViewById(R.id.fitb_qtext_textview);
        blank1EditText = (EditText) findViewById(R.id.fitb_blank1_answer_edittext);
        blank2EditText = (EditText) findViewById(R.id.fitb_blank2_answer_edittext);
        blank3EditText = (EditText) findViewById(R.id.fitb_blank3_answer_edittext);
        for(int i = 1; i <= 3; i++) {
            hideBlank(i);
        }
    }

    private void hideBlank(int blank) {
        if(blank == 1) {
            blank1EditText.setVisibility(View.GONE);
            blank1EditText.setText(null);
        }
        else if(blank == 2) {
            blank2EditText.setVisibility(View.GONE);
            blank2EditText.setText(null);
        }
        else if(blank == 3) {
            blank3EditText.setVisibility(View.GONE);
            blank3EditText.setText(null);
        }
    }

    private void unhideBlank(int blank) {
        if(blank == 1) {
            blank1EditText.setVisibility(View.VISIBLE);
        }
        else if(blank == 2) {
            blank2EditText.setVisibility(View.VISIBLE);
        }
        else if(blank == 3) {
            blank3EditText.setVisibility(View.VISIBLE);
        }
    }

    private void setQuestionText() {
        String qtext = null;
        try {
            qtext = question.getData().getString("questionText");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(qtext != null) {
            for (int i = 0; i < qtext.length(); i++) {
                if (qtext.charAt(i) == (char) 1) {
                    insertBlank();
                } else {
                    questionTextTextView.append(Character.toString(qtext.charAt(i)));
                }
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
            d = getResources().getDrawable(R.drawable.blank_1);
        }
        else if(numBlanks == 2) {
            d = getResources().getDrawable(R.drawable.blank_2);
        }
        else if(numBlanks == 3) {
            d = getResources().getDrawable(R.drawable.blank_3);
        }
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d);
        s.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        questionTextTextView.append(s);
        unhideBlank(numBlanks);
    }

    private void fillBlanks() {
        if(answerText != null) {
            String blanks[] = answerText.split(";");
            for(int i = 0; i < blanks.length; i++) {
                if(i == 0) {
                    blank1EditText.setText(blanks[i]);
                } else if(i == 1) {
                    blank2EditText.setText(blanks[i]);
                } else if(i == 2) {
                    blank3EditText.setText(blanks[i]);
                }
            }
        }
    }

    @Override
    public void setQuestion(int index, Question question) {
        this.question = question;
        setQuestionText();
        questionTitleTextView.setText(String.valueOf(index) + ". Fill in the Blank");
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public Answer getAnswer() {
        String answerText = blank1EditText.getText().toString() + ";" + blank2EditText.getText().toString() + ";" + blank3EditText.getText().toString() + ";";
        Answer answer = new Answer(question.toParseObject().getObjectId(), answerText);
        return answer;
    }

    @Override
    public void setAnswer(String answerText) {
        this.answerText = answerText;
        fillBlanks();
    }

    @Override
    public boolean isQuestionComplete() {
        if(numBlanks >= 1 && blank1EditText.getText().length() == 0) {
            return false;
        }
        if(numBlanks >= 2 && blank2EditText.getText().length() == 0) {
            return false;
        }
        if(numBlanks >= 3 && blank3EditText.getText().length() == 0) {
            return false;
        }
        return true;
    }
}
