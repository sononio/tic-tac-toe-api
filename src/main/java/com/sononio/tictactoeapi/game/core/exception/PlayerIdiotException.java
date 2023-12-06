package com.sononio.tictactoeapi.game.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerIdiotException extends RuntimeException {
    public PlayerIdiotException(String message) {
        super(message);
    }
}
