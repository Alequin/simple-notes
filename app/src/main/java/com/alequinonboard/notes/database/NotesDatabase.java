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

    public static final String ID = "_id";

    public static final String NOTES_TABLE_TITLE = "notes";
    public static final String TITLE = "notes_title";
    public static final String MAIN_TEXT = "main_text";
    public static final String DATE = "date";

    public static final String FAVOURITES_TABLE_TITLE = "favourites";
    public static final String NOTES_ID = "notes_id";

    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(String.format(
                "CREATE TABLE %s(" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s TEXT, %s TEXT);",
                NOTES_TABLE_TITLE, ID, TITLE, MAIN_TEXT, DATE
        ));

        database.execSQL(String.format(
                "CREATE TABLE %s(" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s INTEGER);",
                FAVOURITES_TABLE_TITLE, ID, NOTES_ID
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void open(Context currentContext){
        database = new NotesDatabase(currentContext).getWritableDatabase();
    }

    public boolean isOpen(){
        return database.isOpen();
    }

    public void end(){
        database.close();
        database = null;
    }

    public Cursor getNotesTableCursor(String...columns){

        String columnsToSelect = "";
        int length = columns.length;
        for(int a=0; a<length; a++){
            //if the loop is on the final iteration don't add a comma on the end
            if(a != length-1){
                columnsToSelect += columns[a] + ",";
            }else{
                columnsToSelect += columns[a];
            }
        }

        Cursor cursor = database.rawQuery(String.format(
                "SELECT %s FROM %s;", columnsToSelect, NOTES_TABLE_TITLE
        ),null);
        cursor.moveToFirst();

        return cursor;
    }

    public Note getNoteById(int id){

        Cursor cursor = database.rawQuery(String.format(
                "SELECT %s, %s, %s FROM %s WHERE %s = %s",
                TITLE, MAIN_TEXT, DATE, NOTES_TABLE_TITLE, ID, id
        ),null);
        cursor.moveToFirst();

        Note note = new Note();
        note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        note.setMainText(cursor.getString(cursor.getColumnIndex(MAIN_TEXT)));
        note.setDate(cursor.getString(cursor.getColumnIndex(DATE)));

        return note;
    }

    public void deleteNoteById(int id){

        //delete note by id
        //query favourites table for id, if found delete

        database.execSQL(String.format(
                "DELETE FROM %s WHERE %s = %s", NOTES_TABLE_TITLE, ID, id
        ));

        database.execSQL(String.format(
                "DELETE FROM %s WHERE %s = %s", FAVOURITES_TABLE_TITLE, NOTES_ID, id
        ));

    }

    public void insertNoteToDatabase(Note newNote){

        database.execSQL(String.format(
                "INSERT INTO %s (%s, %s, %s) VALUES ('%s', '%s', '%s');",
                NOTES_TABLE_TITLE,
                TITLE, MAIN_TEXT, DATE,
                newNote.getTitle(), newNote.getMainText(), newNote.getDate()
        ));

        // is note is marked as favourite add id to favourites table
        if(newNote.isFavourite()){

            final String getIdQuery = String.format(
                    "SELECT %s FROM %s WHERE %s = %s ORDER BY %s DESC LIMIT 1;",
                    ID, NOTES_TABLE_TITLE, TITLE, newNote.getTitle(),
                    ID
            );

            database.execSQL(String.format(
                    "INSERT INTO %s (%s) VALUES ((%s));",
                    FAVOURITES_TABLE_TITLE,
                    ID,
                    getIdQuery
            ));
        }
    }
}
