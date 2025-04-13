package model;

import java.util.Random;

public class Controller {
    public Game currentGame;
    private Random random;

    public Controller() {
        this.random = new Random();
    }

    public void createStandardGame() {
        Player human = new Player("Human");
        Player computer = new Player("Computer");
        this.currentGame = new Game(human, computer, true);
        setupComputerShips(true);
    }

    public void createCustomGame() {
        Player human = new Player("Human");
        Player computer = new Player("Computer");
        this.currentGame = new Game(human, computer, false);
        setupComputerShips(false);
    }

    public void setupComputerShips(boolean isStandard) {
        if (isStandard) {
            addComputerShip(1, "Patrol Boat", false);
            addComputerShip(2, "Destroyer", true);
            addComputerShip(3, "Submarine", false);
            addComputerShip(3, "Cruiser", true);
            addComputerShip(4, "Battleship", false);
            addComputerShip(5, "Carrier", true);
        } else {
            int shipCount = random.nextInt(10) + 1;
            for (int i = 1; i <= shipCount; i++) {
                int length = random.nextInt(5) + 1;
                boolean vertical = random.nextBoolean();
                addComputerShip(length, "Ship " + i, vertical);
            }
        }
    }

    public void addComputerShip(int length, String name, boolean vertical) {
        int x= 0; 
        int y = 0;
        do {
            x = random.nextInt(10);
            y = random.nextInt(10);

            if (vertical && y + length > 10) y = 10 - length;
            if (!vertical && x + length > 10) x = 10 - length;
        } while (!isValidShipPlacement(x, y, length, vertical, currentGame.getComputer()));

        Ship ship = new Ship(name, x, y, length, vertical);
        for (int i = 0; i < length; i++) {
            int coordX;
            int coordY;
            if (vertical) {
                coordX = x;
                coordY = y + i;
            } else {
                coordX = x + i;
                coordY = y;
            }
            ship.getCoordinates().add(new Coordinate(coordX, coordY));
            currentGame.getComputer().getBoard().getGrid()[coordY][coordX] = 1;
        }
        currentGame.getComputer().getBoard().getShips().add(ship);
    }

    public void addPlayerShip(int length, boolean vertical, int x, int y, String name) {
        Ship ship = new Ship(name, x, y, length, vertical);
        for (int i = 0; i < length; i++) {
            int coordX;
            int coordY;
            if (vertical) {
                coordX = x;
                coordY = y + i;
            } else {
                coordX = x + i;
                coordY = y;
            }
            ship.getCoordinates().add(new Coordinate(coordX, coordY));
        }
        currentGame.getHuman().getBoard().getShips().add(ship);
    }

    public boolean isValidShipPlacement(int x, int y, int length, boolean vertical, Player player) {
        for (int i = 0; i < length; i++) {
            int checkX;
            int checkY;
            if (vertical) {
                checkX = x;
                checkY = y + i;
            } else {
                checkX = x + i;
                checkY = y;
            }

            if (checkX < 0 || checkX >= 10 || checkY < 0 || checkY >= 10) {
                return false;
            }

            for (Ship ship : player.getBoard().getShips()) {
                for (Coordinate coord : ship.getCoordinates()) {
                    if (coord.getX() == checkX && coord.getY() == checkY) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String playerAttack(int x, int y) {
        Player computer = currentGame.getComputer();
        boolean hit = false;
        boolean sunk = false;
        Ship hitShip = null;

        for (Ship ship : computer.getBoard().getShips()) {
            for (Coordinate coord : ship.getCoordinates()) {
                if (coord.getX() == x && coord.getY() == y) {
                    hit = true;
                    computer.getBoard().getGrid()[y][x] = 2;
                    hitShip = ship;
                    break;
                }
            }
            if (hit) break;
        }

        if (!hit) {
            computer.getBoard().getGrid()[y][x] = 2;
            return "missed";
        }

        sunk = true;
        for (Coordinate coord : hitShip.getCoordinates()) {
            if (computer.getBoard().getGrid()[coord.getY()][coord.getX()] != 2) {
                sunk = false;
                break;
            }
        }

        if (sunk) {
            hitShip.setState(ShipState.SUNKEN);
            for (Coordinate coord : hitShip.getCoordinates()) {
                computer.getBoard().getGrid()[coord.getY()][coord.getX()] = 3;
            }
            return "---Hit " + hitShip.getName() + " ---sunk";
        }

        return "---Hit";
    }

    public String computerAttack() {
        Player human = currentGame.getHuman();
        int x, y;

        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
        } while (human.getBoard().getGrid()[y][x] == 2 || human.getBoard().getGrid()[y][x] == 3);

        boolean hit = false;
        boolean sunk = false;
        Ship hitShip = null;

        for (Ship ship : human.getBoard().getShips()) {
            for (Coordinate coord : ship.getCoordinates()) {
                if (coord.getX() == x && coord.getY() == y) {
                    hit = true;
                    human.getBoard().getGrid()[y][x] = 2;
                    hitShip = ship;
                    break;
                }
            }
            if (hit) break;
        }

        if (!hit) {
            human.getBoard().getGrid()[y][x] = 2;
            return "Computer attacked [" + x + "," + y + "] --- Miss!";
        }

        sunk = true;
        for (Coordinate coord : hitShip.getCoordinates()) {
            if (human.getBoard().getGrid()[coord.getY()][coord.getX()] != 2) {
                sunk = false;
                break;
            }
        }

        if (sunk) {
            hitShip.setState(ShipState.SUNKEN);
            for (Coordinate coord : hitShip.getCoordinates()) {
                human.getBoard().getGrid()[coord.getY()][coord.getX()] = 3;
            }
            return "Computer attacked [" + x + "," + y + "] - ---Hit! " + hitShip.getName() + " sunk!";
        }

        return "Computer attacked [" + x + "," + y + "] - ---Hit!";
    }

    public boolean isHumanWinner() {
        for (Ship ship : currentGame.getComputer().getBoard().getShips()) {
            if (ship.getState() != ShipState.SUNKEN) {
                return false;
            }
        }
        return true;
    }

    public boolean isComputerWinner() {
        for (Ship ship : currentGame.getHuman().getBoard().getShips()) {
            if (ship.getState() != ShipState.SUNKEN) {
                return false;
            }
        }
        return true;
    }

    public String getHumanBoard() {
        return formatBoard(currentGame.getHuman().getBoard(), true);
    }

    public String getComputerBoard() {
        return formatBoard(currentGame.getComputer().getBoard(), false);
    }

    public String formatBoard(Board board, boolean showShips) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int cell = board.getGrid()[y][x];
                if (!showShips && cell == 1) {
                    sb.append("0 ");
                } else {
                    sb.append(cell).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
