package com.slava0135.pixelparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.floor.Palette;

public class GameOverScreen implements Screen {
    private final int CAMERA_SIZE = 1000;

    private PixelGame core;
    private Stage stage;
    private float time = 0;

    GameOverScreen(final PixelGame game, Integer score) {
        this.core = game;
        stage = new Stage(new StretchViewport(CAMERA_SIZE, CAMERA_SIZE));
        Gdx.audio.newSound(Gdx.files.internal("sound/gameover.mp3")).play();

        Label title = new Label("Your Score:\n\n" + score, PixelGame.gameSkin);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = CAMERA_SIZE / 10;
        title.setStyle(new Label.LabelStyle(game.generator.generateFont(parameter), Palette.randomColor().color));
        title.setAlignment(Align.center);
        title.setY(stage.getHeight() / 2);
        title.setWidth(stage.getWidth());
        stage.addActor(title);
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
        stage.act();
        stage.draw();
        if (Gdx.input.isTouched() && time > 1) {
            core.setScreen(new MainMenuScreen(core));
            dispose();
        }
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
