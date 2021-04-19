package com.home.selfview.CustomCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.home.selfview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenYesheng On 2021/4/8 10:33
 * Desc:
 */
public class VerticalSliderCalendar extends ConstraintLayout {

    public VerticalSliderCalendar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VerticalSliderCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VerticalSliderCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {


        initView(context);
    }

    private void initView(Context context) {
        //直接attachToRoot,就不用再addView了
        LayoutInflater.from(context).inflate(R.layout.vrtical_slider_calendar_layout, this, true);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Calendar> calendars = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            calendars.add(new Calendar());
        }
        recyclerView.setAdapter(new RVAdapter(calendars));
    }


    private static class RVAdapter extends RecyclerView.Adapter<RVAdapter.RvViewHolder> {
        private List<Calendar> list;

        public RVAdapter(List<Calendar> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_, parent, false);
            return new RvViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
            if (position >= 0 && position < list.size()) {
                Calendar calendar = list.get(position);
                holder.onBind(calendar, position);
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        protected class RvViewHolder extends RecyclerView.ViewHolder {

            private com.haibin.calendarview.CalendarView calendar;
            private TextView title;

            public RvViewHolder(@NonNull View itemView) {
                super(itemView);
                calendar = itemView.findViewById(R.id.calendarView);
                title = itemView.findViewById(R.id.data_title);
            }

            protected void onBind(Calendar cale, int position) {
                if (calendar.getCurMonth() - position == calendar.getCurMonth() - 1) {
                    title.setText(calendar.getCurYear() - 1 + "-" + (calendar.getCurMonth() + (12 - calendar.getCurMonth())));
                } else {
                    title.setText(calendar.getCurYear() + "-" + (calendar.getCurMonth() - position));
                }

                calendar.scrollToCalendar(calendar.getCurYear(), calendar.getCurMonth() - position, calendar.getCurDay());
            }
        }


    }

}
