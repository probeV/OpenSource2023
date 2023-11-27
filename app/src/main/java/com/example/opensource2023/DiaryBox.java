package com.example.opensource2023;

public class DiaryBox {
    private int id;
    private String month;
    private String day;

    public DiaryBox(int id, String month, String day) {
        this.id = id;
        this.month = month;
        this.day = day;
    }

    public int getId() {return id;}

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }
}
