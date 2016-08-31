package com.example.administrator.calc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends Activity {
    private EditText Factor1 = null;
    private EditText Factor2 = null;
    private TextView Symbol = null;
    private Button Calc = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Factor1 = (EditText)findViewById(R.id.factor1);
        Factor2 = (EditText)findViewById(R.id.factor2);
        Symbol = (TextView)findViewById(R.id.symbol);
        Calc = (Button)findViewById(R.id.calc);
        Symbol.setText(R.string.symbol);
        Calc.setText(R.string.calc);
        Calc.setOnClickListener(new CalcButtonListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, 0, R.string.exit);
        menu.add(0, 2, 0, R.string.about);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == 1){
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class CalcButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String Factor1Str = Factor1.getText().toString();
            String Factor2Str = Factor2.getText().toString();

            Intent CalcButtonIntent = new Intent();
            CalcButtonIntent.putExtra("factor1", Factor1Str);
            CalcButtonIntent.putExtra("factor2", Factor2Str);
            CalcButtonIntent.setClass(MainActivity.this, ResultActivity.class);
            MainActivity.this.startActivity(CalcButtonIntent);
        }
    }
}
