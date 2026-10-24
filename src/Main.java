
import core.Game;
import ui.ConsoleUI;
import java.util.Scanner;

/**
 * Entry point for the Console Chess game.
 *
 * Developer: Dev 4 (Towsif)
 */
public class Main {

    /** Prevents construction of the entry-point class. */
    private Main() { }

    /**
     * Starts the interactive console chess game.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleUI ui = new ConsoleUI(scanner);
        Game game = new Game(ui);

        game.start();
    }
}
