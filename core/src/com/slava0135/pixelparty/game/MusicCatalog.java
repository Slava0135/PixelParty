package com.slava0135.pixelparty.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MusicCatalog {
    EDM_DETECTION_MODE("music/edm-detection-mode-by-kevin-macleod-from-filmmusic-io.mp3"),
    RAVING_ENERGY_FASTER("music/raving-energy-faster-by-kevin-macleod-from-filmmusic-io.mp3"),
    REALIZER("music/realizer-by-kevin-macleod-from-filmmusic-io.mp3"),
    WERQ("music/werq-by-kevin-macleod-from-filmmusic-io.mp3");

    private final String path;
    MusicCatalog(String path) {
        this.path = path;
    }

    private static Random RANDOM = new Random();
    private static final List<MusicCatalog> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    public static String randomMusic()  {
        return VALUES.get(RANDOM.nextInt(SIZE)).path;
    }
}
