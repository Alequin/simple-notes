package com.alequinonboard.notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by Alequin on 23/03/2017.
 */

public class Note {

    private String title;
    private String body;
    private String timeStamp;
    private boolean isFavourite;

    public static final DateFormat timeStampFormat;

    static{
        timeStampFormat = new SimpleDateFormat("HH:MM dd/MM/yy");
        timeStampFormat.setTimeZone(TimeZone.getDefault());
    }

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

    public static String getGeneratedTitleFromTimeStamp(Date timeStamp){
        return "Note: "+ timeStampFormat.format(timeStamp).toString();
    }

    public void generateAndSetNewTitle(){
        title = "Note: "+ this.timeStamp;
    }

    public String getBody() {
        if(body == null){
            throw new NullPointerException("main text should not be null when get is called");
        }
        return body;
    }

    public void setBody(String body) {
        if(body == null){
            throw new NullPointerException("main text should not be null");
        }
        this.body = body;
    }

    public String getTimeStamp() {
        if(timeStamp == null){
            throw new NullPointerException("timeStamp should not be null when get is called");
        }
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        if(timeStamp == null){
            throw new NullPointerException("timeStamp should not be null");
        }
        this.timeStamp = timeStampFormat.format(timeStamp).toString();
    }

    public void setTimeStamp(String timeStamp) {
        this.checkTimeStampValueValidity(timeStamp);
        this.checkTimeStampFormatValidity(timeStamp);
        this.timeStamp = timeStamp;
    }

    private void checkTimeStampValueValidity(String timeStamp){
        if(timeStamp == null){
            throw new NullPointerException("timeStamp should not be null");
        }
        final String errorMessage = "Given %s is not valid: " + timeStamp;
        final int day = Integer.parseInt(timeStamp.substring(6,8));
        if(day < 1 || day > 31){
            throw new IllegalArgumentException(String.format(errorMessage, "day"));
        }
        final int month = Integer.parseInt(timeStamp.substring(9,11));
        if(month < 1 || month > 12){
            throw new IllegalArgumentException(String.format(errorMessage, "month"));
        }
        final int year = Integer.parseInt(timeStamp.substring(12));
        if(year < 17){
            throw new IllegalArgumentException(String.format(errorMessage, "year"));
        }
    }

    private void checkTimeStampFormatValidity(String timeStamp){
        Pattern datePattern = Pattern.compile("^\\d\\d\\:\\d\\d\\s\\d\\d/\\d\\d/\\d+$");
        if(!(datePattern.matcher(timeStamp)).find()){
            throw new IllegalArgumentException("Date format should match dd/mm/yyyy");
        }
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }


}
