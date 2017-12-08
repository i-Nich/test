package ssau.lab_3;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ssau.lab_3.Adapters.CategoryAdapter;
import ssau.lab_3.DBModel.Category;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Record;


public class CategoryTimesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lvCategories;
    TextView tvTime;
    DBHelper dbHelper;
    Category[] categories;
    ArrayList<Category> selectedCategories = new ArrayList<>();
    TextView tvStart, tvEnd;
    Date start;
    Date end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_times);
        setTitle("Категории");
        initDates();
        dbHelper = new DBHelper(this);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvStart.setOnClickListener(this);
        tvEnd = (TextView) findViewById(R.id.tvEnd);
        tvEnd.setOnClickListener(this);
        lvCategories = (ListView) findViewById(R.id.lvCategories);
        lvCategories.setOnItemClickListener(this);
        tvTime = (TextView) findViewById(R.id.tvTime);
        categories = Category.getCategories(dbHelper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillCats();
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
        CalcTime();
    }

    private void fillCats() {
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        lvCategories.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) view.getTag();
        if (category != null) {
            int index = selectedCategories.indexOf(category);
            if (index == -1) {
                selectedCategories.add(category);
                view.setBackground(new ColorDrawable(Color.argb(255, 200, 200, 200)));
            } else {
                selectedCategories.remove(category);
                view.setBackground(new ColorDrawable(Color.argb(0, 225, 225, 225)));
            }
            view.invalidate();
            CalcTime();
        }
    }

    private void CalcTime() {
        long time = Record.getSumInterval(dbHelper, selectedCategories.toArray(new Category[0]), start.getTime(), end.getTime());
        long hh = time / 3600000;
        long mm = (time - hh * 3600000) / 60000;
        tvTime.setText((selectedCategories.size() == 0 ? "Всего: " : "") + hh + " часов " + mm + " минут");
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