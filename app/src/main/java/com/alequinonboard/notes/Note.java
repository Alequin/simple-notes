package com.alequinonboard.notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alequin on 23/03/2017.
 */

public class Note {

    private String title;
    private String mainText;
    private String date;
    private boolean isFavourite;

    public String getTitle() {
        if(mainText == null){
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
        if(mainText == null){
            throw new NullPointerException("date should not be null when get is called");
        }
        return date;
    }

    public void setDate(Date currentDate) {
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.date = dateFormat.format(currentDate);
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
