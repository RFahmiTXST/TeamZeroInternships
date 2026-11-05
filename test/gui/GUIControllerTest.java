package test.gui;

import board.Board;
import board.Position;
import gui.ChessFrame;
import gui.GUIController;
import gui.board.BoardPanel;
import pieces.Color;
import pieces.King;
import pieces.Piece;
import pieces.Queen;
import test.TestUtils;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.SwingUtilities;

/** Headless-compatible integration checks for Dev 3's Milestone 1 controller. */
public final class GUIControllerTest {
    /** Prevents utility construction. */
    private GUIControllerTest() { }

    /**
     * Runs board/view checks on the EDT and verifies invalid thread usage.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        AtomicReference<GUIController> controller = new AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                testInitialPlacementAndReset();
                testExistingBoardAndRefresh();
                testReplacementAndNullArguments();
                testFrameIntegration();
                Board board = new Board();
                GUIController value = new GUIController(board, new BoardPanel(board));
                value.startNewGame();
                value.getBoardPanel().getSquarePanel(0, 0).setSelected(true);
                controller.set(value);
            });
            GUIController value = controller.get();
            assertThrows(IllegalStateException.class, value::startNewGame,
                    "Reset rejects non-EDT calls");
            assertThrows(IllegalStateException.class, value::refreshBoard,
                    "Refresh rejects non-EDT calls");
            assertThrows(IllegalStateException.class, () -> value.setBoard(new Board()),
                    "Board replacement rejects non-EDT calls");
            assertThrows(IllegalStateException.class,
                    () -> new GUIController(value.getBoard(), value.getBoardPanel()),
                    "Controller construction rejects non-EDT calls");
            SwingUtilities.invokeAndWait(() -> {
                TestUtils.assertEquals(16, value.getBoard().getPieces(Color.WHITE).size(),
                        "Wrong-thread calls preserve active board");
                TestUtils.assertEquals(true, value.getBoardPanel().getSquarePanel(0, 0).isSelected(),
                        "Wrong-thread calls preserve selection");
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Controller tests interrupted", e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("Controller tests failed", e.getCause());
        }
    }

    /** Verifies every starting square, then resets a changed board and selection. */
    private static void testInitialPlacementAndReset() {
        Board board = new Board();
        BoardPanel view = new BoardPanel(board);
        GUIController controller = new GUIController(board, view);
        controller.startNewGame();
        String backRank = "RNBQKBNR";
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String expected = null;
                if (row == 0 || row == 7) {
                    expected = (row == 0 ? "w" : "b") + backRank.charAt(col);
                } else if (row == 1 || row == 6) {
                    expected = row == 1 ? "wp" : "bp";
                }
                Piece piece = board.getPiece(new Position(row, col));
                Piece displayed = view.getSquarePanel(row, col).getPiece();
                TestUtils.assertEquals(expected, displayed == null ? null : displayed.toString(),
                        "Controller starting placement at " + new Position(row, col));
                TestUtils.assertEquals(true, piece == displayed
                                && (piece == null || (!piece.hasMoved()
                                && piece.getPosition().equals(new Position(row, col)))),
                        "Starting model/view identity and state at " + new Position(row, col));
            }
        }
        Piece oldPawn = board.getPiece(new Position(1, 4));
        board.movePiece(new Position(1, 4), new Position(6, 3));
        view.getSquarePanel(6, 3).setSelected(true);
        view.getSquarePanel(1, 4).setHighlighted(true);
        controller.startNewGame();
        TestUtils.assertEquals(16, board.getPieces(Color.BLACK).size(),
                "Reset restores captured Black piece");
        TestUtils.assertEquals(16, board.getPieces(Color.WHITE).size(),
                "Reset restores White pieces");
        TestUtils.assertEquals(false, view.getSquarePanel(6, 3).isSelected(),
                "Reset clears selection");
        TestUtils.assertEquals(false, view.getSquarePanel(1, 4).isHighlighted(),
                "Reset clears highlight");
        TestUtils.assertEquals(true, board.getPiece(new Position(1, 4)) != oldPawn
                        && !board.getPiece(new Position(1, 4)).hasMoved(),
                "Reset creates a fresh unmoved pawn");
        TestUtils.assertEquals("bp", view.getSquarePanel(6, 3).getPiece().toString(),
                "Reset refreshes capture destination");
    }

    /** Verifies connecting and refreshing a supplied model does not reset it. */
    private static void testExistingBoardAndRefresh() {
        Board board = new Board();
        Piece queen = new Queen(Color.BLACK, new Position(4, 2));
        board.setPiece(queen.getPosition(), queen);
        BoardPanel view = new BoardPanel(new Board());
        GUIController controller = new GUIController(board, view);
        TestUtils.assertEquals(board, controller.getBoard(), "Controller retains supplied board");
        TestUtils.assertEquals(view, controller.getBoardPanel(), "Controller retains supplied view");
        TestUtils.assertEquals(board, view.getBoard(), "Controller binds view to supplied model");
        TestUtils.assertEquals(queen, view.getSquarePanel(4, 2).getPiece(),
                "Connecting a custom board preserves its pieces");
        TestUtils.assertEquals(0, board.getPieces(Color.WHITE).size(),
                "Connecting does not add starting pieces");
        board.movePiece(new Position(4, 2), new Position(3, 5));
        controller.refreshBoard();
        TestUtils.assertEquals(null, view.getSquarePanel(4, 2).getPiece(),
                "Refresh clears old piece location");
        TestUtils.assertEquals(queen, view.getSquarePanel(3, 5).getPiece(),
                "Refresh displays updated model location");
        TestUtils.assertEquals(true, queen.hasMoved(), "Refresh preserves moved state");
    }

    /** Verifies replacement-board synchronization and rejected null arguments. */
    private static void testReplacementAndNullArguments() {
        Board original = new Board();
        original.initialize();
        BoardPanel view = new BoardPanel(original);
        GUIController controller = new GUIController(original, view);
        Board replacement = new Board();
        Piece king = new King(Color.WHITE, new Position(2, 2));
        replacement.setPiece(king.getPosition(), king);
        view.getSquarePanel(0, 0).setSelected(true);
        controller.setBoard(replacement);
        TestUtils.assertEquals(replacement, controller.getBoard(), "Replacement becomes active model");
        TestUtils.assertEquals(replacement, view.getBoard(), "Replacement becomes displayed model");
        TestUtils.assertEquals(null, view.getSquarePanel(0, 0).getPiece(),
                "Replacement removes obsolete piece icons");
        TestUtils.assertEquals(king, view.getSquarePanel(2, 2).getPiece(),
                "Replacement displays custom king");
        TestUtils.assertEquals(false, view.getSquarePanel(0, 0).isSelected(),
                "Replacement clears stale selection");
        TestUtils.assertEquals(16, original.getPieces(Color.WHITE).size(),
                "Replacement leaves previous model untouched");
        assertThrows(IllegalArgumentException.class, () -> controller.setBoard(null),
                "Null replacement rejected");
        TestUtils.assertEquals(replacement, controller.getBoard(),
                "Rejected replacement preserves model");
        assertThrows(IllegalArgumentException.class, () -> new GUIController(null, view),
                "Null constructor model rejected");
        assertThrows(IllegalArgumentException.class, () -> new GUIController(original, null),
                "Null constructor view rejected");
    }

    /** Verifies frame wiring when an actual display is available. */
    private static void testFrameIntegration() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("SKIP: Controller/frame integration requires a display.");
            return;
        }
        ChessFrame initial = new ChessFrame();
        try {
            TestUtils.assertEquals(16, initial.getBoard().getPieces(Color.WHITE).size(),
                    "Default frame starts controller with White pieces");
            TestUtils.assertEquals(16, initial.getBoard().getPieces(Color.BLACK).size(),
                    "Default frame starts controller with Black pieces");
            TestUtils.assertEquals(initial.getBoardPanel(), initial.getController().getBoardPanel(),
                    "Frame and controller share one view");
        } finally {
            initial.dispose();
        }
        Board custom = new Board();
        ChessFrame frame = new ChessFrame(custom);
        try {
            TestUtils.assertEquals(0, frame.getBoard().getPieces(Color.WHITE).size(),
                    "Supplied frame board is not reinitialized");
            Board replacement = new Board();
            frame.getController().setBoard(replacement);
            frame.refresh();
            TestUtils.assertEquals(replacement, frame.getBoard(),
                    "Frame getter follows controller board replacement");
            TestUtils.assertEquals(replacement, frame.getBoardPanel().getBoard(),
                    "Frame refresh uses replacement board");
        } finally {
            frame.dispose();
        }
    }

    /**
     * Records whether an operation throws the expected exception.
     *
     * @param expected expected exception class
     * @param action operation to run
     * @param description assertion description
     */
    private static void assertThrows(Class<? extends RuntimeException> expected,
                                     Runnable action, String description) {
        try {
            action.run();
            TestUtils.assertEquals(true, false, description);
        } catch (RuntimeException e) {
            TestUtils.assertEquals(true, expected.isInstance(e), description);
        }
    }
}
