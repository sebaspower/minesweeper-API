package com.api.minesweeper.data.entity;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Table(name="games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Min(1)
    @Max(80)
    private int totalRow;

    @Min(1)
    @Max(80)
    private int totalCol;

    @Min(1)
    @Max(1000)
    private int totalMines;

    private int totalPossibleMines;

    @Column(name="user_id")
    private long userId;

    @Column(columnDefinition = "boolean default false")
    private boolean finished;

    @Column(columnDefinition = "boolean default false")
    private boolean isGameOver;

    @Column
    @Lob
    private Cell[][] board;

    @Column(name="time_started")
    private long started;

    public Game(){
    }

    public Game(int totalRow, int totalCol, int totalMines, long userId) {
        this.totalRow = totalRow;
        this.totalCol = totalCol;
        this.totalMines = totalMines;
        this.userId = userId;
        this.started = new Date().getTime();
        this.board = new Cell[totalRow][totalCol];
        this.totalPossibleMines = 0;
        for (int i=0; i<totalRow; i++){
            for(int j = 0; j< totalCol; j++)
                board[i][j]= new Cell();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int row) {
        this.totalRow = row;
    }

    public int getTotalCol() {
        return totalCol;
    }

    public void setTotalCol(int col) {
        this.totalCol = col;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public void setTotalMines(int mines) {
        this.totalMines = mines;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long user) {
        this.userId = user;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public long getStarted() {
        return started;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public int getTotalPossibleMines() {
        return totalPossibleMines;
    }

    public void increaseTotalPossibleMines() {
        this.totalPossibleMines = this.getTotalPossibleMines() + 1;
    }
    public void decreaseTotalPossibleMines() {
        totalPossibleMines= this.getTotalPossibleMines() - 1;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
