package com.alequinonboard.notes.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

import java.util.Date;

public class NewNoteActivity extends NoteActivity {

    public static final String ID_OF_NOTE_TO_EDIT = "ID_OF_NOTE_TO_EDIT";
    public static final String IF_EDIT_MODE = "IF_EDIT_MODE";

    private boolean editMode;
    private Note noteToEdit;

    private AlertDialog editModeDateChangeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editMode = getIntent().getBooleanExtra(IF_EDIT_MODE, false);

        if(editMode){
            editModeDateChangeDialog = buildEditModeDateChangeDialog();
            database = initialisedAndOpenDatabaseIfRequired();
            noteToEdit = database.getNoteById(getIdOfNoteToEdit());
            setTitleViewText(noteToEdit.getTitle());
            setMainTextViewTest(noteToEdit.getMainText());
        }
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

            case R.id.tick_icon_new_notes_activity:
                database = initialisedAndOpenDatabaseIfRequired();
                if(!editMode) {
                    saveNoteToDatabase();
                    setResult(NotesMainActivity.UPDATE_RESULT_CODE);
                    finish();
                }else{
                    editModeDateChangeDialog.show();
                }
                break;

            case R.id.favourite_icon_new_notes_activity:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog buildEditModeDateChangeDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final AlertDialog.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == -1){
                    noteToEdit.setDate(new Date());
                }
                updateNoteInDatabase();
                setResult(NoteViewerActivity.UPDATE_RESULT_CODE);
                finish();
            }
        };

        builder.setMessage(getString(R.string.date_change_dialog_message));
        builder.setPositiveButton(getString(R.string.yes), listener);
        builder.setNegativeButton(getString(R.string.no), listener);

        return builder.create();
    }

    private void saveNoteToDatabase(){
        database.insertNoteToDatabase(initialiseNewNote());
    }

    private void updateNoteInDatabase(){
        noteToEdit.setTitle(getTitleTextFromView());
        noteToEdit.setMainText(getMainTextFromView());
        database.updateNoteInDatabase(getIdOfNoteToEdit(), noteToEdit);
    }

    private Note initialiseNewNote(){

        final Note newNote = new Note();

        newNote.setTitle(getTitleTextFromView());
        newNote.setMainText(getMainTextFromView());
        newNote.setDate(new Date());

        return newNote;
    }

    private String getTitleTextFromView(){
        return ((EditText) findViewById(R.id.title_new_notes_activity)).getText().toString();
    }

    private String getMainTextFromView(){
        return ((EditText) findViewById(R.id.main_text_new_notes_activity)).getText().toString();
    }

    private void setTitleViewText(String textToShow){
        ((EditText) findViewById(R.id.title_new_notes_activity)).setText(textToShow);
    }

    private void setMainTextViewTest(String textToShow){
        ((EditText) findViewById(R.id.main_text_new_notes_activity)).setText(textToShow);
    }

    private int getIdOfNoteToEdit(){
        return getIntent().getIntExtra(ID_OF_NOTE_TO_EDIT, 0);
    }

}
