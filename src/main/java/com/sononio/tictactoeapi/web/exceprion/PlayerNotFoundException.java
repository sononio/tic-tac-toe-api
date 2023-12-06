package com.sononio.tictactoeapi.web.exceprion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "No such player")
public class PlayerNotFoundException extends RuntimeException {
}
