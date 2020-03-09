package com.slava0135.pixelparty.world;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Pallete {
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
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

    Color color;
    Pallete(Color color) {
        this.color = color;
    }

    private static final List<Pallete> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Pallete randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
