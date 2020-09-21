package com.example.minesweeper.business.service;

import com.example.minesweeper.data.entity.Game;
import com.example.minesweeper.data.entity.Cell;
import com.example.minesweeper.data.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public Iterable<Game> lookup(){
        return gameRepository.findAll();
    }

    public Game findByUserId(long userId){
        return gameRepository.findByUserId(userId);
    }

    public Game showCell(long id, int row, int col){
        Game game = findById(id);
        if ((game != null) && (row <= game.getTotalRow() && col <= game.getTotalCol())) {
            game = updateClickGame(game, row, col);
        }
        return game;

    }

    public Game setPossibleMine(long id, int row, int col){
        Game game = findById(id);

        if ((game != null) && (row <= game.getTotalRow() && col <= game.getTotalCol())) {
            game = updateMineGame(game, row, col);
            gameRepository.save(game);
        }
        return game;
    }
    public Game findById(long gameId){
        return gameRepository.findById(gameId);
    }

    public Game createGame(Game request){
        Game game = new Game(request.getTotalRow(),request.getTotalCol(),request.getTotalMines(),request.getUserId());
        gameRepository.save(game);
        return game;

    }

    private Game updateClickGame(Game game, int row, int col){
        Cell[][] board = game.getBoard();
        board[row][col].setShow(true);
        game.setBoard(board);
        return game;
    }

    private Game updateMineGame(Game game, int row, int col){
        Cell[][] board = game.getBoard();

        if((board[row][col].isPossibleMine() == false) && (game.getTotalPossibleMines() < game.getTotalMines())){
            board[row][col].setPossibleMine(true);
            game.increaseTotalPossibleMines();
        } else if(board[row][col].isPossibleMine()) {
            board[row][col].setPossibleMine(false);
            game.decreaseTotalPossibleMines();
        }
        game.setBoard(board);
        return game;
    }

}
