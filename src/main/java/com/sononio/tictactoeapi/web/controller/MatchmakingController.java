package com.sononio.tictactoeapi.web.controller;

import com.sononio.tictactoeapi.game.matchmaking.model.PlayerStatus;
import com.sononio.tictactoeapi.web.dto.PlayerDto;
import com.sononio.tictactoeapi.web.dto.RegisterInfoDto;
import com.sononio.tictactoeapi.web.mapper.PlayerMapper;
import com.sononio.tictactoeapi.web.service.MatchmakingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matchmaking/queue")
@RequiredArgsConstructor
@Slf4j
public class MatchmakingController {
    private final MatchmakingService matchmakingService;

    private final PlayerMapper playerMapper;

    @Operation(summary = "Регистрирует в очереди матчмейкинга и возвращает сущность игрока")
    @ApiResponse(responseCode = "200", description = "Игрок успешно зарегистрирован")
    @PostMapping
    public ResponseEntity<PlayerDto> register(@RequestBody @Valid RegisterInfoDto registerInfoDto) {
        val player = matchmakingService.searchForGame(registerInfoDto.name());

        return ResponseEntity.ok(playerMapper.toDto(player.id(), PlayerStatus.IN_QUEUE, null));
    }

    @Operation(summary = "Удаляет игрока из матчмейкинга")
    @ApiResponse(responseCode = "204", description = "Игрок был удален")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> leave(@PathVariable @NotNull UUID id) {
        matchmakingService.leaveSearching(id);
        return ResponseEntity.status(204).build();
    }
}
