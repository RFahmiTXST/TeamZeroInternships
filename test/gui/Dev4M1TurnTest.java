package test.gui;

import board.Board;
import board.Position;
import gui.GUIController;
import gui.board.BoardPanel;
import pieces.Color;
import test.TestUtils;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/**
 * Dev 4 Phase 3 M1 tests for GUI turn ownership, legality, and state preservation.
 * This suite depends on Dev 3's strict GUIController.movePiece implementation.
 */
public final class Dev4M1TurnTest {
    /** Checks executed in the suite. */
    private static int checks;

    /** Test-only utility class. */
    private Dev4M1TurnTest() { }

    /**
     * Runs controller checks on the Swing Event Dispatch Thread.
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        int oldFailures = TestUtils.getFailedCount();
        checks = 0;
        try {
            SwingUtilities.invokeAndWait(() -> {
                selectionHonorsTurn();
                wrongSideCannotMove();
                illegalGeometryPreservesState();
                legalMovesAlternateTurns();
            });
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Dev 4 turn suite interrupted", exception);
        } catch (InvocationTargetException exception) {
            throw new AssertionError("Dev 4 turn suite failed", exception.getCause());
        }
        System.out.println("Dev 4 Phase 3 M1 GUI turns: " + checks + " assertions executed.");
        if (TestUtils.getFailedCount() != oldFailures) {
            throw new AssertionError("Dev 4 M1 GUI turn regression failed.");
        }
    }

    /** Ensures a player can only select pieces of the active color. */
    private static void selectionHonorsTurn() {
        GUIController controller = freshController();
        controller.selectSquare(at("E7"));
        check(null, controller.getSelected(), "White cannot select Black pawn E7");
        controller.selectSquare(at("E2"));
        check(at("E2"), controller.getSelected(), "White can select White pawn E2");
        controller.setCurrentTurn(Color.BLACK);
        controller.selectSquare(at("E2"));
        check(null, controller.getSelected(), "Black cannot select White pawn E2");
        controller.selectSquare(at("E7"));
        check(at("E7"), controller.getSelected(), "Black can select Black pawn E7");
    }

    /** Direct move calls must enforce ownership, not merely mouse selection. */
    private static void wrongSideCannotMove() {
        GUIController white = freshController();
        check(false, white.movePiece(at("E7"), at("E5")),
                "White turn cannot execute Black E7-E5");
        check(true, white.getBoard().getPiece(at("E7")) != null,
                "Rejected Black pawn stays on E7");
        check(true, white.getBoard().getPiece(at("E5")) == null,
                "Rejected Black pawn leaves E5 empty");
        check(Color.WHITE, white.getCurrentTurn(), "Wrong-side move preserves White turn");
        check(0, white.getHistory().size(), "Wrong-side move creates no history");
        check(false, white.canUndo(), "Wrong-side move creates no Undo entry");

        GUIController black = freshController();
        black.setCurrentTurn(Color.BLACK);
        check(false, black.movePiece(at("E2"), at("E4")),
                "Black turn cannot execute White E2-E4");
        check(true, black.getBoard().getPiece(at("E2")) != null,
                "Rejected White pawn stays on E2");
        check(Color.BLACK, black.getCurrentTurn(), "Wrong-side move preserves Black turn");
        check(0, black.getHistory().size(), "Wrong-side move creates no history for Black");
    }

    /** Illegal geometry cannot move pieces, switch turns or create history. */
    private static void illegalGeometryPreservesState() {
        GUIController controller = freshController();
        check(false, controller.movePiece(at("E2"), at("E5")),
                "White pawn cannot advance three ranks");
        check(true, controller.getBoard().getPiece(at("E2")) != null,
                "Illegal pawn move preserves source");
        check(Color.WHITE, controller.getCurrentTurn(), "Illegal pawn move preserves turn");
        check(0, controller.getHistory().size(), "Illegal pawn move preserves history");
        check(false, controller.canUndo(), "Illegal pawn move creates no Undo");
        check(false, controller.movePiece(at("A1"), at("A3")),
                "Rook cannot jump over A2 pawn");
        check(true, controller.getBoard().getPiece(at("A1")) != null,
                "Illegal rook move preserves A1");
        check(0, controller.getBoard().getCapturedPieces().size(),
                "Rejected moves create no captures");
    }

    /** Legal pawn moves switch turns once, while wrong-turn moves cannot act. */
    private static void legalMovesAlternateTurns() {
        GUIController controller = freshController();
        check(true, controller.movePiece(at("E2"), at("E4")), "White E2-E4 accepted");
        check(Color.BLACK, controller.getCurrentTurn(), "Legal White move passes turn to Black");
        check(1, controller.getHistory().size(), "First legal move adds one history entry");
        check(false, controller.movePiece(at("D2"), at("D4")),
                "White cannot make a second consecutive move");
        check(Color.BLACK, controller.getCurrentTurn(), "Second White attempt keeps Black turn");
        check(1, controller.getHistory().size(), "Second White attempt does not add history");
        check(true, controller.movePiece(at("E7"), at("E5")), "Black E7-E5 accepted");
        check(Color.WHITE, controller.getCurrentTurn(), "Legal Black move passes turn to White");
        check(2, controller.getHistory().size(), "Two legal moves add two history entries");
        check(false, controller.movePiece(at("E5"), at("E4")),
                "White cannot capture own pawn using Black pawn");
        check(Color.WHITE, controller.getCurrentTurn(), "Rejected Black attempt keeps White turn");
        check(2, controller.getHistory().size(), "Rejected Black attempt does not add history");
    }

    /** Initializes the controller and view on the EDT. */
    private static GUIController freshController() {
        Board board = new Board();
        board.initialize();
        return new GUIController(board, new BoardPanel(board));
    }

    /** Converts uppercase algebraic square notation into board coordinates. */
    private static Position at(String square) {
        return new Position(square.charAt(1) - '1', square.charAt(0) - 'A');
    }

    /** Records an assertion using the shared test framework. */
    private static void check(Object expected, Object actual, String label) {
        checks++;
        TestUtils.assertEquals(expected, actual, "Dev4 M1: " + label);
    }
}
