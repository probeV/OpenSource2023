package com.example.opensource2023;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener{

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
    int id;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

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
            Cursor cursor = db.rawQuery("select text, gpt from diarylist where id= \'"+ id + "\'", null);
            while(cursor.moveToNext()) {
                diaryContents = cursor.getString(0);
                stringOutput = cursor.getString(1);
            }
            cursor.close();
            db.close();
            diaryWrite.setText(diaryContents);
            gptResponseText.setText(stringOutput);
        }
    }

    public void ButtonInit(){
        gptResponseButton = (ImageButton) findViewById(R.id.GptResponseButton);
        gptTitleImage = (ImageView) findViewById(R.id.GptTitleImage);
        gptTitleText = (TextView) findViewById(R.id.GptTitleText);
        gptResponseText = (TextView) findViewById(R.id.GPTResponseTextView);
        youtubeResponseButton = (ImageButton) findViewById(R.id.YoutubeResponseButton);
        youtubeTitleImage = (ImageView) findViewById(R.id.YoutubeTitleImage);
        youtubeTitleText = (TextView) findViewById(R.id.YoutubeTitleText);
        diaryCheckButton = (ImageButton) findViewById(R.id.DiaryCheckButton);
        diaryClearButton = (ImageButton) findViewById(R.id.DiaryClearButton);
        diaryWrite = (EditText) findViewById(R.id.DiaryWrite);

        gptResponseButton.setOnClickListener(this);
        youtubeResponseButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == gptResponseButton) {
            gptResponseButton.setVisibility(View.INVISIBLE);
            gptTitleImage.setVisibility(View.VISIBLE);
            gptTitleText.setVisibility(View.VISIBLE);
            gptResponseText.setVisibility(View.VISIBLE);
            diaryContents = diaryWrite.getText().toString();
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("model", "gpt-3.5-turbo");

                JSONArray jsonArrayMessage = new JSONArray();
                JSONObject jsonObjectMessage = new JSONObject();
                jsonObjectMessage.put("role", "user");
                jsonObjectMessage.put("content", diaryContents + "이 일기에 대해 반응을 해줘.");
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
            youtubeResponseButton.setVisibility(View.INVISIBLE);
            youtubeTitleImage.setVisibility(View.VISIBLE);
            youtubeTitleText.setVisibility(View.VISIBLE);
        }

        if(view == diaryCheckButton) {
            diaryContents = diaryWrite.getText().toString();

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