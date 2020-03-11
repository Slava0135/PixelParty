package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Palette {
    //WHITE(Color.WHITE, "White"), is background (null)
    GRAY(Color.GRAY, "Gray"),
    LIME(Color.LIME, "Lime"),
    RED(Color.RED, "Red"),
    ORANGE(Color.ORANGE, "Orange"),
    YELLOW(Color.YELLOW, "Yellow"),
    GREEN(Color.GREEN, "Green"),
    PURPLE(Color.PURPLE, "Purple"),
    PINK(Color.PINK, "Pink"),
    BLUE(Color.BLUE, "Blue"),
    BROWN(Color.BROWN, "Brown"),
    CYAN(Color.CYAN, "Cyan"),
    TEAL(Color.TEAL, "Teal");

    public Color color;
    public String name;
    Palette(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    private static final List<Palette> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Palette randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
