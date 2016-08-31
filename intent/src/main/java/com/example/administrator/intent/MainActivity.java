package com.example.administrator.intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    private Button MyIntentButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyIntentButton = (Button)findViewById(R.id.IntentButton);
        MyIntentButton.setOnClickListener(new IntentButtonListener());
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
    class IntentButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent NewIntent = new Intent();
            NewIntent.putExtra("TestIntent", "123");
            NewIntent.setClass(MainActivity.this, MainActivity2Intent.class);
            MainActivity.this.startActivity(NewIntent);
        }
    }
}
