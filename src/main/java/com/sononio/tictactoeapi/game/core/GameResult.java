package com.sononio.tictactoeapi.game.core;

public enum GameResult {
    X_WIN,
    O_WIN,
    DRAW;

    public static GameResult winOf(Side side) {
        return switch (side) {
            case X -> X_WIN;
            case O -> O_WIN;
        };
    }
}
