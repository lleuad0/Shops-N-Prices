package com.example.shopsandprices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditProductActivity extends AppCompatActivity {

    TextView productTextView;
    TextView shopTextView;
    TextView priceTextView;

    String productName;
    String productShop;
    double productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productTextView = findViewById(R.id.productNameEditable);
        shopTextView = findViewById(R.id.productShopEditable);
        priceTextView = findViewById(R.id.productPriceEditable);

        Intent intent = getIntent();
        productName = intent.getStringExtra("name");
        productShop = intent.getStringExtra("shop");
        productPrice = intent.getDoubleExtra("price",0.0);

        productTextView.setText(productName);
        shopTextView.setText(productShop);
        priceTextView.setText(String.valueOf(productPrice));
    }

    public void saveAndExit(View view){
        super.onBackPressed();
    }

}
