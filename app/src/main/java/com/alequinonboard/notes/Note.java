package com.alequinonboard.notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Alequin on 23/03/2017.
 */

public class Note {

    private String title;
    private String mainText;
    private String date;
    private boolean isFavourite;

    public static final DateFormat creationDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public String getTitle() {
        if(title == null){
            throw new NullPointerException("title should not be null when get is called");
        }
        return title;
    }

    public void setTitle(String title) {
        if(title == null){
            throw new NullPointerException("title should not be null");
        }
        this.title = title;
    }

    public boolean isTitleEmpty(){
        return title.isEmpty();
    }

    public void generateTitle(int noteNumber){
        title = String.format("Note %02d: %s", noteNumber, this.date);
    }

    public String getMainText() {
        if(mainText == null){
            throw new NullPointerException("main text should not be null when get is called");
        }
        return mainText;
    }

    public void setMainText(String mainText) {
        if(mainText == null){
            throw new NullPointerException("main text should not be null");
        }
        this.mainText = mainText;
    }

    public String getDate() {
        if(date == null){
            throw new NullPointerException("date should not be null when get is called");
        }
        return date;
    }

    public void setDate(Date date) {
        if(date == null){
            throw new NullPointerException("date should not be null");
        }
        this.date = creationDateFormat.format(date).toString();
    }

    public void setDate(String date) {
        checkDateFormatAndValueValidity(date);
        this.date = date;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    private void checkDateFormatAndValueValidity(String date){
        if(date == null){
            throw new NullPointerException("date should not be null");
        }
        this.checkDateFormatValidity(date);
        this.checkDateValueValidity(date);
    }

    private void checkDateFormatValidity(String date){
        Pattern datePattern = Pattern.compile("^\\d\\d/\\d\\d/\\d+$");
        if(!(datePattern.matcher(date)).find()){
            throw new IllegalArgumentException("Date format should match dd/mm/yyyy");
        }
    }

    private void checkDateValueValidity(String date){
        final String errorMessage = "Given %s is not valid: " + date;
        final int day = Integer.parseInt(date.substring(0,2));
        if(day < 1 || day > 31){
            throw new IllegalArgumentException(String.format(errorMessage, "day"));
        }
        final int month = Integer.parseInt(date.substring(3,5));
        if(month < 1 || month > 12){
            throw new IllegalArgumentException(String.format(errorMessage, "month"));
        }
        //User can edit year to be any value they want as long as it is numerical
    }
}
