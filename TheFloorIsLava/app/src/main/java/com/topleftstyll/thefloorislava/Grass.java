package com.topleftstyll.thefloorislava;

/**
 * Created by Topleftstyll on 5/16/2017.
 */

public class Grass extends GameObject {

    Grass(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        // choose a bitmap
        setBitmapName("turf");

        // where does the tile start
        // x and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);

        setRectHitbox();
    }

    public void update(long fps, float gravity) {}
}
