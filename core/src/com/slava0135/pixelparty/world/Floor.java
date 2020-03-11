package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

final public class Floor {
    final private static int size = 16;
    final private Palette[][] grid = new Palette[size][size];
    public Palette currentColor;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void generateFloor() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[x][y] = Palette.randomColor();
            }
        }
        currentColor = Palette.randomColor();
    }

    public void throwFloor() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] != currentColor) {
                    grid[x][y] = null;
                }
            }
        }
    }

    public void draw(int x, int y, int scale) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < Floor.size; col++) {
                Palette color = grid[row][col];
                if (color != null) {
                    shapeRenderer.setColor(color.color);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rect(x + col * scale, y + row * scale, scale, scale);
                    shapeRenderer.end();
                }
            }
        }
    }

    public boolean isOnTile(double gridX, double gridY, double radius) { //transform coords before using
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[y][x] != null) {
                    int up = y + 1, down = y, left = x, right = x + 1;
                    if (gridY <= up && gridY >= down && gridX >= left && gridX <= right) return true;
                }
            }
        }
        return false;
    }

    public Vector2 findClosest(float gridX, float gridY) {
        double minDist = 1000000000;
        Vector2 vector = new Vector2(gridX, gridY);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[y][x] == currentColor) {
                    double newDist = vector.dst2(x, y);
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
