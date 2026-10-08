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
     * TODO(Dev 3): Implement showWelcome()
     */
    public void showWelcome() {
    }

    /**
     * Displays the current board state.
     * 
     * @param board The board to display.
     * TODO(Dev 3): Implement showBoard()
     */
    public void showBoard(Board board) {
    }

    /**
     * Indicates whose turn it is.
     * 
     * @param color The active color.
     * TODO(Dev 3): Implement showTurn()
     */
    public void showTurn(Color color) {
    }

    /**
     * Prompts the player to enter a move and reads the input.
     * 
     * @param color The active color.
     * @return The raw input string entered by the user.
     * TODO(Dev 3): Implement promptMove()
     */
    public String promptMove(Color color) {
        return null;
    }

    /**
     * Shows an error message to the user.
     * 
     * @param error The error text.
     * TODO(Dev 3): Implement showError()
     */
    public void showError(String error) {
    }
}

