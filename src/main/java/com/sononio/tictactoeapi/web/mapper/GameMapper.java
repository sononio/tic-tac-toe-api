package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.core.Game;
import com.sononio.tictactoeapi.web.dto.GameDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FieldMapper.class, TurnMapper.class})
public interface GameMapper {
    GameDto toDto(Game entity);
}
