package com.github.lleuad0.shopsandprices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ProductInfoActivity extends AppCompatActivity {

    String productName;
    String productShop;
    double productPrice;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_product_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        TextView productTextView = findViewById(R.id.productTitleTextView);
        TextView shopTextView =  findViewById(R.id.shopTextView);
        TextView priceTextView =  findViewById(R.id.priceTextView);

        Intent intent = getIntent();
        productName = intent.getStringExtra("name");

        Cursor cursor = MainActivity.sqLiteDatabase.rawQuery("SELECT Price,Shop FROM Prices WHERE Product=?", new String[]{productName});
        cursor.moveToFirst();

        int shopIndex = cursor.getColumnIndex("Shop");
        int priceIndex = cursor.getColumnIndex("Price");

        productShop = cursor.getString(shopIndex);
        productPrice = cursor.getInt(priceIndex);

        cursor.close();

        productTextView.setText(productName);
        shopTextView.setText(productShop);
        priceTextView.setText(String.valueOf(productPrice));
      }

      public void launchEditor(MenuItem item){
        Intent intent = new Intent(getApplicationContext(),EditProductActivity.class);
        intent.putExtra("name", productName);
        intent.putExtra("shop",productShop);
        intent.putExtra("price",productPrice);
        startActivity(intent);
      }

}
