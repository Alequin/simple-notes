package com.alequinonboard.notes.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.alequinonboard.notes.BooleanMenuItem;
import com.alequinonboard.notes.DoubleTapCheck;
import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.SoftInputVisibilityController;

import java.util.Date;

public class NewNoteActivity extends NoteActivity {

    public static final String ID_OF_NOTE_TO_EDIT = "ID_OF_NOTE_TO_EDIT";
    public static final String IF_EDIT_MODE = "IF_EDIT_MODE";

    private boolean editMode;
    private String noteToEditCreationDate;

    private AlertDialog warnUserOfTitleOverride;
    private AlertDialog warnUserOfBlankBodyDialog;
    private AlertDialog editModeDateChangeDialog;

    private final BooleanMenuItem favouriteMenuIcon = new BooleanMenuItem();

    private final DoubleTapCheck doubleTapCheck = new DoubleTapCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editMode = getIntent().getBooleanExtra(IF_EDIT_MODE, false);

        if(editMode){
            this.prepareActivityAsEditMode();
        }

        this.setUpFavouriteMenuIcon();
        this.applyTagIconListener();
        warnUserOfTitleOverride = this.buildOverrideTitleWarningDialog();
        warnUserOfBlankBodyDialog = this.buildBlankBodyDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_notes, menu);

        favouriteMenuIcon.setMenuItem(menu.getItem(0));
        favouriteMenuIcon.applyCurrentState();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(doubleTapCheck.isSecondTap()){
            return true;
        }

        switch(id){

            case R.id.tick_icon_new_notes_activity:
                this.onPressTickButton();
                break;

            case R.id.favourite_icon_new_notes_activity:
                this.onPressFavouriteButton();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoftInputVisibilityController.hideAndResetSoftInput(this);
    }

    private void onPressTickButton(){
        // main text cannot be empty. If it is ask user for input and return
        if(isBodyEmpty()) {
            warnUserOfBlankBodyDialog.show();
            return;
        }

        if(!editMode) {
            this.addNoteToDatabase();
            this.setResult(NotesMainActivity.UPDATE_RESULT_CODE);
            this.finish();
        }else{
            editModeDateChangeDialog.show();
        }
    }

    private void onPressFavouriteButton(){
        favouriteMenuIcon.setState(!favouriteMenuIcon.isStateTrue());
    }

    private void applyTagIconListener(){
        findViewById(R.id.generate_title_tag_icon_new_note_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressTagIcon();
            }
        });
    }

    private void onPressTagIcon(){
        if(doubleTapCheck.isSecondTap()){
            return;
        }
        if(this.isTitleEmpty()){
            this.setGeneratedTitle();
        }else{
            warnUserOfTitleOverride.show();
        }
    }

    private void setGeneratedTitle(){
        setTitleViewString(Note.getGeneratedTitleFromTimeStamp(new Date()));
    }

    private void setUpFavouriteMenuIcon(){
        favouriteMenuIcon.setIconToUseWhenTrue(ContextCompat.getDrawable(this, R.drawable.fav_green_icon));
        favouriteMenuIcon.setIconToUseWhenFalse(ContextCompat.getDrawable(this, R.drawable.fav_icon));

        if(editMode){
            final Note noteToEdit = database.getNoteById(getIdOfNoteToEdit());
            favouriteMenuIcon.setState(noteToEdit.isFavourite());
        }else{
            favouriteMenuIcon.setState(false);
        }
    }

    private AlertDialog buildOverrideTitleWarningDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.title_override_warning));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setGeneratedTitle();
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        return builder.create();
    }

    private AlertDialog buildBlankBodyDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.blank_main_text_dialog_message));
        return builder.create();
    }

    private AlertDialog buildEditModeDateChangeDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final AlertDialog.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean ifUpdateDate = (i == -1);
                updateNoteInDatabase(ifUpdateDate);
                finish();
            }
        };

        builder.setMessage(getString(R.string.date_change_dialog_message));
        builder.setPositiveButton(getString(R.string.yes), listener);
        builder.setNegativeButton(getString(R.string.no), listener);

        return builder.create();
    }

    private void prepareActivityAsEditMode(){
        editModeDateChangeDialog = buildEditModeDateChangeDialog();
        final Note noteToEdit = database.getNoteById(getIdOfNoteToEdit());
        this.setTitleViewString(noteToEdit.getTitle());
        this.setBodyViewString(noteToEdit.getBody());
        noteToEditCreationDate = noteToEdit.getTimeStamp();

    }

    private String getTitleStringFromView(){
        return ((EditText) findViewById(R.id.title_new_notes_activity)).getText().toString();
    }

    private String getBodyStringFromView(){
        return ((EditText) findViewById(R.id.main_text_new_notes_activity)).getText().toString();
    }

    private boolean isTitleEmpty(){
        return this.getTitleStringFromView().isEmpty();
    }

    private boolean isBodyEmpty(){
        return getBodyStringFromView().isEmpty();
    }

    private void setTitleViewString(String textToShow){
        ((EditText) findViewById(R.id.title_new_notes_activity)).setText(textToShow);
    }

    private void setBodyViewString(String textToShow){
        ((EditText) findViewById(R.id.main_text_new_notes_activity)).setText(textToShow);
    }

    private Note initialiseNewNote(Date creationDate){
       return initialiseNewNote(Note.timeStampFormat.format(creationDate).toString());
    }

    private Note initialiseNewNote(String creationDate){

        final Note newNote = new Note();

        newNote.setTitle(getTitleStringFromView());
        newNote.setBody(getBodyStringFromView());
        newNote.setTimeStamp(creationDate);
        newNote.setFavourite(favouriteMenuIcon.isStateTrue());

        if(newNote.isTitleEmpty()){
            newNote.generateAndSetNewTitle();
        }

        return newNote;
    }

    private Note getNoteWithEditedValues(boolean ifAlterDate){
        Note noteWithUpdatedValues;
        if(ifAlterDate){
            noteWithUpdatedValues = initialiseNewNote(new Date());
        }else{
            noteWithUpdatedValues = initialiseNewNote(noteToEditCreationDate);
        }
        return noteWithUpdatedValues;
    }

    private void addNoteToDatabase(){
        Note newNote = initialiseNewNote(new Date());
        database.insertToNotesTable(newNote);
        if(newNote.isFavourite()){
            database.insertLastAddedNoteToFavouritesTable();
        }
    }

    private void updateNoteInDatabase(boolean ifAlterDate){
        final int noteId = getIdOfNoteToEdit();
        final Note editedNote = this.getNoteWithEditedValues(ifAlterDate);
        database.updateNoteInDatabase(noteId, editedNote);
        if(favouriteMenuIcon.isStateTrue()){
            database.insertIdToFavouritesTable(noteId);
        }else{
            database.removeNoteFromFavouritesById(noteId);
        }
        setResult(NoteViewerActivity.UPDATE_RESULT_CODE);
    }

    private int getIdOfNoteToEdit(){
        return getIntent().getIntExtra(ID_OF_NOTE_TO_EDIT, 0);
    }
}
