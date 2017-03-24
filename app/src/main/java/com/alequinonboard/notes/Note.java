package com.alequinonboard.notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.regex.Pattern;

/**
 * Created by Alequin on 23/03/2017.
 */

public class Note {

    private String title;
    private String mainText;
    private String date;
    private boolean isFavourite;

    public String getTitle() {
        if(title == null){
            throw new NullPointerException("title should not be null when get is called");
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        if(mainText == null){
            throw new NullPointerException("main text should not be null when get is called");
        }
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getDate() {
        if(date == null){
            throw new NullPointerException("date should not be null when get is called");
        }
        return date;
    }

    public void setDate(Date date) {
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.date = dateFormat.format(date).toString();
    }

    public void setDate(String date) {
        Pattern datePattern = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        if(!(datePattern.matcher(date)).find()){
            throw new IllegalArgumentException("Date format should match dd/mm/yyyy");
        }
        this.date = date;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
