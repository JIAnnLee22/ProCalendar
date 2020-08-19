package com.example.procalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<CalendarSelect> calendarSelects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private float x1, x2, y1, y2;

    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @Override
    protected void onResume() {
        super.onResume();

        calendarSelects = new ArrayList<>();
        calendarSelects.add(new CalendarSelect(getApplicationContext()));
        calendarSelects.add(new CalendarSelect(getApplicationContext()));
        calendarSelects.add(new CalendarSelect(getApplicationContext()));
        final LinearLayoutManager ls = new LinearLayoutManager(this);
        ls.setOrientation(RecyclerView.HORIZONTAL);
        final CalendarAdapter calendarAdapter = new CalendarAdapter(calendarSelects);
        recyclerView = findViewById(R.id.cs_list);
        recyclerView.setAdapter(calendarAdapter);
        recyclerView.setLayoutManager(ls);

    }
}