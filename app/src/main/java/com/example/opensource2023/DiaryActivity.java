package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
    private String date;
    private Date calendar;

    private int id = -1;
    private GridAdapter gridAdapter;
    DBHelper helper;
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

        helper = new DBHelper(this);

        initYearAndMonthText();
        bindGrid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                final DiaryBox item = (DiaryBox) gridAdapter.getItem(a_position);
                date = item.getDay();
                id = item.getId();
                nextActivity();
            }
        });
    }

    private void bindGrid() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<DiaryBox> itemList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select id, day from diarylist where year = \'" + year + "\' and month= \'"+ month + "\' order by day", null);
        while(cursor.moveToNext()) {
            itemList.add(new DiaryBox(cursor.getInt(0), month, cursor.getString(1)));
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
        int m = Integer.parseInt(month);
        month = Integer.toString(m);
        date = dayFormat.format(calendar);
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
        intent.putExtra("day", date);
        intent.putExtra("id", Integer.toString(id));
        id = -1;

        Log.v("day", date);

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
            DialogFragment dialogFragment = new DatePickerFragment();
            dialogFragment.show(getSupportFragmentManager(), "datePicker");
            Log.v("정보", year + " " + month + " " + date);
            initYearAndMonthText();
            nextActivity();
        }
    }
}