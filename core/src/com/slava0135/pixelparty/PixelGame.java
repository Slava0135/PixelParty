package com.slava0135.pixelparty;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slava0135.pixelparty.Screens.MainMenuScreen;

public class PixelGame extends Game {

	static public Skin gameSkin;

	public void create() {
		gameSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}
