package com.sononio.tictactoeapi.game.core;

import com.sononio.tictactoeapi.game.core.exception.OutOfTurnActionException;
import com.sononio.tictactoeapi.game.core.exception.WrongCoordinatesException;
import com.sononio.tictactoeapi.game.core.exception.WrongMoveException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class Game {
    @Getter
    private final UUID uuid = UUID.randomUUID();
    @Getter
    private final Field field;
    @Getter
    private final GameSettings settings;
    @Getter
    private final List<Turn> turnHistory = new ArrayList<>();
    @Getter
    private GameState state;
    @Getter
    private GameResult result;

    public Game(GameSettings settings) {
        this.field = new Field(settings.width(), settings.height());
        this.settings = settings;
        this.state = GameState.X_MOVE;
    }

    public synchronized GameState move(Side side, int x, int y) {
        checkMove(side, x, y);
        changeField(side, x, y);
        changeGameState();
        return state;
    }

    public int getCurrentTurn() {
        return turnHistory.size() + 1;
    }

    public Optional<Turn> getLastTurn() {
        if (turnHistory.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(turnHistory.get(turnHistory.size() - 1));
    }

    private void changeField(Side side, int x, int y) {
        field.set(x, y, Cell.of(side));
        this.turnHistory.add(new Turn(turnHistory.size() + 1, side, new Coords(x, y)));
    }

    private void changeGameState() {
        if (isWinner(state.convertToSideUnsafe())) {
            finishWithWin(state.convertToSideUnsafe());
        } else if (isDraw()) {
            finishWithDraw();
        } else {
            nextMove();
        }
    }

    private void nextMove() {
        this.state = GameState.ofSideTurn(state.convertToSideUnsafe().other());
    }

    private void finishWithWin(Side winner) {
        this.result = GameResult.winOf(winner);
        this.state = GameState.FINISHED;
    }

    private void finishWithDraw() {
        this.result = GameResult.DRAW;
        this.state = GameState.FINISHED;
    }

    private boolean isDraw() {
        return !field.hasEmptyCells();
    }

    private boolean isWinner(Side side) {
        return checkWinConditionOnField(side, 1, 0)
                || checkWinConditionOnField(side, 0, 1)
                || checkWinConditionOnField(side, 1, 1)
                || checkWinConditionOnField(side, 1, -1);
    }

    private boolean checkWinConditionOnField(Side side, int xDiff, int yDiff) {
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeight(); j++) {
                if (checkWinConditionOnLine(side, i, j, xDiff, yDiff))
                    return true;
            }
        }

        return false;
    }

    private boolean checkWinConditionOnLine(Side side, int xStart, int yStart, int xDiff, int yDiff) {
        int currentPlayerCells = 0;
        int k = 0;
        int x = xStart;
        int y = yStart;

        while (x >= 0 && x < field.getWidth() && y >= 0 && y < field.getHeight()) {
            if (field.get(x, y) == Cell.of(side))
                currentPlayerCells++;
            else
                currentPlayerCells = 0;

            if (currentPlayerCells >= settings.winCondition())
                return true;

            k++;
            x = xStart + xDiff * k;
            y = yStart + yDiff * k;
        }

        return false;
    }

    private void checkMove(Side side, int x, int y) {
        if (!canAct(side))
            throw new OutOfTurnActionException(state);

        if (x < 0 || x >= field.getWidth() || y < 0 || y >= field.getHeight()) {
            throw new WrongCoordinatesException(field.getWidth(), field.getHeight());
        }

        if (field.get(x, y) != Cell.EMPTY) {
            throw new WrongMoveException(x, y, field.get(x, y));
        }
    }

    private boolean canAct(Side side) {
        return state == GameState.X_MOVE && side == Side.X ||
                state == GameState.O_MOVE && side == Side.O;
    }
}
