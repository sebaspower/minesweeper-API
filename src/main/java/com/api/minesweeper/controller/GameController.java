package com.example.minesweeper.controller;

import com.example.minesweeper.data.entity.Game;
import com.example.minesweeper.business.service.GameService;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    public Game post(@Valid @RequestBody Game game){
        return gameService.createGame(game);
    }

    @PutMapping("/{gameId}/mine/{x},{y}")
    Game put(@PathVariable("gameId") long gameId,
             @PathVariable("x") @Min(0) @Validated int row,
             @PathVariable("y") @Min(0) @Validated int col ){
        Game game = gameService.setPossibleMine(gameId, row, col);
        if (game == null)
            throw new NotFoundException("None Game Id: "+ gameId);
        return game;
    }

    @PutMapping("/{gameId}/show/{row},{col}")
    Game show(@PathVariable("gameId") long gameId,
             @PathVariable("row") @Min(0) @Validated int row,
             @PathVariable("col") @Min(0) @Validated int col ){
        Game game = gameService.showCell(gameId, row, col);
        if (game == null)
            throw new NotFoundException("None Game Id: "+ gameId);
        return game;
    }

    @GetMapping("/{gameId}")
    Game one(@PathVariable long gameId) throws Exception {
        Game game = gameService.findById(gameId);
        if (game == null)
            throw new NotFoundException("None Game Id: "+ gameId);
        return game;
    }

    @GetMapping("resume/{userId}")
    Game resume(@PathVariable long userId) {
        Game game =gameService.resume(userId);
        if (game == null)
            throw new NotFoundException("None Game available to resume for User Id: "+ userId);
         return game;
    }
}
