package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.core.Turn;
import com.sononio.tictactoeapi.web.dto.TurnDto;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TurnMapper {
    TurnDto toDto(Turn entity);
    default Optional<TurnDto> toDto(Optional<Turn> entity) {
        return entity.map(this::toDto);
    }
}
