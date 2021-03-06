package com.chaotichippos.finalproject.app.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;
import com.chaotichippos.finalproject.app.util.DebugLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateMatchingView extends LinearLayout implements QuestionViewer{

    private TextView questionTitleTextView;
    private ListView list1;
    private ListView list2;
    private ArrayAdapter<String> listAdapter1;
    private ArrayAdapter<String> listAdapter2;
    private EditText matchingText1;
    private EditText matchingText2;
    private Path path = new Path();
    Paint paint = new Paint();
    private ArrayList<String> myColors;
    private MatchingAdapter listAdapter;
    private static final String TAG = "OUTPUT";
    private ArrayList<TeacherMatchingPair> matchItems;
    private int curSelectedPair;
    private MultiChoiceModeListener mMultiChoiceModeListener;
    private Set<Integer> selectedIndexes;
    private Question question;


    public CreateMatchingView(Context context) {
        super(context);

        //((Activity)getContext()).startActionMode();
        question = new Question();
        question.setType(Question.Type.MATCHING);
        myColors = new ArrayList<String>();
        myColors.add("#5F9F9F");
        myColors.add("#00FFFF");
        LayoutInflater.from(context).inflate(R.layout.create_matching_list, this, true);
        questionTitleTextView = (TextView) findViewById(R.id.matching_create_title);
        curSelectedPair = -1;

        setWillNotDraw(false);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);



        selectedIndexes = new HashSet<Integer>();
        matchItems = new ArrayList<TeacherMatchingPair>();
        listAdapter = new MatchingAdapter(getContext(), R.layout.create_matching_list_item, matchItems);
        list1 = (ListView) findViewById(R.id.createMatchingList1);
        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list1.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                DebugLog.v(TAG, "here: " + Integer.toString(i));
                if(selectedIndexes.contains(i))
                {
                    selectedIndexes.remove(i);
                    matchItems.get(i).setSelected(false);
                    listAdapter.notifyDataSetChanged();
                } else {
                    selectedIndexes.add(i);
                    matchItems.get(i).setSelected(true);
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                ((Activity)getContext()).getMenuInflater().inflate(R.menu.create_matching_menu, menu);
                return true;
                //return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                deleteSelectedMatch();
                actionMode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                for(int i:selectedIndexes)
                {
                    matchItems.get(i).setSelected(false);
                }
                selectedIndexes.clear();
                listAdapter.notifyDataSetChanged();
            }
        });

        matchingText1 = (EditText) findViewById(R.id.matchingText1);
        matchingText2 = (EditText) findViewById(R.id.matchingText2);
        list1.setAdapter(listAdapter);

		findViewById(R.id.add_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!matchingText1.getText().toString().isEmpty() && !matchingText2.getText().toString().isEmpty())
                {
                    listAdapter.add(new TeacherMatchingPair(matchingText1.getText().toString(),matchingText2.getText().toString(),false));
                    matchingText1.setText("");
                    matchingText2.setText("");
                }
            }
        });


    }

    public void deleteSelectedMatch() {
        /*List<Integer> myTempList = new ArrayList<Integer>();

        for(Integer i: selectedIndexes)
        {
            Log.v(TAG,Integer.toString(i));
            matchItems.remove(i);
            //selectedIndexes.remove(i);
            listAdapter.notifyDataSetChanged();
        }*/

        List<TeacherMatchingPair> deleteItems = new ArrayList<TeacherMatchingPair>();

        for(int i: selectedIndexes) {
            deleteItems.add(matchItems.get(i));
        }

        for(int i: selectedIndexes) {
            matchItems.get(i).setSelected(false);
        }

        for(int i = 0; i < deleteItems.size(); i++) {
            matchItems.remove(deleteItems.get(i));
        }

        selectedIndexes.clear();
        //update checks to null and callbacks

        listAdapter.notifyDataSetChanged();
    }

    private class MatchingAdapter extends ArrayAdapter<TeacherMatchingPair>{

        private static final String TAG = "OUTPUT";

        private TextView questionText;
        private TextView answerText;
        private ArrayList<TeacherMatchingPair> items;
        private TeacherMatchingPair myPair;
        public MatchingAdapter(Context context, int textViewResourceId, ArrayList<TeacherMatchingPair> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public void deleteSelectedItem() {
            matchItems.remove(matchItems.get(curSelectedPair));
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            myPair = items.get(position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.create_matching_list_item, null);
            }
            if (myPair != null) {
                questionText = (TextView) v.findViewById(R.id.createQuestionText);
                answerText = (TextView) v.findViewById(R.id.createAnswerText);

                if(questionText != null)
                {
                    questionText.setText(myPair.getQuestion());
                }
                if(answerText != null) {
                    answerText.setText(myPair.getAnswer());
                }

                if(myPair.getSelected())
                {
                    v.setBackgroundColor(Color.parseColor("#aa66cc"));
                } else {
                    v.setBackgroundDrawable(null);
                }
            }
            return v;
        }
    }

    private class TeacherMatchingPair {
        private String question;
        private String answer;
        private boolean isSelected;

        TeacherMatchingPair(String question, String answer, boolean isSelected)
        {
            this.question = question;
            this.answer = answer;
            this.isSelected = isSelected;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public boolean getSelected() {
            return isSelected;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }

    private void setMyQuestion() {
        try {
            DebugLog.v(TAG, "inside try");

            JSONArray left = this.question.getData().getJSONArray("leftSide");
            JSONArray right = this.question.getData().getJSONArray("rightSide");
            if(left != null) {
                DebugLog.v(TAG, "not null list");
                for(int i = 0; i < left.length(); i++)
                {
                    listAdapter.add(new TeacherMatchingPair((String)left.get(i),(String)right.get(i),false));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Question getQuestion() {
        JSONObject data = new JSONObject();
        JSONArray leftSide =  new JSONArray();
        JSONArray rightSide =  new JSONArray();
        for(TeacherMatchingPair pair: matchItems)
        {
            leftSide.put(pair.getQuestion());
            rightSide.put(pair.getAnswer());
        }
        try {
            data.put("leftSide", leftSide);
            data.put("rightSide", rightSide);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        question.setData(data);
        return question;
    }

    @Override
    public void setQuestion(int index, Question question) {
        this.question = question;
        setMyQuestion();
        questionTitleTextView.setText(String.valueOf(index) + ". " +
				getContext().getString(R.string.matching_title));
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
        if(list1.getCount() >= 2)
        {
            return true;
        }
		return false;
	}
}