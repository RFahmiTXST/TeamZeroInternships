package test.gui;

import board.Board;
import board.Position;
import gui.GUIController;
import gui.board.BoardPanel;
import gui.board.SquarePanel;
import pieces.Color;
import pieces.King;
import pieces.Rook;
import test.TestUtils;

import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.List;

/** Exercises legal move markers and their lifecycle on the event dispatch thread. */
public final class MoveHintTest {
    /** Utility class. */
    private MoveHintTest() { }

    /** @param args unused command-line arguments */
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(MoveHintTest::runChecks);
        } catch (Exception exception) {
            throw new IllegalStateException("Move hint test failed", exception);
        }
    }

    /** Verifies starting positions, pin filtering, painting and cancellation. */
    private static void runChecks() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        Position b1 = new Position(0, 1);
        controller.clickSquare(b1);
        assertHints(panel, List.of(new Position(2, 0), new Position(2, 2)), "Knight destinations");
        SquarePanel empty = panel.getSquarePanel(2, 0);
        TestUtils.assertEquals(true, markerChangesPaint(empty), "Empty destination paints a dot");
        controller.clickSquare(b1);
        assertHints(panel, List.of(), "Repeat click clears hints");
        controller.selectSquare(new Position(1, 4));
        assertHints(panel, List.of(new Position(2, 4), new Position(3, 4)), "Pawn destinations");
        panel.getActionMap().get("cancelMove").actionPerformed(null);
        assertHints(panel, List.of(), "Escape action clears hints");
        controller.selectSquare(new Position(0, 0));
        assertHints(panel, List.of(), "Blocked rook has no hints");
        controller.selectSquare(new Position(1, 4));
        controller.selectSquare(b1);
        assertHints(panel, List.of(new Position(2, 0), new Position(2, 2)), "Switching selection replaces hints");
        controller.selectSquare(new Position(1, 4));
        controller.refreshBoard();
        assertHints(panel, List.of(), "Board refresh clears stale hints");
        controller.selectSquare(new Position(1, 4));
        controller.clickSquare(null);
        assertHints(panel, List.of(), "Outside click clears hints");
        controller.selectSquare(new Position(1, 4));
        controller.movePiece(new Position(1, 4), new Position(3, 4));
        assertHints(panel, List.of(), "Move completion clears hints");
        controller.setCurrentTurn(Color.WHITE);

        Board pinned = new Board();
        Position king = new Position(0, 4), rook = new Position(1, 4), enemy = new Position(6, 4);
        pinned.setPiece(king, new King(Color.WHITE, king));
        pinned.setPiece(rook, new Rook(Color.WHITE, rook));
        pinned.setPiece(enemy, new Rook(Color.BLACK, enemy));
        controller.setBoard(pinned);
        controller.selectSquare(rook);
        assertHints(panel, List.of(new Position(2, 4), new Position(3, 4), new Position(4, 4),
                new Position(5, 4), enemy), "Pinned rook only hints along pin");
        TestUtils.assertEquals(true, markerChangesPaint(panel.getSquarePanel(enemy)), "Capture destination paints a ring");
        controller.setCurrentTurn(Color.BLACK);
        assertHints(panel, List.of(), "Turn change clears hints");
    }

    /** Checks every square against the expected destinations. */
    private static void assertHints(BoardPanel panel, List<Position> expected, String message) {
        boolean matches = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position position = new Position(row, col);
                matches &= panel.getSquarePanel(position).hasMoveHint() == expected.contains(position);
            }
        }
        TestUtils.assertEquals(true, matches, message);
    }

    /** Compares rendered pixels with and without the marker. */
    private static boolean markerChangesPaint(SquarePanel square) {
        square.setSize(80, 80);
        square.doLayout();
        BufferedImage with = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = with.createGraphics();
        square.paint(graphics);
        graphics.dispose();
        square.setMoveHint(false);
        BufferedImage without = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        graphics = without.createGraphics();
        square.paint(graphics);
        graphics.dispose();
        square.setMoveHint(true);
        for (int y = 0; y < 80; y++) {
            for (int x = 0; x < 80; x++) {
                if (with.getRGB(x, y) != without.getRGB(x, y)) return true;
            }
        }
        return false;
    }
}
