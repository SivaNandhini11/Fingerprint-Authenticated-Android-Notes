package com.example.fingerprintauthentication;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

    public class SignUpActivity extends AppCompatActivity {
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

            setContentView(R.layout.activity_sign_up);
        }

        public void GoBackonClick(View view){
            Intent MainPage = new Intent(this, MainActivity.class);
            startActivity(MainPage);
        }

        public void RegisteronClick(View view){
            EditText username = (EditText)findViewById(R.id.usernameRegister);
            EditText password = (EditText)findViewById(R.id.passwordRegister);
            EditText password2 = (EditText)findViewById(R.id.Password2Register);

            String EnteredUsername = username.getText().toString();
            String EnteredPassword = password.getText().toString();
            String EnteredPassword2 = password2.getText().toString();

            if((EnteredUsername.contentEquals("")) && (EnteredPassword.contentEquals(""))){
                Toast error_message = Toast.makeText(getApplicationContext(), "Username and Password cannot be empty !", Toast.LENGTH_SHORT);
                error_message.setGravity(Gravity.CENTER, 0, 0);
                error_message.show();
            }
            else if(EnteredUsername.contentEquals("")){
                Toast error_message = Toast.makeText(getApplicationContext(), "Enter username.", Toast.LENGTH_SHORT);
                error_message.setGravity(Gravity.CENTER, 0, 0);
                error_message.show();
            }
            else if(EnteredPassword.contentEquals("")){
                Toast error_message = Toast.makeText(getApplicationContext(), "Enter password.", Toast.LENGTH_SHORT);
                error_message.setGravity(Gravity.CENTER, 0, 0);
                error_message.show();
            }
            else if(EnteredPassword2.contentEquals("")){
                Toast error_message = Toast.makeText(getApplicationContext(), "Enter password again.", Toast.LENGTH_SHORT);
                error_message.setGravity(Gravity.CENTER, 0, 0);
                error_message.show();
            }
            else if(EnteredPassword.contentEquals(EnteredPassword2)){
                writeToFile(EnteredUsername, EnteredPassword, this);

                Toast succeed_message = Toast.makeText(getApplicationContext(), "You are successfully registered", Toast.LENGTH_SHORT);
                succeed_message.setGravity(Gravity.CENTER, 0, 0);
                succeed_message.show();

                Intent MainPage = new Intent(this, MainActivity.class);
                startActivity(MainPage);
            }
            else
            {
                Toast error_message = Toast.makeText(getApplicationContext(), "Passwords didn't matched.", Toast.LENGTH_SHORT);
                error_message.setGravity(Gravity.CENTER, 0, 0);
                error_message.show();
            }
        }

        private void writeToFile(String username, String password, Context context){

            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        this.openFileOutput("userInformation.txt", MODE_APPEND), "utf-8"));

                writer.write(String.format("%s|%s\n", username, EncryptionHelper.encryptPassword(password)));
                writer.close();
            }
            catch (IOException e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

