package com.example.administrator.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button open = null;
    private EditText url = null;
    private TextView progress = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        open = (Button)findViewById(R.id.open);
        url = (EditText)findViewById(R.id.url);
        progress = (TextView)findViewById(R.id.progress);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUrlContentTask(MainActivity.this).execute(url.getText().toString());
            }
        });

    }

    class GetUrlContentTask extends AsyncTask<String, Integer, String>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setText(s);
            progressDialog.dismiss();
        }


        public GetUrlContentTask(Context context){
            progressDialog = new ProgressDialog(context);
            progressDialog.setButton("Cancel", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            progressDialog.setCancelable(true);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder urlContent = new StringBuilder();
            try{
                URL url = new URL("http://192.168.199.18:8080/mp3/resources.xml");
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestMethod("POST");
                //urlConnection.setRequestProperty("Accept-Encoding", "identity");

                if (urlConnection.getResponseCode() == 200) {
                    int totalSize = urlConnection.getContentLength();
                    InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream(), "gb2312");

                    BufferedReader bufferedReader = new BufferedReader(inputStream);
                    String inputLine = null;

                    int size = 0;

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        urlContent.append(inputLine);
                        size += inputLine.length();

                        if (size > 0) {
                            publishProgress((int) (((float) size / totalSize) * 100));
                        }
                        Thread.sleep(500);
                    }
                    bufferedReader.close();
                    inputStream.close();


                }
                urlConnection.disconnect();
                return urlContent.toString();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }
}
