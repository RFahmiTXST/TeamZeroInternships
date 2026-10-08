import core.Game;
import ui.ConsoleUI;
import java.util.Scanner;

/**
 * Entry point for the Console Chess game.
 * 
 * Developer: Dev 4 (Towsif)
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleUI ui = new ConsoleUI(scanner);
        Game game = new Game(ui);
        
        // TODO(Dev 4): Ensure game starts properly
        game.start();
    }
}

