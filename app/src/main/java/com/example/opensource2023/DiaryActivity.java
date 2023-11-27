package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView yearView;
    private TextView monthView;
    private ImageButton previousMonthBtn;
    private ImageButton nextMonthBtn;
    private ImageButton plusBtn;
    private GridView gridView;
    private String year;
    private String month;
    private String day;
    private Date calendar;
    private GridAdapter gridAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        yearView = (TextView) findViewById(R.id.year);
        monthView = (TextView) findViewById(R.id.month);
        previousMonthBtn = (ImageButton) findViewById(R.id.arrow_left);
        nextMonthBtn = (ImageButton) findViewById(R.id.arrow_right);
        plusBtn = (ImageButton) findViewById(R.id.plus);

        previousMonthBtn.setOnClickListener(this);
        nextMonthBtn.setOnClickListener(this);
        plusBtn.setOnClickListener(this);

        initYearAndMonthText();
        bindGrid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                final DiaryBox item = (DiaryBox) gridAdapter.getItem(a_position);
                day = item.getDay();
                nextActivity();
            }
        });
    }

    private void bindGrid() {
        int count = 0;
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        List<DiaryBox> itemList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select day from diarylist where month= \'"+ month + "\' and year = \'" + year + "\' order by day", null);
        while(cursor.moveToNext()) {
            itemList.add(new DiaryBox(month, cursor.getString(0)));
            count++;
        }
        cursor.close();
        db.close();

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(this, itemList);
        gridView.setAdapter(gridAdapter);
    }

    private void initYearAndMonthText() {
        calendar = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        year = yearFormat.format(calendar);
        month = monthFormat.format(calendar);
        day = dayFormat.format(calendar);
        yearView.setText(year);
        monthView.setText(getMonthName(Integer.parseInt(month)));
    }

    private String getMonthName(int month) {
        String[] monthNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }

    private void nextActivity() {
        Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);

        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);

        Log.v("day", day);

        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == previousMonthBtn) {
            int m = Integer.parseInt(month) - 1;
            int y = Integer.parseInt(year);
            if(m < 1) {
                m = 12;
                y -= 1;
                year = Integer.toString(y);
                yearView.setText(year);
            }
            month = Integer.toString(m);
            monthView.setText(getMonthName(m));
            bindGrid();
        }

        if (view == nextMonthBtn) {
            int m = Integer.parseInt(month) + 1;
            int y = Integer.parseInt(year);
            if(m > 12) {
                m = 1;
                y += 1;
                year = Integer.toString(y);
                yearView.setText(year);
            }
            month = Integer.toString(m);
            monthView.setText(getMonthName(m));
            bindGrid();
        }

        if (view == plusBtn) {
            nextActivity();
        }
    }
}