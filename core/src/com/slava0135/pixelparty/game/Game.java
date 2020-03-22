package com.slava0135.pixelparty.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.floor.Fall;
import com.slava0135.pixelparty.game.floor.Floor;
import com.slava0135.pixelparty.game.world.GameWorld;

import static com.slava0135.pixelparty.game.world.GameWorld.UNIT_RADIUS;

public class Game implements Disposable {

    public final static int MAX_UNIT_AMOUNT = 50;
    private final static float FINISHING_TIME = 3;

    public final static int SCALE = 50;
    public final static int BORDER = SCALE * 2;

    private GameWorld world;
    private GameStage stage;
    private Fall fall;
    private Floor floor;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font;

    private float time = 0;
    private Integer score = 0;
    private float speedMultiplier = 1.05f;
    private boolean gameIsOver = false;
    private float roundLength = 3;
    private float timeSinceDeath = 0;

    Game(PixelGame game) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Floor.SIZE * SCALE + 2 * BORDER, Floor.SIZE * SCALE + 2 * BORDER);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        font = game.generator.generateFont(parameter);

        floor = new Floor();
        floor.generateFloor();
        stage = GameStage.WAIT;
        fall = new Fall();
        world = new GameWorld(floor, fall);
    }

    public void update(Vector2 click, float delta) {
        boolean isOver = time > roundLength;
        switch(stage) {
            case WAIT: break;
            case RUN: {
                if (isOver) {
                    floor.throwFloor();
                }
                break;
            }
            case BREAK: {
                if (isOver) {
                    floor.generateFloor();
                    score++;
                    world.speedUp(speedMultiplier);
                    roundLength /= speedMultiplier;
                    world.spawnUnits(MAX_UNIT_AMOUNT - world.getBodiesPositions().size);
                }
                break;
            }
        }
        if (isOver) {
            time = 0;
            stage = stage.next();
        }
        if (gameIsOver || !world.update(stage, click)) {
            gameIsOver = true;
        }
        if (gameIsOver) {
            timeSinceDeath += delta;
        }
    }

    public boolean isFinished() {
        return timeSinceDeath < FINISHING_TIME;
    };

    public void draw(float delta) {
        Gdx.gl.glClearColor(PixelGame.BACKGROUND.r, PixelGame.BACKGROUND.g, PixelGame.BACKGROUND.b, PixelGame.BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        if (stage != GameStage.WAIT) printColor();

        fall.drawAll(delta);
        floor.draw(BORDER, BORDER, SCALE, shapeRenderer);
        drawWorld();
    }

    private void drawWorld() {
        for (Vector2 position: world.getBodiesPositions()) {
            drawBody(position, Color.BLACK, Color.BLACK);
        }
        if (!gameIsOver) {
            if (stage == GameStage.WAIT) {
                drawBody(world.getPlayerPosition(), Color.WHITE, Color.BLACK);
            } else {
                drawBody(world.getPlayerPosition(), floor.currentColor.color, Color.BLACK);
            }
        }
    }

    private void drawBody(Vector2 position, Color color, Color borderColor) {
        shapeRenderer.setColor(borderColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(position.x * SCALE + BORDER, position.y * SCALE + BORDER, UNIT_RADIUS * SCALE);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x * SCALE + BORDER, position.y * SCALE + BORDER, UNIT_RADIUS * SCALE * 0.5f);
        shapeRenderer.end();
    }

    private void printColor() {
        batch.begin();
        font.setColor(floor.currentColor.color);
        font.draw(batch, floor.currentColor.name,300, 975); //magic
        batch.end();
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
