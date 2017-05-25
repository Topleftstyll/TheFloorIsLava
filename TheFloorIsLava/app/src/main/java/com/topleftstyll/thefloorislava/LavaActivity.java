package com.topleftstyll.thefloorislava;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by Topleftstyll on 5/15/2017.
 */

public class LavaActivity extends Activity {

    //our object to handle the View
    private LavaView lavaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get a display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        // And finally set the view for our game
        // also passing in the screen resolution
        lavaView = new LavaView(this, resolution.x, resolution.y);

        // make our lava view the view for the activity
        setContentView(lavaView);
    }

    // if the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        lavaView.pause();
    }

    // if the activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        lavaView.resume();
    }
}

