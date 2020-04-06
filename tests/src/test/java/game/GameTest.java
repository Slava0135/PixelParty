package game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.slava0135.pixelparty.game.Game;
import com.slava0135.pixelparty.game.GameStage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

    @Test
    public void testGame() throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < 5; i++) {
            Game game = new Game();
            Field roundLength = game.getClass().getDeclaredField("roundLength");
            roundLength.setAccessible(true);
            float time = roundLength.getFloat(game);

            game.update(Vector2.Zero, 1.01f * time);
            assertEquals(GameStage.RUN, game.stage);

            game.update(Vector2.Zero, 1.01f * time);
            assertEquals(GameStage.BREAK, game.stage);

            game.update(Vector2.Zero, 1.01f * time);
            assertEquals(GameStage.WAIT, game.stage);

            Field score = game.getClass().getDeclaredField("score");
            score.setAccessible(true);
            int points = (int) score.get(game);
            assertEquals(points == 0, game.gameIsOver);

            game.dispose();
        }
    }
}
