package ui;
import java.util.Scanner;
import model.Controller;
/**
 * Constructor de la clase Main.
 * Inicializa todo.
 */
public class Main {
    private Controller controller;
    private Scanner scanner;
    private int humanWins;
    private int computerWins;
    private int standardHumanWins;
    private int standardComputerWins;
    private int customHumanWins;
    private int customComputerWins;

    public Main() {
        this.controller = new Controller();
        this.scanner = new Scanner(System.in);
        this.humanWins = 0;
        this.computerWins = 0;
        this.standardHumanWins = 0;
        this.standardComputerWins = 0;
        this.customHumanWins = 0;
        this.customComputerWins = 0;
    }
//--------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        Main game = new Main();
        game.startGame();
    }
/**
 * Muestra el menú principal del juego y permite al usuario escoger entre las opciones disponibles.
 */
    public void startGame() {
        System.out.println("Welcome to BattleShip 2D simulator");
        
        boolean playing = true;
        while (playing) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Play normal game");
            System.out.println("2. Play custom Game");
            System.out.println("3. stats");
            System.out.println("4.  exit");
            System.out.print("human option: ");
            
            int choice = getIntInput(1, 4);
            
            switch (choice) {
                case 1:
                    playStandardGame();
                    break;
                    //--
                case 2:
                    playCustomGame();
                    break;
                    //--
                case 3:
                    showStatistics();
                    break;
                    //--
                case 4:
                    playing = false;
                    System.out.println("Thank u for playing");
                    break;
            }
        }
    }
/**
 * Inicia una partida estándar con barcos predeterminados.
 * Llama al controlador para crear la partida y luego permite al jugador colocar los barcos.
 */
    private void playStandardGame() {
        System.out.println("\nNormal game");
        controller.createStandardGame();
        setupHumanShips(true);
        playGame();
    }
/**
 * Inicia una partida personalizada donde el jugador puede editar los barcos.
 * Llama al controlador para crear la partida y gestiona a los barcos personalizados.
 */

    private void playCustomGame() {
        System.out.println("\nCustom game");
        controller.createCustomGame();
        setupHumanShips(false);
        playGame();
    }
/**
 * Permite al jugador humano colocar sus barcos en el tablero.
 * @param isStandard true si es un juego estándar, false si es personalizado.
 */
    private void setupHumanShips(boolean isStandard) {
        System.out.println("\nPlace your ships:");
        
        if (isStandard) {
            placeShip(1, "Patrol Boat", false);
            placeShip(2, "Destroyer xxx", true);
            placeShip(3, "Submarine", false);
            placeShip(3, "Cruiser", true);
            placeShip(4, "Battleship", false);
            placeShip(5, "Carrier", true);
        } else {
            System.out.print("How many ships? (1-10): ");
            int shipCount = getIntInput(1, 10);
            
            for (int i = 1; i <= shipCount; i++) {
                System.out.println("\nShip #" + i);
                System.out.print("Length (1-5): ");
                int length = getIntInput(1, 5);
                
                System.out.print("Orientation (1. Vertical / 2. Horizontal): ");
                int orientation = getIntInput(1, 2);
                boolean vertical = orientation == 1;
                
                System.out.print("Ship name: ");
                String name = scanner.nextLine();
                
                placeShip(length, name, vertical);
            }
        }
    }
/**
 * Permite colocar un barco específico del jugador humano en el tablero, validando la posición.
 * @param length longitud del barco.
 * @param name nombre del barco.
 * @param vertical orientación del barco (true si es vertical).
 */
    private void placeShip(int length, String name, boolean vertical) {
        boolean placed = false;
        while (!placed) {
            System.out.println("\nPlacing " + name + " (" + length + " spaces, " + 
                              (vertical ? "vertical" : "horizontal") + ")");
            
            System.out.print("Start X coordinate (0-9): ");
            int x = getIntInput(0, 9);
            
            System.out.print("Start Y coordinate (0-9): ");
            int y = getIntInput(0, 9);
            
            if (controller.isValidShipPlacement(x, y, length, vertical, controller.getCurrentGame().getHuman())) {
                controller.addPlayerShip(length, vertical, x, y, name);
                System.out.println(name + " placed successfully!");
                placed = true;
            } else {
                System.out.println("thats a invalid place.");
                System.out.println("Try again.");
                System.out.println("");
            }
        }
    }
/**
 * Ejecuta el ciclo principal de la partida, alternando entre turnos del jugador y de la computadora.
 * Actualiza las estadísticas al final de la partida.
 */
    private void playGame() {
        System.out.println("\n.......");
        System.out.println("");
        
        boolean gameOver = false;
        boolean humanTurn = true;
        
        while (!gameOver) {
            if (humanTurn) {
                humanTurn();
                if (controller.isHumanWinner()) {
                    gameOver = true;
                    humanWins++;
                    if (controller.getCurrentGame().isStandardGame()) {
                        standardHumanWins++;
                    } else {
                        customHumanWins++;
                    }
                    System.out.println("\ncongrats bro U won");
                    System.out.println("");
                }
            } else {
                computerTurn();
                if (controller.isComputerWinner()) {
                    gameOver = true;
                    computerWins++;
                    if (controller.getCurrentGame().isStandardGame()) {
                        standardComputerWins++;
                    } else {
                        customComputerWins++;
                    }
                    System.out.println("\nHell nah, the computer won the game");
                    System.out.println("");
                }
            }
            humanTurn = !humanTurn;
        }
    }
    //----------------------------------------------------------------
/**
 * tiene el turno del jugador humano.
 */
    private void humanTurn() {
        System.out.println("\nPLAYER TURN");
        System.out.println("ENEMY Board:");
        System.out.println(controller.getComputerBoard());
        
        System.out.print("Enter X coordinate to attack (0-9): ");
        int x = getIntInput(0, 9);
        
        System.out.print("Enter Y coordinate to attack (0-9): ");
        int y = getIntInput(0, 9);
        
        String result = controller.playerAttack(x, y);
        System.out.println(result);
    }
    //----------------------------------------------------------------
/**
 * tiene el turno del computador.
 */
    private void computerTurn() {
        System.out.println("\nCOMPUTERS TURN");
        String result = controller.computerAttack();
        System.out.println(result);
        
        System.out.println("\nYour Board:");
        System.out.println(controller.getHumanBoard());
    }
/**
 * Muestra las estadisticas acumuladas
 */
    private void showStatistics() {
        System.out.println("\nGAME STATS");
        System.out.println("Total Wins:");
        System.out.println("player: " + humanWins);
        System.out.println("Computer: " + computerWins);
        
        System.out.println("\nNormal games:");
        System.out.println("player: " + standardHumanWins);
        System.out.println("Computer: " + standardComputerWins);
        
        System.out.println("\nCustom Games:");
        System.out.println("player: " + customHumanWins);
        System.out.println("Computer: " + customComputerWins);
    }
/**
 * solicita un numero int de un valor en especifico.
 * @param min valor mínimo permitido.
 * @param max valor máximo permitido.
 * @return número entero válido introducido por el usuario.
 */
    private int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print("Invalid input. Enter between " + min + "-" + max + ": ");
                    System.out.println("...");
                    System.out.println("");
                }
                //exeption e para que cualquier accion incorrecta nos tire ese mensaje
            } catch (Exception e) {
                System.out.print("Invalid input. Enter a number: ");
                System.out.println("...");
                System.out.println("");
                scanner.nextLine();
            }
        }
    }
}