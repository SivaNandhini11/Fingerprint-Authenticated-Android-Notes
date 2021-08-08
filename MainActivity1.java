package com.example.fingerprintauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.example.fingerprintauthentication.AddNotesActivity.DESCRIPTION;
import static com.example.fingerprintauthentication.AddNotesActivity.TITLE;

public class MainActivity1 extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Model> notesList;
    DatabaseClass databaseClass;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isDarkModeOn;
    private static MainActivity1 singleton = null;

    public static MainActivity1 getInstance() {

        if(singleton == null)
        {
            singleton = new MainActivity1();
        }
        return singleton;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = this;
        sharedPreferences = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isDarkModeOn = sharedPreferences
                .getBoolean("isDarkModeOn", false);
        editor.putString(TITLE, "");
        editor.putString(DESCRIPTION, "");
        editor.commit();

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
        setContentView(R.layout.activity_main1);

        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.layout_main);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity1.this, AddNotesActivity.class);
                startActivity(intent);
            }
        });


        notesList = new ArrayList<>();
        databaseClass = new DatabaseClass(this);
        fetchAllNotesFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, MainActivity1.this, notesList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);


    }


    void fetchAllNotesFromDatabase() {
        Cursor cursor = databaseClass.readAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                notesList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Notes Here");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Delete_all_notes) {
            deleteAllNotes();
        }
        if( item.getItemId() == R.id.dark_mode){
            if (isDarkModeOn) {
                Toast.makeText(getApplicationContext(),"Disabling Dark theme", Toast.LENGTH_SHORT).show();

                // if dark mode is on it
                // will turn it off
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                // it will set isDarkModeOn
                // boolean to false
                editor.putBoolean(
                        "isDarkModeOn", false);
                editor.apply();
                isDarkModeOn = false;

            }
            else {
                Toast.makeText(getApplicationContext(),"Enabling Dark theme", Toast.LENGTH_SHORT).show();

                // if dark mode is off
                // it will turn it on
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

                // it will set isDarkModeOn
                // boolean to true
                editor.putBoolean(
                        "isDarkModeOn", true);
                editor.apply();
                isDarkModeOn = true;


            }
            finish();
            startActivity(new Intent(MainActivity1.this, MainActivity1.this.getClass()));
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        DatabaseClass db = new DatabaseClass(MainActivity1.this);
        db.deleteAllNotes();
        recreate();
    }


    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            final Model item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)) {
                                DatabaseClass db = new DatabaseClass(MainActivity1.this);
                                db.deleteSingleItem(item.getId());
                            }


                        }
                    });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };

}
