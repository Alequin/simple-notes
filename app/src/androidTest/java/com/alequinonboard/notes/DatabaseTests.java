package com.alequinonboard.notes;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alequinonboard.notes.database.NotesDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    @Test
    public void dbTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        NotesDatabase db = NotesDatabase.getDatabaseAndInitialisedIfRequired(appContext);

        Log.d("test", db.getNumberOfCurrentNotes()+"");

        try {
            db.insertIdToFavouritesTable(1);
            db.insertIdToFavouritesTable(1);
        }catch(SQLiteConstraintException cex){
            Log.d("test", "Constraint Exception thrown");
        }
    }


}
