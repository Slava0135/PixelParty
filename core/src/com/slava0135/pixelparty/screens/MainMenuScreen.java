package com.slava0135.pixelparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.floor.Floor;

import static com.slava0135.pixelparty.PixelGame.soundIsOn;

public class MainMenuScreen implements Screen {
    private final int CAMERA_SIZE = 1000;

    private OrthographicCamera camera;
    private PixelGame core;
    private Stage stage;
    private Floor floor;
    private float time = 0;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private SpriteBatch batch = new SpriteBatch();
    private Texture soundon = new Texture(Gdx.files.internal("soundon.png"));
    private Texture soundoff = new Texture(Gdx.files.internal("soundoff.png"));

    public MainMenuScreen(final PixelGame game) {
        this.core = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_SIZE, CAMERA_SIZE);
        stage = new Stage(new StretchViewport(CAMERA_SIZE, CAMERA_SIZE, camera));
        floor = new Floor(shapeRenderer);
        floor.generateFloor();

        Label title = new Label("PIXEL PARTY", PixelGame.gameSkin);
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = CAMERA_SIZE / 10;
        title.setStyle(new Label.LabelStyle(game.generator.generateFont(parameter), Color.BLACK));
        title.setAlignment(Align.center);
        title.setY(stage.getHeight() * 0.89f);
        title.setWidth(stage.getWidth());
        stage.addActor(title);

        Label credits = new Label("Music by Kevin MacLeod ", PixelGame.gameSkin);
        parameter.size = CAMERA_SIZE / 25;
        credits.setStyle(new Label.LabelStyle(game.generator.generateFont(parameter), Color.LIGHT_GRAY));
        credits.setAlignment(Align.right);
        credits.setY(stage.getHeight() * 0.02f);
        credits.setWidth(stage.getWidth());
        stage.addActor(credits);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        parameter.size = CAMERA_SIZE / 10;
        textButtonStyle.font = game.generator.generateFont(parameter);
        textButtonStyle.fontColor = Color.BLACK;
        TextButton playButton = new TextButton("Play!", textButtonStyle);
        playButton.setWidth(stage.getHeight() / 2f);
        playButton.setPosition(stage.getWidth() / 2f - playButton.getWidth() / 2,stage.getHeight() / 2f - playButton.getHeight() / 2);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);

        Button soundButton = new Button(new Button.ButtonStyle());
        soundButton.setWidth(CAMERA_SIZE / 15f);
        soundButton.setHeight(CAMERA_SIZE / 15f);
        soundButton.setPosition(CAMERA_SIZE / 25f, CAMERA_SIZE / 25f);
        soundButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundIsOn = !soundIsOn;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(soundButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        time += delta;
        Gdx.gl.glClearColor(PixelGame.BACKGROUND.r, PixelGame.BACKGROUND.g, PixelGame.BACKGROUND.b, PixelGame.BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (time > 0.5) {
            floor.generateFloor();
            time = 0;
        }
        if (soundIsOn) {
            batch.begin();
            batch.draw(soundon, CAMERA_SIZE / 25f, CAMERA_SIZE / 25f, CAMERA_SIZE / 15f, CAMERA_SIZE / 15f);
            batch.end();
        } else {
            batch.begin();
            batch.draw(soundoff, CAMERA_SIZE / 25f, CAMERA_SIZE / 25f, CAMERA_SIZE / 15f, CAMERA_SIZE / 15f);
            batch.end();
        }
        floor.draw((CAMERA_SIZE - Floor.SIZE * CAMERA_SIZE / 25f) / 2,(CAMERA_SIZE - Floor.SIZE * CAMERA_SIZE / 25f) / 2, CAMERA_SIZE / 25);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        soundon.dispose();
        soundoff.dispose();
    }
}
