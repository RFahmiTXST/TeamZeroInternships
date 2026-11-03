package test.gui;

import board.Board;
import board.Position;
import gui.board.BoardPanel;
import gui.board.SquarePanel;
import test.TestUtils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** Headless tests for board layout, model mapping and square feedback. */
public final class BoardPanelTest {
    /** Prevents instantiation. */
    private BoardPanelTest() { }

    /**
     * Runs Swing component checks on the event dispatch thread.
     * @param args unused command line arguments
     */
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(BoardPanelTest::runChecks);
        } catch (Exception exception) {
            throw new IllegalStateException("Board panel test failed", exception);
        }
    }

    /** Exercises mapping, resizing, theme and interaction states. */
    private static void runChecks() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        boolean mapping = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                SquarePanel square = panel.getSquarePanel(row, col);
                mapping &= square != null && square.getPiece() == board.getPiece(new Position(row, col));
                mapping &= square.isLightSquare() == ((row + col) % 2 != 0);
            }
        }
        TestUtils.assertEquals(true, mapping, "All squares map to the board and alternate colors");
        TestUtils.assertEquals(null, panel.getSquarePanel(-1, 0), "Off-board square is rejected");
        TestUtils.assertEquals(null, panel.getSquarePanel((Position) null), "Null position is rejected");
        JPanel grid = (JPanel) panel.getComponent(0);
        TestUtils.assertEquals(panel.getSquarePanel(7, 0), grid.getComponent(0), "A8 is top left");
        TestUtils.assertEquals(panel.getSquarePanel(0, 7), grid.getComponent(63), "H1 is bottom right");

        for (int[] size : new int[][] {{900, 500}, {450, 850}, {688, 688}}) {
            panel.setSize(size[0], size[1]);
            panel.doLayout();
            grid.doLayout();
            SquarePanel square = panel.getSquarePanel(0, 0);
            TestUtils.assertEquals(square.getWidth(), square.getHeight(), "Squares remain square at " + size[0] + "x" + size[1]);
            TestUtils.assertEquals(true, grid.getX() + grid.getWidth() <= panel.getWidth(), "Grid fits width");
            TestUtils.assertEquals(true, grid.getY() + grid.getHeight() <= panel.getHeight(), "Grid fits height");
        }
        JPanel files = (JPanel) panel.getComponent(1);
        JPanel ranks = (JPanel) panel.getComponent(2);
        TestUtils.assertEquals("A", ((JLabel) files.getComponent(0)).getText(), "First file label is A");
        TestUtils.assertEquals("8", ((JLabel) ranks.getComponent(0)).getText(), "First rank label is 8");
        TestUtils.assertEquals(grid.getX() + 2, files.getX(), "Files align with grid cells");
        TestUtils.assertEquals(grid.getY() + 2, ranks.getY(), "Ranks align with grid cells");

        Color light = new Color(230, 220, 200);
        Color dark = new Color(100, 80, 60);
        panel.setThemeColors(light, dark);
        SquarePanel a1 = panel.getSquarePanel(0, 0);
        TestUtils.assertEquals(dark, a1.getBackground(), "Dark theme is applied");
        TestUtils.assertEquals(light, panel.getSquarePanel(0, 1).getBackground(), "Light theme is applied");
        board.movePiece(new Position(1, 4), new Position(3, 4));
        panel.updateFromBoard();
        TestUtils.assertEquals(null, panel.getSquarePanel(1, 4).getPiece(), "Refresh clears source");
        TestUtils.assertEquals(board.getPiece(new Position(3, 4)), panel.getSquarePanel(3, 4).getPiece(), "Refresh displays destination");

        MouseEvent enter = new MouseEvent(a1, MouseEvent.MOUSE_ENTERED, 0, 0, 1, 1, 0, false);
        MouseEvent exit = new MouseEvent(a1, MouseEvent.MOUSE_EXITED, 0, 0, 1, 1, 0, false);
        for (MouseListener listener : a1.getMouseListeners()) { listener.mouseEntered(enter); }
        TestUtils.assertEquals(true, a1.getBorder() != null, "Hover shows a border");
        for (MouseListener listener : a1.getMouseListeners()) { listener.mouseExited(exit); }
        TestUtils.assertEquals(null, a1.getBorder(), "Hover exit clears the border");
        for (FocusListener listener : a1.getFocusListeners()) { listener.focusGained(new FocusEvent(a1, FocusEvent.FOCUS_GAINED)); }
        TestUtils.assertEquals(true, a1.getBorder() != null, "Focus shows a border");
        for (FocusListener listener : a1.getFocusListeners()) { listener.focusLost(new FocusEvent(a1, FocusEvent.FOCUS_LOST)); }
        a1.setHighlighted(true);
        panel.clearHighlights();
        TestUtils.assertEquals(false, a1.isHighlighted(), "Clear highlights resets explicit highlight");

        a1.setSize(40, 40);
        a1.dispatchEvent(new ComponentEvent(a1, ComponentEvent.COMPONENT_RESIZED));
        JLabel iconLabel = (JLabel) a1.getComponent(0);
        TestUtils.assertEquals(true, iconLabel.getIcon().getIconWidth() <= 40, "Piece icon fits a small square");
    }
}
