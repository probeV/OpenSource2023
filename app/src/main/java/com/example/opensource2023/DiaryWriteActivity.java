package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.opensource2023.sticker.BitmapStickerIcon;
import com.example.opensource2023.sticker.StickerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener{

    //Youtube 객체 변수
    private static final String YOUTUBE_API_KEY = "AIzaSyDi0HrZJq5Jj-EYHlry84gjXU0lveG1xbE";
    private static final String [] YOUTUBE_VIDEO_ID = {"NU6XxRcx5LA", "WayAzmTDzUU", "xU6FRGi4lHw", "xlyrt5eAtKI", "pF5ElVjLFuo", "b_6EfFZyBxY"};
    private YouTubePlayerView youTubePlayerView;


    //ViewManager 객체 변수
    private ViewManager gptViewManager;
    private ViewManager youtubeViewManager;
    private String stringOutput = "";

    // GPT 관련 변수
    private String stringURLEndPoint = "https://api.openai.com/v1/chat/completions";
    private String APIKey = "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";

    //상단 버튼 변수
    ImageButton diaryCheckButton;
    ImageButton diaryClearButton;

    //GTP, Youtube 응답 관련 변수
    ImageButton gptResponseButton;
    ImageView gptTitleImage;
    TextView gptTitleText;
    TextView gptResponseText;
    ImageButton youtubeResponseButton;
    ImageView youtubeTitleImage;
    TextView youtubeTitleText;

    TextView yearView;
    TextView monthView;
    TextView dateView;
    TextView dayView;

    EditText diaryWrite;
    String year;
    String month;
    String date;
    String day;
    String diaryContents;
    String gptContents;
    int id;
    int randomValue = -1;
    boolean checkYoutube = false;

    //스티커 뷰
    StickerView stickerView;
    ImageButton stickerViewButton;
    GridView horizontalGridView;

    String [] stickerViewImage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        //스티커 뷰
//        stickerView = (StickerView)findViewById(R.id.StickerView);
//
//        BitmapStickerIcon deleteIcon= new BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.sticker_ic_close_white_18dp),BitmapStickerIcon.LEFT_TOP);
//        BitmapStickerIcon flipIcon  = new BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.sticker_ic_flip_white_18dp),BitmapStickerIcon.RIGHT_BOTOM);
//        BitmapStickerIcon scaleIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.sticker_ic_scale_white_18dp),BitmapStickerIcon.LEFT_BOTTOM);

        stickerViewButton = (ImageButton)findViewById(R.id.StickerViewButton);

        //스티커 선택 이미지 그리드 뷰
        horizontalGridView = (GridView)findViewById(R.id.HorizontalGridView);




        yearView = (TextView) findViewById(R.id.Year);
        monthView = (TextView) findViewById(R.id.Month);
        dateView = (TextView) findViewById(R.id.Date);
        dayView = (TextView) findViewById(R.id.Day);

        Intent intent = getIntent(); /*데이터 수신*/

        year = intent.getExtras().getString("year");
        month = intent.getExtras().getString("month");
        date = intent.getExtras().getString("day");
        id = Integer.parseInt(intent.getExtras().getString("id"));
        int m = Integer.parseInt(month);
        yearView.setText(year);
        monthView.setText(getMonthName(m));

        dateView.setText(date);

        // 요일 구하여 출력
        day = year + "-" + month + "-" + date;
        try {
            // 문자열을 Date 객체로 변환
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(day);

            // 요일을 출력하기 위해 다시 문자열로 변환
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
            String dayOfWeek = dayFormat.format(date);
            dayView.setText(dayOfWeek);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ButtonInit();
        InitVar();

        diaryCheckButton.setOnClickListener(this);
        diaryClearButton.setOnClickListener(this);
        diaryWrite.setOnClickListener(this);

        if(id != -1) {
            gptResponseButton.setVisibility(View.INVISIBLE);
            gptTitleImage.setVisibility(View.VISIBLE);
            gptTitleText.setVisibility(View.VISIBLE);
            gptResponseText.setVisibility(View.VISIBLE);
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor cursor = db.rawQuery("select text, gpt, song from diarylist where id= \'"+ id + "\'", null);
            cursor.moveToNext();
            diaryContents = cursor.getString(0);
            gptContents = cursor.getString(1);
            randomValue = Integer.parseInt(cursor.getString(2));
            Log.v("rv", cursor.getString(2));
            if(!gptContents.equals("")) {
                gptResponseButton.setVisibility(View.INVISIBLE);
                gptTitleImage.setVisibility(View.VISIBLE);
                gptTitleText.setVisibility(View.VISIBLE);
                gptResponseText.setVisibility(View.VISIBLE);
            }
            if(randomValue != -1) {
                youtubeResponseButton.setVisibility(View.INVISIBLE);
                youtubeTitleImage.setVisibility(View.VISIBLE);
                youtubeTitleText.setVisibility(View.VISIBLE);
                youTubePlayerView.setVisibility(View.VISIBLE);
            }
            cursor.close();
            db.close();
            diaryWrite.setText(diaryContents);

            gptResponseText.setText(stringOutput);
        }

        getLifecycle().addObserver(youTubePlayerView);

        if(randomValue == -1) {
          double random=Math.random();
            randomValue = (int)(Math.round(random*(YOUTUBE_VIDEO_ID.length-1)));
        }


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID[randomValue], 0);
            }
        });
    }


    public void InitVar(){
        gptResponseButton = (ImageButton) findViewById(R.id.GptResponseButton);
        gptTitleImage = (ImageView) findViewById(R.id.GptTitleImage);
        gptTitleText = (TextView) findViewById(R.id.GptTitleText);
        gptResponseText = (TextView) findViewById(R.id.GPTResponseTextView);

        youtubeResponseButton = (ImageButton) findViewById(R.id.YoutubeResponseButton);
        youtubeTitleImage = (ImageView) findViewById(R.id.YoutubeTitleImage);
        youtubeTitleText = (TextView) findViewById(R.id.YoutubeTitleText);
        youTubePlayerView = findViewById(R.id.YoutubePlayerView);

        diaryCheckButton = (ImageButton) findViewById(R.id.DiaryCheckButton);
        diaryClearButton = (ImageButton) findViewById(R.id.DiaryClearButton);
        diaryWrite = (EditText) findViewById(R.id.DiaryWrite);

        gptResponseButton.setOnClickListener(this);
        youtubeResponseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == gptResponseButton) {
            diaryContents = diaryWrite.getText().toString();
            if(diaryContents.equals("")) {
                return;
            }
            gptResponseButton.setVisibility(View.INVISIBLE);
            gptTitleImage.setVisibility(View.VISIBLE);
            gptTitleText.setVisibility(View.VISIBLE);
            gptResponseText.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("model", "gpt-3.5-turbo");

                JSONArray jsonArrayMessage = new JSONArray();
                JSONObject jsonObjectMessage = new JSONObject();
                jsonObjectMessage.put("role", "user");
                jsonObjectMessage.put("content", diaryContents + "내 일기에 대해 반응을 해줘!");
                jsonArrayMessage.put(jsonObjectMessage);

                jsonObject.put("messages", jsonArrayMessage);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    stringURLEndPoint, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    String stringText = null;
                    try {
                        stringText = response.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    stringOutput = stringOutput + stringText;
                    gptResponseText.setText(stringOutput);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> mapHeader = new HashMap<>();
                    mapHeader.put("Authorization", "Bearer " + APIKey);
                    mapHeader.put("Content-Type", "application/json");

                    return mapHeader;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
                }
            };

            int intTimeoutPeriod = 60000; // 60 seconds timeout duration defined
            RetryPolicy retryPolicy = new DefaultRetryPolicy(intTimeoutPeriod,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(retryPolicy);
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        }

        if(view == youtubeResponseButton) {
            diaryContents = diaryWrite.getText().toString();
            if(diaryContents.equals("")) {
                return;
            }
            checkYoutube = true;
            youtubeResponseButton.setVisibility(View.INVISIBLE);
            youtubeTitleImage.setVisibility(View.VISIBLE);
            youtubeTitleText.setVisibility(View.VISIBLE);
            youTubePlayerView.setVisibility(View.VISIBLE);
        }

        if(view == diaryCheckButton) {
            if(!checkYoutube) {
                randomValue = -1;
            }
            if(id == -1) {
                diaryContents = diaryWrite.getText().toString();
                gptContents = gptResponseText.getText().toString();
                //Log.v("check", diaryContents);
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into diarylist (year, month, day, text, gpt, song) values(?, ?, ?, ?, ?, ?)", new String[]{year, month, day, diaryContents, gptContents, Integer.toString(randomValue)});
                db.close();
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
            } else {
                diaryContents = diaryWrite.getText().toString();
                gptContents = gptResponseText.getText().toString();
                //Log.v("pre", "pre");
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("update diarylist set year = \'" + year + "\', month = \'" + month + "\', day = \'" + day + "\', text = \'" + diaryContents + "\', gpt = \'" + gptContents + "\', song = \'" + Integer.toString(randomValue) + "\' WHERE id = " + id);
                db.close();
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
            }

            Log.v("check", diaryContents);
            Log.v("check 2", year + " " + month + " " + date);
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into diarylist (year, month, day, text, gpt) values(?, ?, ?, ?, ?)", new String[]{year, month, date, diaryContents, stringOutput});
            db.close();
            Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
            startActivity(intent);
        }

        if(view == diaryClearButton) {
            finish();
        }
    }

    private String getMonthName(int month) {
        String[] monthNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }

}