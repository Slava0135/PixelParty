package com.slava0135.pixelparty.game;

public enum GameStage {
    WAIT, RUN, BREAK;
    private static GameStage[] vals = values();
    public GameStage next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}