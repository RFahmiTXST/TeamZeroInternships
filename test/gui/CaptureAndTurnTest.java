package test.gui;

import board.Board;
import board.Position;
import gui.ChessFrame;
import gui.GUIController;
import gui.board.BoardPanel;
import gui.board.SquarePanel;
import pieces.Bishop;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import test.TestUtils;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Verification test suite for Dev 1's Milestone 2 deliverables:
 * piece capture handling, clean disappearance of captured pieces,
 * and active player turn tracking.
 *
 * @author Dev 1 (Rayed)
 */
public final class CaptureAndTurnTest {

    /** Private constructor to prevent instantiation. */
    private CaptureAndTurnTest() { }

    /**
     * Executes the test suite on the EDT with headless safety checks.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        AtomicReference<GUIController> controllerRef = new AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                testInitialTurnState();
                testTurnSwitchingAndManualSet();
                testNonCaptureMovementSwitchesTurn();
                testPieceCaptureHandling();
                testDirectCapturePieceMethod();
                testFriendlyCaptureRejection();
                testKingCaptureDetection();
                testResetAndSetBoardRestoresTurn();
                testFrameStatusBarIntegration();

                Board board = new Board();
                board.initialize();
                GUIController ctrl = new GUIController(board, new BoardPanel(board));
                controllerRef.set(ctrl);
            });

            // Verify EDT enforcement outside the EDT
            GUIController ctrl = controllerRef.get();
            assertThrows(IllegalStateException.class, ctrl::switchTurn,
                    "switchTurn rejects non-EDT execution");
            assertThrows(IllegalStateException.class, () -> ctrl.setCurrentTurn(Color.BLACK),
                    "setCurrentTurn rejects non-EDT execution");
            assertThrows(IllegalStateException.class, () -> ctrl.movePiece(new Position(1, 0), new Position(2, 0)),
                    "movePiece rejects non-EDT execution");
            assertThrows(IllegalStateException.class, () -> ctrl.capturePiece(new Position(1, 0)),
                    "capturePiece rejects non-EDT execution");
            assertThrows(IllegalStateException.class, () -> ctrl.addStatusListener(msg -> { }),
                    "addStatusListener rejects non-EDT execution");
            assertThrows(IllegalStateException.class, () -> ctrl.addCaptureListener((p, pos) -> { }),
                    "addCaptureListener rejects non-EDT execution");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("CaptureAndTurnTest interrupted", e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("CaptureAndTurnTest failed", e.getCause());
        }
    }

    /** Verifies that a fresh game starts on White's turn with appropriate status text. */
    private static void testInitialTurnState() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        TestUtils.assertEquals(Color.WHITE, controller.getCurrentTurn(),
                "Initial active turn is White");
        TestUtils.assertEquals("White's Turn", controller.getTurnStatusText(),
                "Initial turn status text indicates White's turn");
        TestUtils.assertEquals("White's Turn", controller.getStatusMessage(),
                "Status message matches turn status text");
        TestUtils.assertEquals(null, controller.getLastCapturedPiece(),
                "No captured piece initially");
    }

    /** Verifies turn switching and listener notification on turn state changes. */
    private static void testTurnSwitchingAndManualSet() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        List<String> statusUpdates = new ArrayList<>();
        GUIController.StatusListener listener = statusUpdates::add;
        controller.addStatusListener(listener);

        controller.switchTurn();
        TestUtils.assertEquals(Color.BLACK, controller.getCurrentTurn(),
                "Switch turn advances from White to Black");
        TestUtils.assertEquals("Black's Turn", controller.getTurnStatusText(),
                "Status text updates to Black's turn");
        TestUtils.assertEquals(1, statusUpdates.size(), "Status listener notified on switch");
        TestUtils.assertEquals("Black's Turn", statusUpdates.get(0), "Listener receives Black's Turn");

        controller.switchTurn();
        TestUtils.assertEquals(Color.WHITE, controller.getCurrentTurn(),
                "Switch turn cycles back to White");
        TestUtils.assertEquals(2, statusUpdates.size(), "Status listener notified on second switch");
        TestUtils.assertEquals("White's Turn", statusUpdates.get(1), "Listener receives White's Turn");

        controller.setCurrentTurn(Color.BLACK);
        TestUtils.assertEquals(Color.BLACK, controller.getCurrentTurn(),
                "setCurrentTurn explicitly sets Black");
        TestUtils.assertEquals(3, statusUpdates.size(), "Status listener notified on manual set");

        controller.removeStatusListener(listener);
        controller.switchTurn();
        TestUtils.assertEquals(3, statusUpdates.size(),
                "Unregistered listener is not notified");

        assertThrows(IllegalArgumentException.class, () -> controller.setCurrentTurn(null),
                "setCurrentTurn rejects null color");
    }

    /** Verifies standard non-capture movement clears source, populates target, and switches turn. */
    private static void testNonCaptureMovementSwitchesTurn() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        Position from = new Position(1, 4); // E2
        Position to = new Position(3, 4);   // E4
        Piece movingPiece = board.getPiece(from);

        boolean moved = controller.movePiece(from, to);

        TestUtils.assertEquals(true, moved, "Non-capture move executes successfully");
        TestUtils.assertEquals(null, controller.getLastCapturedPiece(),
                "lastCapturedPiece remains null after quiet move");
        TestUtils.assertEquals(null, board.getPiece(from), "Source square cleared in model");
        TestUtils.assertEquals(movingPiece, board.getPiece(to), "Destination square populated in model");
        TestUtils.assertEquals(null, panel.getSquarePanel(from).getPiece(),
                "Source square cleared in view");
        TestUtils.assertEquals(movingPiece, panel.getSquarePanel(to).getPiece(),
                "Destination square populated in view");
        TestUtils.assertEquals(Color.BLACK, controller.getCurrentTurn(),
                "Move switches turn to Black");
        TestUtils.assertEquals("Black's Turn", controller.getStatusMessage(),
                "Status message updates to Black's Turn");
    }

    /**
     * Verifies complete piece capture handling:
     * opponent piece removed from model, icon cleared from display, captured piece recorded,
     * turn switched to opponent.
     */
    private static void testPieceCaptureHandling() {
        Board board = new Board();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        // Set up White Queen at D1 and Black Rook at D7
        Position queenPos = new Position(0, 3);
        Position rookPos = new Position(6, 3);
        Piece whiteQueen = new Queen(Color.WHITE, queenPos);
        Piece blackRook = new Rook(Color.BLACK, rookPos);
        board.setPiece(queenPos, whiteQueen);
        board.setPiece(rookPos, blackRook);
        controller.refreshBoard();

        TestUtils.assertEquals(whiteQueen, panel.getSquarePanel(queenPos).getPiece(),
                "White Queen rendered at start");
        TestUtils.assertEquals(blackRook, panel.getSquarePanel(rookPos).getPiece(),
                "Black Rook rendered at target");

        List<Piece> capturedEvents = new ArrayList<>();
        List<Position> capturePosEvents = new ArrayList<>();
        controller.addCaptureListener((capturedPiece, targetPos) -> {
            capturedEvents.add(capturedPiece);
            capturePosEvents.add(targetPos);
        });

        // Execute capture
        boolean moved = controller.movePiece(queenPos, rookPos);
        Piece captured = controller.getLastCapturedPiece();

        TestUtils.assertEquals(true, moved, "Capture move executes successfully");
        TestUtils.assertEquals(blackRook, captured, "Captured piece recorded in controller");
        TestUtils.assertEquals(blackRook, controller.getLastCapturedPiece(),
                "lastCapturedPiece recorded in controller");
        TestUtils.assertEquals(1, capturedEvents.size(), "Capture listener triggered once");
        TestUtils.assertEquals(blackRook, capturedEvents.get(0), "Capture listener received Black Rook");
        TestUtils.assertEquals(rookPos, capturePosEvents.get(0), "Capture listener received target pos");

        // Verify model state
        TestUtils.assertEquals(null, board.getPiece(queenPos), "Source square cleared in board");
        TestUtils.assertEquals(whiteQueen, board.getPiece(rookPos), "White Queen occupies target in board");
        TestUtils.assertEquals(0, board.getPieces(Color.BLACK).size(),
                "Captured piece no longer in active pieces list");
        TestUtils.assertEquals(1, board.getCapturedPieces().size(),
                "Captured piece recorded in board captured list");
        TestUtils.assertEquals(blackRook, board.getCapturedPieces().get(0),
                "Captured piece in board is Black Rook");
        TestUtils.assertEquals(1, board.getCapturedPieces(Color.BLACK).size(),
                "Black captured pieces list has size 1");
        TestUtils.assertEquals(0, board.getCapturedPieces(Color.WHITE).size(),
                "White captured pieces list has size 0");

        // Verify view state: old icon disappeared completely, replaced by moving piece
        SquarePanel fromSquare = panel.getSquarePanel(queenPos);
        SquarePanel toSquare = panel.getSquarePanel(rookPos);
        TestUtils.assertEquals(null, fromSquare.getPiece(), "Source square panel has null piece");
        TestUtils.assertEquals(whiteQueen, toSquare.getPiece(),
                "Target square panel displays White Queen");

        // Verify turn state
        TestUtils.assertEquals(Color.BLACK, controller.getCurrentTurn(),
                "Turn switched to Black after White capture");
        TestUtils.assertEquals("Black's Turn", controller.getStatusMessage(),
                "Status bar shows Black's Turn after capture");
    }

    /** Verifies standalone capturePiece method removes piece from board and display. */
    private static void testDirectCapturePieceMethod() {
        Board board = new Board();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        Position pos = new Position(3, 3);
        Piece knight = new Knight(Color.BLACK, pos);
        board.setPiece(pos, knight);
        controller.refreshBoard();

        TestUtils.assertEquals(knight, panel.getSquarePanel(pos).getPiece(), "Knight displayed at target");

        Piece captured = controller.capturePiece(pos);

        TestUtils.assertEquals(knight, captured, "capturePiece returns the captured knight");
        TestUtils.assertEquals(null, board.getPiece(pos), "Board square is cleared");
        TestUtils.assertEquals(null, panel.getSquarePanel(pos).getPiece(), "SquarePanel piece is cleared");
        TestUtils.assertEquals(1, board.getCapturedPieces().size(), "Board recorded captured knight");

        // Capturing an already empty square returns null without error
        Piece none = controller.capturePiece(pos);
        TestUtils.assertEquals(null, none, "Capturing empty square returns null");
    }

    /** Verifies friendly fire / friendly captures are strictly rejected. */
    private static void testFriendlyCaptureRejection() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        Position from = new Position(0, 0); // White Rook
        Position to = new Position(1, 0);   // White Pawn

        Piece rook = board.getPiece(from);
        Piece pawn = board.getPiece(to);

        boolean moved = controller.movePiece(from, to);
        TestUtils.assertEquals(false, moved, "Friendly capture rejected without modifying board");

        // State remains completely intact
        TestUtils.assertEquals(rook, board.getPiece(from), "Friendly piece preserved at from");
        TestUtils.assertEquals(pawn, board.getPiece(to), "Friendly piece preserved at to");
        TestUtils.assertEquals(Color.WHITE, controller.getCurrentTurn(),
                "Rejected friendly move preserves turn");
        TestUtils.assertEquals(0, board.getCapturedPieces().size(),
                "No piece captured on rejected move");
    }

    /** Verifies King capture detection for Dev 4 milestone 2 endgame integration. */
    private static void testKingCaptureDetection() {
        Board board = new Board();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        Position bishopPos = new Position(2, 2);
        Position kingPos = new Position(6, 6);
        Piece whiteBishop = new Bishop(Color.WHITE, bishopPos);
        Piece blackKing = new King(Color.BLACK, kingPos);
        board.setPiece(bishopPos, whiteBishop);
        board.setPiece(kingPos, blackKing);
        controller.refreshBoard();

        boolean moved = controller.movePiece(bishopPos, kingPos);
        Piece captured = controller.getLastCapturedPiece();

        TestUtils.assertEquals(true, moved, "King capture move executes successfully");
        TestUtils.assertEquals(true, captured instanceof King,
                "Captured piece identified as King instance");
        TestUtils.assertEquals(blackKing, captured, "Captured piece matches Black King");
        TestUtils.assertEquals(blackKing, controller.getLastCapturedPiece(),
                "getLastCapturedPiece returns Black King");
    }

    /** Verifies that startNewGame and setBoard reset turn state and capture history. */
    private static void testResetAndSetBoardRestoresTurn() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);

        // Make move to change turn to Black and record capture
        controller.movePiece(new Position(1, 4), new Position(3, 4));
        TestUtils.assertEquals(Color.BLACK, controller.getCurrentTurn(), "Turn is Black before reset");

        controller.startNewGame();
        TestUtils.assertEquals(Color.WHITE, controller.getCurrentTurn(),
                "startNewGame resets turn to White");
        TestUtils.assertEquals("White's Turn", controller.getStatusMessage(),
                "Status message reset to White's Turn");
        TestUtils.assertEquals(null, controller.getLastCapturedPiece(),
                "lastCapturedPiece reset to null");
        TestUtils.assertEquals(0, controller.getBoard().getCapturedPieces().size(),
                "Captured pieces cleared on reset");

        // Test setBoard
        controller.switchTurn();
        TestUtils.assertEquals(Color.BLACK, controller.getCurrentTurn(), "Turn is Black before setBoard");
        Board replacement = new Board();
        controller.setBoard(replacement);
        TestUtils.assertEquals(Color.WHITE, controller.getCurrentTurn(),
                "setBoard resets turn to White");
        TestUtils.assertEquals("White's Turn", controller.getStatusMessage(),
                "Status message reset to White's Turn after setBoard");
    }

    /** Verifies ChessFrame status bar dynamically updates on moves and captures. */
    private static void testFrameStatusBarIntegration() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("SKIP: Frame status bar integration requires display.");
            return;
        }

        ChessFrame frame = new ChessFrame();
        try {
            TestUtils.assertEquals("White's Turn", frame.getStatusLabel().getText(),
                    "Frame status label shows White's Turn initially");
            TestUtils.assertEquals(Color.WHITE, frame.getCurrentTurn(),
                    "frame.getCurrentTurn() matches White");

            // Execute move through frame
            frame.movePiece(new Position(1, 4), new Position(3, 4));

            TestUtils.assertEquals(Color.BLACK, frame.getCurrentTurn(),
                    "frame.getCurrentTurn() advances to Black");
            TestUtils.assertEquals("Black's Turn", frame.getStatusLabel().getText(),
                    "Frame status label dynamically updates to Black's Turn");

            // Reset game
            frame.getController().startNewGame();
            TestUtils.assertEquals(Color.WHITE, frame.getCurrentTurn(),
                    "Frame turn resets to White");
            TestUtils.assertEquals("White's Turn", frame.getStatusLabel().getText(),
                    "Frame status label resets to White's Turn");

        } finally {
            frame.dispose();
        }
    }

    /**
     * Helper to assert that an operation throws an expected exception.
     *
     * @param expected expected exception class
     * @param action executable lambda
     * @param message assertion description
     */
    private static void assertThrows(Class<? extends RuntimeException> expected,
                                     Runnable action, String message) {
        try {
            action.run();
            TestUtils.assertEquals(true, false, message + " (did not throw)");
        } catch (RuntimeException e) {
            TestUtils.assertEquals(true, expected.isInstance(e),
                    message + " (threw " + e.getClass().getSimpleName() + ")");
        }
    }
}
