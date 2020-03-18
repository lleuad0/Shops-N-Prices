package com.example.shopsandprices;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase sqLiteDatabase;
    ListView productsListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> productsList;
    TextView noItemsTextView;

    int testCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noItemsTextView = findViewById(R.id.noItemsTextView);

        productsListView = findViewById(R.id.productsListView);
        productsList = loadFromDatabase();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, productsList);
        productsListView.setAdapter(arrayAdapter);

        ProductListener productListener = new ProductListener();
        productsListView.setOnItemClickListener(productListener);
        productsListView.setOnItemLongClickListener(productListener);

    }

    public ArrayList<String> loadFromDatabase() {
        sqLiteDatabase = this.openOrCreateDatabase("Prices", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Prices (Product VARCHAR, Price INT, Shop VARCHAR)");

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Prices", null);
        cursor.moveToFirst();
        int productIndex = cursor.getColumnIndex("Product");

        ArrayList<String> products = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            products.add(cursor.getString(productIndex));
            cursor.moveToNext();
        }
        cursor.close();

        if (products.isEmpty()) {
            showEmptyMessage();
        }

        return products;
    }

    public void addProduct(View view) {
        if (productsList.isEmpty()){
            showProductsList();
        }

        String newProductName = "test product " + testCount++;
        String newProductPrice = "test price";
        String newProductShop = "test shop";

        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("INSERT INTO Prices (Product, Price, Shop) VALUES (?,?,?)");
        sqLiteStatement.bindString(1, newProductName);
        sqLiteStatement.bindString(2, newProductPrice);
        sqLiteStatement.bindString(3, newProductShop);
        sqLiteStatement.execute();
        makeToast(getString(R.string.added_success));

        productsList.add(newProductName);
        arrayAdapter.notifyDataSetChanged();
    }

    public class ProductListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String productName = ((TextView) view).getText().toString();

            Intent intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
            intent.putExtra("name", productName);
            startActivity(intent);
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final String productName = ((TextView) view).getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.del_alert_title))
                    .setMessage(getString(R.string.del_alert_message))
                    .setPositiveButton(getString(R.string.del_alert_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteProduct(productName);
                            makeToast(getString(R.string.deleted_success));
                        }
                    })
                    .setNegativeButton(getString(R.string.del_alert_negative), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;

        }
    }

    public void deleteProduct(String productName) {
        sqLiteDatabase.execSQL("DELETE FROM Prices WHERE Product=?", new String[]{productName});
        productsList.remove(productName);
        arrayAdapter.notifyDataSetChanged();

        if (productsList.isEmpty()) {
            showEmptyMessage();
        }
    }

    public void makeToast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    private void showEmptyMessage() {
        noItemsTextView.setVisibility(View.VISIBLE);
        productsListView.setVisibility(View.GONE);
    }

    private void showProductsList(){
        noItemsTextView.setVisibility(View.GONE);
        productsListView.setVisibility(View.VISIBLE);
    }
}
