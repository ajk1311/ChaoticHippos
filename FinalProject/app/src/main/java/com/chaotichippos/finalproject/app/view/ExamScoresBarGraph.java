package com.chaotichippos.finalproject.app.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaotichippos.finalproject.app.R;
import com.chaotichippos.finalproject.app.util.ScreenUtil;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SebastianMartinez on 4/18/14.
 */
public class ExamScoresBarGraph extends RelativeLayout {

	private static final int BAR_WIDTH = ScreenUtil.getDimensionPixelSize(50);

    int[] countArray = new int[21];
    List<String> setNames = new ArrayList<String>();
    ArrayList<String> myColors = new ArrayList<String>();

    public ExamScoresBarGraph(Context context, float[] scores) {
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

        for(int i = 0, sz = scores.length; i < sz; i++) {
            countArray[(int) scores[i] / 5]++;
        }

        ArrayList<Bar> bars = new ArrayList<Bar>();

        for(int i = 0, sz = countArray.length; i < sz; i++) {
			if (countArray[i] == 0) {
				continue;
			}
            Bar bar = new Bar();
            bar.setColor(Color.parseColor(myColors.get(i)));
            bar.setName(setNames.get(i));
            bar.setValue(countArray[i]);
            bars.add(bar);
        }

		BarGraph graph = (BarGraph)findViewById(R.id.BarGraph);
		if (bars.size() > 0) {
			final int barHeight = context.getResources()
					.getDimensionPixelSize(R.dimen.bar_graph_height);
			final FrameLayout.LayoutParams params =
					new FrameLayout.LayoutParams(bars.size() * BAR_WIDTH, barHeight);
			graph.setLayoutParams(params);
			graph.setBars(bars);
		} else {
			graph.setVisibility(View.GONE);
		}

		// Statistics
		final double[] data = new double[scores.length];
		for (int i = 0, sz = scores.length; i < sz; i++) {
			data[i] = (double) scores[i];
		}
		DescriptiveStatistics stats = new DescriptiveStatistics(data);
		((TextView) findViewById(R.id.bar_graph_mean_value))
				.setText(String.valueOf(round(stats.getMean())));
		((TextView) findViewById(R.id.bar_graph_median_value))
				.setText(String.valueOf(round(stats.apply(new Median()))));
		((TextView) findViewById(R.id.bar_graph_stddev_value))
				.setText(String.valueOf(round(stats.getStandardDeviation())));
    }

	private float round(double toRound) {
		return (float) Math.round(toRound * 1000) / 1000;
	}
}
