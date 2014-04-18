package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.model.Answer;
import com.chaotichippos.finalproject.app.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by Nick on 4/3/14.
 */

public class StudentMatchingView extends LinearLayout implements QuestionViewer{

    private ListView list1;
    private ListView list2;
    private MatchQuestionAdapter listAdapter1;
    private MatchAnswerAdapter listAdapter2;
    private ArrayList<String> myColors;
    private String curSelectedColor;
    private int curSelectedQuestion;
    private  ArrayList<MatchingQuestion> m_questions;
    private ArrayList<MatchingAnswer> m_answers;
    private static final String TAG = "OUTPUT";
    private Question question;



    class questData {

        public String text;
        public String color;

        questData(String text, String color)
        {
            this.text = text;
            this.color = color;
        }

        @Override public String toString() {
            return text;
        }
    }


    public StudentMatchingView(Context context) {
        super(context);

        //((Activity)getContext()).startActionMode();

        LayoutInflater.from(context).inflate(R.layout.student_matching_list, this, true);


        m_questions = new ArrayList<MatchingQuestion>();
        m_answers = new ArrayList<MatchingAnswer>();
        myColors = new ArrayList<String>();

        /**** ADD COLORS TO ARRAY ****/

        myColors.add("#000099"); //dark blue
        myColors.add("#660066"); //magenta
        myColors.add("#FF66CC"); //dark green
        myColors.add("#0066FF"); //gray pale blue
        myColors.add("#CC9900"); //gold
        myColors.add("#FF0000"); //red
        myColors.add("#669999"); // gray blue
        myColors.add("#009933"); //lighter green
        myColors.add("#FF00FF"); //pinkish
        myColors.add("#FFFF00"); //yellow
        myColors.add("#66FFFF"); //very light blue
        myColors.add("#663300"); //brown
        myColors.add("#00FF00"); //bright green
        myColors.add("#FF66CC"); //light pink
        myColors.add("#FF6600"); //orange
        myColors.add("#FFFF99"); //light pale
        myColors.add("#FF99CC"); //very light pink
        myColors.add("#66FF66"); //pale green
        myColors.add("#CC3300"); //dark orange
        myColors.add("#00CCFF"); //light blue

        /*****************************/

        listAdapter1 = new MatchQuestionAdapter(getContext(), android.R.layout.simple_list_item_1, m_questions);
        listAdapter2 = new MatchAnswerAdapter(getContext(), android.R.layout.simple_list_item_1, m_answers);
        list1 = (ListView) findViewById(R.id.studentMatchingList1);
        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list1.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
        list2 = (ListView) findViewById(R.id.studentMatchingList2);
        list1.setAdapter(listAdapter1);
        list2.setAdapter(listAdapter2);

        /*for(int i = 0; i < 20; i++)
        {
            listAdapter1.add(new MatchingQuestion("Question " + i,myColors.get(i),false,i,-1));
            listAdapter2.add(new MatchingAnswer("Answer " + i,-1));
        }*/


        //testing


        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.v(TAG, view.getBackground().toString());
                if(view.getBackground() != null)
                {
                    if(((MatchingQuestion)adapterView.getItemAtPosition(i)).getAnswerId() != -1)
                    {
                        m_answers.get(((MatchingQuestion)adapterView.getItemAtPosition(i)).getAnswerId()).setQuestionID(-1);
                        listAdapter2.notifyDataSetChanged();
                    }
                    view.setBackgroundDrawable(null);
                    if(curSelectedQuestion != -1 && m_questions.get(curSelectedQuestion).getAnswerId() == -1)
                    {
                        Log.v(TAG, "in");
                        Log.v(TAG, Integer.toString( m_questions.get(curSelectedQuestion).getAnswerId()));
                        Log.v(TAG, Integer.toString(curSelectedQuestion));


                    } else
                    {
                        Log.v(TAG, "out");
                        curSelectedColor = "";
                        curSelectedQuestion = -1;
                    }

                    ((MatchingQuestion)adapterView.getItemAtPosition(i)).setSelected(false);
                    ((MatchingQuestion)adapterView.getItemAtPosition(i)).setAnswerId(-1);
                    listAdapter1.notifyDataSetChanged();


                } else
                {
                    if(curSelectedQuestion != -1 && m_questions.get(curSelectedQuestion).getAnswerId() == -1)
                    {
                        m_questions.get(curSelectedQuestion).setAnswerId(-1);
                        m_questions.get(curSelectedQuestion).setSelected(false);
                        view.setBackgroundColor(Color.parseColor(((MatchingQuestion)adapterView.getItemAtPosition(i)).getColor()));
                        curSelectedColor = ((MatchingQuestion)adapterView.getItemAtPosition(i)).getColor();
                        curSelectedQuestion = ((MatchingQuestion)adapterView.getItemAtPosition(i)).getId();
                        ((MatchingQuestion)adapterView.getItemAtPosition(i)).setSelected(true);
                        listAdapter1.notifyDataSetChanged();
                    }
                    view.setBackgroundColor(Color.parseColor(((MatchingQuestion) adapterView.getItemAtPosition(i)).getColor()));
                    curSelectedColor = ((MatchingQuestion)adapterView.getItemAtPosition(i)).getColor();
                    curSelectedQuestion = ((MatchingQuestion)adapterView.getItemAtPosition(i)).getId();
                    ((MatchingQuestion)adapterView.getItemAtPosition(i)).setSelected(true);
                }
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(view.getBackground() != null)
                {
                    if(curSelectedColor.isEmpty())
                    {
                        view.setBackgroundDrawable(null);
                        if(((MatchingAnswer)adapterView.getItemAtPosition(i)).getQuestionID() != -1)
                        {
                            Log.v(TAG,Integer.toString(((MatchingAnswer)adapterView.getItemAtPosition(i)).getQuestionID()));

                            m_questions.get(((MatchingAnswer)adapterView.getItemAtPosition(i)).getQuestionID()).setAnswerId(-1);
                            m_questions.get(((MatchingAnswer)adapterView.getItemAtPosition(i)).getQuestionID()).setSelected(false);
                            listAdapter1.notifyDataSetChanged();
                        }
                        ((MatchingAnswer)adapterView.getItemAtPosition(i)).setQuestionID(-1);
                    } else if(((ColorDrawable)view.getBackground()).getColor() == Color.parseColor(curSelectedColor))
                    {
                        view.setBackgroundDrawable(null);
                        ((MatchingAnswer)adapterView.getItemAtPosition(i)).setQuestionID(-1);
                        m_questions.get(curSelectedQuestion).setAnswerId(-1);
                        m_questions.get(curSelectedQuestion).setSelected(false);
                        curSelectedQuestion = -1;
                        curSelectedColor = "";
                        listAdapter1.notifyDataSetChanged();
                    } else
                    {
                        if(m_questions.get(curSelectedQuestion).getAnswerId() != -1)
                        {
                            m_answers.get(m_questions.get(curSelectedQuestion).getAnswerId()).setQuestionID(-1);
                            listAdapter2.notifyDataSetChanged();
                        }
                        m_questions.get(((MatchingAnswer)adapterView.getItemAtPosition(i)).getQuestionID()).setAnswerId(-1);
                        m_questions.get(((MatchingAnswer)adapterView.getItemAtPosition(i)).getQuestionID()).setSelected(false);
                        ((MatchingAnswer)adapterView.getItemAtPosition(i)).setQuestionID(curSelectedQuestion);
                        m_questions.get(curSelectedQuestion).setAnswerId(i);
                        view.setBackgroundColor(Color.parseColor(curSelectedColor));
                        listAdapter1.notifyDataSetChanged();
                    }


                } else
                {
                    if(!curSelectedColor.isEmpty())
                    {
                        if(m_questions.get(curSelectedQuestion).getAnswerId() != -1)
                        {
                            m_answers.get(m_questions.get(curSelectedQuestion).getAnswerId()).setQuestionID(-1);
                            listAdapter2.notifyDataSetChanged();
                        }
                        ((MatchingAnswer)adapterView.getItemAtPosition(i)).setQuestionID(curSelectedQuestion);
                        Log.v(TAG,Integer.toString(i));
                        m_questions.get(curSelectedQuestion).setAnswerId(i);
                        view.setBackgroundColor(Color.parseColor(curSelectedColor));
                    }
                }
            }
        });

    }

    private class MatchQuestionAdapter extends ArrayAdapter<MatchingQuestion>{

        //private static final String TAG = "OUTPUT";

        private TextView questionText;
        private ArrayList<MatchingQuestion> items;
        private MatchingQuestion myQuestion;
        public MatchQuestionAdapter(Context context, int textViewResourceId, ArrayList<MatchingQuestion> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myQuestion = items.get(position);


            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(android.R.layout.simple_list_item_1, null);
            }

            questionText = (TextView) convertView.findViewById(android.R.id.text1);

            if (myQuestion != null)
            {
                if(questionText != null)
                {
                    questionText.setText(myQuestion.getQuestion());
                }
                if(myQuestion.getIsSelected())
                {
                    if(myQuestion.getColor() != null)
                    {
                        convertView.setBackgroundColor(Color.parseColor(myQuestion.getColor()));
                        /*if(((ColorDrawable)convertView.getBackground()).getColor() != Color.parseColor(curSelectedColor))
                        {
                            convertView.getBackground().setAlpha(128);
                        }*/
                    }
                } else
                {
                    convertView.setBackgroundDrawable(null);
                }
            }
            return convertView;
        }
    }


    private class MatchAnswerAdapter extends ArrayAdapter<MatchingAnswer>{

        private TextView answerText;
        private ArrayList<MatchingAnswer> items;
        private MatchingAnswer myAnswer;
        public MatchAnswerAdapter(Context context, int textViewResourceId, ArrayList<MatchingAnswer> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myAnswer = items.get(position);


            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(android.R.layout.simple_list_item_1, null);
            }

            answerText = (TextView) convertView.findViewById(android.R.id.text1);

            if (myAnswer != null)
            {
                if(answerText != null)
                {
                    answerText.setText(myAnswer.getAnswer());
                }
                if(myAnswer.getQuestionID() != -1)
                {
                    if(m_questions.get(myAnswer.getQuestionID()).getColor() != null)
                    {
                        convertView.setBackgroundColor(Color.parseColor(m_questions.get(myAnswer.getQuestionID()).getColor()));
                    }
                } else
                {
                    convertView.setBackgroundDrawable(null);
                }
            }
            return convertView;
        }
    }

    private int findColor(String color) {
        for(MatchingQuestion myQuestion: m_questions)
        {
            if(myQuestion.getColor() == color)
            {
                return myQuestion.getId();
            }
        }
        return -1;
    }

    private class MatchingAnswer {

        private String answer;
        private int questionID;

        MatchingAnswer(String answer, int questionID)
        {
            this.answer = answer;
            this.questionID = questionID;
        }

        public String getAnswer() {
            return answer;
        }

        public int getQuestionID() {
            return questionID;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setQuestionID(int questionID) {
            this.questionID = questionID;
        }

        @Override public String toString() {
            return answer;
        }
    }

    private class MatchingQuestion {

        private String question;
        private String color;
        private boolean isSelected;
        private int answerId;
        private int id;

        public MatchingQuestion(String question, String color, boolean isSelected, int id, int answerId)
        {
            this.question = question;
            this.color = color;
            this.isSelected = isSelected;
            this.id = id;
            this.answerId = answerId;
        }

        public String getColor() {
            return color;
        }

        public String getQuestion() {
            return question;
        }

        public boolean getIsSelected() {
            return isSelected;
        }

        public int getAnswerId() {
            return answerId;
        }

        public int getId() {
            return id;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setAnswerId(int answerId) {
            this.answerId = answerId;
        }

        @Override public String toString() {
            return question;
        }
    }

    private void parseAnswers(String answerText) {
        String[] strArray;
        Integer[] intas;
        String delimiter = ";";
        strArray = answerText.split(delimiter);
        for(int i = 0; i <  strArray.length; i++)
        {
            if(!strArray[i].equals("*") || !strArray[i].isEmpty())
            {
                for(int j = 0; j < m_answers.size(); j++)
                {
                    if(m_answers.get(j).getAnswer().equals(strArray[i]))
                    {
                        if(!m_questions.get(i).getIsSelected())
                        {
                            m_questions.get(i).setSelected(true);
                            m_questions.get(i).setAnswerId(j);
                            m_answers.get(j).setQuestionID(i);
                            break;
                        }
                    }
                }
            }
        }
        listAdapter1.notifyDataSetChanged();
        listAdapter2.notifyDataSetChanged();
    }


    private void setMyQuestion() {
        JSONArray left = new JSONArray();
        JSONArray right = new JSONArray();
        List<String> rightSide = new ArrayList<String>();

        try {
            left = question.getData().getJSONArray("leftSide");
            right = question.getData().getJSONArray("rightSide");
            if(left != null) {
                for(int i = 0; i < left.length(); i++)
                {
                    listAdapter1.add(new MatchingQuestion((String)left.get(i) ,myColors.get(i),false,i,-1));
                }
            }
            if(right != null)
            {
                for(int i = 0; i < right.length(); i++)
                {
                    rightSide.add((String)right.get(i));
                }
                Collections.shuffle(rightSide, new Random(32));
                for(int i = 0; i < rightSide.size(); i++)
                {
                    listAdapter2.add(new MatchingAnswer(rightSide.get(i),-1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setQuestion(int index, Question question) {
        this.question = question;
        setMyQuestion();
        //String test = "";
        //test += "everyone else;doony;";
        //parseAnswers(test);
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public Answer getAnswer() {
        //mine
        String answerText = "";
        for(MatchingQuestion question: m_questions)
        {
            if(question.getAnswerId() != -1)
            {
                answerText += m_answers.get(question.getAnswerId()).getAnswer() + ";";
            } else {
                answerText += "*;";
            }
        }
       Answer answer = new Answer(question.getObjectId(), answerText);
       return answer;

//        String answerText = blank1EditText.getText().toString() + ";" + blank2EditText.getText().toString() + ";" + blank3EditText.getText().toString() + ";";
//        Answer answer = new Answer(question.getObjectId(), answerText);
//        return answer;
    }

	@Override
	public void setAnswer(String answerText) {
        if(answerText != null)
        {
            parseAnswers(answerText);
        }
	}

	@Override
	public boolean isQuestionComplete() {
        for(MatchingQuestion question: m_questions)
        {
            if(question.getAnswerId() == -1)
            {
                return false;
            }
        }
		return true;
	}
}