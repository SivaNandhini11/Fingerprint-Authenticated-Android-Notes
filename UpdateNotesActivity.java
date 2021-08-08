package com.example.fingerprintauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class UpdateNotesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isDarkModeOn;

    EditText title,description;
    Button updateNotes;
    String id;
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

        setContentView(R.layout.activity_update_notes);

        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        updateNotes=findViewById(R.id.updateNote);

        Intent i =getIntent();
        title.setText(i.getStringExtra("title"));
        description.setText(i.getStringExtra("description"));
        id=i.getStringExtra("id");

        updateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()))
                {

                    DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                    db.updateNotes(title.getText().toString(),description.getText().toString(),id);

                    Intent i=new Intent(UpdateNotesActivity.this,MainActivity1.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();


                }
                else
                {
                    Toast.makeText(UpdateNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}