package gui;

import board.Board;
import board.Position;
import gui.board.BoardPanel;
import pieces.Color;
import pieces.Piece;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Connects the GUI's board model to its Swing view and coordinates gameplay state.
 *
 * <p>Orchestrates board initialization, model-to-view synchronization, unrestricted Phase 2
 * mouse gestures (click-and-move and drag-and-drop), opponent piece capture handling, and active
 * player turn tracking. All state-mutating operations enforce execution on the Swing Event
 * Dispatch Thread (EDT).</p>
 *
 * @author Dev 3 (Sabid - Architecture &amp; Input) &amp; Dev 1 (Rayed - Capture Logic &amp; Turn State)
 */
public final class GUIController {

    /**
     * Listener invoked whenever the active player turn or status text changes.
     */
    @FunctionalInterface
    public interface StatusListener {
        /**
         * Invoked when the status message or active turn changes.
         *
         * @param statusMessage updated status text
         */
        void onStatusChanged(String statusMessage);
    }

    /**
     * Listener invoked whenever a piece is captured on the board.
     */
    @FunctionalInterface
    public interface CaptureListener {
        /**
         * Invoked when a piece is captured on the board.
         *
         * @param capturedPiece the captured Piece instance
         * @param targetPosition board position where the piece was captured
         */
        void onPieceCaptured(Piece capturedPiece, Position targetPosition);
    }

    /** Receives immutable move-history snapshots on the EDT. */
    @FunctionalInterface
    public interface HistoryListener {
        /**
         * Refreshes history after a successful move or reset.
         * @param entries immutable chronological history
         */
        void onHistoryChanged(List<HistoryEntry> entries);
    }

    /**
     * Reversible state retained for each successful GUI move.
     * @param from original square
     * @param to destination square
     * @param moving moved piece
     * @param captured captured piece, if any
     * @param movedBefore movement flag before the move
     * @param turnBefore active turn before the move
     */
    private record UndoEntry(Position from, Position to, Piece moving, Piece captured,
                             boolean movedBefore, Color turnBefore) { }
    /** Most recent reversible move first. */
    private final Deque<UndoEntry> undoEntries = new ArrayDeque<>();

    /** Completed GUI moves; one entry per accepted gesture. */
    private final List<HistoryEntry> history = new ArrayList<>();
    /** Observers displaying completed moves. */
    private final List<HistoryListener> historyListeners = new ArrayList<>();

