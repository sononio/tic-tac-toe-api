package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.core.Side;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerStatus;
import com.sononio.tictactoeapi.web.dto.PlayerDto;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class PlayerMapper {
    @Autowired
    protected PlayerStatusMapper playerStatusMapper;

    @Autowired
    protected SideMapper sideMapper;

    public PlayerDto toDto(UUID id, PlayerStatus status, Side side) {
        return new PlayerDto(id, playerStatusMapper.toDto(status), sideMapper.toDto(side));
    }
}
