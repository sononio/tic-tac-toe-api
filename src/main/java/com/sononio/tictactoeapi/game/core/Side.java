package com.sononio.tictactoeapi.game.core;

public enum Side {
    X,
    O;

    public Side other() {
        return switch (this) {
            case X -> O;
            case O -> X;
        };
    }
}
