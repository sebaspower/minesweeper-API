package com.api.minesweeper.business.service;

import com.api.minesweeper.data.repository.GameRepository;
import com.api.minesweeper.data.entity.Game;
import com.api.minesweeper.data.entity.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public Game showCell(long id, int row, int col){
        Game game = findById(id);
        if ((game != null ) && (game.isGameOver() == false)) {
            if ((game != null) && (row <= game.getTotalRow() && col <= game.getTotalCol())) {
                game = updateClickGame(game, row, col);
            }
            gameRepository.save(game);
        }
        return game;
    }

    public Game resume(long userId){
        return gameRepository.getFirstByUserIdAndFinishedOrderByStartedDesc(userId, false);
    }

    public Game setPossibleMine(long id, int row, int col){
        Game game = findById(id);
        if ((game != null) && (game.isGameOver() == false)) {
            if ((game != null) && (row <= game.getTotalRow() && col <= game.getTotalCol())) {
                game = updateMineGame(game, row, col);
                gameRepository.save(game);
            }
        }
        return game;
    }

    public Game findById(long gameId){
        return gameRepository.findById(gameId);
    }

    public Game createGame(Game game){
        return createGame(game.getTotalRow(),game.getTotalCol(), game.getTotalMines(), game.getUserId());
    }

    private Game createGame(int row, int col, int mines, long userId){
        Game game = null;
        List<int[]> minesList;

        // At least one empty cell.
        if( mines < (row * col) -1) {
            game = new Game(row, col, mines, userId);
            game = populateMines(game);
            gameRepository.save(game);
        }
        return game;
    }

    private Game populateMines(Game game) {
        Random randNumber = new Random();
        int randRow, randCol, minesCreated = 0;

        Cell[][] board = game.getBoard();
        while (minesCreated < game.getTotalMines()) {
            randRow = randNumber.nextInt(game.getTotalRow());
            randCol = randNumber.nextInt(game.getTotalCol());

            if (board[randRow][randCol].isHasMine() == false) {
                board[randRow][randCol].setHasMine(true);
                minesCreated = minesCreated + 1;
                board = getUpdateNearMinesBoard(game, randRow, randCol);
            }
        }
        game.setBoard(board);
        return game;
    }

    private Cell[][] getUpdateNearMinesBoard(Game game, int rowMine, int colMine){
        Cell[][] board = game.getBoard();

        int totalCol = game.getTotalCol();
        int totalRow = game.getTotalRow();
        for (int x = (rowMine == 0) ? 0 : rowMine-1; (x <= rowMine+1 && x < totalRow); x++){
            for(int y = (colMine == 0) ? 0 : colMine-1; (y <= colMine +1 && y < totalCol); y++){
                if(!((x == rowMine) && (y == colMine)))
                    board[x][y].incNearMines();
            }
        }
        return board;
    }

    private List<int[]> getAdjacents(int totalRow, int totalCol, int row, int col){
        List<int[]> list = new ArrayList<>();
        for (int x = (row == 0) ? 0 : row-1; (x <= row+1 && x < totalRow); x++){
            for(int y = (col == 0) ? 0 : col-1; (y <= col +1 && y < totalCol); y++){
                if(!((x == row) && (y == col))) {
                    list.add(new int[]{x, y});
                }
            }
        }
        return list;
    }

    private Cell[][] showAdjacentCells (Cell[][] board, int totalRow, int totalCol, int row, int col){
        List<int[]> adjacents;
        if ((board[row][col].isShow() == false) &&
                (board[row][col].getNearMines() == 0) &&
                (board[row][col].isHasMine() == false)) {
                board[row][col].setShow(true);
                adjacents = getAdjacents(totalRow, totalCol, row, col);
                for(int[] cell: adjacents) board = showAdjacentCells(board, totalRow, totalCol, cell[0], cell[1]);
            }
        return board;
    }

    private Game updateClickGame(Game game, int row, int col){
        Cell[][] board = game.getBoard();
        if(board[row][col].isPossibleMine() == false) {
            if (board[row][col].isHasMine() == false) {
                if(board[row][col].getNearMines() == 0) {
                    board = showAdjacentCells(board, game.getTotalRow(), game.getTotalCol(), row, col);
                }else {
                    board[row][col].setShow(true);
                }
            } else {
                game.setFinished(true);
                game.setGameOver(true);
            }
            game.setBoard(board);
        }
        return game;
    }

    private Game updateMineGame(Game game, int row, int col){
        Cell[][] board = game.getBoard();

        // Only set the mine when is not shown
        if (board[row][col].isShow() == false) {
            if (board[row][col].isPossibleMine()){
                board[row][col].setPossibleMine(false);
                game.decreaseTotalPossibleMines();
            } else {
                if (game.getTotalPossibleMines() < game.getTotalMines()) {
                    board[row][col].setPossibleMine(true);
                    game.increaseTotalPossibleMines();
                }
            }
            game.setBoard(board);
        }
        return game;
    }
}
