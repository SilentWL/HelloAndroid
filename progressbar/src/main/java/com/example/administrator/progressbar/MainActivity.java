package com.example.administrator.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class MainActivity extends Activity {
    private ProgressBar FirstBar = null;
    private ProgressBar SecondBar = null;
    private Button myButton = null;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirstBar = (ProgressBar)findViewById(R.id.FirstBar);
        SecondBar = (ProgressBar)findViewById(R.id.SecondBar);
        myButton = (Button)findViewById(R.id.Button1);
        myButton.setOnClickListener(new ProgressBarChangeButtonListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ProgressBarChangeButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (i == 0){
                FirstBar.setVisibility(View.VISIBLE);
                SecondBar.setVisibility(View.VISIBLE);
                i+=10;
            }
            else if (i < 100){
                FirstBar.setProgress(i);
                FirstBar.setSecondaryProgress(i + 10);
                SecondBar.setProgress(i);
                i+=10;
            }
            else{
                FirstBar.setVisibility(View.GONE);
                SecondBar.setVisibility(View.GONE);
                i = 0;
            }

        }
    }
}
