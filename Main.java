package ui;

import java.util.Scanner;
import model.Controller;

public class Main {
    private static Controller controller = new Controller();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;

        do {
            showMenu();  
            option = scanner.nextInt();  

            switch (option) {
                case 1:
                    controller.createStandardGame();
                    playGame(); 
                    break;
                case 2:
                    controller.createCustomGame();
                    playGame();  
                    break;
                case 3:
                    System.out.println("Gracias por jugar!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (option != 3);  
    }

    
    public static void showMenu() {
        System.out.println("menu");
        System.out.println("1. Jugar partida estandar");
        System.out.println("2. Jugar partida personalizada");
        System.out.println("3. salir");
        System.out.print("Selecciona una opción: ");
    }

    
    public static void playGame() {
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
            if (controller.isHumanWinner()) {
                System.out.println("¡Felicidades, has ganado!");
                gameOver = true;
            } else if (controller.isComputerWinner()) {
                System.out.println("¡La computadora ha ganado!");
                gameOver = true;
            }
        }
    }

    //
    public static void playerTurn() {
        System.out.println("¡Es tu turno!");
        System.out.print("Ingresa la coordenada X para disparar: ");
        int x = scanner.nextInt();
        System.out.print("Ingresa la coordenada Y para disparar: ");
        int y = scanner.nextInt();

        
        String result = controller.playerAttack(x, y);
        System.out.println("Resultado del ataque: " + result);
    }

    // turno de la pc
    public static void computerTurn() {
        System.out.println("¡Es el turno de la computadora!");
        String result = controller.computerAttack();
        System.out.println(result);
    }
}
