package com.krishnachaitanya.expensetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;

public class remainder extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
String[] loopday={"daily","weekly","monthly"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);

        Spinner s=(Spinner) findViewById(R.id.spin);

        ArrayAdapter aa =new ArrayAdapter(this,android.R.layout.simple_spinner_item,loopday);
        s.setAdapter(aa);
        s.setOnItemSelectedListener(this);

    }





    private void daily() {


    }
    private void weekly() {

    }
    private void monthly() {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            daily();
        }
        else if(position==1){

            weekly();
        }
        else{
            monthly();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
