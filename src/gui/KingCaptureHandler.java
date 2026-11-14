package gui;

import board.Position;
import gui.dialogs.DialogHelper;
import pieces.Color;
import pieces.King;
import pieces.Piece;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Window;
import java.util.Objects;

/**
 * Handles Phase 2's King-capture endgame notification.
 *
 * <p>Attach {@link #onPieceCaptured(Piece, Position)} to the GUI controller's
 * capture callback after the movement and capture PRs are integrated. The
 * handler does not implement moves and does not modify the board model.</p>
 *
 * @author Dev 4 (Towsif)
 */
public final class KingCaptureHandler {

    /** Abstraction for the modal dialog, allowing headless tests. */
    @FunctionalInterface
    public interface WinnerDialog {
        /**
         * Shows a winner announcement and invokes the dismissal action once
         * the player acknowledges the message.
         *
         * @param parent parent component for the dialog
         * @param message winner announcement
         * @param onDismiss action to execute after the dialog closes
         */
        void show(Component parent, String message, Runnable onDismiss);
    }

    /** Parent window or component used to center the modal dialog. */
    private final Component parent;
    /** The presenter used for the win announcement. */
    private final WinnerDialog dialog;
    /** Action that terminates the application after the modal dialog. */
    private final Runnable onExit;
    /** Prevents duplicate announcements once the King has been captured. */
    private boolean ended;

    /**
     * Creates the application handler with a Swing modal dialog and an exit action.
     *
     * @param parent the active chess window, used to center the dialog
     */
    public KingCaptureHandler(Component parent) {
        this(parent, DialogHelper::showEndgameDialog, () -> {
            Window window = parent instanceof Window
                    ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
            if (window != null) {
                window.dispose();
            }
            System.exit(0);
        });
    }

    /**
     * Creates a handler with injected dialog and exit actions for headless tests.
     *
     * @param parent dialog parent, which may be null in tests
     * @param dialog modal winner presenter
     * @param onExit action called after player dismissal
     * @throws NullPointerException if dialog or onExit is null
     */
    public KingCaptureHandler(Component parent, WinnerDialog dialog, Runnable onExit) {
        this.parent = parent;
        this.dialog = Objects.requireNonNull(dialog, "dialog");
        this.onExit = Objects.requireNonNull(onExit, "onExit");
    }

    /**
     * Checks whether the captured piece is a King.
     *
     * @param captured the captured piece, or null for a normal move
     * @return true only for King captures
     */
    public static boolean isKingCapture(Piece captured) {
        return captured instanceof King;
    }

    /**
     * Builds the winner announcement from the color of the captured King.
     *
     * @param captured the captured King
     * @return winner announcement
     * @throws IllegalArgumentException when the piece is not a King
     */
    public static String winnerMessage(Piece captured) {
        if (!isKingCapture(captured)) {
            throw new IllegalArgumentException("A captured King is required.");
        }
        String capturedSide = captured.getColor() == Color.WHITE ? "White" : "Black";
        String winner = captured.getColor() == Color.WHITE ? "Black" : "White";
        return "Game Over! " + winner + " captured " + capturedSide + "'s King and wins!";
    }

    /**
     * Processes a capture event on the Swing event dispatch thread.
     * Ordinary captures never open a dialog. A King capture opens the modal
     * winner announcement and invokes the exit action after it is dismissed.
     *
     * @param captured captured piece, or null if there was no capture
     * @return true when a new King capture was handled
     * @throws IllegalStateException if called outside the Swing EDT
     */
    public boolean handleCapture(Piece captured) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("King-capture handling must run on the Swing EDT.");
        }
        if (ended || !isKingCapture(captured)) {
            return false;
        }
        ended = true;
        dialog.show(parent, winnerMessage(captured), onExit);
        return true;
    }

    /**
     * Adapter for a capture listener accepting a piece and target square.
     *
     * @param capturedPiece captured piece
     * @param targetPosition target square where the capture occurred
     */
    public void onPieceCaptured(Piece capturedPiece, Position targetPosition) {
        handleCapture(capturedPiece);
    }

    /**
     * Reports whether the handler has already announced a King capture.
     *
     * @return true if the game has ended via King capture
     */
    public boolean hasEnded() {
        return ended;
    }
}
