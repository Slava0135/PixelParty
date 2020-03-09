package com.slava0135.pixelparty.world;

import com.badlogic.gdx.utils.Array;

public class Floor {
    static final int size = 16;
    Pallete[][] grid;

    Floor() {
        grid = new Pallete[][]{};
    }

    public void generateFloor() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Pallete.randomColor();
            }
        }
    }
}
