package com.slava0135.pixelparty.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.slava0135.pixelparty.game.GameStage;
import com.slava0135.pixelparty.game.world.GameWorld;

public class GameWorldView {
    public GameWorld world;

    private ShapeRenderer shapeRenderer;

    private final float SCALE;
    private final float RADIUS;
    private final Vector2 POSITION;

    public GameWorldView(GameWorld world, Vector2 position, ShapeRenderer shapeRenderer, float scale, float radius) {
        this.world = world;
        this.shapeRenderer = shapeRenderer;
        this.SCALE = scale;
        this.RADIUS = radius;
        this.POSITION = position;
    }

    private void drawBody(Vector2 position, Color color, Color borderColor) {
        shapeRenderer.setColor(borderColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle((POSITION.x + position.x) * SCALE, (POSITION.y + position.y) * SCALE, RADIUS);
        shapeRenderer.setColor(color);
        shapeRenderer.circle((POSITION.x + position.x) * SCALE, (POSITION.y + position.y) * SCALE, RADIUS * 0.5f);
        shapeRenderer.end();
    }

    public void draw(boolean gameIsOver, GameStage stage, Color floorColor) {
        for (Vector2 position: world.getBodiesPositions()) {
            drawBody(position, Color.BLACK, Color.BLACK);
        }
        if (!gameIsOver) {
            if (stage == GameStage.WAIT) {
                drawBody(world.getPlayerPosition(), Color.WHITE, Color.BLACK);
            } else {
                drawBody(world.getPlayerPosition(), floorColor, Color.BLACK);
            }
        }
    }
}
