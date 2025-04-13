package model;
/**
 * Representa un jugador en el juego
 * Cada jugador tiene un nombre, un tablero donde coloca sus barcos y un contador de partidas ganadas.
 */
public class Player {
    private String name;
    private Board board;
    private int gamesWon;

    public Player(String name) {
        this.name = name;
        this.board = new Board();
        this.gamesWon = 0;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}