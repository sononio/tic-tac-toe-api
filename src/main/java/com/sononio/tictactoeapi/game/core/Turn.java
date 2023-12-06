package com.sononio.tictactoeapi.game.core;

public record Turn(
        int n,
        Side side,
        Coords coords
) {
}
