package com.slava0135.pixelparty.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.slava0135.pixelparty.game.drop.Drop;
import com.slava0135.pixelparty.game.drop.DropUnit;

public class DropView {
    public Drop drop;

    private ShapeRenderer shapeRenderer;

    private final float SCALE;
    private final float RADIUS;
    private final Vector2 POSITION;

    public DropView(Drop drop, Vector2 position, ShapeRenderer shapeRenderer, float scale, float radius) {
        this.drop = drop;
        this.shapeRenderer = shapeRenderer;
        this.SCALE = scale;
        this.RADIUS = radius;
        this.POSITION = position;
    }

    private void drawUnit(DropUnit unit) {
        shapeRenderer.setColor(unit.borderColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle((POSITION.x + unit.position.x) * SCALE, (POSITION.y + unit.position.y) * SCALE, RADIUS * SCALE * unit.getSize());
        shapeRenderer.setColor(unit.color);
        shapeRenderer.circle((POSITION.x + unit.position.x) * SCALE, (POSITION.y + unit.position.y) * SCALE, RADIUS * SCALE * 0.5f * unit.getSize());
        shapeRenderer.end();
    }

    public void draw() {
        for (DropUnit unit: drop.getUnits()) {
            drawUnit(unit);
        }
    }
}
