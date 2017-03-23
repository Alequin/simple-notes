package com.alequinonboard.notes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "%s INTEGER NOT NULL AUTO_INCREMENT, " +
                "%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL",
                NOTES_TABLE_TITLE, NOTES_ID, TITLE, MAIN_TEXT, DATE
        ));

        database.execSQL(String.format(
                "CREATE TABLE %s(" +
                "%s INTEGER NOT NULL AUTO_INCREMENT, " +
                "%s INTEGER NOT NULL AUTO_INCREMENT",
                FAVOURITES_TABLE_TITLE, FAVOURITES_ID, NOTES_ID
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static SQLiteDatabase getDatabase(Context currentContext){
        if(database == null || !database.isOpen()){
            openDatabase(currentContext);
        }
        return database;
    }

    private static void openDatabase(Context currentContext){
        database = new NotesDatabase(currentContext).getWritableDatabase();
    }


}
