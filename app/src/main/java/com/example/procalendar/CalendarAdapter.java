package com.example.procalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<CalendarSelect> calendarSelects;

    public CalendarAdapter() {

    }

    public CalendarAdapter(List<CalendarSelect> calendarSelects) {
        this.calendarSelects = calendarSelects;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CalendarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return getCalendarSelects().size();
    }

    public List<CalendarSelect> getCalendarSelects() {
        return calendarSelects;
    }

    public void setCalendarSelects(List<CalendarSelect> calendarSelects) {
        this.calendarSelects = calendarSelects;
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        private CalendarSelect calendarSelect;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            calendarSelect = itemView.findViewById(R.id.cs_item);
        }
    }
}
