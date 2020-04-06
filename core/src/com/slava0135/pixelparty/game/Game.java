package com.slava0135.pixelparty.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.drop.Drop;
import com.slava0135.pixelparty.game.floor.Floor;
import com.slava0135.pixelparty.game.world.GameWorld;

public class Game implements Disposable {

    private final static int MAX_UNIT_AMOUNT = 50;
    private final static float FINISHING_TIME = 3;

    public GameStage stage;

    public GameWorld world;
    public Drop drop;
    public Floor floor;

    public boolean gameIsOver = false;

    private float time = 0;
    private Integer score = 0;
    private float speedMultiplier = 1.05f;
    private float roundLength = 3;
    private float timeSinceDeath = 0;

    public Game() {
        floor = new Floor();
        floor.generateFloor();
        drop = new Drop();
        world = new GameWorld(floor, drop, MAX_UNIT_AMOUNT);
        stage = GameStage.WAIT;
    }

    public void update(Vector2 click, float delta) {
        time += delta;
        drop.update(delta);
        boolean isOver = time > roundLength;
        switch(stage) {
            case WAIT:
                break;
            case RUN: {
                if (isOver) {
                    floor.throwFloor();
                }
                break;
            }
            case BREAK: {
                if (isOver) {
                    floor.generateFloor();
                    if (!gameIsOver) {
                        score++;
                    }
                    world.speedUp(speedMultiplier);
                    roundLength /= speedMultiplier;
                }
                break;
            }
        }

        if (isOver) {
            time = 0;
            stage = stage.next();
        }

        boolean playerIsAlive = world.update(stage, new Vector2(click.x, click.y));
        if (!gameIsOver && !playerIsAlive) {
            gameIsOver = true;
            drop.addUnit(PixelGame.BACKGROUND, Color.BLACK, world.getPlayerPosition());
        }

        if (gameIsOver) {
            timeSinceDeath += delta;
        }
    }

    public boolean isFinished() {
        return timeSinceDeath > FINISHING_TIME;
    };

    public Integer getScore() {
        return score;
    }

    public float getProgress() {
        return time / roundLength;
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
