package com.sononio.tictactoeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicTacToeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeApiApplication.class, args);
    }

}
