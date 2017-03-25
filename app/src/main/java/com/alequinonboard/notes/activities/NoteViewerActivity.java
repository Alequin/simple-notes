package com.alequinonboard.notes.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

public class NoteViewerActivity extends NoteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = NotesDatabase.getInitialisedAndOpenDatabase(this);
        setNoteTitleAndMainTextFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.delete_icon_viewer_activity:
                database.deleteNoteById(getCurrentNoteID());
                setResult(NotesMainActivity.IF_UPDATE_RESULT_CODE);
                finish();
                break;

            case R.id.favourite_icon_new_notes_activity:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNoteTitleAndMainTextFromDatabase(){
        Note note = database.getNoteById(getCurrentNoteID());
        setTitleText(note);
        setMainText(note);
    }

    private void setTitleText(Note note){
        ((TextView) findViewById(R.id.title_viewer_activity)).setText(note.getTitle());
    }

    private void setMainText(Note note){
        ((TextView) findViewById(R.id.main_text_viewer_activity)).setText(note.getMainText());;
    }

    private int getCurrentNoteID(){
        return getIntent().getIntExtra(NotesMainActivity.NOTE_ID_EXTRA, 1);
    }

}
