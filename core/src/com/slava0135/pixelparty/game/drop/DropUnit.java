package com.slava0135.pixelparty.game.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class DropUnit {
    private final static float LIFETIME = 3;

    private float time = LIFETIME;
    public final Color color;
    public final Color borderColor;
    public final Vector2 position;

    DropUnit(Color color, Color borderColor, Vector2 position) {
        this.color = color;
        this.borderColor = borderColor;
        this.position = position;
    }

    public float getSize() {
        return time / LIFETIME;
    }

    public boolean update(float delta) {
        time -= delta;
        return time > 0; //is exist
    }
}
