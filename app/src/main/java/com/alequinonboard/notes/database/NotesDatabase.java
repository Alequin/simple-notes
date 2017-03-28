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

    private static NotesDatabase database;
    private SQLiteDatabase accessDatabase;

    private int totalNotesMade;

    public static final String ID = "_id";

    public static final String NOTES_TABLE_TITLE = "notes";
    public static final String TITLE = "notes_title";
    public static final String MAIN_TEXT = "main_text";
    public static final String DATE = "date";

    private static final String FAVOURITES_TABLE_TITLE = "favourites";
    private static final String NOTES_ID = "notes_id";

    private static final String NOTE_COUNTER_TABLE_TITLE = "note_counter";
    private static final String NUMBER_OF_NOTES = "note_counter";

    private NotesDatabase(Context context) {
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
                "%s INTEGER, " +
                "CONSTRAINT unique_constraint UNIQUE (%s), " +
                "FOREIGN KEY (%s) " +
                "REFERENCES %s (%s));",
                FAVOURITES_TABLE_TITLE, ID, NOTES_ID, NOTES_ID, NOTES_ID, NOTES_TABLE_TITLE, ID
        ));

        database.execSQL(String.format(
                "CREATE TABLE %s (" +
                "%s INTEGER);",
                NOTE_COUNTER_TABLE_TITLE, NUMBER_OF_NOTES
        ));

        database.execSQL(String.format(
                "INSERT INTO %s VALUES (%s)", NOTE_COUNTER_TABLE_TITLE, 0
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static NotesDatabase getDatabaseAndInitialisedIfRequired(Context currentContext){
        return (database == null || !database.isOpen()) ?
                NotesDatabase.getInitialisedAndOpenedDatabase(currentContext) : database;
    }

    public static NotesDatabase getInitialisedAndOpenedDatabase(Context currentContext){
        database = new NotesDatabase(currentContext);
        database.open(currentContext);
        return database;
    }

    public void open(Context currentContext){
        accessDatabase = new NotesDatabase(currentContext).getWritableDatabase();
    }

    public boolean isOpen(){
        return accessDatabase.isOpen();
    }

    public void closeDatabase(){
        accessDatabase.close();
        accessDatabase = null;
    }

    public Cursor getNotesTableQueryByTitle(String searchTerm){

        Cursor cursor;
        final String baseQuery = String.format(
                "SELECT %s, %s FROM %s", ID, TITLE, NOTES_TABLE_TITLE);

        if(searchTerm == null || searchTerm.isEmpty()){
            cursor = accessDatabase.rawQuery(baseQuery+";",null);
        }else{
            cursor = this.queryTablesBySearchTerm(baseQuery, searchTerm);
        }
        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getNotesJoinedFavouritesTableQueryByTitle(String searchTerm){

        Cursor cursor;
        final String baseQuery = String.format(
                "SELECT %s, %s " +
                "FROM %s INNER JOIN %s " +
                "ON %s = %s"
                , NOTES_TABLE_TITLE+"."+ID, NOTES_TABLE_TITLE+"."+TITLE,
                NOTES_TABLE_TITLE, FAVOURITES_TABLE_TITLE,
                NOTES_TABLE_TITLE+"."+ID, FAVOURITES_TABLE_TITLE+"."+NOTES_ID);

        if(searchTerm == null || searchTerm.isEmpty()){
            cursor = accessDatabase.rawQuery(baseQuery+";",null);
        }else{
            cursor = this.queryTablesBySearchTerm(baseQuery, searchTerm);
        }
        cursor.moveToFirst();

        return cursor;
    }

    private Cursor queryTablesBySearchTerm(String baseQuery, String searchTerm){

        final String titleColumn = NOTES_TABLE_TITLE +"."+ TITLE;

        final String querySearchTermAtStart = String.format(
                "%s WHERE %s = '%s' OR %s LIKE '%s'",
                baseQuery, titleColumn, searchTerm, titleColumn, searchTerm + "%"
        );
        final String querySearchTermInMiddle = String.format(
                "%s WHERE %s LIKE '%s'",
                baseQuery, titleColumn, "%_"+searchTerm+"_%"
        );
        final String querySearchTermAtEnd = String.format(
                "%s WHERE %s LIKE '%s'",
                baseQuery, titleColumn, "%_"+searchTerm
        );

        Cursor cursor = accessDatabase.rawQuery(String.format(
                "%s UNION ALL %s UNION ALL %s;",
                querySearchTermAtStart, querySearchTermInMiddle, querySearchTermAtEnd
        ),null);

        return cursor;
    }

    public Note getNoteById(int id){

        Cursor cursor = accessDatabase.rawQuery(String.format(
                "SELECT %s, %s, %s FROM %s WHERE %s = %s",
                TITLE, MAIN_TEXT, DATE, NOTES_TABLE_TITLE, ID, id
        ),null);

        cursor.moveToFirst();
        Note note = this.getNoteFromCursor(cursor);
        cursor.close();

        note.setFavourite(this.isNoteIdInFavouritesTable(id));

        return note;
    }

    public void insertToNotesTable(Note newNote){
        accessDatabase.execSQL(String.format(
                "INSERT INTO %s (%s, %s, %s) VALUES ('%s', '%s', '%s');",
                NOTES_TABLE_TITLE,
                TITLE, MAIN_TEXT, DATE,
                newNote.getTitle(), newNote.getBody(), newNote.getDate()
        ));
        this.incrementTotalNotesCreated();
    }

    public void insertIdToFavouritesTable(int noteId){

        accessDatabase.execSQL(String.format(
                "INSERT INTO %s (%s) VALUES (%s);",
                FAVOURITES_TABLE_TITLE,
                NOTES_ID,
                noteId
        ));
    }

    public void insertLastAddedNoteToFavouritesTable(){

        final String queryForId = String.format(
                "SELECT %s FROM %s ORDER BY %s DESC LIMIT 1",
                ID, NOTES_TABLE_TITLE,
                ID
        );

        final Cursor cursor = accessDatabase.rawQuery(queryForId, null);
        cursor.moveToFirst();

        final int noteId = cursor.getInt(0);
        this.insertIdToFavouritesTable(noteId);
    }

    public void updateNoteInDatabase(int id, Note editedNote){

        accessDatabase.execSQL(String.format(
                "UPDATE %s SET %s = '%s', %s = '%s', %s = '%s' WHERE %s = %s",
                NOTES_TABLE_TITLE, TITLE, editedNote.getTitle(),
                MAIN_TEXT, editedNote.getBody(),
                DATE, editedNote.getDate(),
                ID, id
        ));
    }

    public void deleteNoteById(int noteId){

        accessDatabase.execSQL(String.format(
                "DELETE FROM %s WHERE %s = %s", NOTES_TABLE_TITLE, ID, noteId
        ));
    }

    public void removeNoteFromFavouritesById(int noteId){

        accessDatabase.execSQL(String.format(
                "DELETE FROM %s WHERE %s = %s", FAVOURITES_TABLE_TITLE, NOTES_ID, noteId
        ));

    }

    private void incrementTotalNotesCreated(){
        accessDatabase.execSQL(String.format(
                "UPDATE %s SET %s = %s + 1",
                NOTE_COUNTER_TABLE_TITLE, NUMBER_OF_NOTES, NUMBER_OF_NOTES
        ));

    }

    public int getNumberOfCurrentNotes(){

        final String columnName = String.format("COUNT(%s)", TITLE);
        Cursor cursor = accessDatabase.rawQuery(String.format(
                "SELECT %s FROM %s", columnName, NOTES_TABLE_TITLE
        ),null);
        cursor.moveToFirst();

        int noteCount = cursor.getInt(cursor.getColumnIndex(columnName));
        cursor.close();
        return noteCount;
    }

    public int getTotalNumberOfNotesCreated(){

        Cursor cursor = accessDatabase.rawQuery(String.format(
                "SELECT %s FROM %s", NUMBER_OF_NOTES, NOTE_COUNTER_TABLE_TITLE
        ),null);
        cursor.moveToFirst();

        int noteCount = cursor.getInt(cursor.getColumnIndex(NUMBER_OF_NOTES));
        cursor.close();
        return noteCount;
    }

    private Note getNoteFromCursor(Cursor cursor){
        Note note = new Note();
        note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        note.setBody(cursor.getString(cursor.getColumnIndex(MAIN_TEXT)));
        note.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
        return note;
    }

    private boolean isNoteIdInFavouritesTable(int id){

        Cursor cursor = accessDatabase.rawQuery(String.format(
                "SELECT %s FROM %s WHERE %s = %s;",
                NOTES_ID, FAVOURITES_TABLE_TITLE, NOTES_ID, id
        ),null);

        cursor.moveToFirst();
        boolean isFavourite = cursor.getCount() == 1;
        cursor.close();
        return isFavourite;
    }
}
