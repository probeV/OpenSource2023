package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton diaryCheckButton;
    ImageButton diaryClearButton;
    ImageButton gptResponseButton;
    ImageButton youtubeResponseButton;
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

        gptResponseButton.findViewById(R.id.GptButton);

        diaryCheckButton.setOnClickListener((View.OnClickListener) this);
        diaryClearButton.setOnClickListener((View.OnClickListener) this);
        diaryWrite.setOnClickListener((View.OnClickListener) this);

        gptResponseButton.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View view) {
        if (view == gptResponseButton) {
        }

        if (view == youtubeResponseButton) {

        }

    }

    private String getMonthName(int month) {
        String[] monthNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }

}