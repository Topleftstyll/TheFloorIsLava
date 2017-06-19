package com.topleftstyll.thefloorislava;

import java.util.ArrayList;

/**
 * Created by Topleftstyll on 5/16/2017.
 */

public class LevelBedroom extends LevelData {

    LevelBedroom() {
        tiles = new ArrayList<String>();
        this.tiles.add("p...................................................................................................");
        this.tiles.add("....................................................................................................");
        this.tiles.add("....................................................................................................");
        this.tiles.add("....................................................................................................");
        this.tiles.add("....................................................................................................");
        this.tiles.add("............11111................................................11111..............................");
        this.tiles.add(".........................................1111111..............................11111.........11111...");
        this.tiles.add(".............................11111111...............................................................");
        this.tiles.add("....................................................................................................");
        this.tiles.add("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.tiles.add("....................................................................................................");
        this.tiles.add("....................................................................................................");
    }
}

// can jump 3 block high and 8 far maybe 9 have to test on a phone
// can slide 8 blocks to make smaller lower the max slide variable
// and make a line of 11111111 and count to where you get stuck