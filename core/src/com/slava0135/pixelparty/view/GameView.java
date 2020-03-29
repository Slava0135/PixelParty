package com.slava0135.pixelparty.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.Game;
import com.slava0135.pixelparty.game.GameStage;
import com.slava0135.pixelparty.game.floor.Floor;
import com.slava0135.pixelparty.game.world.GameWorld;

public class GameView implements Disposable {
    public Game game;

    private DropView dropView;
    private FloorView floorView;
    private GameWorldView worldView;

    private final float SCALE;
    private final float BORDER;
    private final float RADIUS;
    private final float SIZE;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();

    public GameView(Game game, OrthographicCamera camera, BitmapFont font, float scale, float size) {
        this.SCALE = scale;
        this.SIZE = size;
        this.BORDER = (SIZE - Floor.SIZE * SCALE) / 2;
        this.RADIUS = GameWorld.UNIT_RADIUS;

        this.camera = camera;
        this.font = font;

        this.game = game;
        dropView = new DropView(game.drop, new Vector2(BORDER / SCALE, BORDER / SCALE), shapeRenderer, SCALE, RADIUS);
        floorView = new FloorView(game.floor, new Vector2(BORDER / SCALE, BORDER / SCALE), shapeRenderer, SCALE);
        worldView = new GameWorldView(game.world, new Vector2(BORDER / SCALE, BORDER / SCALE), shapeRenderer, SCALE, RADIUS);
    }

    private void printColor() {
        layout.setText(font, floorView.floor.currentColor.name);
        batch.begin();
        font.setColor(floorView.floor.currentColor.color);
        font.draw(batch, floorView.floor.currentColor.name, (SIZE - layout.width) / 2f, SIZE * 0.98f);
        batch.end();
    }

    private void showProgress() {
        float progress = game.getProgress();
        shapeRenderer.setColor(floorView.floor.currentColor.color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(BORDER / 4f, progress * SIZE / 2, SCALE, (1 - progress) * SIZE);
        shapeRenderer.rect(SIZE - (3 * BORDER / 4f), progress * SIZE / 2, SCALE, (1 - progress) * SIZE);
        shapeRenderer.end();
    }

    public void update(Vector2 click, float delta) {
        game.update(new Vector2((click.x - BORDER) / SCALE, (click.y - BORDER) / SCALE), delta);
    }

    public void draw() {
        Gdx.gl.glClearColor(PixelGame.BACKGROUND.r, PixelGame.BACKGROUND.g, PixelGame.BACKGROUND.b, PixelGame.BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        if (game.stage != GameStage.WAIT) {
            printColor();
        }

        if (game.stage == GameStage.RUN) {
            showProgress();
        }

        dropView.draw();
        floorView.draw();
        worldView.draw(game.gameIsOver, game.stage, floorView.floor.currentColor.color);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
