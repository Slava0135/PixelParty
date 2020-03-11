package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.graalvm.compiler.lir.phases.PostAllocationOptimizationPhase;

public class Floor {
    final public static int size = 16;
    final private Palette[][] grid = new Palette[size][size];
    ShapeRenderer shapeRenderer = new ShapeRenderer();

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

    public void draw(int x, int y, int scale) {
        for (int i = 0; i < Floor.size; i++) {
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

    public Palette[][] getGrid() {
        Palette[][] newGrid = new Palette[size][size];
        for (int i = 0; i < size; i++) {
            newGrid[i] = grid[(size - 1) - i];
        }
        return newGrid;
    }
}
