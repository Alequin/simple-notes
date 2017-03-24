package com.alequinonboard.notes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

public class NotesMainActivity extends AppCompatActivity {

    private NotesDatabase database;

    public static final int IF_UPDATE_REQUEST_CODE = 1;
    public static final int IF_UPDATE_RESULT_CODE = 1;

    private ListView listView;
    private CursorAdapter listAdapter;
    private Cursor notesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new NotesDatabase(this);
        database.open(this);

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
                startActivityForResult(new Intent(this, NewNoteActivity.class), IF_UPDATE_REQUEST_CODE);
                break;

            case R.id.search_icon_action_bar:
                break;

            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildListView(){

        notesCursor = getListCursor();
        listView = (ListView) findViewById(R.id.notes_list_main_activity);
        listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, notesCursor, new String[]{NotesDatabase.TITLE},
                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        listView.setAdapter(listAdapter);
    }

    private void updateListView(){
        notesCursor.close();
        notesCursor = getListCursor();
        listAdapter.swapCursor(notesCursor);
    }

    private Cursor getListCursor(){
        return database.getNotesTableCursor(NotesDatabase.ID, NotesDatabase.TITLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IF_UPDATE_REQUEST_CODE && resultCode == IF_UPDATE_RESULT_CODE){
            updateListView();
        }

    }
}
