package com.sebastian.piechart2.app;

import android.graphics.Color;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    //Put in Constructor---------
    int correctMax = 6;
    List<Float> answers = new ArrayList<Float>();
    //---------------------------

    MyAdapter adapter;
    ListView listView;
    int unAnswered = 0;

    int[] countArray = new int[correctMax + 1];
    private ArrayList<String> myColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answers.add(6f);
        answers.add(6f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(5f);
        answers.add(4f);
        answers.add(4f);
        answers.add(3f);
        answers.add(3f);
        answers.add(3f);
        answers.add(3f);
        answers.add(2f);
        answers.add(1f);
        answers.add(1f);
        answers.add(0f);
        answers.add(-1f);
        answers.add(-1f);

        myColors = new ArrayList<String>();

        /**** ADD COLORS TO ARRAY ****/
        myColors.add("#FF0000"); //red
        myColors.add("#000099"); //dark blue
        myColors.add("#660066"); //magenta
        myColors.add("#FF66CC"); //dark green
        myColors.add("#0066FF"); //gray pale blue
        myColors.add("#CC9900"); //gold
        myColors.add("#669999"); // gray blue
        myColors.add("#009933"); //lighter green
        myColors.add("#CC3300"); //dark orange
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
        myColors.add("#000099"); //dark blue
        myColors.add("#CC3300"); //dark orange
        myColors.add("#00CCFF"); //light blue
        /*****************************/

        // Instantiating an adapter for the list
        adapter = new MyAdapter();

        // Getting a reference to listview of main.xml layout file
        listView = ( ListView ) findViewById(R.id.listview);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        PieGraph pg = (PieGraph)findViewById(R.id.PieGraph);
        pg.setThickness(80);

        for(int i = 0; i < answers.size(); i ++) {
            if (answers.get(i) == -1) {
                unAnswered++;
            } else {
                countArray[answers.get(i).intValue()]++;

            }
        }

        for(int i = 0; i < countArray.length; i ++) {
            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor(myColors.get(i)));
            slice.setValue(countArray[i]);
            pg.addSlice(slice);

            double value = (double)countArray[i]/answers.size()*100;
            double finalValue = (double)Math.round(value*100)/100;
            adapter.addString("Score " + i + "/" + correctMax + ": " + countArray[i] + " students (" + finalValue + "%)");
        }

        PieSlice firstSlice = new PieSlice();
        firstSlice.setColor(Color.parseColor(myColors.get(countArray.length)));
        firstSlice.setValue(unAnswered);
        pg.addSlice(firstSlice);
        double value = (double)unAnswered/answers.size()*100;
        double finalValue = (double)Math.round(value*100)/100;
        adapter.addString("No Answer: " +  unAnswered + " students (" + finalValue + "%)");
    }

    private class MyAdapter extends BaseAdapter {

        private List<String> mList;

        public MyAdapter() {
            mList = new ArrayList<String>();
        }

        public void addString(String stringData) {
            mList.add(stringData);
            notifyDataSetChanged();
        }

        public void setList(List<String> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public List<String> getList() {
            return mList;
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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, null);
            }

            // TextViews
            String stringData = mList.get(position);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(stringData);
            text.setTextColor(Color.parseColor(myColors.get(position)));

            return convertView;
        }
    }

}
