package com.slava0135.pixelparty.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.slava0135.pixelparty.PixelGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Pixel Party");
		config.setWindowedMode(1000, 1000);
		config.setWindowSizeLimits(1000, 1000, 1000, 1000);
		new Lwjgl3Application(new PixelGame(), config);
	}
}
