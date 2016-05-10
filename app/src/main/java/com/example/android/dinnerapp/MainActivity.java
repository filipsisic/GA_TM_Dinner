/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.dinnerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {

    TagManager tagManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make sure that Analytics tracking has started
        ((App) getApplication()).startTracking();
        loadGTMContainer();
    }

    private void loadGTMContainer(){
        tagManager = ((App) getApplication()).getTagManager();
        tagManager.setVerboseLoggingEnabled(true);

        PendingResult pendingResult = tagManager.loadContainerPreferFresh("GTM-5HMX68", R.raw.gtm_default);
        pendingResult.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                if (!containerHolder.getStatus().isSuccess()) {
                    return;
                }
                containerHolder.refresh();
                ((App) getApplication()).setContainerHolder(containerHolder);
            }
        }, 2, TimeUnit.SECONDS);
    }

    /*
     * Show a pop up menu of food preferences.
     * Menu items are defined in menus/food_prefs_menu.xml
     */
    public void showFoodPrefsMenu(View view) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.food_prefs_menu, popup.getMenu());

        // Set the action of the menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                getDinnerSuggestion(item.getItemId());
                return true;
            }
        });
        // Show the popup menu
        popup.show();
    }

    public void showDinnerList(View view) {
        Intent intent = new Intent(this, ShowAllDinnersActivity.class);
        startActivity(intent);
    }

    public void showDailySpecial(View view) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.food_prefs_menu, popup.getMenu());

        // Set the action of the menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                putFoodPrefInDataLayer(item);
                startActivity(new Intent(MainActivity.this, ShowDailySpecialActivity.class));
                return true;
            }
        });
        // Show the popup menu
        popup.show();
    }

    /*
     * Suggest dinner for tonight.
     * This method is invoked by the button click action of the food prefs menu.
     * We use the Dinner class to figure out the dinner, based on the food pref.

     */
    public String getDinnerSuggestion(int item) {

        // Get a new Dinner, and use it to get tonight's dinner
        Dinner dinner = new Dinner(this, item);
        String dinnerChoice = dinner.getDinnerTonight();
        // Utility.showMyToast("dinner suggestion: " + dinnerChoice, this);

        // Start an intent to show the dinner suggestion
        // Put the suggested dinner in the Intent's Extras bundle
        Intent dinnerIntent = new Intent(this, ShowDinnerActivity.class);

        dinnerIntent.putExtra(String.valueOf(R.string.selected_dinner), dinnerChoice);
        startActivity(dinnerIntent);

        return dinnerChoice;
    }

    private void putFoodPrefInDataLayer(MenuItem item) {
        DataLayer dataLayer = tagManager.getDataLayer();
        String value;
        switch (item.getItemId()) {
            case R.id.vegan_pref:
                value = "vegan";
                break;
            case R.id.vegetarian_pref:
                value = "vegetarian";
                break;
            case R.id.fish_pref:
                value = "fish";
                break;
            case R.id.meat_pref:
                value = "meat";
                break;
            default:
                value = "unrestricted";
        }
        dataLayer.push("food_pref", value);
    }
}

