package com.example.minesweeper.data.entity;

import javax.persistence.Entity;
import java.io.Serializable;

public class Cell implements Serializable {

    private boolean show;
    private boolean hasMine;
    private boolean possibleMine;
    private int nearMines;

    public Cell(){
        this.show = false;
        this.nearMines = 0;
    }

    public int getNearMines() {
        return nearMines;
    }

    public void incNearMines(int nearMines) {
        this.nearMines = this.nearMines + 1 ;
    }


    public boolean isHasMine() {
        return hasMine;
    }

    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isPossibleMine() {
        return possibleMine;
    }

    public void setPossibleMine(boolean possibleMine) {
        this.possibleMine = possibleMine;
    }
}
