package com.sononio.tictactoeapi.web.controller;

import com.sononio.tictactoeapi.web.dto.GameDto;
import com.sononio.tictactoeapi.web.dto.GameListDto;
import com.sononio.tictactoeapi.web.mapper.GameMapper;
import com.sononio.tictactoeapi.web.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;

    @Operation(summary = "Получить список всех игр от новых к старым")
    @GetMapping
    public ResponseEntity<GameListDto> list() {
        return ResponseEntity.ok(new GameListDto(gameService.list()));
    }

    @Operation(summary = "Получить подробные данные об игре",
            description = "При использовании long polling ответ от сервера придет только когда в игре наступит ход " +
                    "с номером > указанного в longPollingLastTurn")
    @GetMapping("{id}")
    public DeferredResult<GameDto> get(
            @PathVariable @NotNull UUID id,
            @RequestParam(required = false, defaultValue = "false") boolean longPollingEnabled,
            @RequestParam(required = false) Optional<Long> longPollingTimeout,
            @RequestParam(required = false, defaultValue = "0") int longPollingLastTurn
    ) {
        val result = new DeferredResult<GameDto>();
        val game = longPollingEnabled
                ? gameService.getWithPolling(id, longPollingLastTurn,longPollingTimeout)
                : gameService.get(id);

        result.setResult(gameMapper.toDto(game));
        return result;
    }
}
