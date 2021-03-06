package com.slava0135.pixelparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.game.Game;
import com.slava0135.pixelparty.game.MusicCatalog;
import com.slava0135.pixelparty.view.GameView;

import static com.slava0135.pixelparty.PixelGame.SIZE;
import static com.slava0135.pixelparty.PixelGame.soundIsOn;

public class GameScreen implements Screen {
    final PixelGame core;
    Stage stage;
    OrthographicCamera camera;
    GameView gameView;
    Music music;

    private Vector3 touchPos = new Vector3(SIZE / 2, SIZE / 2, 0);

    public GameScreen(final PixelGame core) {
        this.core = core;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SIZE, SIZE);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (SIZE / 10);

        gameView = new GameView(new Game(), camera, core.generator.generateFont(parameter), SIZE / 20, SIZE);
        stage = new Stage(new ScreenViewport());

        music = Gdx.audio.newMusic(Gdx.files.internal(MusicCatalog.randomMusic()));
        music.setVolume(0.2f);
        music.setLooping(true);
        if (soundIsOn) {
            Gdx.audio.newSound(Gdx.files.internal("sound/click.mp3")).play();
            music.play();
        }
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }
        gameView.update(new Vector2(touchPos.x, touchPos.y), delta);
        gameView.draw();
        if (gameView.game.isFinished()) {
            core.setScreen(new GameOverScreen(core, gameView.game.getScore()));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        gameView.dispose();
        stage.dispose();
        music.dispose();
    }
}
