package com.example.pricesbase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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

    String noItems = "No items added";
    int testCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsListView = findViewById(R.id.productsListView);
        productsList = loadFromDatabase();
        arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,productsList);
        productsListView.setAdapter(arrayAdapter);

        ProductListener productListener = new ProductListener();
        productsListView.setOnItemClickListener(productListener);
        productsListView.setOnItemLongClickListener(productListener);

    }

    public ArrayList<String> loadFromDatabase(){
        boolean isTestRun = false;

        ArrayList<String> products;
        sqLiteDatabase = this.openOrCreateDatabase("Prices", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Prices (Product VARCHAR, Price INT, Shop VARCHAR)");
        if (isTestRun){
            sqLiteDatabase.execSQL("INSERT INTO Prices (Product, Price, Shop) VALUES ('eggs', 10, 'Walmart')");
            sqLiteDatabase.execSQL("INSERT INTO Prices (Product, Price, Shop) VALUES ('ham', 15, 'K-market')");
        }

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Prices",null);
        cursor.moveToFirst();
        products=new ArrayList<>();

        int productIndex = cursor.getColumnIndex("Product");

        while (!cursor.isAfterLast()){
            products.add(cursor.getString(productIndex));
            cursor.moveToNext();
        }
        cursor.close();

        if (products.isEmpty()){
            products.add(noItems);
        }

        return products;
    }

    public void addProduct(View view){
        String newProductName = "test product "+testCount++;
        String newProductPrice = "test price";
        String newProductShop = "test shop";

        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("INSERT INTO Prices (Product, Price, Shop) VALUES (?,?,?)");
        sqLiteStatement.bindString(1,newProductName);
        sqLiteStatement.bindString(2,newProductPrice);
        sqLiteStatement.bindString(3,newProductShop);
        sqLiteStatement.execute();
        makeToast("Product added");

        productsList.remove(noItems);

        productsList.add(newProductName);
        arrayAdapter.notifyDataSetChanged();

    }


     public class ProductListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String productName = ((TextView) view).getText().toString();

            if (!productName.equals(noItems)) {
                Intent intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
                intent.putExtra("name",productName);
                startActivity(intent);
            }
        }
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final String productName = ((TextView) view).getText().toString();
            if (!productName.equals(noItems)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete")
                        .setMessage("Delete this product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(productName);
                            }
                        })
                        .setNegativeButton("No",null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            else return false;
        }
    }


    public void makeToast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }

    public void deleteProduct(String productName){
        sqLiteDatabase.execSQL("DELETE FROM Prices WHERE Product=?",new String[]{productName});
        productsList.remove(productName);
        if (productsList.isEmpty()){
            productsList.add("No items added");
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
