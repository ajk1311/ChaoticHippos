package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.FillInTheBlankQuestion;

/**
 *
 */
public class CreateFillInTheBlankView extends LinearLayout {

    private FillInTheBlankQuestion question;
    private Button insertBlankButton;
    private Button createButton;
    private EditText questionTitleEditText;
    private EditText questionTextEditText;
    private EditText blank1EditText;
    private EditText blank2EditText;
    private EditText blank3EditText;

    public CreateFillInTheBlankView(Context context) {
        super(context);
        question = new FillInTheBlankQuestion();
        insertBlankButton = (Button) findViewById(R.id.fitb_insertblank_button);
        createButton = (Button) findViewById(R.id.fitb_create_button);
        questionTitleEditText = (EditText) findViewById(R.id.fitb_title_edittext);
        questionTextEditText = (EditText) findViewById(R.id.fitb_qtext_edittext);
        blank1EditText = (EditText) findViewById(R.id.fitb_blank1_edittext);
        blank2EditText = (EditText) findViewById(R.id.fitb_blank2_edittext);
        blank3EditText = (EditText) findViewById(R.id.fitb_blank3_edittext);

        insertBlankButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String qText = questionTextEditText.getText().toString();
                int cPos = questionTextEditText.getSelectionStart();
                String first = qText.substring(0, cPos);
                String second = qText.substring(cPos);
                String newQText = first + " <BLANK> " + second;
                questionTextEditText.setSelection(cPos + 9);
                questionTextEditText.setText(newQText);
            }
        });

        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                question.setQuestionText(questionTextEditText.getText().toString());
                question.setAnswer1(blank1EditText.getText().toString());
                question.setAnswer2(blank2EditText.getText().toString());
                question.setAnswer3(blank3EditText.getText().toString());
            }
        });
    }



}
