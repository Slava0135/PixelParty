package com.slava0135.pixelparty.world;

public class Floor {
    static final int size = 16;
    final public Pallete[][] grid;

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

    public void leaveOne() {
        Pallete color = Pallete.randomColor();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != color) {
                    grid[i][j] = null;
                }
            }
        }
    }
}
