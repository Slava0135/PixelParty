package com.slava0135.pixelparty.game.floor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import static com.slava0135.pixelparty.game.world.GameWorld.UNIT_RADIUS;
import static com.slava0135.pixelparty.screens.GameScreen.*;

public class Fall {
    private final Array<Unit> units = new Array<>();
    private final static float lifetime = 3;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    private class Unit {
        private float time = lifetime;
        private final Color color;
        private final Color borderColor;
        private final Vector2 position;
        Unit(Color color, Color borderColor, Vector2 position) {
            this.color = color;
            this.borderColor = borderColor;
            this.position = position;
        }

        boolean draw(float delta) {
            shapeRenderer.setColor(borderColor);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(position.x * SCALE + BORDER, position.y * SCALE + BORDER, UNIT_RADIUS * SCALE * time / lifetime);
            shapeRenderer.setColor(color);
            shapeRenderer.circle(position.x * SCALE + BORDER, position.y * SCALE + BORDER, UNIT_RADIUS * SCALE * 0.5f * time / lifetime);
            shapeRenderer.end();
            time -= delta;
            return time < 0;
        }
    }
    public void addUnit(Color color, Color borderColor, Vector2 position) {
        units.add(new Unit(color, borderColor, position));
    }
    public void drawAll(float delta) {
        for (Iterator<Unit> iter = units.iterator(); iter.hasNext(); ) {
            Unit unit = iter.next();
            if (unit.draw(delta)) {
                units.removeValue(unit, true);
            }
        }
    }
}
