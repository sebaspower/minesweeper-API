package com.example.minesweeper.controller;

import com.example.minesweeper.data.entity.Game;
import com.example.minesweeper.business.service.GameService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Iterable<Game> get() {
        return gameService.lookup();
    }

    @PostMapping("new")
    public Game post(@Valid @RequestBody Game game) {
        return gameService.createGame(game);
    }

    @PutMapping("/{gameId}/mine/{x},{y}")
    Game put(@PathVariable("gameId") long gameId,
             @PathVariable("x") @Min(0) @Validated int row,
             @PathVariable("y") @Min(0) @Validated int col ) throws Exception{
        return gameService.setPossibleMine(gameId, row, col);
    }

    @PutMapping("/{gameId}/show/{row},{col}")
    Game show(@PathVariable("gameId") long gameId,
             @PathVariable("row") @Min(0) @Validated int row,
             @PathVariable("col") @Min(0) @Validated int col ) throws Exception{
        return gameService.showCell(gameId, row, col);
    }

    @GetMapping("/{gameId}")
    Game one(@PathVariable long gameId) throws Exception {
        return gameService.findById(gameId);
    }

    @GetMapping("resume/{userId}")
    Game resume(@PathVariable long userId) throws Exception {
        return gameService.findByUserId(userId);
    }
}
