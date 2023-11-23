package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton diaryCheckButton;
    ImageButton diaryClearButton;
    ImageButton gptResponseButton;
    ImageButton youtubeResponseButton;

    EditText diaryWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        diaryCheckButton.findViewById(R.id.DiaryCheckButton);
        diaryClearButton.findViewById(R.id.DiaryClearButton);
        diaryWrite.findViewById(R.id.DiaryWrite);

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

}