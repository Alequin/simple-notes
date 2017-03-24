package com.alequinonboard.notes.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alequinonboard.notes.Note;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.database.NotesDatabase;

public class NoteViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buildNote();
    }

    private void buildNote(){

        TextView title = (TextView) findViewById(R.id.title_note_viewer_activity);
        TextView mainText = (TextView) findViewById(R.id.main_text_note_viewer_activity);
        TextView date = (TextView) findViewById(R.id.date_note_viewer_activity);

        NotesDatabase database = new NotesDatabase(this);
        database.open(this);

        Note note = database.getNoteById(getIntent().getIntExtra(NotesMainActivity.NOTE_ID_EXTRA, 1));

        title.setText(note.getTitle());
        mainText.setText(note.getMainText());
        date.setText(note.getDate());
    }

}
