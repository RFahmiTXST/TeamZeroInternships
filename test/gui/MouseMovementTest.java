package test.gui;

import board.Board;
import board.Position;
import gui.GUIController;
import gui.board.BoardPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JRootPane;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import test.TestUtils;

/** Regression tests for real mouse events, unrestricted movement and cancellation. */
public final class MouseMovementTest {
    private MouseMovementTest() { }
    public static void main(String[] args) {
        try { SwingUtilities.invokeAndWait(MouseMovementTest::run); }
        catch (Exception e) { throw new AssertionError(e); }
    }
    private static void run() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        JRootPane root = new JRootPane();
        root.setContentPane(panel);
        root.setSize(820, 700);
        layout(root);
        Position e2 = new Position(1,4), e5 = new Position(4,4);
        Object pawn = board.getPiece(e2);
        click(panel, e2);
        check(true, panel.getSquarePanel(e2).isSelected(), "Label click selects source");
        click(panel, e5);
        check(pawn, board.getPiece(e5), "Click moves pawn unrestricted three ranks");
        check(null, panel.getSquarePanel(e2).getPiece(), "Source icon cleared");
        check(pawn, panel.getSquarePanel(e5).getPiece(), "Destination icon refreshed");
        check(e5, board.getPiece(e5).getPosition(), "Model coordinates updated");
        Position b1 = new Position(0,1), f6 = new Position(5,5);
        Object knight = board.getPiece(b1);
        Component label = panel.getSquarePanel(b1).getComponent(0);
        Point start = new Point(label.getWidth()/2, label.getHeight()/2);
        Point end = SwingUtilities.convertPoint(panel.getSquarePanel(f6), 20, 20, label);
        send(label, MouseEvent.MOUSE_PRESSED, start, MouseEvent.BUTTON1);
        send(label, MouseEvent.MOUSE_DRAGGED, end, MouseEvent.NOBUTTON);
        check(1, root.getLayeredPane().getComponentsInLayer(JLayeredPane.DRAG_LAYER).length, "Drag ghost shown");
        check(knight, board.getPiece(b1), "Drag does not mutate before drop");
        send(label, MouseEvent.MOUSE_RELEASED, end, MouseEvent.BUTTON1);
        check(knight, board.getPiece(f6), "Drag from child label maps to target");
        check(0, root.getLayeredPane().getComponentsInLayer(JLayeredPane.DRAG_LAYER).length, "Ghost removed on drop");
        check(false, controller.movePiece(e5, e5), "Same square rejected");
        check(false, controller.movePiece(e5, new Position(0,4)), "Friendly capture rejected");
        check(false, controller.movePiece(e2, e5), "Empty source rejected");
        check(false, controller.movePiece(e5, new Position(9,4)), "Offboard rejected");
        check(false, controller.movePiece(null, e5), "Null source rejected");
        check(pawn, board.getPiece(e5), "Rejected moves preserve source");
        Position e7 = new Position(6,4);
        check(true, controller.movePiece(e5, e7), "Opposing capture allowed");
        check(pawn, board.getPiece(e7), "Captured square replaced");
        check(15, board.getPieces(pieces.Color.BLACK).size(), "Captured opponent removed");
        root.setSize(1000, 550); layout(root);
        click(panel, e7); click(panel, e7);
        check(false, panel.getSquarePanel(e7).isSelected(), "Repeat click deselects at resized layout");
        click(panel, new Position(3,3));
        check(false, panel.getSquarePanel(e7).isSelected(), "Empty click leaves no selection");
        label = panel.getSquarePanel(e7).getComponent(0);
        start = new Point(10,10);
        end = SwingUtilities.convertPoint(panel, -30, -30, label);
        send(label, MouseEvent.MOUSE_PRESSED, start, MouseEvent.BUTTON1);
        send(label, MouseEvent.MOUSE_DRAGGED, end, MouseEvent.NOBUTTON);
        send(label, MouseEvent.MOUSE_RELEASED, end, MouseEvent.BUTTON1);
        check(pawn, board.getPiece(e7), "Offboard drop preserves piece");
        check(0, root.getLayeredPane().getComponentsInLayer(JLayeredPane.DRAG_LAYER).length, "Offboard drop cleans ghost");
        click(panel, e7);
        Component empty = panel.getSquarePanel(e2).getComponent(0);
        Point target = SwingUtilities.convertPoint(panel.getSquarePanel(e5), 10,10,empty);
        send(empty, MouseEvent.MOUSE_PRESSED, new Point(10,10), MouseEvent.BUTTON1);
        send(empty, MouseEvent.MOUSE_DRAGGED, target, MouseEvent.NOBUTTON);
        send(empty, MouseEvent.MOUSE_RELEASED, target, MouseEvent.BUTTON1);
        check(pawn, board.getPiece(e7), "Empty-origin drag cannot move previous selection");
        send(label, MouseEvent.MOUSE_PRESSED, start, MouseEvent.BUTTON1);
        send(label, MouseEvent.MOUSE_DRAGGED, end, MouseEvent.NOBUTTON);
        panel.getActionMap().get("cancelMove").actionPerformed(null);
        send(label, MouseEvent.MOUSE_RELEASED, end, MouseEvent.BUTTON1);
        check(pawn, board.getPiece(e7), "Escape cancels pending gesture");
        check(0, root.getLayeredPane().getComponentsInLayer(JLayeredPane.DRAG_LAYER).length, "Escape removes ghost");
        send(label, MouseEvent.MOUSE_PRESSED, start, MouseEvent.BUTTON1);
        controller.startNewGame();
        send(label, MouseEvent.MOUSE_RELEASED, start, MouseEvent.BUTTON1);
        check(false, panel.getSquarePanel(e7).isSelected(), "Reset cancels pending press");
        check(16, board.getPieces(pieces.Color.WHITE).size(), "Reset restores pieces");
        send(label, MouseEvent.MOUSE_PRESSED, start, MouseEvent.BUTTON3);
        send(label, MouseEvent.MOUSE_RELEASED, start, MouseEvent.BUTTON3);
        check(false, panel.getSquarePanel(e7).isSelected(), "Right click ignored");
        send(label, MouseEvent.MOUSE_PRESSED, start, MouseEvent.BUTTON1);
        Board replacement = new Board(); replacement.initialize();
        controller.setBoard(replacement);
        send(label, MouseEvent.MOUSE_RELEASED, start, MouseEvent.BUTTON1);
        check(false, panel.getSquarePanel(e7).isSelected(), "Replacement cancels pending gesture");
    }
    private static void layout(Container c) {
        c.doLayout();
        for (Component child : c.getComponents()) if (child instanceof Container) layout((Container) child);
    }
    private static void click(BoardPanel p, Position pos) {
        Component label = p.getSquarePanel(pos).getComponent(0);
        Point point = new Point(label.getWidth()/2, label.getHeight()/2);
        send(label, MouseEvent.MOUSE_PRESSED, point, MouseEvent.BUTTON1);
        send(label, MouseEvent.MOUSE_RELEASED, point, MouseEvent.BUTTON1);
    }
    private static void send(Component c, int id, Point p, int button) {
        int modifiers = id == MouseEvent.MOUSE_DRAGGED ? MouseEvent.BUTTON1_DOWN_MASK : 0;
        c.dispatchEvent(new MouseEvent(c, id, System.currentTimeMillis(), modifiers, p.x,p.y,1,false,button));
    }
    private static void check(Object expected, Object actual, String message) {
        TestUtils.assertEquals(expected, actual, message);
    }
}
