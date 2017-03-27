package com.alequinonboard.notes;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

/**
 * Created by Alequin on 27/03/2017.
 */

public class BooleanMenuItem {

    private MenuItem item;
    private boolean state;

    private Drawable falseIcon;
    private Drawable trueIcon;

    public BooleanMenuItem(MenuItem item) {
        this.item = item;
        state = false;
    }

    public void setStateTrue(){
        if(trueIcon == null){
            throw new NullPointerException("No drawable has been given to set when state is true");
        }
        this.state = true;
        item.setIcon(trueIcon);
    }

    public void setStateFalse(){
        if(trueIcon == null){
            throw new NullPointerException("No drawable has been given to set when state is false");
        }
        this.state = false;
        item.setIcon(falseIcon);
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


}
