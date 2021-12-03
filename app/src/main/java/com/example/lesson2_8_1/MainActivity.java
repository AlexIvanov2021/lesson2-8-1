package com.example.lesson2_8_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_CODE_PHONE_STATE = 1;
    private static final String LOG_TAG = "AndroidExamle";
    private EditText editTextPhoneNumber;
    private Button buttonGetPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editTextPhoneNumber = findViewById(R.id.editTextInfo);
        this.buttonGetPhone = findViewById(R.id.buttonInfo);

        this.buttonGetPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissionAndGetPhoneNumbers();


            }
        });
    }

    private void askPermissionAndGetPhoneNumbers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readPhoneStatePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_CODE_PHONE_STATE);
                return;
            }
        }
        this.getPhoneNumbers();
    }

    @SuppressLint("MissingPermission")
    private void getPhoneNumbers() {
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber1 = manager.getLine1Number();

            this.editTextPhoneNumber.setText(phoneNumber1);

            Log.i(LOG_TAG, "You phone numbers:" + phoneNumber1);
            Toast.makeText(this, "You phone numbers:" + phoneNumber1, Toast.LENGTH_LONG).show();

            if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.O){
                String imei = manager.getImei();
              int phoneCount = manager.getPhoneCount();

                Log.i(LOG_TAG, "Phone count:" + phoneCount);
                Log.i(LOG_TAG, "IMEI:" + imei);
            }
            if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.P) {
                SignalStrength signalStrength = manager.getSignalStrength();
                int level = signalStrength.getLevel();
                Log.i(LOG_TAG, "Signal Strength level:" + level);
            }

        } catch (Exception  ex) {
            Log.e(LOG_TAG, "erorr" + ex);
            Toast.makeText(this, "erorr" + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();

        }
    }




    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grandResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResult);
        switch (requestCode) {
            case MY_PERMISSION_CODE_PHONE_STATE:
                if (grandResult.length > 0 && grandResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "permission grand");
                    Toast.makeText(this, "permission grande ", Toast.LENGTH_LONG).show();
                    this.getPhoneNumbers();
                } else {
                    Log.i(LOG_TAG, "permission denied");
                    Toast.makeText(this, "permission denied ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSION_CODE_PHONE_STATE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "action ok", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "action cancelled", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "action failed", Toast.LENGTH_LONG).show();

            }
        }
    }
}

