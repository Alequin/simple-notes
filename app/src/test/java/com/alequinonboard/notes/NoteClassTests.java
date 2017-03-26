package com.alequinonboard.notes;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class NoteClassTests {

    private Note getNoteWithBlankTitle(){
        Note note = new Note();

        note.setTitle("");
        note.setMainText("I am text");
        note.setDate(new Date());
        return note;
    }

    @Test
    public void testNoteTitleGeneration(){
        Note note = getNoteWithBlankTitle();
        note.generateAndSetNewTitle(5);
        assertEquals("Note 05: 26/03/2017", note.getTitle());
    }
}