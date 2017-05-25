package com.topleftstyll.thefloorislava;

import android.graphics.Rect;

/**
 * Created by Topleftstyll on 5/16/2017.
 */

public class Viewport {

    private Vector2Point5D currentViewportWorldCenter;
    private Rect convertedRect;
    private int pixelsPerMeterX;
    private int pixelsPerMeterY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCenterX;
    private int screenCenterY;
    private int metersToShowX;
    private int metersToShowY;
    private int numClipped;

    Viewport(int x, int y) {
        screenXResolution = x;
        screenYResolution = y;

        screenCenterX = screenXResolution / 2;
        screenCenterY = screenYResolution / 2;

        pixelsPerMeterX = screenXResolution / 32;
        pixelsPerMeterY = screenYResolution / 18;

        metersToShowX = 34;
        metersToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenter = new Vector2Point5D();
    }

    public Rect worldToScreen(float objX, float objY, float objWidth, float objHeight) {
        int left = (int)(screenCenterX - ((currentViewportWorldCenter.x - objX) * pixelsPerMeterX));

        int top = (int)(screenCenterY - ((currentViewportWorldCenter.y - objY) * pixelsPerMeterY));

        int right = (int)(left + (objWidth * pixelsPerMeterX));

        int bottom = (int)(top + (objHeight * pixelsPerMeterY));

        convertedRect.set(left, top, right, bottom);

        return convertedRect;
    }

    public boolean clipObjects(float objX, float objY, float objWidth, float objHeight) {
        boolean clipped = true;

        if (objX - objWidth < currentViewportWorldCenter.x + (metersToShowX / 2)) {
            if (objX + objWidth > currentViewportWorldCenter.x - (metersToShowX / 2)) {
                if (objY - objHeight < currentViewportWorldCenter.y + (metersToShowY / 2)) {
                    if (objY + objHeight > currentViewportWorldCenter.y - (metersToShowY / 2)) {
                        clipped = false;
                    }
                }
            }
        }

        // For debugging
        if (clipped) {
            numClipped++;
        }

        return clipped;
    }

    void setWorldCenter(float x, float y, int xOffSet) {
        currentViewportWorldCenter.x = x + xOffSet;
        currentViewportWorldCenter.y = y;
    }

    public int getScreenWidth() {
        return screenXResolution;
    }

    public int getScreenHeight() {
        return screenYResolution;
    }

    public int getPixelsPerMeterX() {
        return pixelsPerMeterX;
    }

    public int getNumClipped() {
        return  numClipped;
    }

    public void resetNumClipped() {
        numClipped = 0;
    }
}
