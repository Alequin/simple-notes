package com.alequinonboard.notes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {

    private NotesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new NotesDatabase(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_notes, menu);
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
                saveNote();
                break;

            case R.id.favourite_icon_new_notes_activity:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveNote(){

        if(!database.isOpen()) {
            database.open(this);
        }

        final EditText titleView = (EditText) findViewById(R.id.title_new_notes_activity);
        final EditText mainTextView = (EditText) findViewById(R.id.main_text_new_notes_activity);

        final Note newNote = new Note();

        newNote.setTitle(titleView.getText().toString());
        newNote.setMainText(mainTextView.getText().toString());
        newNote.setDate(new Date());

        database.insertNoteToDatabase(newNote);

        setResult(NotesMainActivity.IF_UPDATE_RESULT_CODE);
        finish();
    }



}
