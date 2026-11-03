package gui.dialogs;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * Utility helper for standard modal dialogs and message pop-ups in the Chess GUI.
 *
 * <p>Provides standardized message boxes, confirmation prompts, error alerts,
 * and endgame winner announcements complying with Phase 2 specifications.</p>
 *
 * @author Dev 1 (Rayed)
 */
public final class DialogHelper {

    /** Private constructor to prevent instantiation. */
    private DialogHelper() { }

    /**
     * Displays an endgame winner announcement dialog and executes a callback upon dismissal.
     *
     * @param parent the parent Component for dialog centering
     * @param winnerMessage the message announcing the winner (e.g., "White wins! Black King captured.")
     * @param onDismiss a callback action to execute after the user acknowledges the dialog (e.g., exit application)
     */
    public static void showEndgameDialog(Component parent, String winnerMessage, Runnable onDismiss) {
        JOptionPane.showMessageDialog(
                parent,
                winnerMessage,
                "Game Over - Winner Declared",
                JOptionPane.INFORMATION_MESSAGE
        );
        if (onDismiss != null) {
            onDismiss.run();
        }
    }

    /**
     * Prompts the user with a Yes/No confirmation dialog.
     *
     * @param parent the parent Component
     * @param title the dialog title
     * @param message the question prompt
     * @return true if the user selected Yes, false otherwise
     */
    public static boolean showConfirmDialog(Component parent, String title, String message) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Displays an informational message dialog.
     *
     * @param parent the parent Component
     * @param title dialog title
     * @param message informational text
     */
    public static void showInfoDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error alert dialog.
     *
     * @param parent the parent Component
     * @param title dialog title
     * @param errorMessage error description
     */
    public static void showErrorDialog(Component parent, String title, String errorMessage) {
        JOptionPane.showMessageDialog(parent, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }
}

