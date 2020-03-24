package com.slava0135.pixelparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.floor.Floor;

public class MainMenuScreen implements Screen {

    OrthographicCamera camera;
    private PixelGame core;
    private Stage stage;
    private Floor floor = new Floor();
    private float time = 0;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public MainMenuScreen(final PixelGame game) {
        this.core = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 1000);
        stage = new Stage(new StretchViewport(1000, 1000, camera));
        floor.generateFloor();

        Label title = new Label("PIXEL PARTY", PixelGame.gameSkin);
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 100;
        title.setStyle(new Label.LabelStyle(game.generator.generateFont(parameter), Color.BLACK));
        title.setAlignment(Align.center);
        title.setY(stage.getHeight() * 0.89f);
        title.setWidth(stage.getWidth());
        stage.addActor(title);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        parameter.size = 100;
        textButtonStyle.font = game.generator.generateFont(parameter);
        textButtonStyle.fontColor = Color.BLACK;
        TextButton playButton = new TextButton("Play!", textButtonStyle);
        playButton.setWidth(stage.getHeight()/2f);
        playButton.setPosition(stage.getWidth()/2f-playButton.getWidth()/2,stage.getHeight()/2f-playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);
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
        floor.draw(180, 180, 40, shapeRenderer);
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
    }
}
