package ui;

import board.Board;
import pieces.Color;

import java.util.Scanner;

/**
 * All console input and output for the game. Keeping {@code System.in} and {@code System.out} in one
 * class keeps {@link core.Game} simple and lets tests feed in moves from a string.
 *
 * <p>Owner: Developer 3. {@link Board#display()} itself belongs to Developer 2.
 */
public class ConsoleUI {

    /** Reads lines typed by the players. */
    private final Scanner scanner;

    /**
     * Creates a console UI that reads from standard input.
     */
    public ConsoleUI() {
        this(new Scanner(System.in));
    }

    /**
     * Creates a console UI that reads from the given scanner. For tests, use
     * {@code new Scanner("E2 E4\nquit\n")}.
     *
     * @param scanner where player input comes from
     */
    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prints the title and short instructions: the move format, optional castling and promotion
     * notation, and how to quit.
     */
    public void showWelcome() {
        // TODO(Dev 3)
    }

    /**
     * Prints the board.
     *
     * @param board the board to show
     */
    public void showBoard(Board board) {
        // TODO(Dev 3): board.display(), optionally followed by a blank line.
    }

    /**
     * Says whose turn it is, for example "White's turn."
     *
     * @param color the side to move
     */
    public void showTurn(Color color) {
        // TODO(Dev 3): use color.getDisplayName().
    }

    /**
     * Asks the current player for a move and reads one line.
     *
     * @param color the side to move, used in the prompt
     * @return the line the player typed, or {@code null} if there is no more input
     */
    public String promptMove(Color color) {
        // TODO(Dev 3): print e.g. "White, enter your move (e.g. E2 E4): " then return scanner.nextLine(),
        //  or null when !scanner.hasNextLine().
        return null;
    }

    /**
     * Prints an error message, for example when the move has the wrong format.
     *
     * @param message what went wrong
     */
    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    /**
     * Prints an ordinary message.
     *
     * @param message the text to show
     */
    public void showMessage(String message) {
        System.out.println(message);
    }
}