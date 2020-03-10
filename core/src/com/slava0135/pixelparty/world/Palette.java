package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Palette {
    //WHITE(Color.WHITE), is background (null)
    GRAY(Color.GRAY),
    LIME(Color.LIME),
    RED(Color.RED),
    ORANGE(Color.ORANGE),
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    PURPLE(Color.PURPLE),
    PINK(Color.PINK),
    BLUE(Color.BLUE),
    BROWN(Color.BROWN),
    CYAN(Color.CYAN),
    TEAL(Color.TEAL);

    public Color color;
    Palette(Color color) {
        this.color = color;
    }

    private static final List<Palette> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Palette randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
