package com.example.android.dinnerapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;

public class ShowAllDinnersActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long timeStart = System.currentTimeMillis();
        setContentView(R.layout.activity_show_all_dinners);
        ListView listView = (ListView) findViewById(android.R.id.list);
        Dinner dinner = new Dinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.show_dinner_in_row, dinner.getAllDinners(this));
        listView.setAdapter(adapter);
        long timeEnd = System.currentTimeMillis();

        long elapsedTime = (timeEnd - timeStart);

        Utility.showMyToast(String.valueOf(elapsedTime), this);
        ((App) getApplication()).getTracker()
                .send(new HitBuilders.TimingBuilder()
                        .setCategory("List all the dinners")
                        .setValue(elapsedTime)
                        .setLabel("Display list")
                        .setVariable("Duration")
                        .build());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this, OrderDinnerActivity.class);
        intent.putExtra(String.valueOf(R.string.selected_dinner), (String) l.getItemAtPosition(position));
        startActivity(intent);
    }
}
