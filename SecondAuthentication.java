package com.example.fingerprintauthentication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.fingerprintauthentication.R;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;


    public class SecondAuthentication extends AppCompatActivity implements FingerPrintAuthCallback{

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        boolean isDarkModeOn;
        Button HomeButton;

        public String user;

        FingerPrintAuthHelper mFingerPrintAuthHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            sharedPreferences = getSharedPreferences(
                    "sharedPrefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            isDarkModeOn = sharedPreferences
                    .getBoolean("isDarkModeOn", false);
            if (isDarkModeOn) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

            }
            else {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);

            }

            setContentView(R.layout.activity_second_authentication);

            HomeButton = (Button)findViewById(R.id.HomeButton);

            HomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SecondAuthentication.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            // finger auth.
            mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
        }

        @Override
        protected void onResume() {
            super.onResume();
            //start finger print authentication
            mFingerPrintAuthHelper.startAuth();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mFingerPrintAuthHelper.stopAuth();
        }

        public void onClick(View view){
            Intent MainPage = new Intent(this, MainActivity.class);
            startActivity(MainPage);
        }

        @Override
        public void onNoFingerPrintHardwareFound() {
            Toast.makeText(this, "Your device does not have fingerprint sensor.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNoFingerPrintRegistered() {

        }

        @Override
        public void onBelowMarshmallow() {
            Toast.makeText(this, "This device does not support fingerprint verification.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
            Toast.makeText(this, "Fingerprint reading successful.", Toast.LENGTH_SHORT).show();
            Intent Page = new Intent(this, MainActivity1.class);
            startActivity(Page);
        }

        @Override
        public void onAuthFailed(int errorCode, String errorMessage) {
            switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.
                case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                    Toast.makeText(this, "There is no such a fingerprint!", Toast.LENGTH_SHORT).show();
                    break;
                case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                     //This is not recoverable error. Try other options for user authentication. like pin, password.
                    break;
                case AuthErrorCodes.RECOVERABLE_ERROR:
                    //Any recoverable error. Display message to the user.
                    break;
            }
        }
    }

