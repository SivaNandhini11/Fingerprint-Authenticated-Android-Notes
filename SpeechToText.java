package com.example.fingerprintauthentication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.fingerprintauthentication.AddNotesActivity.DESCRIPTION;
import static com.example.fingerprintauthentication.AddNotesActivity.TITLE;
import static com.example.fingerprintauthentication.AddNotesActivity.TYPE;


public class SpeechToText extends AppCompatActivity
{
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    Button submitData;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String type = "";
    String title = "", description= "";

    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechtotext);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        submitData = (Button)findViewById(R.id.submitData);
        btnSpeak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                promptSpeechInput();
            }
        });
        type = getIntent().getStringExtra(TYPE);
        sharedPreferences = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        type = getIntent().getStringExtra(TYPE);
        if(type!=null){
            txtSpeechInput.setText("Enter "+ type.toLowerCase());
        }
        submitData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SpeechToText.this,AddNotesActivity.class);
                if(type.toUpperCase().equals(TITLE))
                    editor.putString(TITLE, txtSpeechInput.getText().toString());
                if(type.toUpperCase().equals(DESCRIPTION))
                    editor.putString(DESCRIPTION, txtSpeechInput.getText().toString());

                editor.commit();
                startActivity(intent);
                finish();

            }
        });


    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
            {
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(TAG, result.get(0));

                    txtSpeechInput.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }
}