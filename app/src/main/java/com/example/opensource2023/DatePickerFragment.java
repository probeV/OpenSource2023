package com.example.opensource2023;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, year, month, day);
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // 인텐트를 통해 액티비티로 정보 전달
        Intent intent = new Intent(getActivity(), DiaryWriteActivity.class);
        intent.putExtra("year", Integer.toString(year));
        intent.putExtra("month", Integer.toString(month + 1));
        intent.putExtra("day", Integer.toString(day));
        intent.putExtra("id", "-1");

        getActivity().startActivity(intent);
    }
}