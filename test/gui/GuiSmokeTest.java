package test.gui;

import board.Board;
import board.Position;
import gui.board.BoardPanel;
import gui.board.SquarePanel;
import pieces.Color;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import test.TestUtils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

/**
 * Headless-compatible checks for Phase 2 board components and model-view updates.
 * No top-level JFrame is created, so this suite can run in a terminal or CI.
 *
 * @author Dev 4 (Towsif)
 */
public final class GuiSmokeTest {

    /** Prevents construction of this test utility. */
    private GuiSmokeTest() { }

    /**
     * Runs component tests on the Swing event dispatch thread.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Running Dev 4 Phase 2 GUI smoke tests...");
        try {
            SwingUtilities.invokeAndWait(() -> {
                testInitialBoardMapping();
                testModelToViewRefresh();
                testBoardReplacement();
                testSelectionAndColors();
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("GUI smoke tests interrupted", e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("GUI smoke tests failed", e.getCause());
        }
    }

    /** Verifies the 64 squares and the starting board-to-view mapping. */
    private static void testInitialBoardMapping() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);

        JPanel grid = (JPanel) panel.getSquarePanel(7, 0).getParent();
        TestUtils.assertEquals(64, grid.getComponentCount(),
                "The GUI contains 64 board squares");
        TestUtils.assertEquals(new Position(7, 0),
                ((SquarePanel) grid.getComponent(0)).getPosition(),
                "Top-left displayed square is A8");
        TestUtils.assertEquals(new Position(0, 7),
                ((SquarePanel) grid.getComponent(63)).getPosition(),
                "Bottom-right displayed square is H1");
        TestUtils.assertEquals(16, board.getPieces(Color.WHITE).size(),
                "Board starts with 16 White pieces");
        TestUtils.assertEquals(16, board.getPieces(Color.BLACK).size(),
                "Board starts with 16 Black pieces");

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position position = new Position(row, col);
                SquarePanel square = panel.getSquarePanel(position);
                TestUtils.assertEquals(true, square != null,
                        "Square exists at " + position);
                TestUtils.assertEquals(position, square.getPosition(),
                        "Square coordinate matches " + position);
                TestUtils.assertEquals(board.getPiece(position), square.getPiece(),
                        "Model and view agree at " + position);
            }
        }

        TestUtils.assertEquals(true, panel.getSquarePanel(0, 4).getPiece() instanceof King,
                "White King is shown at E1");
        TestUtils.assertEquals(true, panel.getSquarePanel(7, 4).getPiece() instanceof King,
                "Black King is shown at E8");
        TestUtils.assertEquals(true, panel.getSquarePanel(0, 3).getPiece() instanceof Queen,
                "White Queen is shown at D1");
        TestUtils.assertEquals(true, panel.getSquarePanel(1, 4).getPiece() instanceof Pawn,
                "White Pawn is shown at E2");
        TestUtils.assertEquals(null, panel.getSquarePanel(3, 4).getPiece(),
                "E4 is initially empty");
    }

    /** Confirms a model change is displayed after the panel refreshes. */
    private static void testModelToViewRefresh() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        Position from = new Position(1, 4); // E2
        Position to = new Position(3, 4);   // E4
        Piece pawn = board.getPiece(from);

        board.movePiece(from, to);
        panel.updateFromBoard();

        TestUtils.assertEquals(null, panel.getSquarePanel(from).getPiece(),
                "E2 view clears after the model moves its Pawn");
        TestUtils.assertEquals(pawn, panel.getSquarePanel(to).getPiece(),
                "E4 view displays the moved Pawn");

        JLabel pieceLabel = (JLabel) panel.getSquarePanel(to).getComponent(0);
        TestUtils.assertEquals(true, pieceLabel.getIcon() != null,
                "Moved Pawn displays an icon");
        TestUtils.assertEquals(true,
                pieceLabel.getToolTipText() != null
                        && pieceLabel.getToolTipText().contains("E4"),
                "Moved Pawn tooltip identifies its new square");
    }

    /** Verifies replacing the domain Board updates its visual state. */
    private static void testBoardReplacement() {
        Board initial = new Board();
        initial.initialize();
        BoardPanel panel = new BoardPanel(initial);
        Board replacement = new Board();
        panel.setBoard(replacement);

        TestUtils.assertEquals(replacement, panel.getBoard(),
                "BoardPanel accepts a replacement Board");
        TestUtils.assertEquals(null, panel.getSquarePanel(0, 4).getPiece(),
                "Empty replacement board clears White King icon");
        TestUtils.assertEquals(null, panel.getSquarePanel(7, 4).getPiece(),
                "Empty replacement board clears Black King icon");
        TestUtils.assertEquals(16, initial.getPieces(Color.WHITE).size(),
                "Original Board is not modified by view replacement");
    }

    /** Checks square colors, selection, highlights, and clearing. */
    private static void testSelectionAndColors() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        SquarePanel a1 = panel.getSquarePanel(0, 0);
        SquarePanel b1 = panel.getSquarePanel(0, 1);
        SquarePanel a2 = panel.getSquarePanel(1, 0);

        TestUtils.assertEquals(false, a1.isLightSquare(), "A1 is a dark square");
        TestUtils.assertEquals(true, b1.isLightSquare(), "B1 is a light square");
        TestUtils.assertEquals(true, a2.isLightSquare(), "A2 is a light square");

        java.awt.Color light = new java.awt.Color(220, 220, 220);
        java.awt.Color dark = new java.awt.Color(90, 90, 90);
        panel.setThemeColors(light, dark);
        TestUtils.assertEquals(dark, a1.getBackground(),
                "Dark square reflects new theme");
        TestUtils.assertEquals(light, b1.getBackground(),
                "Light square reflects new theme");

        a1.setSelected(true);
        b1.setHighlighted(true);
        TestUtils.assertEquals(true, a1.isSelected(), "Selection is retained");
        TestUtils.assertEquals(true, b1.isHighlighted(), "Highlight is retained");
        panel.clearHighlights();
        TestUtils.assertEquals(false, a1.isSelected(), "Clear removes selection");
        TestUtils.assertEquals(false, b1.isHighlighted(), "Clear removes highlight");
        TestUtils.assertEquals(null, panel.getSquarePanel(-1, 0),
                "Off-board square lookup is rejected");
    }
}
