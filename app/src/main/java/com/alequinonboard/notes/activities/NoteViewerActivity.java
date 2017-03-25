package com.alequinonboard.notes.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;

public class NoteViewerActivity extends NoteActivity {

    public static final int UPDATE_REQUEST_CODE = 1;
    public static final int UPDATE_RESULT_CODE = 2;

    private Note noteToShow;

    private AlertDialog dateCreatedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = this.initialisedAndOpenDatabaseIfRequired();
        noteToShow = database.getNoteById(getCurrentNoteID());

        displayNoteTitleAndMainText();
        dateCreatedDialog = getDateCreatedDialog();
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

            case R.id.edit_icon_viewer_activity:
                openNewNoteActivityInEditMode();
                break;

            case R.id.delete_icon_viewer_activity:
                database.deleteNoteById(getCurrentNoteID());
                setResult(NotesMainActivity.UPDATE_RESULT_CODE);
                finish();
                break;

            case R.id.date_created_viewer_activity:
                dateCreatedDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == UPDATE_REQUEST_CODE && resultCode == UPDATE_RESULT_CODE){
            updateNote();
        }
    }

    private void updateNote() {
        noteToShow = database.getNoteById(getCurrentNoteID());
        displayNoteTitleAndMainText();
        //result is set as if update is called the note must have changed. The list view must also
        //change on return
        setResult(NotesMainActivity.UPDATE_RESULT_CODE);
    }

    private void displayNoteTitleAndMainText(){
        setTitleText(noteToShow);
        setMainText(noteToShow);
    }

    private AlertDialog getDateCreatedDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.date_created_dialog_title));
        builder.setMessage(noteToShow.getDate());

        return builder.create();
    }

    private void openNewNoteActivityInEditMode() {

        Intent startNewNoteActivity = new Intent(this, NewNoteActivity.class);
        startNewNoteActivity.putExtra(NewNoteActivity.ID_OF_NOTE_TO_EDIT, getCurrentNoteID());
        startNewNoteActivity.putExtra(NewNoteActivity.IF_EDIT_MODE, true);

        startActivityForResult(startNewNoteActivity, UPDATE_REQUEST_CODE);

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
