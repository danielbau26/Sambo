package ui;

import java.util.Scanner;
import model.Controller;
/**
 * Battleship 2D Simluator!.
 *
 * This program simulates a 2D Battleship game, where you can play against the computer.
 * The game has two modes: a standard mode with fixed ship sizes and positions, and a custom mode 
 * where you can choose the number of ships, their sizes, and directions.
 * 
 * Features:
 * - Place ships on the board (both standard and custom modes).
 * - Take turns attacking the opponent (player vs. computer).
 * - Track wins and losses for both the player and the computer.
 * - See game statistics showing the number of wins for each side.
 *
 * The game uses a command-line interface for interaction and has a simple menu to choose game modes, 
 * place ships, and play the game.
 * 
 * The program uses the Model-View-Controller (MVC) design pattern:
 * - Model: Contains the game rules, board, and ships.
 * - View: Displays the game board and statistics to the user.
 * - Controller: Manages user input and connects it to the game logic.
 */
public class Main {
    private Controller controller;
    private Scanner scanner;
    private int [] winnerHuman;
    private int [] winnerComputer;
    /**
     * Constructor for the main class. initializes the controller, scanner and arrays.
     */
    public Main (){
        this.controller = new Controller();
        this.scanner = new Scanner (System.in);
        this.winnerHuman = new int [2];
        this.winnerComputer = new int [2];
    }
    
    public static void main(String[] args) {
        Main main = new Main();
        main.menu();
    }
    /**
     * Displays the main menu and allows the user to select an option.
     * including Standard game, Custom game, Game stats and Exit
     */
    public void menu() {
        boolean flag = true;
        while (flag){
        
            System.out.println("menu");
            System.out.println("1. Jugar partida estandar");
            System.out.println("2. Jugar partida personalizada");
            System.out.println("3. Ver estadisticas");
            System.out.println("4. salir");
            System.out.print("Selecciona una opción: ");
            int option = scanner.nextInt();  

            switch (option) {
                case 1:
                    controller.createStandardGame();
                    standardGame();
                    playGame(0); 
                    break;
                case 2:
                    customGame();
                    playGame(1);  
                    break;
                case 3:
                    viewStats();  
                    break;
                case 4:
                    System.out.println("Gracias por jugar!");
                    flag = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
         
            }
        }  
        
    }

    public void standardGame(){
        placeShips(1, "Patrol Boat", false);
        placeShips(2, "Destroyer", true);
        placeShips(3, "Submarine", false);
        placeShips(3, "Cruiser", true);
        placeShips(4, "Battleship", false);
        placeShips(5, "Carrier", true);
    }
    /**
     * Prompts the user to place a ship of a given size and name on the board.
     *
     * @param size        The size (length) of the ship.
     * @param name        The name of the ship.
     * @param orientation True for vertical placement, false for horizontal.
     */

    public void placeShips(int size, String name, boolean orientation){

        int x,y;
        System.out.println("");
        System.out.println("Place your " + name + " (occupation " + size + " box)");
        System.out.println("Place " + orientation + " on the board");

        while(true){
            System.out.println("Coordinate X (1-10):");
            x = scanner.nextInt();
            System.out.println("Coordinate Y (1-10):");
            y = scanner.nextInt();
            scanner.nextLine();

            if (x>=1 && y>=1 && x<=10 && y<=10){
                x-=1;
                y-=1;
                controller.addPlayerShip(size, orientation, x, y, name);
                break;
            }else{
                System.out.println("Numbers out of range.");
            }
        }
    }
    /**
     * Manages the game loop for either standard or custom mode.
     * Alternates turns between player and computer, and checks for a winner.
     *
     * @param numero 0 for standard game, 1 for custom game.
     */

    public void playGame(int numero) {
        boolean gameOver = false;

        while (!gameOver) {
            
            System.out.println("Tablero Humano:");
            System.out.println(controller.getHumanBoard());

            System.out.println("Tablero Computadora:");
            System.out.println(controller.getComputerBoard());

            // Turno del jugador
            playerTurn();

            // Turno de la computadora
            if (!controller.isHumanWinner()) {
                computerTurn();
            }

            // verificar ganador
            if (numero == 0){
                if (controller.isHumanWinner()) {
                    System.out.println("¡Felicidades, has ganado!");
                    winnerHuman[0] += 1;
                    gameOver = true;
                } else if (controller.isComputerWinner()) {
                    System.out.println("¡La computadora ha ganado!");
                    winnerComputer[0] += 1;
                    gameOver = true;
                }
            }else if (numero == 1){
                if (controller.isHumanWinner()) {
                    System.out.println("¡Felicidades, has ganado!");
                    winnerHuman[1] += 1;
                    gameOver = true;
                } else if (controller.isComputerWinner()) {
                    System.out.println("¡La computadora ha ganado!");
                    winnerHuman[1] += 1;
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Executes the player's turn by asking for coordinates and attacking the computers board
     */
    public void playerTurn() {
        System.out.println("¡Es tu turno!");
        System.out.print("Ingresa la coordenada X para disparar: ");
        int x = scanner.nextInt();
        System.out.print("Ingresa la coordenada Y para disparar: ");
        int y = scanner.nextInt();

        
        String result = controller.playerAttack(x, y);
        System.out.println("Resultado del ataque: " + result);
    }

    /**
     * Executes the computer's turn by asking for coordinates and attacking the players board
     */
    public void computerTurn() {
        System.out.println("¡Es el turno de la computadora!");
        String result = controller.computerAttack();
        System.out.println(result);
    }
    /**
     * Handles the setup of a custom game, including the number of boats,
     * their names, sizes, and orientations as defined by the player.
     */

    public void customGame(){
        int boats = 0;
        System.out.println("How many boats u are gonna place? (up to 10)");
        while(true){
            boats = scanner.nextInt();
            if(boats >= 1 || boats <= 10){
                break;
            }
        }

        for (int i = 0; i <= boats; i++){
            System.out.println("writte the name of the boat: ");
            String boatName  = scanner.nextLine();
            System.out.println("Write the lenght of the boat (up to 5): ");
            int boatSize  = 0;
            while (true){
                boatSize= scanner.nextInt() ;
                if (boatSize >= 1 && boatSize <= 5){
                    break;
                }
            }

            System.out.println("writte the orientation of the boat (VERTICAL / HORIZONTAL):  ");
            String orientation = "";
            boolean orientationFlag = true;
            while(true){
                orientation = scanner.nextLine().toUpperCase();
                if(orientation.equals("VERTICAL")){
                    orientationFlag = true;
                    break;
                }else if (orientation.equals("HORIZONTAL")){
                    orientationFlag = false;
                    break;
                }
            }
            
            controller.createCustomGame(boatName, boatSize, orientationFlag);
        }
    }
    /**
    * Displays game statistics for standard and custom games, showing wins for player and computer.
    */

    public void viewStats(){
        
        System.out.println("Game stats");
        System.out.println(" Which gamemode u wanna know the stats (1) Standart game (2) Custom");
        int gamemodeOption = 0;

        while (true){
            gamemodeOption = scanner.nextInt();
            if (gamemodeOption == 1 || gamemodeOption == 2){
                break;
            }
        }
        if (gamemodeOption == 1){
            System.out.println("STANDARD GAME");
            System.out.println("Player stats " + winnerHuman[0]);
            System.out.println("Computer stats " + winnerComputer[0]);
        }
        if (gamemodeOption == 2){
            System.out.println("CUSTOM GAME");
            System.out.println("Player stats " + winnerHuman[2]);
            System.out.println("Computer stats " + winnerComputer[2]);
        }
        
    }
}
