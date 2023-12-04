package com.example.opensource2023;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewManager {
    private ImageButton resposeButton;
    private TextView titleText;
    private ImageView titleImage;

    public ViewManager(ImageButton responseButton, TextView titleText, ImageView titleImage) {
        this.resposeButton = responseButton;
        this.titleText = titleText;
        this.titleImage = titleImage;
    }

    public void toggleVisibility() {
        int buttonVisibility = resposeButton.getVisibility();

        // 현재 버튼이 보이는 상태 -> 응답 출력
        if (buttonVisibility == View.VISIBLE) {
            resposeButton.setVisibility(View.INVISIBLE);
            titleText.setVisibility(View.VISIBLE);
            titleImage.setVisibility(View.VISIBLE);
        }
    }
}
