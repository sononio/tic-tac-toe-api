package com.sononio.tictactoeapi.game.matchmaking.model;

import java.util.UUID;

public record GameInfo(UUID gameId, UUID player1, UUID player2) {

}
