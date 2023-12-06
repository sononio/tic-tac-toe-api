package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.matchmaking.model.PlayerStatus;
import com.sononio.tictactoeapi.web.dto.PlayerStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerStatusMapper {
    PlayerStatusDto toDto(PlayerStatus entity);
}
