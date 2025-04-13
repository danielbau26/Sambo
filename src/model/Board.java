package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] grid;
    private List<Ship> ships;

    public Board() {
        this.grid = new int[10][10]; // 0=water, 1=ship, 2=hit, 3=sunk
        this.ships = new ArrayList<>();
    }

    public int[][] getGrid() {
        return grid;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}
