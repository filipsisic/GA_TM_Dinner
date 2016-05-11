package com.example.android.dinnerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tagmanager.ContainerHolder;

public class ShowDailySpecialActivity extends Activity {

    private TextView dailySpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_daily_special);

        dailySpecial = ((TextView) findViewById(R.id.textView_daily_special));
        updateDailySpecial();
    }

    public void orderOnline(View view) {
        Intent intent = new Intent(this, OrderDinnerActivity.class);
        intent.putExtra(OrderDinnerActivity.selectedDinnerExtrasKey, dailySpecial.getText());
        startActivity(intent);
    }

    public void updateDailySpecial() {
        ContainerHolder holder = ((App) getApplication()).getContainerHolder();
        String value = holder.getContainer().getString("daily-special");
        String test = holder.getContainer().getString("test-variable");
        dailySpecial.setText(value);
        dailySpecial.append("\n" + test);
    }
}
