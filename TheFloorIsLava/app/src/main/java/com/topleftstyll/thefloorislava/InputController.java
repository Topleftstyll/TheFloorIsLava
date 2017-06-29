package com.topleftstyll.thefloorislava;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Topleftstyll on 5/15/2017.
 */

public class InputController {

    Rect pause;
    private float mStartingPositionY;
    private double mVerticalFactor;

    private final double mMaxDistance;

    InputController(int screenWidth, int screenHeight) {
        // configure the player buttons
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        double pixelFactor = screenHeight / 400d;
        mMaxDistance = 50*pixelFactor;

        pause = new Rect(screenWidth - buttonPadding - buttonWidth, buttonPadding, screenWidth - buttonPadding, buttonPadding + buttonHeight);
    }

    public Rect getButton() {
        return pause;
    }

    public void handleInput(MotionEvent motionEvent, LevelManager l, Viewport vp) {

        int pointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            if(l.isPlaying()){
                switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        } else {
                            mStartingPositionY = motionEvent.getY(0);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mVerticalFactor = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!pause.contains(x,y)){
                            mVerticalFactor = (motionEvent.getY(0) - mStartingPositionY) / mMaxDistance;
                            if (mVerticalFactor < -1) {
                                l.player.startJump();
                                System.out.println("JUMP");
                            } else if (mVerticalFactor > 1){
                                System.out.println("SLIDE");
                                l.player.startSlide();
                            }
                        }
                        break;

                } //enf if(l.playing)
            } else { //not playing
                //move the viewport around to explore the map
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (pause.contains(x, y)) {
                            l.switchPlayingStatus();
                            //Log.w("pause", "DOWN");
                        }
                        break;
                }
            }
        }
    }
}
