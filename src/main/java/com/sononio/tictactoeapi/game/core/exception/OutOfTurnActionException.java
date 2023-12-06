package com.sononio.tictactoeapi.game.core.exception;

import com.sononio.tictactoeapi.game.core.GameState;

public class OutOfTurnActionException extends PlayerIdiotException {
    public OutOfTurnActionException(GameState gameState) {
        super(String.format("You cannot act now because current game state is: '%s'", gameState));
    }
}
