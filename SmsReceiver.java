package com.krishnachaitanya.expensetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.krishnachaitanya.expensetracker.utils.SmsListener;

/**
 * Created by chaitanya on 12/5/18.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read

            if (sender.equals("Krishna"))
            {

                String messageBody = smsMessage.getMessageBody();

                //Pass the message text to interface
                mListener.messageReceived(messageBody);

            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
