package com.alequinonboard.notes;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class NoteClassTests {

    private Note getNoteWithBlankTitle(){
        Note note = new Note();

        note.setTitle("");
        note.setBody("I am text");
        note.setTimeStamp(new Date());
        return note;
    }
}