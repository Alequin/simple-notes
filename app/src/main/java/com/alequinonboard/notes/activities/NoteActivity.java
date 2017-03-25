package com.alequinonboard.notes.activities;

import android.support.v7.app.AppCompatActivity;

import com.alequinonboard.notes.database.NotesDatabase;

/**
 * Created by Alequin on 25/03/2017.
 */

public class NoteActivity extends AppCompatActivity{

    protected NotesDatabase database;

    protected void initialiseAndOpenDatabase(){
        database = new NotesDatabase(this);
        database.open(this);
    }

}
