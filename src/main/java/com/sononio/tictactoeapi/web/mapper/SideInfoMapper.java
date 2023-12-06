package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.matchmaking.model.PlayerInGame;
import com.sononio.tictactoeapi.web.dto.SideInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SideMapper.class})
public interface SideInfoMapper {
    SideInfoDto toDto(PlayerInGame entity);
}
