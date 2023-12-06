package com.sononio.tictactoeapi.game.core.exception;

public class WrongCoordinatesException extends PlayerIdiotException {
    public WrongCoordinatesException(int xMax, int yMax) {
        super(String.format("Coordinates out of range. Valid range is: ([0;%s], [0;%s])", xMax, yMax));
    }
}
