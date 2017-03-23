package com.alequinonboard.notes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

public class NotesMainActivity extends AppCompatActivity {

    private NotesDatabase notesDatabase;

    private ListView listView;
    private CursorAdapter listAdapter;
    private Cursor notesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notesDatabase = new NotesDatabase(this);
        notesDatabase.open(this);

        this.buildListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.add_icon_action_bar:
                startActivity(new Intent(this, NewNoteActivity.class));
                break;

            case R.id.search_icon_action_bar:
                break;

            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildListView(){

        notesTable = notesDatabase.getNotesTableCursor(NotesDatabase.ID, NotesDatabase.TITLE);
        listView = (ListView) findViewById(R.id.notes_list_main_activity);
        listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, notesTable, new String[]{NotesDatabase.TITLE},
                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        listView.setAdapter(listAdapter);
    }

    private void updateListView(){

    }
}
