package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

final public class Floor {
    final private static int size = 16;
    final private Palette[][] grid = new Palette[size][size];
    private Palette currentColor;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void generateFloor() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Palette.randomColor();
            }
        }
        currentColor = Palette.randomColor();
    }

    public void round() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != currentColor) {
                    grid[i][j] = null;
                }
            }
        }
    }

    public void draw(int x, int y, int scale) {
        for (int i = Floor.size - 1; i >= 0; i--) { //from "up to down" to "down to up"
            for (int j = 0; j < Floor.size; j++) {
                Palette color = grid[i][j];
                if (color != null) {
                    shapeRenderer.setColor(color.color);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rect(x + i * scale, y + j * scale, scale, scale);
                    shapeRenderer.end();
                }
            }
        }
    }

    public boolean isOnTile(double gridX, double gridY, double radius) { //transform coords before using
        gridY = size - gridY;
        if (gridX + radius < 0 || gridX - radius < size || gridY + radius < 0 || gridY - radius < size) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != null) {
                    int up = i + 1, down = i, left = j, right = j + 1;
                    if (gridY < up && gridY > down && gridX > left && gridY < right) return true; //inside the square
                }
            }
        }
        return false;
    }
}
