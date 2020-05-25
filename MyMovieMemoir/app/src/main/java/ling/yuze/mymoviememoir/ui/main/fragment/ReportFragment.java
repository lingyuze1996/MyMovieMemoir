package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.network.RestService;

public class ReportFragment extends Fragment implements View.OnClickListener {
    private int id;
    private BarChart barChart;
    private CalendarView calendarStart;
    private CalendarView calendarEnd;
    private String test;
    private String startDate;
    private String endDate;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        barChart = v.findViewById(R.id.barChart);
        initializeBarChart();

        pieChart = v.findViewById(R.id.pieChart);

        Spinner spinner = v.findViewById(R.id.spinner_year);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                int year = Integer.parseInt(selected);
                new TaskGetMoviesPerMonth().execute(year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageView dateStart = v.findViewById(R.id.imageStartDate);
        ImageView dateEnd = v.findViewById(R.id.imageEndDate);
        dateStart.setOnClickListener(this);
        dateEnd.setOnClickListener(this);
        calendarStart = v.findViewById(R.id.calendarStart);
        calendarEnd = v.findViewById(R.id.calendarEnd);
        calendarStart.setVisibility(View.GONE);
        calendarEnd.setVisibility(View.GONE);
        calendarStart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                if (endDate != null)
                    new TaskGetMoviesPerSuburb().execute(startDate, endDate);
            }
        });
        calendarEnd.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                endDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                if (startDate != null)
                    new TaskGetMoviesPerSuburb().execute(startDate, endDate);
            }
        });

        SharedPreferences shared = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
        id = shared.getInt("id", 0);

        return v;
    }

    private void initializeBarChart() {
        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);

        XAxis x = barChart.getXAxis();
        x.setLabelCount(12);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y = barChart.getAxisLeft();
        y.setAxisMaximum(15f);
        y.setAxisMinimum(0f);
    }

    private void initializePieChart(List<PieEntry> dataList) {
        PieDataSet dataSet = new PieDataSet(dataList, "Suburb Postcode");
        dataSet.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorGreen, null));
        colors.add(getResources().getColor(R.color.colorYellow, null));
        colors.add(getResources().getColor(R.color.colorBlue, null));
        colors.add(getResources().getColor(R.color.colorLight, null));
        colors.add(getResources().getColor(R.color.colorOrange, null));
        colors.add(getResources().getColor(R.color.colorPrimaryDark, null));
        colors.add(getResources().getColor(R.color.colorPink, null));

        dataSet.setColors(colors);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.8f);
        dataSet.setValueLineColor(getResources().getColor(R.color.colorPrimaryDark, null));
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(20f);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);
        pieChart.postInvalidate();
    }

    private void initializeBarChart(List<BarEntry> dataList) {
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(12);
        xAxis.setTextSize(10f);
        final String[] labelName =
                {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
        xAxis.setYOffset(10);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(6f);
        yAxis.setAxisMinimum(0f);
        yAxis.setTextSize(10f);
        yAxis.setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);

        BarDataSet dataSet = new BarDataSet(dataList, "Month");
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "";
            }
        });
        dataSet.setColor(getResources().getColor(R.color.colorBlue, null));

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.4f);
        barChart.setData(barData);
        barChart.setExtraOffsets(20f, 0f, 20f, 20f);
        barChart.postInvalidate();
    }

    private List<PieEntry> getPieChartData(HashMap<String, Integer> map) {
        List<PieEntry> pieData = new ArrayList<>();
        float total = 0;
        for (String key: map.keySet()) {
            total += map.get(key);
        }
        for (String key : map.keySet()) {
            PieEntry pieEntry = new PieEntry(map.get(key) / total, key);
            pieData.add(pieEntry);
        }
        return pieData;
    }

    private List<BarEntry> getBarChartData(int[] ints) {
        List<BarEntry> barData = new ArrayList<>();
        for (int i = 0; i < ints.length; i ++) {
            BarEntry barEntry = new BarEntry(i, ints[i]);
            if (ints[i] == 0)
                barEntry = new BarEntry(i, 0.1f);
            barData.add(barEntry);
        }
        return barData;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageStartDate:
                if (calendarStart.getVisibility() == View.GONE)
                    calendarStart.setVisibility(View.VISIBLE);
                else
                    calendarStart.setVisibility(View.GONE);
                break;
            case R.id.imageEndDate:
                if (calendarEnd.getVisibility() == View.GONE)
                    calendarEnd.setVisibility(View.VISIBLE);
                else
                    calendarEnd.setVisibility(View.GONE);
                break;
        }
    }

    private class TaskGetMoviesPerSuburb extends AsyncTask<String, Void, HashMap<String, Integer>> {
        @Override
        protected HashMap<String, Integer> doInBackground(String... params) {
            RestService rs = new RestService();
            HashMap<String, Integer> map = rs.getMoviesPerSuburb(id, params[0], params[1]);
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> map) {
            if (map.isEmpty())
                return;
            else
                initializePieChart(getPieChartData(map));
        }
    }

    private class TaskGetMoviesPerMonth extends AsyncTask<Integer, Void, int[]> {
        @Override
        protected int[] doInBackground(Integer... integers) {
            RestService rs = new RestService();
            int[] numbers = rs.getMoviesPerMonth(id, integers[0]);
            return numbers;
        }

        @Override
        protected void onPostExecute(int[] ints) {
            initializeBarChart(getBarChartData(ints));
        }
    }
}
