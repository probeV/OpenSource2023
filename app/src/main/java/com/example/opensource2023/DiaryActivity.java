package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

public class DiaryActivity extends AppCompatActivity {

    ImageButton ib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        ib.findViewById(R.id.arrow_left);
    }

    public void onClick(View view) {
    }
}