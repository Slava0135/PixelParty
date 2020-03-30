package game.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slava0135.pixelparty.game.drop.Drop;
import com.slava0135.pixelparty.game.drop.DropUnit;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DropTest {

    @Test
    public void update() throws NoSuchFieldException, IllegalAccessException {

        Field lifetime = DropUnit.class.getDeclaredField("LIFETIME");
        lifetime.setAccessible(true);
        float time = lifetime.getFloat(null);

        Drop drop = new Drop();
        drop.addUnit(Color.BLACK, Color.BLACK, new Vector2(0, 0));

        drop.update(time / 2);
        assertEquals(1, drop.getUnits().size);

        drop.update(time);
        assertEquals(0, drop.getUnits().size);

        drop.addUnit(Color.BLACK, Color.BLACK, new Vector2(0, 0));
        drop.update(time / 2);

        drop.addUnit(Color.BLACK, Color.BLACK, new Vector2(0, 0));
        drop.update(0.9f * time);

        assertEquals(1, drop.getUnits().size);
    }
}
