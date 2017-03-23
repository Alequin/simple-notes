package com.alequinonboard.notes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alequinonboard.notes.Note;

/**
 * Created by Alequin on 23/03/2017.
 */

public class NotesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notes_database";
    public static final int DATABASE_VERSION = 1;

    private static SQLiteDatabase database;

    public static final String NOTES_TABLE_TITLE = "notes";
    public static final String NOTES_ID = "notes_id";
    public static final String TITLE = "notes_title";
    public static final String MAIN_TEXT = "main_text";
    public static final String DATE = "date";

    public static final String FAVOURITES_TABLE_TITLE = "favourites";
    public static final String FAVOURITES_ID = "favourites_id";

    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(String.format(
                "CREATE TABLE %s(" +
                "%s INTEGER NOT NULL AUTOINCREMENT, " +
                "%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL," +
                "PRIMARY KEY (%s)",
                NOTES_TABLE_TITLE, NOTES_ID, TITLE, MAIN_TEXT, DATE,
                NOTES_ID
        ));

        database.execSQL(String.format(
                "CREATE TABLE %s(" +
                "%s INTEGER NOT NULL AUTOINCREMENT, " +
                "%s INTEGER NOT NULL, " +
                "PRIMARY KEY (%s)",
                FAVOURITES_TABLE_TITLE, FAVOURITES_ID, NOTES_ID,
                FAVOURITES_ID
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void open(Context currentContext){
        database = new NotesDatabase(currentContext).getWritableDatabase();
    }

    public static void end(){
        database.close();
        database = null;
    }

    public static void insertNoteToDatabase(Note newNote){

        database.execSQL(String.format(
                "INSERT INTO %s (%s, %s, %s) VALUES (%s, %s, %s)",
                NOTES_TABLE_TITLE,
                TITLE, MAIN_TEXT, DATE,
                newNote.getTitle(), newNote.getMainText(), newNote.getDate()
        ));

        // is note is marked as favourite add id to favourites table
        if(newNote.isFavourite()){

            final String getIdQuery = String.format(
                    "SELECT %s FROM %s WHERE %s = %s ORDER BY %s DESC LIMIT 1",
                    NOTES_ID, NOTES_TABLE_TITLE, TITLE, newNote.getTitle(),
                    NOTES_ID
            );

            database.execSQL(String.format(
                    "INSERT INTO %s (%s) VALUES ((%s))",
                    FAVOURITES_TABLE_TITLE,
                    FAVOURITES_ID,
                    getIdQuery
            ));
        }
    }
}
