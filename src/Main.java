import core.Game;
import ui.ConsoleUI;
import java.util.Scanner;

/**
 * Entry point for the Console Chess game.
 * 
 * Developer: Dev 4 (Towsif)
 */
public class Main {
    /** Prevents entry-point construction. */
    private Main() { }
    /** Starts the console game.
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleUI ui = new ConsoleUI(scanner);
        Game game = new Game(ui);
        
        game.start();
    }
}

