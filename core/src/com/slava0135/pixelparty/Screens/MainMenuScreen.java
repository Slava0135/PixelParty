package com.slava0135.pixelparty.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slava0135.pixelparty.PixelGame;

public class MainMenuScreen implements Screen {

    PixelGame game;
    private Stage stage;
    final static Color background = Color.WHITE;

    public MainMenuScreen(final PixelGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Label title = new Label("Title Screen", PixelGame.gameSkin);
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2f/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton playButton = new TextButton("Play!", PixelGame.gameSkin);
        playButton.setWidth(Gdx.graphics.getWidth()/2f);
        playButton.setPosition(Gdx.graphics.getWidth()/2f-playButton.getWidth()/2,Gdx.graphics.getHeight()/2f-playButton.getHeight()/2);
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

        TextButton optionsButton = new TextButton("Options", PixelGame.gameSkin);
        optionsButton.setWidth(Gdx.graphics.getWidth()/2f);
        optionsButton.setPosition(Gdx.graphics.getWidth()/2f-optionsButton.getWidth()/2,Gdx.graphics.getHeight()/4f-optionsButton.getHeight()/2);
        optionsButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new OptionScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(optionsButton);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        game.dispose();
    }
}
