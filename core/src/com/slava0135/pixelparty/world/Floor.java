package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.Color;

public class Floor {
    static final int size = 16;
    final public Palette[][] grid = new Palette[size][size];

    public void generateFloor() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Palette.randomColor();
            }
        }
    }

    public void round() {
        Palette color = Palette.randomColor();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != color) {
                    grid[i][j] = null;
                }
            }
        }
    }
}
