package model;

import java.util.Random;

public class Controller {
    public Game currentGame;
    private Random random;

    public Controller() {
        this.random = new Random();
    }
    
    /**
     * Create a standard game between a human player and the computer
     * this game, put automatically 6 boats
     * in the computer board with different size and orientations

     */
    public void createStandardGame() {
        Player human = new Player("Human");
        Player computer = new Player("Computer");
        this.currentGame = new Game(human, computer, true);

        addComputerShip(1, "Patrol Boat", false);
        addComputerShip(2, "Destroyer", true);
        addComputerShip(3, "Submarine", false);
        addComputerShip(3, "Cruiser", true);
        addComputerShip(4, "Battleship", false);
        addComputerShip(5, "Carrier", true);
    }
    /**
     * Creates a custom match between the player and the computer, the boats that are placed into the board had
     * a predefined size that the user decides
     * @param name -The name of the boat
     * @param boatSize -The size of the boat
     * @param orientation -The orientation of the boat
     */
    public void createCustomGame(String name, int boatSize, boolean orientation) {
        Player human = new Player("Human");
        Player computer = new Player("Computer");
        this.currentGame = new Game(human, computer, false);
        addComputerShip(boatSize, name, orientation);
    }
    /**
     * add a boat to the computer's board into a random place
     * this also can verify if the boats collide
     * @param length - Size of the boat (num of boxes)
     * @param name - Name of the boat
     * @param vertical -  orientation of the boat (true if the boat is horizontal, false if the boat is vertical)
     */
    public void addComputerShip(int length, String name, boolean vertical) {
        int x= 0; 
        int y = 0;

        do {
            x = random.nextInt(10);
            y = random.nextInt(10);

            if (vertical && y + length > 10){
                y = 10 - length;
            }else if (!vertical && x + length > 10){
                x = 10 - length;
            }
        } while (!isValidShipPlacement(x, y, length, vertical, currentGame.getComputer()));

        Ship ship = new Ship(name, length, vertical);
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
    
    /**
     * add a boat into the player's board and verify if the board collides
     * @param length -size of the boat (num of boxes)
     * @param vertical - orientation of the boat (true if the boat is horizontal, false if the boat is vertical)
     * @param x - start x coordinate
     * @param y - start y coordinate
     * @param name - name of the boat
     */
    public void addPlayerShip(int length, boolean vertical, int x, int y, String name) {
        Player player = currentGame.getHuman();
        Ship ship = new Ship(name, length, vertical);
        if (vertical){
            if (x+length>10){
                x = 10-length;
            }
        }else{
            if (y+length>10){
                y = 10-length;
            }
        }
        
        boolean flag = isValidShipPlacement(x, y, length, vertical, player);

        if (flag == true){
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
                currentGame.getHuman().getBoard().getGrid()[coordY][coordX] = 1;
            }
            currentGame.getHuman().getBoard().getShips().add(ship);
        }
    }

    /**
     * Checks if a ship can be placed at the specified coordinates without going out of bounds
     * or overlapping with existing ships on the board.
     *
     * @param x         Starting x coordinate.
     * @param y         Starting y coordinate.
     * @param length    Length of the ship (number of cells).
     * @param vertical  true for vertical orientation, false for horizontal.
     * @param player    The player whose board is being validated.
     * @return          true if the placement is valid, false otherwise.
     */

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

    /**
     * Handles the player's attack on the computer's board.
     *
     * @param x The x coordinate of the attack.
     * @param y The y coordinate of the attack.
     * @return  A message indicating the result: "missed", "---Hit", or "---Hit [ship name] ---sunk".
     */

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
    /**
     * Handles the computer's attack on the player's board
     * it randomly selects a coordinate that hasn't been attacked yet
     * @return a message indicating the coords and the result that have been attacked
     */
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

    /**
     * Checks if the human player won the game.
     * 
     * @return true if all the computers ship have been sunk.
     */
    public boolean isHumanWinner() {
        for (Ship ship : currentGame.getComputer().getBoard().getShips()) {
            if (ship.getState() != ShipState.SUNKEN) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if the computer player won the game.  
     * 
     * @return true if all the players ship have been sunk.
     */
    public boolean isComputerWinner() {
        for (Ship ship : currentGame.getHuman().getBoard().getShips()) {
            if (ship.getState() != ShipState.SUNKEN) {
                return false;
            }
        }
        return true;
    }
    /**
     * Returns a formatted string of the human player's board.
     * Ships are visible in this representation.
     *
     * @return The formatted board as a string.
     */

    public String getHumanBoard() {
        return formatBoard(currentGame.getHuman().getBoard(), true);
    }

    /**
     * Returns a fromatted string of the computer's board.
     * Ships are hidden in this representation.
     * 
     * @return the formatted board as string.
     */
    public String getComputerBoard() {
        return formatBoard(currentGame.getComputer().getBoard(), false);
    }
    /**
     * Formats a board as a string, either showing or hiding the ships depending on the parameter.
     *
     * @param board      The board to format.
     * @param showShips  True to show ships, false to hide them.
     * @return           A string representing the board.
     */

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
    /**
     * Returns the current game instance.
     *
     * @return The current Game object.
     */

    public Game getCurrentGame() {
        return currentGame;
    }
}
