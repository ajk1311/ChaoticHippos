package com.chaotichippos.finalproject.app.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SebastianMartinez on 4/10/14.
 */
public class MultipleChoiceAnswerView extends RelativeLayout implements QuestionViewer {
    ListView listView;
    TextView questionText;
    TextView selectedAnswerText;

    String currentlySelectedAnswerString;
    String blank = "_____";

    List<String> alphabet = new ArrayList<String>();

    MyAdapter adapter;

    private Question question;

    public MultipleChoiceAnswerView(Context context)  {
        this(context, null, 0);
    }

    public MultipleChoiceAnswerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleChoiceAnswerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.multiple_choice_student_view, this, true);

        listView = (ListView) findViewById(R.id.listview);
        questionText = (TextView) findViewById(R.id.QuestionText);
        selectedAnswerText = (TextView) findViewById(R.id.SelectedAnswerText);

        createAlphabet();

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                int checkedCount =  listView.getCheckedItemCount();
                MenuItem deleteItem = menu.findItem(R.id.Delete);
                MenuItem answerItem = menu.findItem(R.id.Answer);

                if(checkedCount > 1) {
                    deleteItem.setVisible(true);
                    answerItem.setVisible(false);
                }
                else if(checkedCount == 1) {
                    deleteItem.setVisible(false);
                    answerItem.setVisible(true);
                }

                return true;
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
                if(item.getItemId() == R.id.Delete) {
                    deleteCheckedItems();
                }
                if(item.getItemId() == R.id.Answer) {
                    setAnswer();
                }

                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                adapter.notifyDataSetChanged();
                mode.invalidate();
            }
        });
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public Answer getAnswer() {
        return new Answer(question.getObjectId(),currentlySelectedAnswerString);
    }

	@Override
	public void setAnswer(String answerText) {
        if(answerText != null) {
            selectedAnswerText.setText("Selected: " + answerText);
            currentlySelectedAnswerString = answerText;
        }
	}

	@Override
	public boolean isQuestionComplete() {
		if(currentlySelectedAnswerString == null) {
            return false;
        } else {
            return true;
        }
	}

    @Override
	public void setQuestion(int index, Question question) {
        this.question = question;
        try {
            questionText.setText(this.question.getData().getString("questionText"));
            JSONArray answers = this.question.getData().getJSONArray("answers");
            selectedAnswerText.setText("Selected: " + blank);
            List<Pair<String, String>> answersList = new ArrayList<Pair<String, String>>();
            if(answers.length() > 0) {
                for(int i = 0; i < answers.length(); i++){
                    answersList.add(new Pair<String,String>(alphabet.get(i),answers.get(i).toString()));
                }
            }
            adapter.setList(answersList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** Returning the selected answers */
    public void deleteCheckedItems() {
        adapter.deleteSelectedItems();
    }

    public void setAnswer() {
        Pair<String,String> answer = adapter.getSelectedAnswer();
        currentlySelectedAnswerString = answer.first;
        selectedAnswerText.setText("Selected: " + answer.first);
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

            for(int i = 0; i < mList.size(); i++) {
                if(checkedItemIndexes.get(i) == true) {
                    deleteItems.add(mList.get(i));
                    if(mList.get(i).first == currentlySelectedAnswerString) {
                        selectedAnswerText.setText("Selected: " + blank);
                        currentlySelectedAnswerString = null;
                    }
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

            notifyDataSetChanged();
        }

        public Pair<String,String> getSelectedAnswer() {
            String answerChar = null;
            String answer = null;
            for (int i = 0; i < mList.size(); i++) {
                if (listView.isItemChecked(i)) {
                    answerChar = mList.get(i).first;
                    answer = mList.get(i).second;
                }
            }

            if(answerChar.length() > 0 && answer.length() > 0) {
                return new Pair<String, String>(answerChar, answer);
            }
            return null;
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

            if(listView.isItemChecked(position)) {
                convertView.setBackgroundColor(Color.parseColor("#aa66cc"));
            }
            else {
                convertView.setBackgroundDrawable(null);
            }

            // TextViews
            Pair<String, String> pair = mList.get(position);
            TextView answer = (TextView) convertView.findViewById(R.id.Answer);
            TextView answerID = (TextView) convertView.findViewById(R.id.AnswerID);
            answerID.setText(pair.first);
            answer.setText(pair.second);

            return convertView;
        }
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

}
