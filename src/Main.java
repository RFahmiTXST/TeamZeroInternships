import core.Game;
import gui.ChessFrame;
import ui.ConsoleUI;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.util.Scanner;

/**
 * Main application entry point for Team Zero Chess.
 *
 * <p>Supports launching either the new Phase 2 Graphical User Interface (GUI, default)
 * or the Phase 1 interactive console chess game via command-line arguments.</p>
 *
 * @author Dev 4 (Towsif) &amp; Dev 1 (Rayed)
 */
public class Main {

    /** Command-line flag to launch the Phase 1 console application. */
    public static final String CONSOLE_FLAG = "--console";
    /** Short command-line flag for console mode. */
    public static final String CONSOLE_SHORT_FLAG = "-c";

    /** Prevents construction of the entry-point class. */
    private Main() { }

    /**
     * Starts the chess application.
     *
     * <p>Launches the Swing GUI by default on the Event Dispatch Thread (EDT). If
     * {@code --console} or {@code -c} is passed in arguments or if the current execution
     * environment is headless, falls back to the interactive console mode.</p>
     *
     * @param args command-line arguments; pass {@code --console} for terminal mode
     */
    public static void main(String[] args) {
        boolean forceConsole = false;
        if (args != null) {
            for (String arg : args) {
                if (CONSOLE_FLAG.equalsIgnoreCase(arg) || CONSOLE_SHORT_FLAG.equalsIgnoreCase(arg)
                        || "console".equalsIgnoreCase(arg)) {
                    forceConsole = true;
                    break;
                }
            }
        }

        if (forceConsole || GraphicsEnvironment.isHeadless()) {
            startConsoleGame();
        } else {
            startGuiGame();
        }
    }

    /**
     * Launches the Phase 2 Swing Graphical User Interface on the Event Dispatch Thread.
     */
    public static void startGuiGame() {
        SwingUtilities.invokeLater(() -> {
            ChessFrame frame = new ChessFrame();
            frame.setVisible(true);
        });
    }

    /**
     * Launches the Phase 1 interactive console chess game.
     */
    public static void startConsoleGame() {
        Scanner scanner = new Scanner(System.in);
        ConsoleUI ui = new ConsoleUI(scanner);
        Game game = new Game(ui);

        game.start();
    }
}
