package com.sononio.tictactoeapi.game.core;

public enum GameState {
    X_MOVE,
    O_MOVE,
    FINISHED;

    public static GameState ofSideTurn(Side side) {
        return switch (side) {
            case X -> X_MOVE;
            case O -> O_MOVE;
        };
    }

    public Side convertToSideUnsafe() {
        return switch (this) {
            case X_MOVE -> Side.X;
            case O_MOVE -> Side.O;
            case FINISHED -> throw new IllegalStateException("Cannot convert FINISHED state to side");
        };
    }
}
