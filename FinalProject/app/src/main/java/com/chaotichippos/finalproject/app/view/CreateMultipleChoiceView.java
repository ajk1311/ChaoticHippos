package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import android.view.LayoutInflater;
import com.chaotichippos.finalproject.app.R;

import android.text.TextWatcher;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.CheckBox;

/**
 * Created by SebastianMartinez on 3/31/14.
 */
public class CreateMultipleChoiceView extends RelativeLayout{

    EditText questionTextEditor;
    String questionText;

    Button addAnswerButton;
    Button deleteSelectedAnswersButton;

    List<HashMap<String,String>> aList;
    SimpleAdapter adapter;
    ListView listView;

    List<String> answerIDs = new ArrayList<String>();
    List<String> answers = new ArrayList<String>();

    public CreateMultipleChoiceView(Context context)  {
        this(context, null, 0);
    }

    public CreateMultipleChoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreateMultipleChoiceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.create_multiple_choice_base_view, this, true);

        questionTextEditor = (EditText)findViewById(R.id.QuestionText);
        questionTextEditor.addTextChangedListener(questionTextWatcher);

        addListenerOnAnswerButton();
        addListenerOnDeleteButton();

        answerIDs.add("AnswerID 1");
        answerIDs.add("AnswerID 2");

        answers.add("Answer");
        answers.add("Answer");

        // Each row in the list stores country name, currency and flag
        aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<answerIDs.size();i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("AnswerID", answerIDs.get(i));
            hm.put("Answer", answers.get(i));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "AnswerID","Answer"};

        // Ids of views in listview_layout
        int[] to = { R.id.AnswerID,R.id.Answer,};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        adapter = new SimpleAdapter(context, aList, R.layout.create_multiple_choice_list_item_view, from, to);

        // Getting a reference to listview of main.xml layout file
        listView = ( ListView ) findViewById(R.id.listview);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }

    private final TextWatcher questionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            questionText = questionTextEditor.getText().toString();
        }
    };

    public void addListenerOnAnswerButton() {
        addAnswerButton = (Button)findViewById(R.id.AddAnswer);

        addAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg) {
                //Add Custom Answer Cell to List
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("AnswerID", "Enter ID");
                hm.put("Answer", "Enter Answer");
                aList.add(hm);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addListenerOnDeleteButton() {
        deleteSelectedAnswersButton = (Button)findViewById(R.id.RemoveSelectedAnswers);

        deleteSelectedAnswersButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg) {
                CheckBox cb;
                int listSize = aList.size();
                for (int x = 0; x < listSize; x++) {
                    cb = (CheckBox)listView.getChildAt(x).findViewById(R.id.checkBox);
                    if(cb.isChecked()){
                        //delete that cell
                        cb.setChecked(false);
                        aList.remove(x);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
    }
}
