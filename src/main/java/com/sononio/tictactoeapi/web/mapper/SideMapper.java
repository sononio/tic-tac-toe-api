package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.core.Side;
import com.sononio.tictactoeapi.web.dto.SideDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SideMapper {
    SideDto toDto(Side entity);
}
