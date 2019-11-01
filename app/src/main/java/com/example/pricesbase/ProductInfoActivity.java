package com.example.pricesbase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

        Intent intent = getIntent();
        productName = intent.getStringExtra("name");

        Cursor cursor = MainActivity.sqLiteDatabase.rawQuery("SELECT Price,Shop FROM Prices WHERE Product=?", new String[]{productName});
        cursor.moveToFirst();

        TextView productTextView = findViewById(R.id.productTitleTextView);
        TextView shopTextView1 =  findViewById(R.id.shopTextView1);
        TextView priceTextView1 =  findViewById(R.id.priceTextView1);

        int shopIndex = cursor.getColumnIndex("Shop");
        int priceIndex = cursor.getColumnIndex("Price");

        productShop = cursor.getString(shopIndex);
        productPrice = cursor.getInt(priceIndex);

        productTextView.setText(productName);
        shopTextView1.setText(productShop);
        priceTextView1.setText(String.valueOf(productPrice));
      }

      public void launchEditor(MenuItem item){
        Intent intent = new Intent(getApplicationContext(),EditProductActivity.class);
        intent.putExtra("name", productName);
        intent.putExtra("shop",productShop);
        intent.putExtra("price",productPrice);
        startActivity(intent);
      }

}
