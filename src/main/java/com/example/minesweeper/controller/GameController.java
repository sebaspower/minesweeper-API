package com.example.minesweeper.controller;

import com.example.minesweeper.data.entity.Game;
import com.example.minesweeper.business.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

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

    @PostMapping
    public Game post(@RequestBody Game request){

        if (request != null){
            return gameService.createGame(request);
        }
        return null;
    }

    @PutMapping("/{gameId}/mine/{x},{y}")
    Game put(@PathVariable("gameId") long gameId,
             @PathVariable("x") @Validated int row,
             @PathVariable("y") @Validated int col ) throws Exception{
        return gameService.setPossibleMine(gameId, row, col);
    }

    @PutMapping("/{gameId}/show/{row},{col}")
    Game show(@PathVariable("gameId") long gameId,
             @PathVariable("row") @Validated int row,
             @PathVariable("col") @Validated int col ) throws Exception{
        return gameService.showCell(gameId, row, col);
    }

//    @GetMapping("/{gameId}")
//    Game get(@PathVariable long gameId) throws Exception {
//        return gameService.findById(gameId);
//    }

    @GetMapping("resume/{userId}")
    Game one(@PathVariable long userId) throws Exception {
        return gameService.findByUserId(userId);
    }
}
