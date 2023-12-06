package com.sononio.tictactoeapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterInfoDto(
        @NotBlank @Size(min = 3, max = 16)
        String name
) {
}
