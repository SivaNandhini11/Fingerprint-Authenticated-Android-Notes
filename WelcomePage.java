package com.example.fingerprintauthentication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;


public class WelcomePage extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isDarkModeOn;

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

            setContentView(R.layout.activity_welcome_page);

            Bundle ComingData = getIntent().getExtras();
            if(ComingData == null){
                return;
            }

            String logged_user = ComingData.getString("WhoAmI");
            TextView username = (TextView)findViewById(R.id.textView5);
            username.setText("Welcome " + logged_user.toUpperCase() + " to our application.");
        }
    }

