package com.krishnachaitanya.expensetracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.krishnachaitanya.expensetracker.Common.Common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class plus extends AppCompatActivity {
    private int mYear, mMonth, mDay;
     private FirebaseDatabase db;
     private DatabaseReference users;
     private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        db = FirebaseDatabase.getInstance();
        users =db.getReference(Common.users_tbl);
        users = db.getReference("Users");

        final EditText t = (EditText) findViewById(R.id.date);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(plus.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                t.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }


        });
        final EditText g = (EditText) findViewById(R.id.cat);
        final Context context = this;
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title

                LayoutInflater inflater = LayoutInflater.from(plus.this);
                View dialogView = inflater.inflate(R.layout.category, null);
                 dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                final EditText editText = (EditText) findViewById(R.id.cat);
                final Button b1 = dialogView.findViewById(R.id.Clothing);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b1.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                final Button b2 = dialogView.findViewById(R.id.Entertainment);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b2.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                final Button b3 = dialogView.findViewById(R.id.Food);
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b3.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                final Button b4 = dialogView.findViewById(R.id.Health);
                b4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b4.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                final Button b5 = dialogView.findViewById(R.id.Fuel);
                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b5.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                final Button b6 = dialogView.findViewById(R.id.Stationary);
                b6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b6.getText().toString());
                        alertDialog.dismiss();
                    }
                });

                final Button b7 = dialogView.findViewById(R.id.Salary);
                b7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(b7.getText().toString());
                        alertDialog.dismiss();
                    }
                });

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
          final   LayoutInflater inflater=this.getLayoutInflater();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final android.app.AlertDialog waitingDialog = new SpotsDialog(plus.this);
                waitingDialog.show();


                final EditText date1=(EditText)findViewById(R.id.date);
                final EditText amount1=(EditText)findViewById(R.id.amount);
                final EditText category1=(EditText)findViewById(R.id.cat);
                final EditText note1=(EditText)findViewById(R.id.note);


                String date = date1.getText().toString();
                String amount = amount1.getText().toString();
                String category=category1.getText().toString();
                String note=note1.getText().toString();
                Map<String,Object> updateInfo =new HashMap<>();
                if(!TextUtils.isEmpty(date))
                    updateInfo.put("date",date);
                if(!TextUtils.isEmpty(amount))
                    updateInfo.put("amount",amount);
                if(!TextUtils.isEmpty(category))
                    updateInfo.put("category",category);
                if(!TextUtils.isEmpty(note))
                    updateInfo.put("note",note);


                DatabaseReference userinformations=FirebaseDatabase.getInstance().getReference().child(Common.expns_tbl).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

               userinformations.updateChildren(updateInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(plus.this," Information Uploaded",Toast.LENGTH_SHORT);
                            waitingDialog.dismiss();
                        }

                        else{
                            Toast.makeText(plus.this,"Information Update Failed!",Toast.LENGTH_SHORT).show();


                        }
                    }
                });
                childEventListener=new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                      plus plus1= dataSnapshot.getValue(plus.class);


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                users.addChildEventListener(childEventListener);












            }




        });
        }






    public void prev(View v) {
        Intent i = new Intent(getApplicationContext(), Welcome.class);
        startActivity(i);
    }

}