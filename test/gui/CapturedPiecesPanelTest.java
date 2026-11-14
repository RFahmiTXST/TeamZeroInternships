package test.gui;

import board.Board;
import board.Position;
import gui.board.CapturedPiecesPanel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import test.TestUtils;

/** Checks captured-piece rack updates on the Swing event dispatch thread. */
public final class CapturedPiecesPanelTest {
    /** Prevents construction. */
    private CapturedPiecesPanelTest() { }

    /**
     * Runs the rack tests.
     * @param args unused arguments
     */
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
            Board board = new Board();
            board.initialize();
            CapturedPiecesPanel panel = new CapturedPiecesPanel();
            panel.refresh(board);
            check(panel, 0, 0);
            board.movePiece(new Position(1, 3), new Position(6, 3));
            panel.refresh(board);
            check(panel, 0, 1);
            board.movePiece(new Position(6, 4), new Position(1, 4));
            panel.refresh(board);
            check(panel, 1, 1);
            board.initialize();
            panel.refresh(board);
            check(panel, 0, 0);
            });
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Checks counts and icon totals for both colors.
     * @param panel panel to inspect
     * @param white number of captured white pieces
     * @param black number of captured black pieces
     */
    private static void check(CapturedPiecesPanel panel, int white, int black) {
        TestUtils.assertEquals("Captured White (" + white + ")",
                ((JLabel) panel.getComponent(0)).getText(), "White capture count");
        TestUtils.assertEquals(white, ((JPanel) panel.getComponent(1)).getComponentCount(),
                "White captured icons");
        TestUtils.assertEquals("Captured Black (" + black + ")",
                ((JLabel) panel.getComponent(2)).getText(), "Black capture count");
        TestUtils.assertEquals(black, ((JPanel) panel.getComponent(3)).getComponentCount(),
                "Black captured icons");
    }
}
