package com.slava0135.pixelparty.game.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Iterator;

import static com.slava0135.pixelparty.game.world.GameWorld.UNIT_RADIUS;
import static com.slava0135.pixelparty.screens.GameScreen.*;

public class Drop {
    private final Array<DropUnit> units = new Array<>();

    public void addUnit(Color color, Color borderColor, Vector2 position) {
        units.add(new DropUnit(color, borderColor, position));
    }

    public DropUnit[] getUnits() {
        return units.items.clone();
    }

    public void update(float delta) {
        for (Iterator<DropUnit> iter = units.iterator(); iter.hasNext(); ) {
            DropUnit unit = iter.next();
            if (!unit.update(delta)) {
                units.removeValue(unit, false);
            }
        }
    }
}
