package com.slava0135.pixelparty.game.floor;

import com.badlogic.gdx.math.Vector2;

final public class Floor {
    public final static int SIZE = 16;
    private final Palette[][] grid = new Palette[SIZE][SIZE];
    public Palette currentColor = null;

    public Palette[][] getGrid() {
        return grid.clone();
    }

    public void generateFloor() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                grid[y][x] = Palette.randomColor();
            }
        }
        currentColor = Palette.randomColor();
    }

    public void throwFloor() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (grid[y][x] != currentColor) {
                    grid[y][x] = null;
                }
            }
        }
    }

    public boolean isOnTile(double gridX, double gridY, double radius) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (grid[y][x] != null) {
                    int up = y + 1, down = y, left = x, right = x + 1;
                    if (gridY < up + radius && gridY > down - radius && gridX > left - radius && gridX < right + radius) return true;
                }
            }
        }
        return false;
    }

    public Vector2 findClosest(float gridX, float gridY) {
        double minDist = 1000000000;
        Vector2 vector = new Vector2(gridX, gridY);
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (grid[y][x] == currentColor) {
                    double newDist = (x + 0.5 - gridX) * (x + 0.5 - gridX) + (y + 0.5 - gridY) * (y + 0.5 - gridY);
                    if (newDist < minDist) {
                        vector = new Vector2(x + 0.5f, y + 0.5f);
                        minDist = newDist;
                    }
                }
            }
        }
        return vector;
    }
}
