package com.example.opensource2023;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleViewManager {
    private TextView titleText;
    private ImageView titleImage;

    public TitleViewManager(TextView titleText, ImageView titleImage) {
        this.titleText = titleText;
        this.titleImage = titleImage;
    }

    public void toggleVisibility() {
        int textVisibility = titleText.getVisibility();
        int imageVisibility = titleImage.getVisibility();

        if (textVisibility == View.VISIBLE && imageVisibility == View.VISIBLE) {
            // 현재 보이는 상태라면 숨김 처리
            titleText.setVisibility(View.INVISIBLE);
            titleImage.setVisibility(View.INVISIBLE);
        } else {
            // 현재 보이지 않는 상태라면 다시 표시
            titleText.setVisibility(View.VISIBLE);
            titleImage.setVisibility(View.VISIBLE);
        }
    }
}
