package com.sononio.tictactoeapi.game.matchmaking.model;

import com.sononio.tictactoeapi.game.core.Game;
import com.sononio.tictactoeapi.game.core.Side;

public record PlayerInGame(PlayerInfo playerInfo, Game game, Side side) {
}
