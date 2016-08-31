package com.example.administrator.handler;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainActivity extends Activity {
    private Button Start = null;
    private Button End = null;
    private Handler Hd = new Handler();
    private HdCls updateThread = new HdCls();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Start = (Button)findViewById(R.id.Btn1);
        End = (Button)findViewById(R.id.Btn2);

        Start.setOnClickListener(new StartButtonOnclickListener());
        End.setOnClickListener(new EndButtonOnclickListener());

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

    class StartButtonOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Hd.post(updateThread);

        }
    }

    class EndButtonOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Hd.removeCallbacks(updateThread);
        }
    }

    class HdCls implements Runnable{
        @Override
        public void run() {
            System.out.println("UpdateThread");
            Hd.postDelayed(updateThread, 3000);
        }
    }
}
