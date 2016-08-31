package com.example.administrator.raidobutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends Activity {
    private RadioGroup SexGroup = null;
    private RadioButton Female = null;
    private RadioButton male = null;
    private CheckBox swimBox = null;
    private CheckBox RunBox = null;
    private CheckBox readBox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SexGroup = (RadioGroup) findViewById(R.id.Group1);
        Female = (RadioButton) findViewById(R.id.femaleButton);
        male = (RadioButton) findViewById(R.id.maleButton);
        swimBox = (CheckBox)findViewById(R.id.swim);
        RunBox = (CheckBox)findViewById(R.id.run);
        readBox = (CheckBox)findViewById(R.id.read);
        SexGroup.setOnCheckedChangeListener(new FemaleChangeListener());
        swimBox.setOnCheckedChangeListener(new FavouriteSelectListener());
        readBox.setOnCheckedChangeListener(new FavouriteSelectListener());
        RunBox.setOnCheckedChangeListener(new FavouriteSelectListener());
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
        class FemaleChangeListener implements RadioGroup.OnCheckedChangeListener {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Female.getId() == checkedId){
                    Toast.makeText(MainActivity.this, R.string.female, Toast.LENGTH_SHORT).show();
                }
                else if (male.getId() == checkedId){
                    Toast.makeText(MainActivity.this, R.string.male, Toast.LENGTH_SHORT).show();
                }
            }
        }
        class FavouriteSelectListener implements CheckBox.OnCheckedChangeListener{
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.getId() == swimBox.getId()){
                        if(isChecked){
                            System.out.println("swim box checked");
                        }
                        else{

                        }
                    }
                    else if (buttonView.getId() == RunBox.getId()){
                        if(isChecked){
                            System.out.println("run box checked");
                        }
                        else{

                        }
                    }
                    else if (buttonView.getId() == readBox.getId()){
                        if(isChecked){
                            System.out.println("read box checked");
                        }
                        else{

                        }
                    }
            }
        }
}
