package com.sononio.tictactoeapi.game.core.exception;

import com.sononio.tictactoeapi.game.core.Cell;

public class WrongMoveException extends PlayerIdiotException {
    public WrongMoveException(int x, int y, Cell currentValue) {
        super(String.format("You cannot set to {%s, %s} because its current value is '%s'", x, y, currentValue));
    }
}
