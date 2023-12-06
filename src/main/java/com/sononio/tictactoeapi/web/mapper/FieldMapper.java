package com.sononio.tictactoeapi.web.mapper;

import com.sononio.tictactoeapi.game.core.Field;
import com.sononio.tictactoeapi.web.dto.CellDto;
import com.sononio.tictactoeapi.web.dto.FieldDto;
import lombok.val;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FieldMapper {
    @Autowired
    protected CellMapper cellMapper;

    public FieldDto toDto(Field field) {
        val cells = new CellDto[field.getWidth()][];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new CellDto[field.getHeight()];
        }

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = cellMapper.toDto(field.get(i, j));
            }
        }

        return new FieldDto(cells, field.getWidth(), field.getHeight());
    }
}
