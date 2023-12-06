package com.sononio.tictactoeapi.web.dto;

public record FieldDto(
        CellDto[][] cells,
        int width,
        int height) {
}