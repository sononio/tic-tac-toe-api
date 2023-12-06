package com.sononio.tictactoeapi.game.core;

import com.sononio.tictactoeapi.game.core.exception.OutOfTurnActionException;
import com.sononio.tictactoeapi.game.core.exception.WrongCoordinatesException;
import com.sononio.tictactoeapi.game.core.exception.WrongMoveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("Can do correct move")
    void move_ok() {
        Game game = new Game(new GameSettings(3, 3, 3));
        assertDoesNotThrow(() -> game.move(Side.X, 1, 1));
    }

    @Test
    @DisplayName("Cannot do move out of turn")
    void move_outOfTurn() {
        Game game = new Game(new GameSettings(3, 3, 3));
        assertAll(
                () -> assertThrows(OutOfTurnActionException.class, () -> game.move(Side.O, 1, 1)),
                () -> assertEquals(GameState.X_MOVE, game.getState())
        );
    }

    @Test
    @DisplayName("Cannot do move out of field")
    void move_outOfField() {
        Game game = new Game(new GameSettings(3, 3, 3));
        assertAll(
                () -> assertThrows(WrongCoordinatesException.class, () -> game.move(Side.X, 5, 5)),
                () -> assertEquals(GameState.X_MOVE, game.getState())
        );
    }

    @Test
    @DisplayName("Cannot do move to not empty cell")
    void move_repeatedCell() {
        Game game = new Game(new GameSettings(3, 3, 3));
        game.move(Side.X, 1, 1);
        assertAll(
                () -> assertThrows(WrongMoveException.class, () -> game.move(Side.O, 1, 1)),
                () -> assertEquals(GameState.O_MOVE, game.getState())
        );
    }

    @Test
    @DisplayName("Returns actual state")
    void getState() {
        Game game = new Game(new GameSettings(3, 3, 3));
        GameState state = game.move(Side.X, 1, 1);
        assertEquals(GameState.O_MOVE, state);
    }

    @Test
    void getResult_gameNotFinished() {
        Game game = new Game(new GameSettings(3, 3, 3));
        assertNull(game.getResult());
    }

    @Test
    void getResult_correctResult() {
        Game game = new Game(new GameSettings(2, 2, 1));
        game.move(Side.X, 0, 0);

        assertAll(
                () -> assertEquals(GameResult.X_WIN, game.getResult()),
                () -> assertEquals(GameState.FINISHED, game.getState())
        );
    }
}