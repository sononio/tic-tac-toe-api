package com.sononio.tictactoeapi.game.core;

public enum Cell {
    X,
    O,
    EMPTY;

    public static Cell of(Side side) {
        return switch (side) {
            case X -> X;
            case O -> O;
        };
    }
}
