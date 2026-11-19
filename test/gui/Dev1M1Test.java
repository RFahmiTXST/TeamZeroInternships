package test.gui;

import board.Board;
import board.Position;
import gui.GUIController;
import gui.board.BoardPanel;
import pieces.Color;
import test.TestUtils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Headless-compatible unit and integration tests for Dev 1's Milestone 1
 * GUIController turn-restricted selection and legal move querying methods.
 *
 * @author Dev 1 (Rayed)
 */
public final class Dev1M1Test {

    /** Prevents instantiation of utility test class. */
    private Dev1M1Test() { }

    /**
     * Executes all Dev 1 Milestone 1 GUI tests on the EDT.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        java.util.concurrent.atomic.AtomicReference<GUIController> controllerRef =
                new java.util.concurrent.atomic.AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                testSelectSquareTurnEnforcement();
                testClickSquareSwitchSelectionAndDeselect();
                testGetSelected();
                testGetLegalMovesForSelected();
                testIsLegalMoveForSelected();

                Board b = new Board();
                b.initialize();
                controllerRef.set(new GUIController(b, new BoardPanel(b)));
            });
            testEdtEnforcement(controllerRef.get());
            System.out.println("Dev 1 Milestone 1 GUI Controller tests passed successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Dev 1 M1 tests interrupted", e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("Dev 1 M1 tests failed", e.getCause());
        }
    }

    /**
     * Verifies that selectSquare only allows selecting pieces matching currentTurn.
     */
    private static void testSelectSquareTurnEnforcement() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        controller.startNewGame();

        Position e2 = new Position(1, 4); // White Pawn
        Position e7 = new Position(6, 4); // Black Pawn
        Position e4 = new Position(3, 4); // Empty square

        // 1. Initial turn is White. Selecting White Pawn at E2 succeeds.
        controller.selectSquare(e2);
        TestUtils.assertEquals(e2, controller.getSelected(), "Selecting White piece on White turn succeeds");
        TestUtils.assertEquals(true, panel.getSquarePanel(e2).isSelected(), "E2 square panel displays selected highlight");

        // 2. Selecting Black piece on White turn is rejected.
        controller.selectSquare(e7);
        TestUtils.assertEquals(null, controller.getSelected(), "Selecting Black piece on White turn is rejected");
        TestUtils.assertEquals(false, panel.getSquarePanel(e7).isSelected(), "E7 square panel is not selected");
        TestUtils.assertEquals(false, panel.getSquarePanel(e2).isSelected(), "Previous E2 selection highlight cleared");

        // 3. Selecting empty square is rejected.
        controller.selectSquare(e4);
        TestUtils.assertEquals(null, controller.getSelected(), "Selecting empty square is rejected");

        // 4. Selecting null or off-board is rejected.
        controller.selectSquare(null);
        TestUtils.assertEquals(null, controller.getSelected(), "Selecting null clears selection");

        // 5. Switch turn to Black. Selecting Black Pawn at E7 succeeds.
        controller.setCurrentTurn(Color.BLACK);
        controller.selectSquare(e7);
        TestUtils.assertEquals(e7, controller.getSelected(), "Selecting Black piece on Black turn succeeds");
        TestUtils.assertEquals(true, panel.getSquarePanel(e7).isSelected(), "E7 square panel displays selected highlight");

