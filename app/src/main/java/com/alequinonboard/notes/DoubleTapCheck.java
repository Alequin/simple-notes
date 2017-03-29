package com.alequinonboard.notes;

import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alequin on 27/11/2016.
 * updated 18/02/16
 */

public class DoubleTapCheck {

    private final long tapTimeFrame;

    private long timeFlag1 = 0;

    public DoubleTapCheck() {
        this.tapTimeFrame = 200;
    }

    public DoubleTapCheck(long time) {
        this.tapTimeFrame = time;
    }

    public boolean isSecondTap(){

        final Long timeFlag2 = System.currentTimeMillis();
        final boolean result = (timeFlag2 - timeFlag1) < tapTimeFrame;
        timeFlag1 = System.currentTimeMillis();
        return result;
    }

}
