package com.topleftstyll.thefloorislava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Topleftstyll on 5/15/2017.
 */

public class LavaView extends SurfaceView implements Runnable{

    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    // for drawing
    private Paint paint;
    // canvas could initially be local but later we will use it outside of draw
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    // our new engine classes
    private LevelManager lm;
    private Viewport vp;
    InputController ic;

    private final int X_OFFSET = 10; // so character isnt center of camera and is a bit to the left

    LavaView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        // initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        // initialize the viewport
        vp = new Viewport(screenWidth, screenHeight);

        //load the first level
        loadLevel("LevelBedroom", 15, 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (lm != null) {
            ic.handleInput(motionEvent, lm, vp); // TODO: add sm to the list
        }
        return true;
    }

    @Override
    public void run() {
        while (running) {
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            // calculate the fps this frame we can then use the result to time animations and movement
            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        for (GameObject go : lm.gameObjects) {
            if(go.isActive()) {
                //Clip anything offscreen
                if (!vp.clipObjects(go.getWorldLocation().x,go.getWorldLocation().y, go.getWidth(),go.getHeight())) {
                    //set visible flag to true
                    go.setVisible(true);

                    // check collisions with player
                    int hit = lm.player.checkCollisions(go.getHitBox());
                    if (hit > 0) {
                        // collision now deal with different types
                        switch (go.getType()) {
                            default:
                                // TODO: add crying sound and effect
                                if (hit == 1) {
                                    // TODO: player dies
                                }
                                if (hit == 2) { //feet
                                    lm.player.isFalling = false;
                                }
                        }
                    }

                    if (lm.isPlaying()) {
                        // run any un-clipped updates
                        go.update(fps, lm.gravity);
                    }
                } else {
                    //set visible flag to false
                    go.setVisible(false);
                    //Now draw() can ignore them
                }
            }
        }

        if (lm.isPlaying()) {
            // reset the players location as the center of the viewport
            vp.setWorldCenter(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, X_OFFSET);
        }
    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            // first we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //rub out the last frame with arbitrary color
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            //draw all the gameobjects
            Rect toScreen2d = new Rect();

            //draw a layer at a time
            for (int layer = -1; layer <= 1; layer ++) {
                for (GameObject go : lm.gameObjects) {
                    //Only draw if visible and this layer
                    if(go.isVisible() && go.getWorldLocation().z == layer) {
                        toScreen2d.set(vp.worldToScreen(go.getWorldLocation().x,go.getWorldLocation().y,go.getWidth(),go.getHeight()));

                        if(go.isAnimated()) {
                            //Get the next frame of the bitmap
                            //Rotate if necessary
                            if(go.getFacing() == 1) {
                                ///ROTATE
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], r.left, r.top, r.width(), r.height(), flipper, true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            } else {
                                //draw it the regular way around
                                canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], go.getRectToDraw(System.currentTimeMillis()), toScreen2d, paint);
                            }
                        } else { // just draw the whole bitmap
                            canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], toScreen2d.left, toScreen2d.top, paint);
                        }
                    }
                }
            }

            //Text for debugging
            if (debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));

                canvas.drawText("fps: " + fps, 10, 60, paint);
                canvas.drawText("num objects: " + lm.gameObjects.size(), 10, 80, paint);
                canvas.drawText("num clipped: " + vp.getNumClipped(), 10, 100, paint);
                canvas.drawText("playerX: " + lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, 10, 120, paint);
                canvas.drawText("playerY: " + lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, 10, 140, paint);

                canvas.drawText("Gravity: " + lm.gravity, 10, 160, paint);
                canvas.drawText("X Velocity: " + lm.gameObjects.get(lm.playerIndex).getxVelocity(), 10, 180, paint);
                canvas.drawText("Y Velocity: " + lm.gameObjects.get(lm.playerIndex).getyVelocity(), 10, 200, paint);

                //for reset the number of clipped objects each frame
                vp.resetNumClipped();
            } // end dBugging

            // draw pause button
            paint.setColor(Color.argb(80, 255, 255, 255));
            RectF rf = new RectF(ic.getButton().left, ic.getButton().top, ic.getButton().right, ic.getButton().bottom);
            canvas.drawRoundRect(rf, 15f, 15f, paint);

            //draw paused text
            if (!this.lm.isPlaying()) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));

                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth() / 2, vp.getScreenHeight() / 2, paint);
            }

            // unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // clean up our thread if the game is interrupted
    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch(InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }

    // make a new thread and start it execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void loadLevel(String level, float px, float py) {
        lm = null;
        //Create a new level manager
        //pass in context, screen details, level name
        //and player location
        lm = new LevelManager(context, vp.getPixelsPerMeterX(), vp.getScreenWidth(), ic, level, px, py);

        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());

        //set the players location as the world center
        vp.setWorldCenter(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, X_OFFSET);
    }
}
