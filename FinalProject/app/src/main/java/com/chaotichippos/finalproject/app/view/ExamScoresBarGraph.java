package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SebastianMartinez on 4/18/14.
 */
public class ExamScoresBarGraph extends RelativeLayout {
    int[] countArray = new int[21];
    List<String> setNames = new ArrayList<String>();
    ListView listView;
    MyAdapter adapter;
    ArrayList<String> myColors = new ArrayList<String>();

    public ExamScoresBarGraph(Context context, List<Float> scores) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bar_graph_base_view, this, true);

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
        myColors.add("#000099"); //dark blue
        myColors.add("#CC3300"); //dark orange
        myColors.add("#66FFFF"); //very light blue
        myColors.add("#663300"); //brown
        myColors.add("#00FF00"); //bright green
        myColors.add("#FF66CC"); //light pink
        myColors.add("#FF6600"); //orange
        myColors.add("#660066"); //magenta
        myColors.add("#FF99CC"); //very light pink
        myColors.add("#66FF66"); //pale green
        myColors.add("#CC3300"); //dark orange
        myColors.add("#00CCFF"); //light blue

        /*****************************/

        /**** ADD SET NAMES TO ARRAY ****/

        setNames.add("0-4");
        setNames.add("5-9");
        setNames.add("10-14");
        setNames.add("15-19");
        setNames.add("20-24");
        setNames.add("25-29");
        setNames.add("30-34");
        setNames.add("35-39");
        setNames.add("40-44");
        setNames.add("45-49");
        setNames.add("50-54");
        setNames.add("55-59");
        setNames.add("60-64");
        setNames.add("65-69");
        setNames.add("70-74");
        setNames.add("75-79");
        setNames.add("80-84");
        setNames.add("85-89");
        setNames.add("90-94");
        setNames.add("95-99");
        setNames.add("100");

        /*****************************/

        // Instantiating an adapter for the list
        adapter = new MyAdapter();

        // Getting a reference to listview of main.xml layout file
        listView = (ListView) findViewById(R.id.listview);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        for(int i = 0; i < scores.size(); i++) {
            countArray[scores.get(i).intValue() / 5]++;
        }

		boolean hasData = false;
        ArrayList<Bar> points = new ArrayList<Bar>();

        for(int i = 0; i < countArray.length; i++) {
            Bar d = new Bar();
            d.setColor(Color.parseColor(myColors.get(i)));
            d.setName(setNames.get(i));
            d.setValue(countArray[i]);
            points.add(d);

			if (countArray[i] > 0) {
				hasData = true;
			}

            double value = (double)countArray[i]/scores.size()*100;
            double finalValue = (double)Math.round(value*100)/100;
            adapter.addString(getContext().getString(R.string.score) + " " + setNames.get(i) + ": " +
					countArray[i] + " " + getContext().getString(R.string.students) + " (" + finalValue + "%)");
        }

        BarGraph g = (BarGraph)findViewById(R.id.BarGraph);
        g.setBars(points);

		g.setVisibility(hasData ? View.VISIBLE : View.INVISIBLE);
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bar_graph_list_item, null);
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
