package com.alequinonboard.notes;

/**
 * Created by Alequin on 23/03/2017.
 */

public class Note {

    private String title;
    private String mainText;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(int day, int month, int year) {
        if(day < 1 || day > 31){
            throw new IllegalArgumentException("Day cannot be less than 1 or more than 31");
        }if(month < 1 || day > 12){
            throw new IllegalArgumentException("Month cannot be less than 1 or more than 12");
        }
        if(year < 1000 || year > 9999){
            throw new IllegalArgumentException("Year must be a four digit number");
        }
        this.date = String.format("%2$d/%2$d/%s", day, month, year);
    }
}
