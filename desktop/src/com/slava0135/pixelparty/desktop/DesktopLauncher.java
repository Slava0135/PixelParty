package com.slava0135.pixelparty.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slava0135.pixelparty.PixelGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pixel Party";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new PixelGame(), config);
	}
}
