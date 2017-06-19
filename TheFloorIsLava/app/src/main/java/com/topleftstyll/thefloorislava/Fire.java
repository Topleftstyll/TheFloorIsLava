package com.topleftstyll.thefloorislava;

import android.content.Context;

/**
 * Created by Topleftstyll on 6/15/2017.
 */

public class Fire extends GameObject {
    Fire(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMeter) {

        final int ANIMATION_FPS = 3;
        final int ANIMATION_FRAME_COUNT = 3;
        final String BITMAP_NAME = "fire";

        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);
        //now for the players other attributes our game engine will use these
        setMoves(false);
        setActive(true);
        setVisible(true);

        //choose a bitmap
        setBitmapName(BITMAP_NAME);
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMeter, true);

        //where does the tile start
        //X and Y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }


    public void update(long fps, float gravity){}
}
