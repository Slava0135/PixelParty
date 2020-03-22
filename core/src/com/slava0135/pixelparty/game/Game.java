package com.slava0135.pixelparty.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.slava0135.pixelparty.game.floor.Fall;
import com.slava0135.pixelparty.game.floor.Floor;
import com.slava0135.pixelparty.game.world.GameWorld;

public class Game implements Disposable {
    public final static int MAX_UNIT_AMOUNT = 50;

    GameWorld world;
    GameStage stage;
    Fall fall;
    Floor floor;

    Integer score = 0;

    Game() {

    }

    public void update(Vector2 click) {

    }

    public void draw(float delta) {
        fall.drawAll(delta);
        floor.draw();
        drawWorld();
    }

    private void drawWorld() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
