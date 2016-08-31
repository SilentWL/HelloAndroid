package com.example.administrator.sqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button BtnCreateDB = null;
    private Button BtnUpdateDB = null;
    private Button BtnInsert = null;
    private Button BtnUpdate = null;
    private Button BtnQuery = null;
    private DatabaseHelper dbHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnCreateDB = (Button)findViewById(R.id.BtnCreateDB);
        BtnUpdateDB = (Button)findViewById(R.id.BtnUpDB);
        BtnInsert = (Button)findViewById(R.id.Insert);
        BtnUpdate = (Button)findViewById(R.id.Update);
        BtnQuery = (Button)findViewById(R.id.query);

        BtnCreateDB.setOnClickListener(new BtnOnClickLintener());
        BtnUpdateDB.setOnClickListener(new BtnOnClickLintener());
        BtnInsert.setOnClickListener(new BtnOnClickLintener());
        BtnUpdate.setOnClickListener(new BtnOnClickLintener());
        BtnQuery.setOnClickListener(new BtnOnClickLintener());

        dbHelper = new DatabaseHelper(MainActivity.this, "TestDB_6");
    }

    class BtnOnClickLintener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == BtnCreateDB.getId()){
                dbHelper.getReadableDatabase();
            }
            else if (v.getId() == BtnUpdateDB.getId()){
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "TestDB_6", 2);
                dbHelper.getReadableDatabase();
            }
            else if (v.getId() == BtnInsert.getId()){
                ContentValues value = new ContentValues();
                value.put("id", 10);
                value.put("name", "WL");

                SQLiteDatabase db = (SQLiteDatabase)dbHelper.getWritableDatabase();
                db.insert("user", null, value);
            }
            else if (v.getId() == BtnUpdate.getId()){
                ContentValues value = new ContentValues();
                value.put("name", "WLWL");

                SQLiteDatabase db = (SQLiteDatabase)dbHelper.getWritableDatabase();
                db.update("user", value, "id=?", new String[]{"10"});
            }else if (v.getId() == BtnQuery.getId()){
                SQLiteDatabase db = (SQLiteDatabase)dbHelper.getReadableDatabase();
                Cursor cursor = db.query("user", new String[]{"id", "name"}, "id=?", new String[]{"10"}, null, null, null);

                while (cursor.moveToNext()) {
                    int Nameindex = cursor.getColumnIndex("id");
                    int Phoneindex = cursor.getColumnIndex("name");
                    System.out.println("user--->" + cursor.getString(Nameindex) + ","
                            + "id---->" + cursor.getString(Phoneindex));
                    System.out.println("==========================================");
                }
            }
        }
    }
}
