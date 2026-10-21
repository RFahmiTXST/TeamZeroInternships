package ui;

import board.Board;
import pieces.Color;
import java.util.Scanner;

/**
 * Handles all console input and output interactions.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays the welcome message and instructions.
     * 
     */
    public void showWelcome() {
        System.out.println("================================");
        System.out.println("       CONSOLE CHESS GAME");
        System.out.println("================================");
        System.out.println("Welcome to Console Chess!");
        System.out.println("White moves first.");
        System.out.println("Enter moves using: E2 E4");
        System.out.println("Type 'quit' or 'exit' to end the game.");
        System.out.println("================================");
    }

    /**
     * Displays the current chessboard in the console.
     *
     * @param board The board to display.
     */
    public void showBoard(Board board) {
        System.out.println(board.render());
    }

    /**
     * Displays which player's turn it is.
     *
     * @param color The color of the active player.
     */
    public void showTurn(Color color) {
        if (color == Color.WHITE) {
            System.out.println("White's turn.");
        } else if (color == Color.BLACK) {
            System.out.println("Black's turn.");
        }
    }

    /**
     * Prompts the current player to enter a chess move.
     *
     * @param color The color of the active player.
     * @return The raw move input entered by the player.
     */
    public String promptMove(Color color) {
        System.out.print(color + ", enter your move (e.g., E2 E4): ");
        return scanner.nextLine();
    }

    /**
     * Displays an error message to the player.
     *
     * @param error The error message to display.
     */
    public void showError(String error) {
        System.out.println("Error: " + error);
    }
}

