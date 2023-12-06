package com.sononio.tictactoeapi.web.dto;

import java.util.List;
import java.util.UUID;

public record GameListDto(List<UUID> ids) {
}
