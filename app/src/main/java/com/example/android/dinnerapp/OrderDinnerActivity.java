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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class OrderDinnerActivity extends Activity {

    private static final String ANALYTICS_CATEGORY = "Shopping steps";
    public static final String selectedDinnerExtrasKey = String.valueOf(R.string.selected_dinner);

    private String dinner, dinnerId;
    private Product product;
    private Button cartButton, checkoutButton, paymentButton, purchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_dinner);
    }

    protected void onStart() {
        super.onStart();

        // Set the heading
        TextView heading_tv = (TextView) findViewById(R.id.textView_info_heading);
        heading_tv.setText(getResources().getText(R.string.order_online_heading));

        // Set the text
        TextView tv = (TextView) findViewById(R.id.textView_info);

        cartButton = (Button) findViewById(R.id.add_to_cart_btn);
        checkoutButton = (Button) findViewById(R.id.checkout_btn);
        paymentButton = (Button) findViewById(R.id.payment_btn);
        purchaseButton = (Button) findViewById(R.id.purchase_btn);

        dinner = getIntent().getStringExtra(selectedDinnerExtrasKey);
        tv.setText("This is where you will order the selected dinner: \n\n" +
                dinner);
        dinnerId = Utility.getDinnerId(dinner);
        sendViewProductHit();
    }

    public void addDinnerToCart(View view) {
        Utility.showMyToast("Dinner " + dinner + " added to the cart", this);
        sendAddToCartHit();
        cartButton.setVisibility(INVISIBLE);
        checkoutButton.setVisibility(VISIBLE);
    }

    public void startCheckout(View view) {
        Utility.showMyToast("Checkout started", this);
        sendStartCheckoutHit();
        checkoutButton.setVisibility(INVISIBLE);
        paymentButton.setVisibility(VISIBLE);
    }

    public void payment(View view) {
        Utility.showMyToast("Payment", this);
        sendPaymentHit();
        paymentButton.setVisibility(INVISIBLE);
        purchaseButton.setVisibility(VISIBLE);
    }

    public void purchase(View view) {
        Utility.showMyToast("Purchased dinner", this);
        sendPurchaseHit();
    }

    public void sendViewProductHit() {
        ProductAction productAction = new ProductAction(ProductAction.ACTION_DETAIL);
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(ANALYTICS_CATEGORY)
                .setAction("View Order Dinner screen")
                .setLabel(dinner)
                .addProduct(getProduct())
                .setProductAction(productAction)
                .build());
    }

    private void sendAddToCartHit() {
        ProductAction productAction = new ProductAction(ProductAction.ACTION_ADD);
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(ANALYTICS_CATEGORY)
                .setAction("Add dinner to cart")
                .setLabel(dinner)
                .addProduct(getProduct())
                .setProductAction(productAction)
                .build());
    }

    private void sendStartCheckoutHit() {
        ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT);
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(ANALYTICS_CATEGORY)
                .setAction("Start checkout")
                .setLabel(dinner)
                .addProduct(getProduct())
                .setProductAction(productAction)
                .build());
    }

    private void sendPaymentHit() {
        ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT_OPTION).setCheckoutStep(2);
        getTracker().send(new HitBuilders.EventBuilder()
        .setCategory(ANALYTICS_CATEGORY)
        .setAction("Payment")
        .setLabel(dinner)
        .addProduct(getProduct())
        .setProductAction(productAction)
        .build());
    }

    private void sendPurchaseHit() {
        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId(Utility.getUniqueTransactionId(dinnerId));
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(ANALYTICS_CATEGORY)
                .setAction("Purchase")
                .setLabel(dinner)
                .addProduct(getProduct())
                .setProductAction(productAction)
                .build());
    }

    private Product getProduct() {
        if (product == null) {
            product = new Product()
                    .setName("dinner")
                    .setPrice(5)
                    .setVariant(dinner)
                    .setId(dinnerId)
                    .setQuantity(1);
        }
        return product;
    }

    private Tracker getTracker() {
        return ((App) getApplication()).getTracker();
    }
}
