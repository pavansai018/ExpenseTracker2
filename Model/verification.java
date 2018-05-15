package com.krishnachaitanya.expensetracker.Model;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.krishnachaitanya.expensetracker.MainActivity;
import com.krishnachaitanya.expensetracker.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by chaitanya on 8/5/18.
 */

public class verification extends AppCompatActivity {

private FirebaseAuth mAuth;

private int btnType=0;
    private LinearLayout mPhoneLayout;
    private LinearLayout mCodeLayout;

    private EditText mPhoneText;
    private EditText mCodeText;

    private ProgressBar mPhoneBar;
    private ProgressBar mCodeBar;
    private Button mSendBtn;
    private TextView mErrorText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);

        mPhoneLayout = (LinearLayout) findViewById(R.id.phnlayout);
        mCodeLayout = (LinearLayout) findViewById(R.id.codelayout);

        mPhoneText = (EditText) findViewById(R.id.phonetext);
        mCodeText = (EditText) findViewById(R.id.codetext);

        mPhoneBar = (ProgressBar) findViewById(R.id.phonebar);
        mCodeBar = (ProgressBar) findViewById(R.id.codebar);

        mErrorText=(TextView)findViewById(R.id.errortext) ;
        mSendBtn = (Button) findViewById(R.id.sendbtn);


       mAuth=FirebaseAuth.getInstance();
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnType==0) {

                    mPhoneBar.setVisibility(View.VISIBLE);
                    mPhoneText.setEnabled(false);
                    mSendBtn.setEnabled(false);
                    String PhoneNumber = mPhoneText.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            PhoneNumber,
                            60,
                            TimeUnit.SECONDS,

                            verification.this,
                            mcallbacks
                    );
                }
                else{


                    mSendBtn.setEnabled(false);
                    mCodeBar.setVisibility(View.VISIBLE);

                    String verificationcode = mCodeText.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);

                    signInWithPhoneAuthCredential(credential);
                }


            }
        });
        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mErrorText.setText("there was some error in verification");
                mErrorText.setVisibility(View.VISIBLE);

            }
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                btnType=1;

                mPhoneBar.setVisibility(View.INVISIBLE);
                mCodeLayout.setVisibility(View.VISIBLE);
                mSendBtn.setText("verify code");
                mSendBtn.setEnabled(true);

                // ...
            }


        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            mErrorText.setText("there was some error in loggin in");
                            mErrorText.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mSendBtn.setEnabled(true);
                            }
                        }
                    }
                });
    }

}
