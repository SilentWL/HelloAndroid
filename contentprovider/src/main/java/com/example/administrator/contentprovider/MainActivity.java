package com.example.administrator.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button insert;
    Button query;
    Button update;
    Button delete;
    Button querys;
    Uri uri = Uri.parse("content://wl.android.contentProvider/teacher");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insert = (Button) findViewById(R.id.insert);
        query = (Button) findViewById(R.id.query);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        querys = (Button) findViewById(R.id.querys);
        // 绑定监听器的两种方法一；
        insert.setOnClickListener(new InsertListener());
        query.setOnClickListener(new QueryListener());
        // 方法二
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ContentResolver cr = getContentResolver();
                ContentValues cv = new ContentValues();
                cv.put("name", "huangbiao");
                cv.put("date_added", (new Date()).toString());
                int uri2 = cr.update(uri, cv, "_ID=?", new String[]{"3"});
                System.out.println("updated"+":"+uri2);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ContentResolver cr = getContentResolver();
                cr.delete(uri, "_ID=?", new String[]{"2"});
            }
        });

        querys.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                ContentResolver cr = getContentResolver();
                // 查找id为1的数据
                Cursor c = cr.query(uri, null, null,null, null);
                if (c.moveToFirst() == false) {
                    // 为空的Cursor
                    return;
                }

                while (c.moveToNext()) {
                    int name = c.getColumnIndex("name");
                    int id = c.getColumnIndex("_id");
                    int sex = c.getColumnIndex("SEX");
                    int date_added = c.getColumnIndex("date_added");
                    System.out.println("user--->" + c.getString(name) + "," + "id--->" + c.getString(id) + "," + "sex--->" + c.getString(sex) + ","
                            + "date_added---->" + c.getString(date_added));
                    System.out.println("==========================================");
                }

                System.out.println(c.getCount());
                c.close();
            }
        });
    }
    class InsertListener implements View.OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            ContentResolver cr = getContentResolver();

            ContentValues cv = new ContentValues();
            cv.put("title", "jiaoshou");
            cv.put("name", "jiaoshi");
            cv.put("sex", true);
            Uri uri2 = cr.insert(uri, cv);
            System.out.println(uri2.toString());
        }

    }

    class QueryListener implements View.OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            ContentResolver cr = getContentResolver();
            // 查找id为1的数据
            Cursor c = cr.query(uri, null, "_ID=?", new String[] { "1" }, null);
            //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
            if (c.moveToFirst() == false) {
                // 为空的Cursor
                return;
            }
            int name = c.getColumnIndex("name");
            System.out.println(c.getString(name));
            c.close();
        }
    }
}
