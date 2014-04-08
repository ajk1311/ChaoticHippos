package com.chaotichippos.finalproject.app.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.MultipleChoiceQuestion;
import com.chaotichippos.finalproject.app.model.Question;

import java.util.ArrayList;
import java.util.List;

public class CreateMultipleChoiceView extends RelativeLayout implements QuestionViewer {
    private static final String TAG = "MainActivity";

    EditText questionTextEditor;
    EditText answerTextEditor;

    Button addAnswerButton;

    MyAdapter adapter;
    ListView listView;

    List<String> answerIDs = new ArrayList<String>();
    List<String> answers = new ArrayList<String>();
    List<String> alphabet = new ArrayList<String>();
    int alphabetIndex = 0;

    MultipleChoiceQuestion thisQuestion;

    public CreateMultipleChoiceView(Context context)  {
        this(context, null, 0);
    }

    public CreateMultipleChoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreateMultipleChoiceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.create_multiple_choice_base_view, this, true);

        listView = ( ListView ) findViewById(R.id.listview);

        /** For contextual action mode, the choice mode should be CHOICE_MODE_MULTIPLE_MODAL */
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        questionTextEditor = (EditText)findViewById(R.id.QuestionText);
        answerTextEditor = (EditText)findViewById(R.id.CurrentAnswerText);

        addListenerOnAnswerButton();

        createAlphabet();

        // Instantiating an adapter for the list
        adapter = new MyAdapter();

        // Getting a reference to listview of main.xml layout file
        listView = ( ListView ) findViewById(R.id.listview);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
			}

			/** This will be invoked when action mode is created. In our case , it is on long clicking a menu item */
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				final Activity activity = (Activity) getContext();
				activity.getMenuInflater().inflate(R.menu.create_multiple_choice_menu, menu);
				return true;
			}

			/** Invoked when an action in the action mode is clicked */
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				deleteCheckedItems();
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
			}
		});
    }

    /** Returning the selected answers */
    public void deleteCheckedItems() {
        adapter.deleteSelectedItems();
    }

    public void addListenerOnAnswerButton() {
        addAnswerButton = (Button)findViewById(R.id.AddAnswer);

        addAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg) {
                //Add Answer Cell to List
                if(alphabetIndex < alphabet.size()) {
                    String answer = answerTextEditor.getText().toString();
                    answerTextEditor.setText(null);

                    adapter.addPair(new Pair<String,String>(alphabet.get(alphabetIndex),answer));

                    alphabetIndex++;
                }
            }
        });
    }

    public void createAlphabet() {
        alphabet.add("A");
        alphabet.add("B");
        alphabet.add("C");
        alphabet.add("D");
        alphabet.add("E");
        alphabet.add("F");
        alphabet.add("G");
        alphabet.add("H");
        alphabet.add("I");
        alphabet.add("J");
        alphabet.add("K");
        alphabet.add("L");
        alphabet.add("M");
        alphabet.add("N");
        alphabet.add("O");
        alphabet.add("P");
        alphabet.add("Q");
        alphabet.add("R");
        alphabet.add("S");
        alphabet.add("T");
        alphabet.add("U");
        alphabet.add("V");
        alphabet.add("W");
        alphabet.add("X");
        alphabet.add("Y");
        alphabet.add("Z");
    }

    private class MyAdapter extends BaseAdapter {

        private List<Pair<String, String>> mList;

        public MyAdapter() {
            mList = new ArrayList<Pair<String, String>>();
        }

        public void addPair(Pair<String, String> pair) {
            mList.add(pair);
            notifyDataSetChanged();
        }

        public void setList(List<Pair<String, String>> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public List<Pair<String, String>> getList() {
            return mList;
        }

        public void deleteSelectedItems() {
            SparseBooleanArray checkedItemIndexes =  listView.getCheckedItemPositions();
            List<Pair<String, String>> deleteItems = new ArrayList<Pair<String, String>>();
            List<Pair<String, String>> newmList = new ArrayList<Pair<String, String>>();

            for(int i = 0; i < mList.size(); i++) {
                if(checkedItemIndexes.get(i) == true) {
                    deleteItems.add(mList.get(i));
                }
            }

            for (int i = 0; i < mList.size(); i++) {
                if (listView.isItemChecked(i)) {
                    listView.setItemChecked(i, false);
                }
            }

            for(int i = 0; i < deleteItems.size(); i++) {
                mList.remove(deleteItems.get(i));
            }

            for(int i = 0; i < mList.size(); i++) {
                newmList.add(new Pair<String,String>(alphabet.get(i), mList.get(i).second));
            }

            mList = newmList;

            alphabetIndex = mList.size();

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //on first create
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.create_multiple_choice_list_item_view, null);
            }

            // Check box state
            CheckBox box = (CheckBox) convertView.findViewById(R.id.checkBox);
            box.setOnCheckedChangeListener(null);
            box.setChecked(listView.isItemChecked(position));
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listView.setItemChecked(position, isChecked);
                }
            });

            // TextViews
            Pair<String, String> pair = mList.get(position);
            TextView answer = (TextView) convertView.findViewById(R.id.Answer);
            TextView answerID = (TextView) convertView.findViewById(R.id.AnswerID);
            answerID.setText(pair.first);
            answer.setText(pair.second);

            return convertView;
        }
    }

    @Override
    public Question getQuestion() {
        thisQuestion.setQuestionText(questionTextEditor.getText().toString());

        List<Pair<String, String>> list = adapter.getList();

        for(int i = 0; i < list.size(); i++) {
            thisQuestion.addChoice(list.get(i).second);
        }

        return thisQuestion;
    }

    @Override
    public Answer getAnswer() {
        return null;
    }

    @Override
    public void setQuestion(Question question) {
        thisQuestion = (MultipleChoiceQuestion)question;

        if(thisQuestion.getQuestionText().length() > 0) {
            questionTextEditor.setText(thisQuestion.getQuestionText());
        }

        if(thisQuestion.getChoices().size() > 0) {
            List<String> list = thisQuestion.getChoices();
            for(int i = 0; i < list.size(); i++) {
                adapter.addPair(new Pair<String,String>(alphabet.get(alphabetIndex),list.get(i)));
            }
        }
    }
}
