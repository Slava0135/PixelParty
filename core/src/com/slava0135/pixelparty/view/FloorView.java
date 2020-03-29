package com.slava0135.pixelparty.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.slava0135.pixelparty.game.floor.Floor;
import com.slava0135.pixelparty.game.floor.Palette;

public class FloorView {
    public Floor floor;

    private ShapeRenderer shapeRenderer;

    private final Vector2 POSITION;
    private final float SCALE;

    public FloorView(Floor floor, Vector2 position, ShapeRenderer shapeRenderer, Float scale) {
        this.floor = floor;
        this.POSITION = position;
        this.SCALE = scale;
        this.shapeRenderer = shapeRenderer;
    }

    public void draw() {
        Palette[][] grid = floor.getGrid();
        for (int row = 0; row < Floor.SIZE; row++) {
            for (int col = 0; col < Floor.SIZE; col++) {
                Palette color = grid[row][col];
                if (color != null) {
                    shapeRenderer.setColor(color.color);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rect((POSITION.x + col) * SCALE, (POSITION.y + row) * SCALE, SCALE, SCALE);
                    shapeRenderer.end();
                }
            }
        }
    }
}
