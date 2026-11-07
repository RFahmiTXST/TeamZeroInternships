package gui;

import board.Board;
import board.Position;
import pieces.Piece;
import gui.board.BoardPanel;
import javax.swing.SwingUtilities;

/**
 * Connects the GUI's board model to its Swing view.
 *
 * <p>Provides placement, reset, refresh, and unrestricted Phase 2 mouse moves.
 * It does not apply the console game's movement rules. Construct the controller
 * and call its mutating methods on the Swing event dispatch thread (EDT).
 * The controller does not create a window, so it can be tested headlessly.</p>
 *
 * @author Dev 3 (Sabid)
 */
public final class GUIController {
    /** Board model used by this GUI session. */
    private Board board;
    /** View displaying the session's board. */
    private final BoardPanel boardPanel;
    /** Selected source square, or null. */
    private Position selected;
    /** Mouse gesture adapter shared by all board components. */
    private final BoardMouseHandler mouseHandler;

    /**
     * Connects an existing board and view without resetting the supplied position.
     *
     * @param board board model, including any existing pieces
     * @param boardPanel view to synchronize with the board
     * @throws IllegalArgumentException if either argument is null
     * @throws IllegalStateException if called outside the EDT
     */
    public GUIController(Board board, BoardPanel boardPanel) {
        requireEventDispatchThread();
        if (board == null || boardPanel == null) {
            throw new IllegalArgumentException("Board and board panel must not be null.");
        }
        this.board = board;
        this.boardPanel = boardPanel;
        mouseHandler = new BoardMouseHandler(this);
        refreshBoard();
    }

    /**
     * Initializes all 32 starting pieces and clears stale visual selections.
     * Existing pieces and captures are reset through {@link Board#initialize()}.
     *
     * @throws IllegalStateException if called outside the EDT
     */
    public void startNewGame() {
        requireEventDispatchThread();
        cancelSelection();
        board.initialize();
        boardPanel.clearHighlights();
        refreshBoard();
    }

    /**
     * Displays a replacement board without changing its pieces or moved flags.
     * This is the integration point for a future load-game action.
     *
     * @param board replacement board model
     * @throws IllegalArgumentException if the replacement is null
     * @throws IllegalStateException if called outside the EDT
     */
    public void setBoard(Board board) {
        requireEventDispatchThread();
        if (board == null) {
            throw new IllegalArgumentException("Board must not be null.");
        }
        cancelSelection();
        this.board = board;
        boardPanel.clearHighlights();
        refreshBoard();
    }

    /**
     * Synchronizes the visual board after a model change made on the EDT.
     *
     * @throws IllegalStateException if called outside the EDT
     */
    public void refreshBoard() {
        requireEventDispatchThread();
        if (boardPanel.getBoard() != board) {
            boardPanel.setBoard(board);
        } else {
            boardPanel.updateFromBoard();
        }
    }

    /**
     * Selects a piece or attempts a move from the current selection.
     * @param target clicked square, or null for the board margin
     */
    public void clickSquare(Position target) {
        requireEventDispatchThread();
        if (selected != null) {
            movePiece(selected, target);
        } else {
            selectSquare(target);
        }
    }

    /**
     * Selects an occupied square and clears previous selection feedback.
     * @param target source square, or null to clear selection
     */
    public void selectSquare(Position target) {
        requireEventDispatchThread();
        boardPanel.clearHighlights();
        selected = target != null && target.isOnBoard() && board.getPiece(target) != null
                ? target : null;
        if (selected != null) boardPanel.getSquarePanel(selected).setSelected(true);
    }

    /**
     * Cancels selection and any pending mouse gesture without changing the model.
     */
    public void cancelSelection() {
        requireEventDispatchThread();
        mouseHandler.cancel();
        selectSquare(null);
    }

    /**
     * Moves without chess legality or turn restrictions, as required by Phase 2.
     * Same-square moves, empty sources and friendly captures are rejected.
     * Opposing captures are recorded by the existing Board implementation.
     * This is the shared integration point for future turn and endgame handling.
     * @param from source coordinate
     * @param to destination coordinate
     * @return true if the model changed
     */
    public boolean movePiece(Position from, Position to) {
        requireEventDispatchThread();
        cancelSelection();
        if (from == null || to == null || !from.isOnBoard() || !to.isOnBoard()
                || from.equals(to)) return false;
        Piece moving = board.getPiece(from);
        Piece target = board.getPiece(to);
        if (moving == null || (target != null && target.getColor() == moving.getColor())) {
            return false;
        }
        board.movePiece(from, to);
        refreshBoard();
        return true;
    }

    /**
     * Returns the active model. Callers must mutate it only on the EDT and
     * then call {@link #refreshBoard()} to synchronize the view.
     *
     * @return active board model
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the associated Swing view.
     *
     * @return board view; any changes to it must occur on the EDT
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * Rejects calls that could mutate Swing components from another thread.
     *
     * @throws IllegalStateException if the current thread is not the EDT
     */
    private static void requireEventDispatchThread() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("GUI controller updates must run on the Swing EDT.");
        }
    }
}
