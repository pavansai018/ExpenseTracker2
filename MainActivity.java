package com.krishnachaitanya.expensetracker;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishnachaitanya.expensetracker.Common.Common;
import com.krishnachaitanya.expensetracker.Model.User;
import com.krishnachaitanya.expensetracker.Model.verification;
import com.krishnachaitanya.expensetracker.utils.SmsListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {

    RelativeLayout rootLayout;
    Button btnSignIn2,btnRegister1;
    FirebaseDatabase db;
   FirebaseAuth auth;
   DatabaseReference users;
   TextView txt_forgot_pwd;
    public static final String OTP_REGEX = "[0-9]{1,6}";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users =db.getReference(Common.users_tbl);
        users = db.getReference("Users");
        btnRegister1 = (Button) findViewById(R.id.btnRegister);
        btnSignIn2 = (Button) findViewById(R.id.btnSignIn);
        rootLayout =(RelativeLayout)findViewById(R.id.rootLayout);
        txt_forgot_pwd=(TextView)findViewById(R.id.txt_forgot_password) ;
        txt_forgot_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialogForgotpwd();
                return false;
            }
        });
//event
        btnRegister1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showRegisterDialog();


            }
        });
  btnSignIn2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          showLoginDialog();
          onStart();
      }
  });
        SmsReceiver.bindListener( new SmsListener() {

            public void messageReceived(String messageText) {

                //From the received text string you may do string operations to get the required OTP
                //It depends on your SMS format
                Log.e("Message",messageText);
                Toast.makeText(MainActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                // If your OTP is six digits number, you may use the below code

                Pattern pattern = Pattern.compile(OTP_REGEX);
                Matcher matcher = pattern.matcher(messageText);
                String otp = null;
                while (matcher.find())
                {
                    otp = matcher.group();
                }

                Toast.makeText(MainActivity.this,"OTP: "+ otp ,Toast.LENGTH_LONG).show();

            }
        });
    }



    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();

    if(currentUser==null){
        Intent authIntent= new Intent(MainActivity.this,verification.class);
        startActivity(authIntent);

    }

    }

    private void showDialogForgotpwd() {


                // If your OTP is six digits number
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View forgot_pwd_layout=inflater.inflate(R.layout.layout_forgot_password,null);
        final EditText edtEmail =(EditText)forgot_pwd_layout.findViewById(R.id.edtEmail);

        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("FORGOT PASSWORD")
                .customView(forgot_pwd_layout, wrapInScrollView)
                .positiveText("SEND EMAIL")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                        final AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                        waitingDialog.show();
                        auth.sendPasswordResetEmail(edtEmail.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();

                                        waitingDialog.dismiss();
                                        Snackbar.make(rootLayout,"Reset Password Link has been sent",Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                waitingDialog.dismiss();
                                Snackbar.make(rootLayout,""+e.getMessage(),Snackbar.LENGTH_LONG)
                                        .show();

                            }
                        });
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText("CANCEL")
                .build()
                .show();

    }

    private void showLoginDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);
        final EditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final EditText edtPassword = login_layout.findViewById(R.id.edtPassword);
        final AlertDialog.Builder dialog =new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("SIGN IN")
                .customView(login_layout, wrapInScrollView)
                .positiveText("SIGN IN")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        btnSignIn2.setEnabled(false);
                        //check validation

                        if(TextUtils.isEmpty(edtEmail.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please enter email address",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }
       /*         if(TextUtils.isEmpty(edtPhone.getText().toString()))
                {

                    Snackbar.make(rootLayout,"Please enter Phone number",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                } */
                        if(TextUtils.isEmpty(edtPassword.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please enter Password",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }
                        if(edtPassword.getText().toString().length()<8)
                        {

                            Snackbar.make(rootLayout,"Password must contain atleast 8 characters",Snackbar.LENGTH_SHORT)

                                    .show();
                            return;

                        }
                        if((edtPassword.getText().toString().charAt(0))==' ')
                        {

                            Snackbar.make(rootLayout,"Password must not contain space at beginning",Snackbar.LENGTH_SHORT)
                                    .show();
                            btnSignIn2.setEnabled(true);


                            return;


                        }
                        if((edtPassword.getText().toString().charAt(edtPassword.getText().toString().length()-1))==' ')
                        {

                            Snackbar.make(rootLayout,"Password must not contain space at end",Snackbar.LENGTH_SHORT)

                                    .show();
                            btnSignIn2.setEnabled(true);

                            return;

                        }
                        for(int i=0;i<edtPassword.getText().toString().length();++i){
                            if(edtPassword.getText().toString().charAt(i)==' ')
                            {
                                Snackbar.make(rootLayout,"Password must not contain space in between",Snackbar.LENGTH_SHORT)

                                        .show();
                                btnSignIn2.setEnabled(true);

                                return;

                            }
                        }


//login
                        final AlertDialog waitingDialog =new SpotsDialog(MainActivity.this);
                        waitingDialog.show();

                        auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString() )
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        waitingDialog.dismiss();
                                        FirebaseDatabase.getInstance().getReference(Common.users_tbl)
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        //after assigned value
                                                        Common.currentUser = dataSnapshot.getValue(User.class);
                                                        //start new activity
                                                        startActivity(new Intent(MainActivity.this,Welcome.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });



                                        //set disable button Sign In if is processing

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                        .show();

                                //active Button
                                btnSignIn2.setEnabled(true);

                            }
                        });
                    }
                })
                .negativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showRegisterDialog(){
        final AlertDialog.Builder dialog =new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);
        final EditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final  EditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final EditText edtRePassword = register_layout.findViewById(R.id.edtRepassword);
        final EditText edtName = register_layout.findViewById(R.id.edtName);
        final EditText edtPhone = register_layout.findViewById(R.id.edtPhone);

        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("SIGN UP")
                .customView(register_layout, wrapInScrollView)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(TextUtils.isEmpty(edtEmail.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please enter email address",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }
                        if(TextUtils.isEmpty(edtPhone.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please enter Phone number",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }
                        if(TextUtils.isEmpty(edtPassword.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please enter Password",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }
                        if(TextUtils.isEmpty(edtRePassword.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please re enter password",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }
                        if(TextUtils.isEmpty(edtName.getText().toString()))
                        {

                            Snackbar.make(rootLayout,"Please enter Name",Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }

                        if(edtPassword.getText().toString().length()<8)
                        {

                            Snackbar.make(rootLayout,"Password must contain atleast 8 characters",Snackbar.LENGTH_SHORT)

                                    .show();
                            return;

                        }
                        if((edtPassword.getText().toString().charAt(0))==' ')
                        {

                            Snackbar.make(rootLayout,"Password must not contain space at beginning",Snackbar.LENGTH_SHORT)

                                    .show();
                            btnRegister1.setEnabled(true);

                            return;

                        }
                        if((edtPassword.getText().toString().charAt(edtPassword.getText().toString().length()-1))==' ')
                        {

                            Snackbar.make(rootLayout,"Password must not contain space at end",Snackbar.LENGTH_SHORT)

                                    .show();
                            btnRegister1.setEnabled(true);

                            return;

                        }
                        for(int i=0;i<edtPassword.getText().toString().length();++i){
                            if(edtPassword.getText().toString().charAt(i)==' ')
                            {
                                Snackbar.make(rootLayout,"Password must not contain space in between",Snackbar.LENGTH_SHORT)

                                        .show();
                                btnRegister1.setEnabled(true);

                                return;

                            }
                        }



                        //Register new User
                        auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        //save user to db
                                        User user=new User();
                                        user.setEmail(edtEmail.getText().toString());
                                        user.setName(edtName.getText().toString());
                                        user.setPhone(edtPhone.getText().toString());
                                        user.setPassword(edtPassword.getText().toString());
                                        user.setRepassword(edtRePassword.getText().toString());
// use email to key

                                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Snackbar.make(rootLayout,"Register Success Fully !!",Snackbar.LENGTH_SHORT)
                                                                .show();
                                                    } })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                });


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                });

                    }
                })
                .positiveText("REGISTER")
                .negativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();

    }
}


