package com.sononio.tictactoeapi.web.dto;

import java.util.Optional;

public record GameDto(
        FieldDto field,
        GameSettingsDto settings,
        GameStateDto state,
        GameResultDto result,
        int currentTurn,
        Optional<TurnDto> lastTurn) {
}
