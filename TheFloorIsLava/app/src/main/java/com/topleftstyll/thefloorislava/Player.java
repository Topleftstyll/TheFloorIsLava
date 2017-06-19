package com.topleftstyll.thefloorislava;

import android.content.Context;
import android.graphics.DashPathEffect;

/**
 * Created by Topleftstyll on 5/16/2017.
 */

public class Player extends GameObject {

    //hitboxes
    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    final float X_VELOCITY = 10; // how fast he runs

    public boolean isFalling;
    private boolean isJumping;
    private boolean isSliding = false;
    private long jumpTime;
    private long slideTime;
    private long maxSlideTime = 700;
    private long maxJumpTime = 1000; // jump 7 10ths of a second

    float startY;

    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMeter) {
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setyVelocity(0);
        setFacing(RIGHT);
        isFalling = false;

        // now for the players other attributes our game engine will use these
        setMoves(true);
        setActive(true);
        setVisible(true);

        setHeight(HEIGHT); // 2 meters tall
        setWidth(WIDTH); // 1 meter wide

        setType('p');

        // choose a Bitmap this is a sprite sheet with multiple frames
        // of animation. so it will look silly until we animate it in chapter 6
        setBitmapName("player");

        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;

        // set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMeter, true);

        // x and y locations from constructor
        setWorldLocation(worldStartX, worldStartY, 0);

        //hitboxes
        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();
    }

    public void update(long fps, float gravity) {
        setxVelocity(X_VELOCITY);

        //jumping and gravity
        if (isJumping) {
            isSliding = false;
            setFacing(RIGHT);
            setHeight(2);
            setWidth(1);
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity); // on the way up
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity);
                }
            } else {
                isJumping = false;
            }
        } else if(isSliding) {
            long timeSliding = System.currentTimeMillis() - slideTime;
            if (timeSliding < maxSlideTime) {
                setFacing(2);
                setHeight(1);
                setWidth(2);
                this.setWorldLocationX(getWorldLocation().x + 0.1f);
                this.setyVelocity(0);
            } else {
                setHeight(2);
                setWidth(1);
                this.setWorldLocationY(startY);
                setFacing(RIGHT);
                isSliding = false;
            }
        } else {
            this.setyVelocity(gravity);
            //  README!! remove the next line to make the game easier (i.e allow double jump)
            isFalling = true;
        }

        this.move(fps);

        //Update all the hitboxes to the new location
        //get the current world location of the play and save them as local variables we will use next
        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        if (!isSliding) {
            // TODO: CHANGE DEPENDING ON NEW CHARACTER
            //update the player feet hitbox
            rectHitboxFeet.top = ly + getHeight() * .95f;
            rectHitboxFeet.left = lx + getWidth() * .2f;
            rectHitboxFeet.bottom = ly + getHeight() * .98f;
            rectHitboxFeet.right = lx + getWidth() * .8f;

            //update player head hitbox
            rectHitboxHead.top = ly;
            rectHitboxHead.left = lx + getWidth() * .4f;
            rectHitboxHead.bottom = ly + getHeight() * .2f;
            rectHitboxHead.right = lx + getWidth() * .6f;

            //update player left hitbox
            rectHitboxLeft.top = ly + getHeight() * .2f;
            rectHitboxLeft.left = lx + getWidth() * .2f;
            rectHitboxLeft.bottom = ly + getHeight() * .8f;
            rectHitboxLeft.right = lx + getWidth() * .3f;

            //update player right hitbox
            rectHitboxRight.top = ly + getHeight() * .2f;
            rectHitboxRight.left = lx + getWidth() * .2f;
            rectHitboxRight.bottom = ly + getHeight() * .8f;
            rectHitboxRight.right = lx + getWidth() * .7f;
        } else {
            //update player right hitbox
            rectHitboxRight.top = ly + getHeight() * .2f;
            rectHitboxRight.left = lx + getWidth() * .2f;
            rectHitboxRight.bottom = ly + getHeight() * .8f;
            rectHitboxRight.right = lx + getWidth() * .7f;
        }
    }

    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;//no collision

        //the left
        if(this.rectHitboxLeft.intersects(rectHitbox)){
            //left has collided
            //move player just to right of current hitbox
            this.setWorldLocationX(rectHitbox.right - getWidth() * .2f);
            collided = 1;
        }

        //the right
        if(this.rectHitboxRight.intersects(rectHitbox)) {
            //right has collided
            //move player just to left of current hitbox
            this.setWorldLocationX(rectHitbox.left - getWidth() * .8f);
            collided = 1;
        }

        //the feet
        if(this.rectHitboxFeet.intersects(rectHitbox)) {
            //feet has collided
            //move feet just above current object
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
        }

        //the head
        if(this.rectHitboxHead.intersects(rectHitbox)) {
            //head has collided
            //move head just below current object
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }
        return collided;
    }

    // TODO: add sound to parameters
    public void startJump() {
        if (!isFalling) { // cant jump if falling
            if (!isJumping) { // not already jumping
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                // TODO: add sm.playSound("jump");
            }
        }
    }

    public void startSlide() {
        startY = getWorldLocation().y;
        if (!isFalling || !isSliding) { // cant slide if falling or sliding
            isSliding = true;
            slideTime = System.currentTimeMillis();
        }
    }
}
