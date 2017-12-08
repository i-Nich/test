package ssau.lab_3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ssau.lab_3.Adapters.RecordAdapter;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Record;

public class MaxCountRecordsActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lvRecords;
    DBHelper dbHelper;
    Record[] records;
    TextView tvStart, tvEnd;
    Date start;
    Date end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_count_records);
        initDates();
        dbHelper = new DBHelper(this);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvStart.setOnClickListener(this);
        tvEnd = (TextView) findViewById(R.id.tvEnd);
        tvEnd.setOnClickListener(this);
        lvRecords = (ListView)findViewById(R.id.lvRecords);
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

    private void fillTimes(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (start.getTime() >= end.getTime()){
            Toast.makeText(this, "Время окончания должно быть больше начала!", Toast.LENGTH_SHORT).show();
            end.setTime(start.getTime() + 86400000);
        }
        tvStart.setText(df.format(start));
        tvEnd.setText(df.format(end));
        fillRecords();
    }

    private void  setStartTime(){
        DateFormat df = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(df.format(start));
        df = new SimpleDateFormat("MM");
        int month = Integer.parseInt(df.format(start)) - 1;
        df = new SimpleDateFormat("dd");
        int day = Integer.parseInt(df.format(start));

        DatePickerDialog dialog = new DatePickerDialog(
                this                ,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                        try {
                            start = df.parse(dayOfMonth + "." + (month + 1)  + "." + year);
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
                            end = df.parse(dayOfMonth + "." + (month + 1)  + "." + year + " 23:59");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fillTimes();
                    }
                }, year, month, day);
        dialog.show();
    }

    private void fillRecords(){
        records = Record.getMaxCountRecords(dbHelper, start.getTime(), end.getTime());
        RecordAdapter adapter = new RecordAdapter(this, records);
        lvRecords.setAdapter(adapter);
    }
}
