package com.slava0135.pixelparty.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slava0135.pixelparty.PixelGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pixel Party";
		config.width = 1000;
		config.height = 1000;
		config.resizable = false;
		new LwjglApplication(new PixelGame(), config);
	}
}
