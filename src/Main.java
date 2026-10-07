import core.Game;

/**
 * Entry point for the console chess game.
 *
 * <p>Build and run from the project root:
 * <pre>
 *   javac -d out $(find src -name "*.java")
 *   java -cp out Main
 * </pre>
 */
public class Main {

    /**
     * Not used. This class only holds {@link #main(String[])}.
     */
    private Main() {
    }

    /**
     * Starts a new two-player game in the console.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Game().start();
    }
}
