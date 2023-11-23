package com.example.opensource2023;

import android.view.View;
import android.widget.TextView;

public class GridIHolder {
    public TextView date;

    public GridIHolder(View a_view) {
        date = a_view.findViewById(R.id.date);
    }
}
