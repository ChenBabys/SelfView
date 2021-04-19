package com.home.selfview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        SaleProgressView saleProgressView = findViewById(R.id.sale_bar);
        saleProgressView.setTotalAndCurrentCount(100, 100);
    }
}