    /** Board model used by this GUI session. */
    private Board board;
    /** View displaying the session's board. */
    private final BoardPanel boardPanel;
    /** Selected source square, or null. */
    private Position selected;
    /** Mouse gesture adapter shared by all board components. */
    private final BoardMouseHandler mouseHandler;
    /** Active player whose turn it is to move. */
    private Color currentTurn = Color.WHITE;
    /** The most recently captured piece, or null if no capture has occurred yet. */
    private Piece lastCapturedPiece;
    /** Registered status listeners notified on turn and state changes. */
    private final List<StatusListener> statusListeners = new ArrayList<>();
    /** Registered capture listeners notified when an opponent piece is captured. */
    private final List<CaptureListener> captureListeners = new ArrayList<>();

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
        this.mouseHandler = new BoardMouseHandler(this);
        refreshBoard();
    }

    /**
     * Initializes all 32 starting pieces, resets the turn to White, and clears stale selections.
     * Existing pieces and captures are reset through {@link Board#initialize()}.
     *
     * @throws IllegalStateException if called outside the EDT
     */
    public void startNewGame() {
        requireEventDispatchThread();
        cancelSelection();
        board.initialize();
        currentTurn = Color.WHITE;
        lastCapturedPiece = null;
        boardPanel.clearHighlights();
        refreshBoard();
        clearHistory();
        notifyStatusListeners();
    }

    /**
     * Displays a replacement board without changing its pieces or moved flags.
     * Resets active turn to White and clears previous capture history.
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
        this.currentTurn = Color.WHITE;
        this.lastCapturedPiece = null;
        boardPanel.clearHighlights();
        refreshBoard();
        clearHistory();
        notifyStatusListeners();
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
     *
     * @param target clicked square, or null for the board margin
     * @throws IllegalStateException if called outside the EDT
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
     *
     * @param target source square, or null to clear selection
     * @throws IllegalStateException if called outside the EDT
     */
    public void selectSquare(Position target) {
        requireEventDispatchThread();
        boardPanel.clearHighlights();
        selected = target != null && target.isOnBoard() && board.getPiece(target) != null
                ? target : null;
        if (selected != null) {
            boardPanel.getSquarePanel(selected).setSelected(true);
        }
    }

    /**
     * Cancels selection and any pending mouse gesture without changing the model.
     *
     * @throws IllegalStateException if called outside the EDT
     */
    public void cancelSelection() {
        requireEventDispatchThread();
        mouseHandler.cancel();
        selectSquare(null);
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
     * Returns the active player whose turn it is to move.
     *
     * @return current active turn Color (WHITE or BLACK)
     */
    public Color getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets the active player turn directly.
     *
     * @param turn the player Color whose turn it should be; must not be null
     * @throws IllegalArgumentException if turn is null
     * @throws IllegalStateException if called outside the EDT
     */
    public void setCurrentTurn(Color turn) {
        requireEventDispatchThread();
        if (turn == null) {
            throw new IllegalArgumentException("Turn color cannot be null.");
        }
        this.currentTurn = turn;
        notifyStatusListeners();
    }

    /**
     * Toggles the active player turn between White and Black and notifies status listeners.
     *
     * @throws IllegalStateException if called outside the EDT
     */
    public void switchTurn() {
        requireEventDispatchThread();
        this.currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
        notifyStatusListeners();
    }

    /**
     * Returns a standardized turn status string ("White's Turn" or "Black's Turn").
     *
     * @return turn status string
     */
    public String getTurnStatusText() {
        return (currentTurn == Color.WHITE) ? "White's Turn" : "Black's Turn";
    }

    /**
     * Returns the current status bar message.
     *
     * @return status message representing active turn
     */
    public String getStatusMessage() {
        return getTurnStatusText();
    }

    /**
     * Retrieves the piece captured in the most recent move, if any.
     *
     * @return the last captured {@link Piece}, or null if none
     */
    public Piece getLastCapturedPiece() {
        return lastCapturedPiece;
    }

    /**
     * Captures and removes the piece currently occupying the specified position.
     *
     * <p>Removes the piece from the underlying {@link Board} model, clears its icon
     * immediately from the display, records the capture, and notifies capture listeners.</p>
     *
     * @param target position of the piece to capture
     * @return the captured {@link Piece}, or null if the square was unoccupied
     * @throws IllegalArgumentException if target is null or off-board
     * @throws IllegalStateException if called outside the EDT
     */
    public Piece capturePiece(Position target) {
        requireEventDispatchThread();
        if (target == null || !target.isOnBoard()) {
            throw new IllegalArgumentException("Target position must be on the board: " + target);
        }
        Piece targetPiece = board.getPiece(target);
        if (targetPiece == null) {
            return null;
        }
        // A direct capture is not a recorded move, so older undo records
        // cannot safely be replayed against the changed board.
        undoEntries.clear();
        board.recordCapture(targetPiece);
        board.setPiece(target, null);
        this.lastCapturedPiece = targetPiece;
        boardPanel.updateSquare(target);
        notifyHistoryListeners();
        notifyCaptureListeners(targetPiece, target);
        return targetPiece;
    }

    /**
     * Moves a piece without chess legality or turn restrictions, as required by Phase 2.
     *
     * <p>Same-square moves, empty sources, off-board coordinates, and friendly captures are
     * rejected without modifying the board or throwing exceptions, allowing mouse gestures
     * to cancel cleanly. If the destination square contains an opponent piece, the captured piece
     * is removed from the board model, its icon is cleared immediately from the display, and
     * capture listeners are notified. Upon a successful move or capture, the active turn switches
     * and status listeners are notified.</p>
     *
     * @param from source coordinate containing the piece to move
     * @param to destination target coordinate
     * @return true if the move was executed; false if rejected
     * @throws IllegalStateException if called outside the EDT
     */
    public boolean movePiece(Position from, Position to) {
        requireEventDispatchThread();
        cancelSelection();
        if (from == null || to == null || !from.isOnBoard() || !to.isOnBoard()
                || from.equals(to)) {
            return false;
        }
        Piece moving = board.getPiece(from);
        Piece target = board.getPiece(to);
        if (moving == null || (target != null && target.getColor() == moving.getColor())) {
            return false;
        }

        boolean movedBefore = moving.hasMoved();
        Color turnBefore = currentTurn;
        Piece captured = board.movePiece(from, to);
        undoEntries.push(new UndoEntry(from, to, moving, captured, movedBefore, turnBefore));
        this.lastCapturedPiece = captured;

        boardPanel.clearHighlights();
        refreshBoard();

        history.add(new HistoryEntry(history.size() + 1, moving.getColor().toString(),
                moving.getClass().getSimpleName(), from.toString(), to.toString(),
                captured == null ? "" : captured.getColor() + " " + captured.getClass().getSimpleName()));
        notifyHistoryListeners();

        if (captured != null) {
            notifyCaptureListeners(captured, to);
        }

        switchTurn();
        return true;
    }

    /**
     * Alias for {@link #movePiece(Position, Position)} supporting alternative caller conventions.
     *
     * @param from starting square
     * @param to destination square
     * @return true if the move was executed; false if rejected
     * @throws IllegalStateException if called outside the EDT
     */
    public boolean executeMove(Position from, Position to) {
        return movePiece(from, to);
    }

    /**
     * Adds a status listener to receive turn and status update notifications.
     *
     * @param listener listener to register; must not be null
     * @throws IllegalArgumentException if listener is null
     * @throws IllegalStateException if called outside the EDT
     */
    public void addStatusListener(StatusListener listener) {
        requireEventDispatchThread();
        if (listener == null) {
            throw new IllegalArgumentException("Status listener must not be null.");
        }
        if (!statusListeners.contains(listener)) {
            statusListeners.add(listener);
        }
    }

    /**
     * Removes a previously registered status listener.
     *
     * @param listener listener to unregister
     * @throws IllegalStateException if called outside the EDT
     */
    public void removeStatusListener(StatusListener listener) {
        requireEventDispatchThread();
        statusListeners.remove(listener);
    }

    /**
     * Adds a capture listener to receive notifications when pieces are captured.
     *
     * @param listener listener to register; must not be null
     * @throws IllegalArgumentException if listener is null
     * @throws IllegalStateException if called outside the EDT
     */
    public void addCaptureListener(CaptureListener listener) {
        requireEventDispatchThread();
        if (listener == null) {
            throw new IllegalArgumentException("Capture listener must not be null.");
        }
        if (!captureListeners.contains(listener)) {
            captureListeners.add(listener);
        }
    }

    /**
     * Removes a previously registered capture listener.
     *
     * @param listener listener to unregister
     * @throws IllegalStateException if called outside the EDT
     */
    public void removeCaptureListener(CaptureListener listener) {
        requireEventDispatchThread();
        captureListeners.remove(listener);
    }

    /**
     * Reports whether a recorded move can currently be undone.
     * @return true if an undo record is available
     * @throws IllegalStateException if called outside the EDT
     */
    public boolean canUndo() {
        requireEventDispatchThread();
        return !undoEntries.isEmpty();
    }

    /**
     * Reverses the last successful GUI move and notifies the view and history.
     * @return true if a move was undone
     * @throws IllegalStateException if called off the EDT or board state changed externally
     */
    public boolean undoLastMove() {
        requireEventDispatchThread();
        if (undoEntries.isEmpty()) return false;
        UndoEntry entry = undoEntries.peek();
        board.undoMove(entry.from(), entry.to(), entry.moving(), entry.captured(), entry.movedBefore());
        undoEntries.pop();
        history.remove(history.size() - 1);
        currentTurn = entry.turnBefore();
        lastCapturedPiece = undoEntries.isEmpty() ? null : undoEntries.peek().captured();
        cancelSelection();
        refreshBoard();
        notifyHistoryListeners();
        notifyStatusListeners();
        return true;
    }

    /**
     * Returns a snapshot that cannot change with future moves.
     * @return immutable chronological move history
     */
    public List<HistoryEntry> getHistory() {
        requireEventDispatchThread();
        return List.copyOf(history);
    }

    /**
     * Registers a history observer and supplies its initial snapshot.
     * @param listener observer; must not be null
     */
    public void addHistoryListener(HistoryListener listener) {
        requireEventDispatchThread();
        if (listener == null) throw new IllegalArgumentException("History listener must not be null.");
        if (!historyListeners.contains(listener)) {
            historyListeners.add(listener);
            listener.onHistoryChanged(getHistory());
        }
    }

    /**
     * Unregisters a history observer.
     * @param listener observer to remove
     */
    public void removeHistoryListener(HistoryListener listener) {
        requireEventDispatchThread();
        historyListeners.remove(listener);
    }

    /** Clears stale history when a new board session starts. */
    private void clearHistory() {
        undoEntries.clear();
        history.clear();
        notifyHistoryListeners();
    }

    /** Publishes a stable snapshot before capture handlers may exit the application. */
    private void notifyHistoryListeners() {
        List<HistoryEntry> snapshot = getHistory();
        for (HistoryListener listener : List.copyOf(historyListeners)) {
            listener.onHistoryChanged(snapshot);
        }
    }

    /**
     * Broadcasts the current status message to all registered status listeners.
     */
    private void notifyStatusListeners() {
        String message = getStatusMessage();
        for (StatusListener listener : statusListeners) {
            listener.onStatusChanged(message);
        }
    }

    /**
     * Broadcasts a piece capture event to all registered capture listeners.
     *
     * @param captured the captured piece
     * @param targetPosition position where the capture occurred
     */
    private void notifyCaptureListeners(Piece captured, Position targetPosition) {
        for (CaptureListener listener : captureListeners) {
            listener.onPieceCaptured(captured, targetPosition);
        }
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
