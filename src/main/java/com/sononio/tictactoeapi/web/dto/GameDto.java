package com.sononio.tictactoeapi.web.dto;

public record GameDto(
        FieldDto field,
        GameSettingsDto settings,
        GameStateDto state,
        GameResultDto result,
        int currentTurn) {
}
