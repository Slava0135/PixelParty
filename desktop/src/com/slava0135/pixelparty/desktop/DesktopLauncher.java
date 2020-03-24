package com.slava0135.pixelparty.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.slava0135.pixelparty.PixelGame;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Pixel Party");

		glfwDefaultWindowHints();
		glfwInit();
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int width, height;
		height = width = (int) (0.9f * vidmode.height());
		config.setWindowedMode(width, height);
		config.setWindowSizeLimits(width, height, width, height);
		new Lwjgl3Application(new PixelGame(), config);
	}
}
