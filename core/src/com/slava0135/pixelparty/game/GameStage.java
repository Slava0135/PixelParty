package com.slava0135.pixelparty.game;

public enum GameStage {
    WAIT, RUN, BREAK;
    public static double length = 3;
    private static GameStage[] vals = values();
    public GameStage next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}