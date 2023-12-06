package com.sononio.tictactoeapi.web.dto;

public record GameSettingsDto(
        int width,
        int height,
        int winCondition) {
}
