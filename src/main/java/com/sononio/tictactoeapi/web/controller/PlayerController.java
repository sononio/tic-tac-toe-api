package com.sononio.tictactoeapi.web.controller;

import com.sononio.tictactoeapi.game.core.Side;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerStatus;
import com.sononio.tictactoeapi.web.dto.CoordsDto;
import com.sononio.tictactoeapi.web.dto.GameDto;
import com.sononio.tictactoeapi.web.dto.PlayerDto;
import com.sononio.tictactoeapi.web.mapper.GameMapper;
import com.sononio.tictactoeapi.web.mapper.PlayerMapper;
import com.sononio.tictactoeapi.web.service.GameService;
import com.sononio.tictactoeapi.web.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
@Slf4j
public class PlayerController {
    private final PlayerService playerService;
    private final GameService gameService;

    private final PlayerMapper playerMapper;
    private final GameMapper gameMapper;

    @Operation(summary = "Получает статус игрока",
            description = "При использовании long polling ответ от сервера придет только когда статус игрока " +
                    "сменится на IN_GAME")
    @GetMapping("{id}")
    public DeferredResult<PlayerDto> get(
            @PathVariable @NotNull UUID id,
            @RequestParam(required = false, defaultValue = "false") boolean longPollingEnabled,
            @RequestParam(required = false) Optional<Long> longPollingTimeout) {

        val result = new DeferredResult<PlayerDto>();
        val status = longPollingEnabled
                ? playerService.getPlayerStatusWithPolling(id, longPollingTimeout)
                : playerService.getPlayerStatus(id);

        Side side = null;
        if (status == PlayerStatus.IN_GAME) {
            side = gameService.getPlayerInGame(id).side();
        }

        result.setResult(playerMapper.toDto(id, status, side));
        return result;
    }

    @Operation(summary = "Получает игру, связанную с игроком",
            description = "При использовании long polling ответ от сервера придет только когда ход перейдет " +
                    "к игроку или игра закончится")
    @GetMapping("{id}/game")
    public DeferredResult<GameDto> getGame(
            @PathVariable @NotNull UUID id,
            @RequestParam(required = false, defaultValue = "false") boolean longPollingEnabled,
            @RequestParam(required = false) Optional<Long> longPollingTimeout) {
        val result = new DeferredResult<GameDto>();
        val game = longPollingEnabled
                ? gameService.getByPlayerWithPolling(id, longPollingTimeout)
                : gameService.getByPlayer(id);

        result.setResult(gameMapper.toDto(game));
        return result;
    }

    @Operation(summary = "Сделать ход")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ход был совершен"),
            @ApiResponse(responseCode = "400", description = "Ход совершен некорректно")
    })
    @PostMapping("{id}/game/turn")
    public ResponseEntity<Void> move(
            @PathVariable @NotNull UUID id,
            @RequestBody @Valid CoordsDto coordsDto) {

        gameService.makeMove(id, coordsDto.x(), coordsDto.y());
        return ResponseEntity.noContent().build();
    }
}
