package ssau.lab_3;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ssau.lab_3.DBModel.Category;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Record;

public class CircleDiagramActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout diagram;
    DBHelper dbHelper;
    PieChart pieChart;
    TextView tvStart, tvEnd;
    Date start;
    Date end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_diagram);
        setTitle("Круговая диаграмма");
        initDates();
        dbHelper = new DBHelper(this);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvStart.setOnClickListener(this);
        tvEnd = (TextView) findViewById(R.id.tvEnd);
        tvEnd.setOnClickListener(this);
        diagram = (RelativeLayout) findViewById(R.id.activity_circle_diagram);
        pieChart = new PieChart(this);
        diagram.addView(pieChart, 0);
        diagram.setBackgroundColor(Color.LTGRAY);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Круговая диаграмма времени по всем категориям");
        pieChart.setDrawHoleEnabled(true);
        //
        pieChart.setHoleRadius(7);
        pieChart.setTransparentCircleRadius(10);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillTimes();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvStart) {
            setStartTime();
        } else if (v.getId() == R.id.tvEnd) {
            setEndTime();
        }
    }

    private void initDates(){
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(df.format(today));
        df = new SimpleDateFormat("MM");
        int month = Integer.parseInt(df.format(today));
        df = new SimpleDateFormat("dd");
        int day = Integer.parseInt(df.format(today));
        df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            end = df.parse(day + "." + month + "." + year + " 23:59");
            df = new SimpleDateFormat("dd.MM.yyyy");
            start = df.parse(day + "." + month + "." + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        start = new Date(start.getTime() - 2592000000L);
    }

    private void fillTimes(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (start.getTime() >= end.getTime()){
            Toast.makeText(this, "Время окончания должно быть больше начала!", Toast.LENGTH_SHORT).show();
            end.setTime(start.getTime() + 86400000);
        }
        tvStart.setText(df.format(start));
        tvEnd.setText(df.format(end));
        fillData();
    }

    private void fillData() {
        ArrayList<String> cNames = new ArrayList<>();
        ArrayList<Entry> cTimes = new ArrayList<>();
        Category[] categories = Category.getCategories(dbHelper);
        for (Category category : categories) {
            long interval = Record.getSumInterval(dbHelper, category, start.getTime(), end.getTime());
            cNames.add(category.getName());
            cTimes.add(new Entry(interval, cNames.size() - 1));
        }

        PieDataSet dataSet = new PieDataSet(cTimes, "Круговая диаграмма времени по всем категориям");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.PASTEL_COLORS) {
            colors.add(color);
        }

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(cNames, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);
        pieChart.invalidate();

//        records = Record.getMaxSumInterval(dbHelper, start.getTime(), end.getTime());
//        RecordAdapter adapter = new RecordAdapter(this, records);
//        lvRecords.setAdapter(adapter);
    }

    private void  setStartTime(){
        DateFormat df = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(df.format(start));
        df = new SimpleDateFormat("MM");
        int month = Integer.parseInt(df.format(start)) - 1;
        df = new SimpleDateFormat("dd");
        int day = Integer.parseInt(df.format(start));

        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                        try {
                            start = df.parse(dayOfMonth + "." + month + "." + year);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fillTimes();
                    }
                }, year, month, day);
        dialog.show();
    }

    private void setEndTime(){
        DateFormat df = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(df.format(end));
        df = new SimpleDateFormat("MM");
        int month = Integer.parseInt(df.format(end)) - 1;
        df = new SimpleDateFormat("dd");
        int day = Integer.parseInt(df.format(end));

        DatePickerDialog dialog = new DatePickerDialog(
                this                ,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        try {
                            end = df.parse(dayOfMonth + "." + (month + 1) + "." + year + " 23:59");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fillTimes();
                    }
                }, year, month, day);
        dialog.show();
    }
}
