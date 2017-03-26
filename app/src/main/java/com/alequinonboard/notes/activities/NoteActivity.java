package com.alequinonboard.notes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alequinonboard.notes.database.NotesDatabase;

/**
 * Created by Alequin on 25/03/2017.
 */

public class NoteActivity extends AppCompatActivity{

    protected NotesDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = NotesDatabase.getDatabaseAndInitialisedIfRequired(this);
    }
}
