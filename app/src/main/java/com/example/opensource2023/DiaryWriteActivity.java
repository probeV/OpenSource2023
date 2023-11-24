package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener{

    //ViewManager 객체 변수
    private TitleViewManager gptTitleViewManager;
    private TitleViewManager youtubeTitleViewManager;

    //상단 버튼 변수
    ImageButton diaryCheckButton;
    ImageButton diaryClearButton;

    //GTP, Youtube 응답 관련 변수
    ImageButton gptResponseButton;
    ImageView gptTitleImage;
    TextView gptTitleText;
    ImageButton youtubeResponseButton;
    ImageView youtubeTitleImage;
    TextView youtubeTitleText;

    TextView monthView;
    EditText diaryWrite;
    String month;

    @SuppressLint("MissingInflatedId")
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

        diaryCheckButton.setOnClickListener(this);
        diaryClearButton.setOnClickListener(this);
        diaryWrite.setOnClickListener(this);

        ButtonInit();

    }

    public void ButtonInit(){
        gptResponseButton = (ImageButton) findViewById(R.id.GptResponseButton);
        gptTitleImage = (ImageView) findViewById(R.id.GptTitleImage);
        gptTitleText = (TextView) findViewById(R.id.GptTitleText);

        youtubeResponseButton = (ImageButton) findViewById(R.id.YoutubeResponseButton);
        youtubeTitleImage = (ImageView) findViewById(R.id.YoutubeTitleImage);
        youtubeTitleText = (TextView) findViewById(R.id.YoutubeTitleText);

        gptResponseButton.setOnClickListener(this);
        youtubeResponseButton.setOnClickListener(this);

        //숨기기
        gptTitleText.setVisibility(View.INVISIBLE);
        gptTitleImage.setVisibility(View.INVISIBLE);
        youtubeTitleText.setVisibility(View.INVISIBLE);
        youtubeTitleImage.setVisibility(View.INVISIBLE);

        //ViewManager 객체 생성
        gptTitleViewManager = new TitleViewManager(gptTitleText, gptTitleImage);
        youtubeTitleViewManager = new TitleViewManager(youtubeTitleText, youtubeTitleImage);
    }

    @Override
    public void onClick(View view) {
        if (view == gptResponseButton) {
            gptTitleViewManager.toggleVisibility();
        }
        if (view == youtubeResponseButton) {
            youtubeTitleViewManager.toggleVisibility();
        }
    }

    private String getMonthName(int month) {
        String[] monthNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }

}