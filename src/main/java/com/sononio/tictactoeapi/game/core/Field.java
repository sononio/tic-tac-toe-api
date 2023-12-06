package com.sononio.tictactoeapi.game.core;

import lombok.Getter;

import java.util.Arrays;

public class Field {
    @Getter
    private final int width;
    @Getter
    private final int height;
    private final Cell[][] cells;

    public Field(int width, int height) {
        assert width > 1;
        assert height > 1;

        this.width = width;
        this.height = height;

        cells = new Cell[width][];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell[height];
        }

        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.EMPTY);
        }
    }

    public Cell get(int x, int y) {
        return cells[x][y];
    }

    public void set(int x, int y, Cell cell) {
        cells[x][y] = cell;
    }

    public boolean hasEmptyCells() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell == Cell.EMPTY)
                    return true;
            }
        }

        return false;
    }
}
