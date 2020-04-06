package com.slava0135.pixelparty.game.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Drop {
    private final Array<DropUnit> units = new Array<>();

    public void addUnit(Color color, Color borderColor, Vector2 position) {
        units.add(new DropUnit(color, borderColor, position));
    }

    public Array<DropUnit> getUnits() {
        return units;
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
