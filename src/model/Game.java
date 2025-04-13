package model;

public class Game {
    private Player human;
    private Player computer;
    private boolean isStandardGame;

    public Game(Player human, Player computer, boolean isStandardGame) {
        this.human = human;
        this.computer = computer;
        this.isStandardGame = isStandardGame;
    }

    public Player getHuman() {
        return human;
    }

    public Player getComputer() {
        return computer;
    }

    public boolean isStandardGame() {
        return isStandardGame;
    }

    public void setHuman(Player human) {
        this.human = human;
    }

    public void setComputer(Player computer) {
        this.computer = computer;
    }

    public void setStandardGame(boolean standardGame) {
        isStandardGame = standardGame;
    }
}