        // 6. On Black turn, selecting White piece at E2 is rejected.
        controller.selectSquare(e2);
        TestUtils.assertEquals(null, controller.getSelected(), "Selecting White piece on Black turn is rejected");
        TestUtils.assertEquals(false, panel.getSquarePanel(e2).isSelected(), "E2 is not selected on Black turn");
    }

    /**
     * Verifies that clickSquare switches selection to another friendly piece,
     * and deselects on clicking the already selected piece.
     */
    private static void testClickSquareSwitchSelectionAndDeselect() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        controller.startNewGame();

        Position e2 = new Position(1, 4); // White Pawn
        Position b1 = new Position(0, 1); // White Knight

        // Click E2: selects White Pawn
        controller.clickSquare(e2);
        TestUtils.assertEquals(e2, controller.getSelected(), "First click selects E2 White Pawn");
        TestUtils.assertEquals(true, panel.getSquarePanel(e2).isSelected(), "E2 is selected");

        // Click B1 (friendly piece): switches selection to B1
        controller.clickSquare(b1);
        TestUtils.assertEquals(b1, controller.getSelected(), "Clicking another friendly piece switches selection to B1");
        TestUtils.assertEquals(true, panel.getSquarePanel(b1).isSelected(), "B1 is selected");
        TestUtils.assertEquals(false, panel.getSquarePanel(e2).isSelected(), "E2 is no longer selected");

        // Click B1 again: deselects B1
        controller.clickSquare(b1);
        TestUtils.assertEquals(null, controller.getSelected(), "Clicking already selected piece deselects it");
        TestUtils.assertEquals(false, panel.getSquarePanel(b1).isSelected(), "B1 selection cleared");
    }

    /**
     * Verifies getSelected() returns correct state through lifecycle.
     */
    private static void testGetSelected() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        controller.startNewGame();

        TestUtils.assertEquals(null, controller.getSelected(), "Initial getSelected is null");

        Position d2 = new Position(1, 3);
        controller.selectSquare(d2);
        TestUtils.assertEquals(d2, controller.getSelected(), "getSelected returns selected square");

        controller.cancelSelection();
        TestUtils.assertEquals(null, controller.getSelected(), "cancelSelection resets getSelected to null");
    }

    /**
     * Verifies getLegalMovesForSelected() returns expected moves and unmodifiable list.
     */
    private static void testGetLegalMovesForSelected() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        controller.startNewGame();

        // When nothing is selected, returns empty list
        List<Position> none = controller.getLegalMovesForSelected();
        TestUtils.assertEquals(0, none.size(), "getLegalMovesForSelected returns empty list when none selected");

        // Select White Pawn at E2
        Position e2 = new Position(1, 4);
        controller.selectSquare(e2);
        List<Position> e2Moves = controller.getLegalMovesForSelected();
        TestUtils.assertEquals(2, e2Moves.size(), "getLegalMovesForSelected for initial E2 returns 2 moves");
        TestUtils.assertEquals(true, e2Moves.contains(new Position(2, 4)), "Contains E3");
        TestUtils.assertEquals(true, e2Moves.contains(new Position(3, 4)), "Contains E4");

        // Verify unmodifiable list
        boolean threwUnsupported = false;
        try {
            e2Moves.add(new Position(4, 4));
        } catch (UnsupportedOperationException e) {
            threwUnsupported = true;
        }
        TestUtils.assertEquals(true, threwUnsupported, "getLegalMovesForSelected returns unmodifiable list");
    }

    /**
     * Verifies isLegalMoveForSelected(Position).
     */
    private static void testIsLegalMoveForSelected() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        controller.startNewGame();

        Position e2 = new Position(1, 4);
        Position e3 = new Position(2, 4);
        Position e4 = new Position(3, 4);
        Position e5 = new Position(4, 4);

        // When nothing selected, returns false
        TestUtils.assertEquals(false, controller.isLegalMoveForSelected(e3), "Returns false when nothing selected");

        controller.selectSquare(e2);
        TestUtils.assertEquals(true, controller.isLegalMoveForSelected(e3), "E3 is legal move for selected E2");
        TestUtils.assertEquals(true, controller.isLegalMoveForSelected(e4), "E4 is legal move for selected E2");
        TestUtils.assertEquals(false, controller.isLegalMoveForSelected(e5), "E5 is not legal move for initial E2");
        TestUtils.assertEquals(false, controller.isLegalMoveForSelected(null), "Null target is not legal move");
        TestUtils.assertEquals(false, controller.isLegalMoveForSelected(new Position(-1, 0)), "Off-board target is not legal move");
    }

    /**
     * Verifies EDT enforcement on selectSquare and clickSquare.
     *
     * @param controller pre-constructed controller
     */
    private static void testEdtEnforcement(GUIController controller) {
        boolean selectThrew = false;
        try {
            controller.selectSquare(new Position(1, 4));
        } catch (IllegalStateException e) {
            selectThrew = true;
        }
        TestUtils.assertEquals(true, selectThrew, "selectSquare rejects non-EDT execution");

        boolean clickThrew = false;
        try {
            controller.clickSquare(new Position(1, 4));
        } catch (IllegalStateException e) {
            clickThrew = true;
        }
        TestUtils.assertEquals(true, clickThrew, "clickSquare rejects non-EDT execution");
    }
}
