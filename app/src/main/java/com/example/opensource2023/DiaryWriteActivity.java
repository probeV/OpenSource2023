package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

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


    EditText diaryWrite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryCheckButton.findViewById(R.id.DiaryCheckButton);
        diaryClearButton.findViewById(R.id.DiaryClearButton);
        diaryWrite.findViewById(R.id.DiaryWrite);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.
                }
            }
        };

        diaryCheckButton.setOnClickListener(listener);
        diaryClearButton.setOnClickListener(listener);
        diaryWrite.setOnClickListener(listener);
    }

    
    private void saveDiary(String readDay) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE
            String content = edtDiary.getText().toString();

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos.write(content.getBytes());
            //fos.flush();      fos.close();
            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(getApplicationContext(), "일기 저장됨", Toast.LENGTH_SHORT).show();
        } catch (Exception e) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "오류오류", Toast.LENGTH_SHORT).show();
        }
    }

}