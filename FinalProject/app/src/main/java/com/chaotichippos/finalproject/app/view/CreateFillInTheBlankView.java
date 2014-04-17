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

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class CreateFillInTheBlankView extends LinearLayout implements QuestionViewer {

    private static char BLANK_CHAR = (char) 1;

    private Question question;
    private Button insertBlankButton;
    private EditText questionTextEditText;
    private EditText blank1EditText;
    private ImageView blank1Image;
    private EditText blank2EditText;
    private ImageView blank2Image;
    private EditText blank3EditText;
    private ImageView blank3Image;
    private int numBlanks = 0;

    private ImageSpan[] blanks;

    public CreateFillInTheBlankView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.create_fill_in_the_blank_base_view, this, true);
        numBlanks = 0;
        insertBlankButton = (Button) findViewById(R.id.fitb_insertblank_button);
        questionTextEditText = (EditText) findViewById(R.id.fitb_qtext_edittext);
        blank1EditText = (EditText) findViewById(R.id.fitb_blank1_edittext);
        blank1Image = (ImageView) findViewById(R.id.fitb_blank1_img);
        blank2EditText = (EditText) findViewById(R.id.fitb_blank2_edittext);
        blank2Image = (ImageView) findViewById(R.id.fitb_blank2_img);
        blank3EditText = (EditText) findViewById(R.id.fitb_blank3_edittext);
        blank3Image = (ImageView) findViewById(R.id.fitb_blank3_img);
        loadBlanks();

        for(int i = 1; i <= 3; i++) {
            hideBlank(i);
        }

        questionTextEditText.addTextChangedListener(new TextWatcher() {
            boolean reset = false;
            boolean disable = false;
            int pos = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (after == (count - 1) && charSequence.charAt(start) == BLANK_CHAR) {
                    reset = true;
                    pos = start;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(reset && !disable) {
                    disable = true;
                    resetQuestionText(editable, pos);
                    disable = false;
                    reset = false;
                }
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

    private void loadBlanks() {
        blanks = new ImageSpan[3];
        Drawable d;
        ImageSpan span;
        d = getResources().getDrawable(R.drawable.blank1);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        span = new ImageSpan(d);
        blanks[0] = span;

        d = getResources().getDrawable(R.drawable.blank2);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        span = new ImageSpan(d);
        blanks[1] = span;

        d = getResources().getDrawable(R.drawable.blank3);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        span = new ImageSpan(d);
        blanks[2] = span;
    }

    private void insertBlank() {
        ++numBlanks;
        String temp = "";
        temp += BLANK_CHAR;
        SpannableString s = new SpannableString(temp);
        int cPos = questionTextEditText.getSelectionStart();
        s.setSpan(blanks[numBlanks-1], 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        questionTextEditText.append(s);
        questionTextEditText.setSelection(cPos + 1);
        unhideBlank(numBlanks);
    }

    private void setQuestionText() {
        String qtext = null;
        String blank1 = null;
        String blank2 = null;
        String blank3 = null;
        try {
            qtext = question.getData().getString("questionText");
            blank1 = question.getData().getString("blank1");
            blank2 = question.getData().getString("blank2");
            blank3 = question.getData().getString("blank3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(qtext != null) {
            for (int i = 0; i < qtext.length(); i++) {
                if (qtext.charAt(i) == BLANK_CHAR) {
                    insertBlank();
                } else {
                    questionTextEditText.append(Character.toString(qtext.charAt(i)));
                }
            }
        }
        blank1EditText.setText(blank1);
        blank2EditText.setText(blank2);
        blank3EditText.setText(blank3);
    }

    private void resetQuestionText(CharSequence qtext, int cursorPos) {
        for(int i = 1; i <= 3; i++) {
            hideBlank(i);
        }
        numBlanks = 0;
        questionTextEditText.setText(null);
        if(qtext != null) {
            for (int i = 0; i < qtext.length(); i++) {
                if (qtext.charAt(i) == BLANK_CHAR) {
                    insertBlank();
                } else {
                    questionTextEditText.append(Character.toString(qtext.charAt(i)));
                }
            }
        }
        questionTextEditText.setSelection(cursorPos);
    }

    @Override
    public Question getQuestion() {
        JSONObject data = new JSONObject();
        try {
            data.put("questionText", questionTextEditText.getText().toString());
            data.put("blank1", blank1EditText.getText().toString());
            data.put("blank2", blank2EditText.getText().toString());
            data.put("blank3", blank3EditText.getText().toString());
            data.put("numBlanks", numBlanks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        question.setData(data);
        return question;
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
        setQuestionText();
    }

    @Override
    public Answer getAnswer() {
        return null;
    }

	@Override
	public void setAnswer(String answerText) {

	}

	@Override
	public boolean isQuestionComplete() {
        if(questionTextEditText.getText().length() == 0 || numBlanks == 0) {
            return false;
        }
		if(numBlanks >=1 && blank1EditText.getText().length() == 0) {
            return false;
        }
        if(numBlanks >=2 && blank2EditText.getText().length() == 0) {
            return false;
        }
        if(numBlanks >=3 && blank3EditText.getText().length() == 0) {
            return false;
        }
        return true;
	}
}
