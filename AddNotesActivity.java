package com.example.fingerprintauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AddNotesActivity extends AppCompatActivity {

    EditText title, description;
    Button addNote, titleMic, desscriptionMic;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isDarkModeOn;
    final public static String TYPE = "TYPE";
    final public static String TITLE = "TITLE";
    final public static String DESCRIPTION = "DESCRIPTION";
    String type,data;

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

        setContentView(R.layout.activity_add_notes);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        addNote = findViewById(R.id.addNote);
        titleMic = findViewById(R.id.TitleMic);
        desscriptionMic = findViewById(R.id.descriptionMic);

            String data1 = sharedPreferences.getString(TITLE, "");
            String data2 = sharedPreferences.getString(DESCRIPTION, "");
            title.setText(data1);
            description.setText(data2);

        titleMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNotesActivity.this,SpeechToText.class);
                intent.putExtra(TYPE, TITLE);
                startActivity(intent);
                finish();

            }
        });
        desscriptionMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNotesActivity.this,SpeechToText.class);
                intent.putExtra(TYPE, DESCRIPTION);
                startActivity(intent);
                finish();
                }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {
                    DatabaseClass db = new DatabaseClass(AddNotesActivity.this);
                    db.addNotes(title.getText().toString(), description.getText().toString());

                    Intent intent = new Intent(AddNotesActivity.this,MainActivity1.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(AddNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}