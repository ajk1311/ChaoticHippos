package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

/**
 * Created by SebastianMartinez on 4/18/14.
 */
public class TrueFalseMultiChoicePierGraph extends RelativeLayout{
    float totalCount;

    TextView correctTextView;
    TextView incorrectTextView;
    TextView noAnswerTextView;
    TextView titleTextView;

    public TrueFalseMultiChoicePierGraph(Context context, float correctCount, float incorrectCount, float noAnswerCount) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.simple_pie_graph, this, true);

        titleTextView = (TextView) findViewById(R.id.Title);

        totalCount = correctCount + incorrectCount + noAnswerCount;

        PieGraph pg = (PieGraph)findViewById(R.id.PieGraph);
        pg.setThickness(80);
        PieSlice slice = new PieSlice();

        correctTextView = (TextView) findViewById(R.id.Correct);
        correctTextView.setTextColor(Color.parseColor("#007d03"));
        double value = (double)(correctCount/totalCount)*100;
        double finalValue = (double)Math.round(value*100)/100;
        correctTextView.setText("Correct: " + correctCount + " students (" + finalValue + "%)");

        incorrectTextView = (TextView) findViewById(R.id.Incorrect);
        incorrectTextView.setTextColor(Color.parseColor("#ff0000"));
        value = (double)(incorrectCount/totalCount)*100;
        finalValue = (double)Math.round(value*100)/100;
        incorrectTextView.setText("Incorrect: " + incorrectCount + " students (" + finalValue + "%)");

        noAnswerTextView = (TextView) findViewById(R.id.NoAnswer);
        noAnswerTextView.setTextColor(Color.parseColor("#ffb71b"));
        value = (double)(noAnswerCount/totalCount)*100;
        finalValue = (double)Math.round(value*100)/100;
        noAnswerTextView.setText("No Answer: " + noAnswerCount + " students (" + finalValue + "%)");

        slice.setColor(Color.parseColor("#007d03"));
        slice.setValue(correctCount);
        pg.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(Color.parseColor("#ff0000"));
        slice.setValue(incorrectCount);

        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#ffb71b"));
        slice.setValue(noAnswerCount);
        pg.addSlice(slice);
    }
}
