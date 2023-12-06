package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.core.Cell;
import com.sononio.tictactoeapi.web.dto.CellDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CellMapper {
    CellDto toDto(Cell entity);
}
