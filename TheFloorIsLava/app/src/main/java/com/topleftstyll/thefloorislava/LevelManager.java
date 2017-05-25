package com.topleftstyll.thefloorislava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Topleftstyll on 5/16/2017.
 */

public class LevelManager {

    private String level;

    int mapWidth;
    int mapHeight;

    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    ArrayList<Rect> currentButtons; //TODO: take out later
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMeter, int screenWidth, InputController ic, String level, float px, float py) {
        this.level = level;

        switch (level) {
            case "LevelBedroom":
                levelData = new LevelBedroom();
                break;
            //Extra levels come later
        }

        // To hold all out gameobjects
        gameObjects = new ArrayList<>();

        // to hold 1 of every bitmap
        bitmapsArray = new Bitmap[25];

        // load all the gameobjects and bitmaps
        loadMapData(context, pixelsPerMeter, px, py);

        // ready to play
        // playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    // each index corresponds to a bitmap
    public Bitmap getBitmap(char blockType) {
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            default:
                index = 0;
                break;
        }
        return bitmapsArray[index];
    }

    // This method allows each gameobject which 'knows'
    // its type to get the correct index to its bitmap
    // in the bitmap array
    public int getBitmapIndex(char blockType) {
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }

    //For now we just load all the grass tiles
    //and the player. Soon we will gave many gameobjects
    private void loadMapData(Context context, int pixelsPerMeter, float px, float py) {
        char c;

        //keep track of where we load out game objects
        int currentIndex = -1;

        //how wide and high is the map? Viewport needs to know
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {
                c = levelData.tiles.get(i).charAt(j);

                //Don't want to load the empty spaces
                if (c != '.') {
                    currentIndex++;
                    switch (c) {
                        case '1':
                            //Add grass to the gameobjects
                            gameObjects.add(new Grass(j, i, c));
                            break;
                        case 'p':
                            //Add a player to the gameobjects
                            gameObjects.add(new Player(context, px, py, pixelsPerMeter));

                            //We want the index of the player
                            playerIndex = currentIndex;
                            //we want a reference to the player
                            player = (Player) gameObjects.get(playerIndex);
                            break;
                    }
                    //if the bitmap isn't prepared yet
                    if (bitmapsArray[getBitmapIndex(c)] == null) {
                        //prepare it now and put it in the bitmaps arrayList
                        bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMeter);
                    }
                }
            }
        }
    }

    public void switchPlayingStatus() {
        playing = !playing;
        if(playing){
            gravity = 6;
        } else {
            gravity = 0;
        }
    }
}
