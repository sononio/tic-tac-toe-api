package com.sononio.tictactoeapi.game.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameWinConditionTest {

    @ParameterizedTest
    @MethodSource({
            "move_winConditionSource_3x3_3",
            "move_winConditionSource_4x4_4",
            "move_winConditionSource_5x5_3",
            "move_winConditionSource_5x5_5",
            "move_winConditionSource_10x10_5",
            "move_winConditionSource_5x10_5",
            "move_winConditionSource_10x5_5",
            "move_winConditionSource_15x15_5",
    })
    void move_winCondition(List<Coords> moves, GameSettings gameSettings, GameResult result) {
        Game game = new Game(gameSettings);
        Side side = Side.X;

        for (Coords move : moves) {
            game.move(side, move.x(), move.y());
            side = side.other();
        }

        assertEquals(result, game.getResult());
    }

    public static Stream<Arguments> move_winConditionSource_3x3_3() {
        return move_winConditionSource(new GameSettings(3, 3, 3));
    }

    public static Stream<Arguments> move_winConditionSource_4x4_4() {
        return move_winConditionSource(new GameSettings(4, 4, 4));
    }

    public static Stream<Arguments> move_winConditionSource_5x5_3() {
        return move_winConditionSource(new GameSettings(5, 5, 3));
    }

    public static Stream<Arguments> move_winConditionSource_5x5_5() {
        return move_winConditionSource(new GameSettings(5, 5, 5));
    }

    public static Stream<Arguments> move_winConditionSource_10x10_5() {
        return move_winConditionSource(new GameSettings(10, 10, 5));
    }

    public static Stream<Arguments> move_winConditionSource_5x10_5() {
        return move_winConditionSource(new GameSettings(5, 10, 5));
    }

    public static Stream<Arguments> move_winConditionSource_10x5_5() {
        return move_winConditionSource(new GameSettings(10, 5, 5));
    }

    public static Stream<Arguments> move_winConditionSource_15x15_5() {
        return move_winConditionSource(new GameSettings(15, 15, 5));
    }

    public static Stream<Arguments> move_winConditionSource(GameSettings gameSettings) {
        return Stream.of(move_winCondition_xWinSource(gameSettings), move_winCondition_oWinSource(gameSettings))
                .flatMap(Function.identity());
    }

    public static Stream<Arguments> move_winCondition_xWinSource(GameSettings gameSettings) {
        return Stream.of(movesOfXWin(gameSettings))
                .flatMap(Function.identity())
                .map(moves -> Arguments.of(moves, gameSettings, GameResult.X_WIN));
    }

    public static Stream<Arguments> move_winCondition_oWinSource(GameSettings gameSettings) {
        return Stream.of(movesOfXWin(gameSettings))
                .flatMap(Function.identity())
                .map(moves -> shiftMoves(moves, gameSettings))
                .map(moves -> Arguments.of(moves, gameSettings, GameResult.O_WIN));
    }

    public static List<Coords> shiftMoves(List<Coords> moves, GameSettings gameSettings) {
        Set<Coords> movesSet = new HashSet<>(moves);
        List<Coords> shiftedMoves = new ArrayList<>(moves.size() + 1);

        loop:
        for (int i = gameSettings.width() - 1; i >= 0; i--) {
            for (int j = 0; j < gameSettings.height(); j++) {
                Coords candidate = new Coords(i, j);
                if (!movesSet.contains(candidate)) {
                    shiftedMoves.add(0, candidate);
                    break loop;
                }
            }
        }

        shiftedMoves.addAll(moves);
        return shiftedMoves;
    }

    public static Stream<Arguments> move_winCondition_drawSource(GameSettings gameSettings) {
        return Stream.empty();
    }

    public static Stream<List<Coords>> movesOfXWin(GameSettings gameSettings) {
        return Stream.of(movesOfXWinHorizontal(gameSettings),
                movesOfXWinVertical(gameSettings),
                movesOfXWinDiagonal1(gameSettings),
                movesOfXWinDiagonal2(gameSettings))

                .flatMap(Function.identity())
                .map(moves -> addSecondPlayerMoves(moves, gameSettings));
    }

    public static List<Coords> movesOfDraw(GameSettings gameSettings) {
//        List<Coords> moves = new ArrayList<>();
//
//        for (int i = 0; i < gameSettings.width(); i++) {
//            for (int j = 0; j < gameSettings.height(); j++) {
//
//            }
//        }

        return List.of();
    }

    private static List<Coords> addSecondPlayerMoves(List<Coords> coords, GameSettings gameSettings) {
        if (coords.size() == 0)
            return new ArrayList<>();

        Iterator<Coords> emptyIterator = generateEmptyCells(coords, gameSettings).iterator();
        Iterator<Coords> movesIterator = coords.iterator();

        List<Coords> allMoves = new ArrayList<>();
        allMoves.add(movesIterator.next());

        while (movesIterator.hasNext()) {
            allMoves.add(emptyIterator.next());
            allMoves.add(movesIterator.next());
        }

        return allMoves;
    }

    public static Stream<List<Coords>> movesOfXWinHorizontal(GameSettings gameSettings) {
        if (gameSettings.width() < gameSettings.winCondition())
            return Stream.empty();

        List<Coords> startPoints = new ArrayList<>();
        for (int i = 0; i < gameSettings.height(); i++) {
            for (int j = 0; j <= gameSettings.width() - gameSettings.winCondition(); j++) {
                startPoints.add(new Coords(j, i));
            }
        }

        return startPoints.stream()
                .map(startPoint -> movesFromPoint(startPoint, 1, 0, gameSettings.winCondition()));
    }

    public static Stream<List<Coords>> movesOfXWinVertical(GameSettings gameSettings) {
        if (gameSettings.height() < gameSettings.winCondition())
            return Stream.empty();

        List<Coords> startPoints = new ArrayList<>();
        for (int i = 0; i < gameSettings.width(); i++) {
            for (int j = 0; j <= gameSettings.height() - gameSettings.winCondition(); j++) {
                startPoints.add(new Coords(i, j));
            }
        }

        return startPoints.stream()
                .map(startPoint -> movesFromPoint(startPoint, 0, 1, gameSettings.winCondition()));
    }

    public static Stream<List<Coords>> movesOfXWinDiagonal1(GameSettings gameSettings) {
        if (gameSettings.height() < gameSettings.winCondition() || gameSettings.width() < gameSettings.winCondition())
            return Stream.empty();

        List<Coords> startPoints = new ArrayList<>();
        for (int i = 0; i <= gameSettings.width() - gameSettings.winCondition(); i++) {
            for (int j = 0; j <= gameSettings.height() - gameSettings.winCondition(); j++) {
                startPoints.add(new Coords(i, j));
            }
        }

        return startPoints.stream()
                .map(startPoint -> movesFromPoint(startPoint, 1, 1, gameSettings.winCondition()));
    }

    public static Stream<List<Coords>> movesOfXWinDiagonal2(GameSettings gameSettings) {
        if (gameSettings.height() < gameSettings.winCondition() || gameSettings.width() < gameSettings.winCondition())
            return Stream.empty();

        List<Coords> startPoints = new ArrayList<>();
        for (int i = gameSettings.winCondition() - 1; i < gameSettings.width(); i++) {
            for (int j = 0; j <= gameSettings.height() - gameSettings.winCondition(); j++) {
                startPoints.add(new Coords(i, j));
            }
        }

        return startPoints.stream()
                .map(startPoint -> movesFromPoint(startPoint, -1, 1, gameSettings.winCondition()));
    }

    public static List<Coords> movesFromPoint(Coords start, int xDiff, int yDiff, int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> new Coords(
                        start.x() + xDiff * i,
                        start.y() + yDiff * i))
                .toList();
    }

    public static Set<Coords> generateEmptyCells(List<Coords> moves, GameSettings gameSettings) {
        Set<Coords> empty = new HashSet<>();

        for (int i = 0; i < gameSettings.width(); i++) {
            for (int j = 0; j < gameSettings.height(); j++) {
                empty.add(new Coords(i, j));
            }
        }

        moves.forEach(empty::remove);
        return empty;
    }
}
