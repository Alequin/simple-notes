package com.alequinonboard.notes;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

/**
 * Created by Alequin on 27/03/2017.
 */

public class BooleanMenuItem {

    private MenuItem item;
    private boolean state;

    private String trueTitle;
    private String falseTitle;

    private Drawable trueIcon;
    private Drawable falseIcon;


    public BooleanMenuItem() {
        this(null);
    }

    public BooleanMenuItem(MenuItem item) {
        this.item = item;
        state = false;
    }

    public void setMenuItem(MenuItem item){
        this.item = item;
    }

    public void setState(boolean newState){
        this.state = newState;
        if(item != null){
            if(newState){
                this.setStateTrue();
            }else{
                this.setStateFalse();
            }
        }
    }

    private void setStateTrue(){
        if(trueIcon != null){
            item.setIcon(trueIcon);
        }
        if(trueTitle != null){
            item.setTitle(trueTitle);
        }
    }

    private void setStateFalse(){
        if (falseIcon != null) {
            item.setIcon(falseIcon);
        }
        if (falseTitle != null) {
            item.setTitle(falseTitle);
        }
    }

    public boolean isStateTrue(){
        return this.state;
    }

    public void setIconToUseWhenTrue(Drawable icon){
        this.trueIcon = icon;
    }

    public void setIconToUseWhenFalse(Drawable icon){
        this.falseIcon = icon;
    }

    public void setTitleToUseWhenTrue(String title){
        this.trueTitle = title;
    }

    public void setTitleToUseWhenFalse(String title){
        this.falseTitle = title;
    }

}
