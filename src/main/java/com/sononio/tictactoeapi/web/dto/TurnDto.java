package com.sononio.tictactoeapi.web.dto;

public record TurnDto(
        SideDto side,
        CoordsDto coords
) {
}
