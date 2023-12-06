package com.sononio.tictactoeapi.web.dto;

import java.util.UUID;

public record PlayerDto(
        UUID id,
        PlayerStatusDto status,
        SideDto side
) {

}
