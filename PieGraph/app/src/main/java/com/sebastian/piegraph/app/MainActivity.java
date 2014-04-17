package com.sebastian.piegraph.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;


public class MainActivity extends Activity {

    //Put in constructor---------
    float correctCount = 13;
    float incorrectCount = 5;
    float noAnswerCount = 3;
    //---------------------------

    float totalCount;

    TextView correctTextView;
    TextView incorrectTextView;
    TextView noAnswerTextView;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
