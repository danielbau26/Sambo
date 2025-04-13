package model;

import java.util.Random;
/**
 * Este es el controller principal del juego Battleship, es decir que aca se ahce la "logica" deljuego la cual se llama en el main.
 * 
 */

public class Controller {
    private Game currentGame;
    private Random random;
    /**
     * Este es el constructo el cual inicializa el controller del Battleship
     */
    //--------------------------------------------------------------------------------------------------------
    public Controller() {
        this.random = new Random();
    }

    /**
     * Crea un juego Normal el cual ya tiene barcos definidos
     */
    public void createStandardGame() {
        Player human = new Player("Human");
        Player computer = new Player("Computer");
        this.currentGame = new Game(human, computer, true);
        setupComputerShips(true);
    }
 /**
  * Crea un juego customizado donde tu placeas tus botes
  */
    public void createCustomGame() {
        Player human = new Player("Human");
        Player computer = new Player("Computer");
        this.currentGame = new Game(human, computer, false);
        setupComputerShips(false);
    }

    
/**
 * Coloca los barcos de la computadora en el tablero.
 * @param isStandard true si es un juego estándar, false si es personalizado.
 * Precondición: debe haber un juego activo.
 * Postcondición: los barcos de la computadora son agregados al tablero.
 */
    private void setupComputerShips(boolean isStandard) {
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
                addComputerShip(length, "S hip " + i, vertical);
            }
        }
    }
/**
 * Agrega un barco al tablero de la computadora.
 * @param length tamaño del barco.
 * @param name nombre del barco.
 * @param vertical orientación del barco.
 * Precondición: las coordenadas deben ser válidas y no superponerse con otros barcos.
 * Postcondición: el barco es agregado al tablero de la computadora.
 */
    private void addComputerShip(int length, String name, boolean vertical) {
        int x, y;
        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
            
            if (vertical && y + length > 10) y = 10 - length;
            if (!vertical && x + length > 10) x = 10 - length;
        } while (!isValidShipPlacement(x, y, length, vertical, currentGame.getComputer()));
        //
        Ship ship = new Ship(name, x, y, length, vertical);
        for (int i = 0; i < length; i++) {
            int coordX = vertical ? x : x + i;
            int coordY = vertical ? y + i : y;
            ship.getCoordinates().add(new Coordinate(coordX, coordY));
        }
        currentGame.getComputer().getBoard().getShips().add(ship);
    }

    /**
 * Agrega un barco al tablero del jugador humano.
 * @param length tamaño del barco.
 * @param vertical orientación del barco.
 * @param x coordenada X inicial.
 * @param y coordenada Y inicial.
 * @param name nombre del barco.
 * Precondición: coordenadas válidas, sin solapamiento.
 * Postcondición: el barco es agregado al tablero del jugador.
 */
    public void addPlayerShip(int length, boolean vertical, int x, int y, String name) {
        Ship ship = new Ship(name, x, y, length, vertical);
        for (int i = 0; i < length; i++) {
            int coordX = vertical ? x : x + i;
            int coordY = vertical ? y + i : y;
            ship.getCoordinates().add(new Coordinate(coordX, coordY));
        }
        currentGame.getHuman().getBoard().getShips().add(ship);
    }
/**
 * Verifica si una posición es válida para colocar un barco.
 * @param x coordenada X.
 * @param y coordenada Y.
 * @param length longitud del barco.
 * @param vertical orientación.
 * @param player jugador al que pertenece el barco.
 * @return true si es válido, false si no.
 */
    public boolean isValidShipPlacement(int x, int y, int length, boolean vertical, Player player) {
        for (int i = 0; i < length; i++) {
            int checkX = vertical ? x : x + i;
            int checkY = vertical ? y + i : y;
            
            if (checkX < 0 || checkX >= 10 || checkY < 0 || checkY >= 10) {
                return false;
            }
            
            for (Ship ship : player.getBoard().getShips())  
            {
                for (Coordinate coord : ship.getCoordinates()) 
                {
                    if (coord.getX() == checkX && coord.getY() == checkY) 
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
 * El jugador ataca una casilla del tablero enemigo.
 * @param x coordenada X del disparo.
 * @param y coordenada Y del disparo.
 * @return mensaje indicando si fue hit, miss o barco hundido.
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
        //misse d hit!
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
 * La computadora ataca una casilla del tablero del jugador.
 * @return mensaje indicando si fue hit, miss o barco hundido.
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
 * Verifica si el jugador humano ha ganado.
 * @return true si todos los barcos enemigos están hundidos.
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
 * Verifica si la computadora ha ganado.
 * @return true si todos los barcos del jugador están hundidos.
 */
    public boolean isComputerWinner() {//gethuman
        for (Ship ship : currentGame.getHuman().getBoard().getShips()) {
            if (ship.getState() != ShipState.SUNKEN) {
                return false;
            }
        }
        return true;
    }
/**
 * Devuelve el tablero del jugador humano en formato texto.
 * @return String con el tablero.
 */
    public String getHumanBoard() {
        return formatBoard(currentGame.getHuman().getBoard(), true);
    }
/**
 * Devuelve el tablero de la computadora en formato texto, ocultando sus barcos.
 * @return String con el tablero.
 */
    public String getComputerBoard() {
        return formatBoard(currentGame.getComputer().getBoard(), false);
    }
/**
 * Formatea el tablero para imprimirlo.
 * @param board tablero a mostrar.
 * @param showShips true si se deben mostrar los barcos.
 * @return String representando el tablero.
 */
    private String formatBoard(Board board, boolean showShips) {
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
 * Devuelve la partida actual.
 * @return Game activo.
 */
    public Game getCurrentGame() {
        return currentGame;
    }
}