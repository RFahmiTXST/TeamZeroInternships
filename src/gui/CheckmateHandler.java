package gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.Objects;
import pieces.Color;

/**
 * Announces Phase 3 checkmate or stalemate without exiting the application.
 * The controller blocks play after the result while retaining game review,
 * Save Game, and New Game functionality.
 *
 * @author Dev 4 (Towsif)
 */
public final class CheckmateHandler {
    /** Injectable modal presenter for deterministic headless tests. */
    @FunctionalInterface
    public interface EndgameDialog {
        /**
         * Presents the completed game's result.
         * @param parent modal dialog parent, possibly null in tests
         * @param message complete outcome message
         */
        void show(Component parent, String message);
    }

    /** Parent component used to center the modal. */
    private final Component parent;
    /** Delegate that displays the outcome. */
    private final EndgameDialog dialog;

    /**
     * Registers a standard Swing modal presenter.
     * @param parent application window
     * @param controller controller whose finished games are announced
     */
    public CheckmateHandler(Component parent, GUIController controller) {
        this(parent, controller, (window, message) ->
                JOptionPane.showMessageDialog(window, message, "Game Over",
                        JOptionPane.INFORMATION_MESSAGE));
    }

    /**
     * Registers a presenter supplied by a test or calling application.
     * Registration must occur on the Swing event dispatch thread.
     *
     * @param parent dialog parent
     * @param controller active game controller
     * @param dialog modal presenter (never null)
     * @throws NullPointerException if controller or dialog is null
     * @throws IllegalStateException if called off the event dispatch thread
     */
    public CheckmateHandler(Component parent, GUIController controller, EndgameDialog dialog) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Endgame handler must be created on the Swing EDT.");
        }
        this.parent = parent;
        this.dialog = Objects.requireNonNull(dialog, "dialog");
        Objects.requireNonNull(controller, "controller").addGameEndListener(this::onGameEnded);
    }

    /**
     * Formats a result independently of the UI.
     * @param result non-ongoing result
     * @param winner winner on checkmate; null on stalemate
     * @return the message displayed to the player
     * @throws IllegalArgumentException for an incomplete outcome
     */
    public static String resultMessage(GUIController.GameOutcome result, Color winner) {
        if (result == GUIController.GameOutcome.CHECKMATE && winner != null) {
            String name = winner == Color.WHITE ? "White" : "Black";
            return "Checkmate! " + name + " wins by checkmate!";
        }
        if (result == GUIController.GameOutcome.STALEMATE && winner == null) {
            return "Stalemate! Draw!";
        }
        throw new IllegalArgumentException("A completed game outcome is required.");
    }

    /** Responds only when an accepted move produces a completed game. */
    private void onGameEnded(GUIController.GameOutcome result, Color winner) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Endgame dialogs must run on the Swing EDT.");
        }
        dialog.show(parent, resultMessage(result, winner));
    }
}
