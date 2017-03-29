package com.alequinonboard.notes;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class NoteClassTests {


    @Test
    public void testDateFormatChecker(){

        Note note = new Note();

        note.setTitle("");
        note.setBody("I am text");
        note.setTimeStamp("12:30 10/06/17");


    }
}