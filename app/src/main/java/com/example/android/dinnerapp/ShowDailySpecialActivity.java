package com.example.android.dinnerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowDailySpecialActivity extends Activity {

    private String todaySpecial = "Fried egg with hit kat rashers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_daily_special);

        ((TextView) findViewById(R.id.textView_daily_special)).setText(todaySpecial);
    }

    public void orderOnline(View view) {
        Intent intent = new Intent(this, OrderDinnerActivity.class);
        intent.putExtra(OrderDinnerActivity.selectedDinnerExtrasKey, todaySpecial);
        startActivity(intent);
    }
}
