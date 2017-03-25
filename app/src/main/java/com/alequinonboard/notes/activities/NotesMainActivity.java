package com.alequinonboard.notes.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

public class NotesMainActivity extends NoteActivity {

    private static Context CURRENT_CONTEXT;


    public static final int UPDATE_REQUEST_CODE = 1;
    public static final int UPDATE_RESULT_CODE = 1;
    public static final String NOTE_ID_EXTRA = "NOTE_ID_EXTRA";

    private ListView listView;
    private CursorAdapter listAdapter;
    private Cursor listCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CURRENT_CONTEXT = this;

        database = this.initialisedAndOpenDatabaseIfRequired();
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
                startActivityForResult(new Intent(this, NewNoteActivity.class), UPDATE_REQUEST_CODE);
                break;

            case R.id.search_icon_action_bar:
                break;

            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildListView(){

        listCursor = getListCursor();
        listView = (ListView) findViewById(R.id.notes_list_main_activity);
        listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, listCursor, new String[]{NotesDatabase.TITLE},
                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(getListListener());
    }

    private void updateListView(){
        listCursor.close();
        listCursor = getListCursor();
        listAdapter.swapCursor(listCursor);
    }

    private Cursor getListCursor(){
        return database.getNotesTableCursor(NotesDatabase.ID, NotesDatabase.TITLE);
    }

    private AdapterView.OnItemClickListener getListListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent startNoteViewer = new Intent(CURRENT_CONTEXT, NoteViewerActivity.class);

                listCursor.moveToPosition(position);
                int idColumnIndex = listCursor.getColumnIndex(NotesDatabase.ID);
                startNoteViewer.putExtra(NOTE_ID_EXTRA, listCursor.getInt(idColumnIndex));

                startActivityForResult(startNoteViewer, UPDATE_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == UPDATE_REQUEST_CODE && resultCode == UPDATE_RESULT_CODE){
            updateListView();
        }

    }
}
