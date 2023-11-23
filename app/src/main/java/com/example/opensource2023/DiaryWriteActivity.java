package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class DiaryWriteActivity extends AppCompatActivity {

    ImageButton diaryCheckButton;
    ImageButton diaryClearButton;
    TextView monthView;


    EditText diaryWrite;
    String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

//        diaryCheckButton.findViewById(R.id.DiaryCheckButton);
//        diaryClearButton.findViewById(R.id.DiaryClearButton);
//        diaryWrite.findViewById(R.id.DiaryWrite);
        monthView = (TextView) findViewById(R.id.Month);
        Intent intent = getIntent(); /*데이터 수신*/

        month = intent.getExtras().getString("month");
        int m = Integer.parseInt(month);
        monthView.setText(getMonthName(m));


    }

    private String getMonthName(int month) {
        String[] monthNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }

}