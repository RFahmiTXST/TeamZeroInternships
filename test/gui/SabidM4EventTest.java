package test.gui;

import board.Board;
import board.Position;
import gui.GUIController;
import gui.GameFileService;
import gui.HistoryPanel;
import gui.board.BoardPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/** Sabid's standalone M4 mouse-event regression suite; no shared runner edits. */
public final class SabidM4EventTest {
    private static int checks;
    private SabidM4EventTest() { }

    /** Runs deterministic Swing events on the EDT. @param args unused */
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(SabidM4EventTest::run);
        System.out.println("Sabid M4: " + checks + " checks passed.");
    }

    private static void run() {
        Board board = new Board(); board.initialize();
        BoardPanel panel = new BoardPanel(board);
        GUIController controller = new GUIController(board, panel);
        HistoryPanel history = new HistoryPanel();
        controller.addHistoryListener(history::setHistory);
        JRootPane root = new JRootPane(); root.setContentPane(panel);
        root.setSize(820, 700); layout(root);
        Position e2 = new Position(1, 4), e4 = new Position(3, 4);
        Object pawn = board.getPiece(e2);
        // Back-to-back press/release events must produce exactly one move per pair.
        for (int i = 0; i < 40; i++) {
            Position from = i % 2 == 0 ? e2 : e4;
            Position to = i % 2 == 0 ? e4 : e2;
            click(panel, from); click(panel, to);
            check(board.getPiece(to) == pawn, "rapid click destination " + i);
            check(controller.getHistory().size() == i + 1, "rapid click history " + i);
        }
        check(history.getTable().getRowCount() == 40, "table tracks rapid moves");
        for (int i = 0; i < 40; i++) controller.undoLastMove();
        check(board.getPiece(e2) == pawn && !board.getPiece(e2).hasMoved(), "undo rapid moves restores initial piece");
        check(history.getTable().getRowCount() == 0, "undo clears rapid history");

        for (int[] size : new int[][] {{520, 600}, {1000, 550}, {900, 1000}}) {
            root.setSize(size[0], size[1]); layout(root);
            for (Point outside : new Point[] {new Point(-500, 100), new Point(2000, 100),
                    new Point(100, -500), new Point(100, 2000)}) {
                Component source = label(panel, e2);
                Point end = SwingUtilities.convertPoint(panel, outside, source);
                send(source, MouseEvent.MOUSE_PRESSED, new Point(10, 10), MouseEvent.BUTTON1);
                send(source, MouseEvent.MOUSE_DRAGGED, end, MouseEvent.NOBUTTON);
                check(ghosts(root) == 1, "off-board drag has ghost");
                send(source, MouseEvent.MOUSE_RELEASED, end, MouseEvent.BUTTON1);
                check(board.getPiece(e2) == pawn && controller.getHistory().isEmpty(), "off-board release preserves state");
                check(ghosts(root) == 0 && !controller.canUndo(), "off-board release clears gesture only");
            }
        }
        for (int mode = 0; mode < 3; mode++) {
            controller.startNewGame(); layout(root);
            Component source = label(panel, e2);
            Point end = SwingUtilities.convertPoint(panel.getSquarePanel(e4), 10, 10, source);
            send(source, MouseEvent.MOUSE_PRESSED, new Point(10, 10), MouseEvent.BUTTON1);
            send(source, MouseEvent.MOUSE_DRAGGED, end, MouseEvent.NOBUTTON);
            if (mode == 0) panel.getActionMap().get("cancelMove").actionPerformed(null);
            if (mode == 1) controller.startNewGame();
            if (mode == 2) { Board replacement = new Board(); replacement.initialize(); controller.setBoard(replacement); }
            send(source, MouseEvent.MOUSE_RELEASED, end, MouseEvent.BUTTON1);
            check(controller.getBoard().getPiece(e4) == null, "stale release cannot move after cancellation " + mode);
            check(ghosts(root) == 0 && history.getTable().getRowCount() == 0 && !controller.canUndo(), "cancel clears ghost and preserves history " + mode);
            click(panel, e2); click(panel, e4);
            check(controller.getHistory().size() == 1, "new gesture works after cancellation " + mode);
        }
        // Round-trip through a real file: snapshot alone retains the mutable live board.
        for (boolean drag : new boolean[] {false, true}) {
            controller.startNewGame();
            controller.movePiece(new Position(1, 0), new Position(6, 0));
            GameFileService.GameState saved = savedCopy(controller);
            check(saved.board() != controller.getBoard(), "file load creates independent board " + drag);
            controller.movePiece(new Position(6, 0), new Position(5, 0));
            check(controller.getBoard().getPiece(new Position(6, 0)) == null
                    && controller.getBoard().getPiece(new Position(5, 0)) != null,
                    "live game diverges from saved position " + drag);
            check(saved.board().getPiece(new Position(6, 0)) != null
                    && saved.board().getPiece(new Position(5, 0)) == null,
                    "saved board unaffected by subsequent move " + drag);
            Component source = label(panel, e2);
            Point end = SwingUtilities.convertPoint(panel.getSquarePanel(e4), 10, 10, source);
            if (drag) {
                send(source, MouseEvent.MOUSE_PRESSED, new Point(10, 10), MouseEvent.BUTTON1);
                send(source, MouseEvent.MOUSE_DRAGGED, end, MouseEvent.NOBUTTON);
            } else click(panel, e2);
            controller.restoreSession(saved.board(), saved.turn(), saved.history());
            checkSavedPosition(controller, drag);
            if (drag) send(source, MouseEvent.MOUSE_RELEASED, end, MouseEvent.BUTTON1);
            else click(panel, e4);
            checkSavedPosition(controller, drag);
            check(controller.getBoard().getPiece(e4) == null, "load cancels old gesture " + drag);
            check(ghosts(root) == 0 && !panel.getSquarePanel(e2).isSelected(), "load clears drag and selection " + drag);
            check(controller.getHistory().equals(saved.history()) && history.getTable().getRowCount() == 1,
                    "load preserves saved history after stale gesture " + drag);
            check(!controller.canUndo() && !controller.undoLastMove(), "load prevents undo into old session " + drag);
            check(controller.getBoard().getCapturedPieces().size() == 1 && controller.getCurrentTurn() == saved.turn(),
                    "stale gesture preserves loaded capture and turn " + drag);
            click(panel, e2); click(panel, e4);
            check(controller.getHistory().size() == 2 && controller.canUndo(), "new gesture works after load " + drag);
            controller.undoLastMove();
            check(controller.getHistory().equals(saved.history()) && !controller.canUndo(), "undo stops at loaded session boundary " + drag);
            checkSavedPosition(controller, drag);
        }
    }
    private static GameFileService.GameState savedCopy(GUIController controller) {
        try {
            Path file = Files.createTempFile("sabid-m4-gesture-", ".chess");
            try {
                GameFileService.save(file, GameFileService.snapshot(controller));
                return GameFileService.load(file);
            } finally {
                Files.deleteIfExists(file);
            }
        } catch (IOException exception) {
            throw new AssertionError("Temporary save/load round-trip failed", exception);
        }
    }
    private static void checkSavedPosition(GUIController controller, boolean drag) {
        Board loaded = controller.getBoard();
        Piece pawn = loaded.getPiece(new Position(6, 0));
        check(pawn instanceof Pawn && pawn.getColor() == Color.WHITE && pawn.hasMoved()
                && pawn.getPosition().equals(new Position(6, 0)), "saved A7 pawn restored " + drag);
        check(loaded.getPiece(new Position(1, 0)) == null && loaded.getPiece(new Position(5, 0)) == null,
                "saved A2 and A6 empty " + drag);
        check(controller.getBoardPanel().getSquarePanel(new Position(6, 0)).getPiece() == pawn,
                "loaded board view matches saved position " + drag);
        check(loaded.getCapturedPieces().size() == 1
                && loaded.getCapturedPieces().get(0) instanceof Pawn
                && loaded.getCapturedPieces().get(0).getColor() == Color.BLACK
                && !loaded.getCapturedPieces().get(0).hasMoved(), "saved captured pawn restored " + drag);
    }
    private static int ghosts(JRootPane root) { return root.getLayeredPane().getComponentsInLayer(JLayeredPane.DRAG_LAYER).length; }
    private static Component label(BoardPanel panel, Position position) { return panel.getSquarePanel(position).getComponent(0); }
    private static void click(BoardPanel panel, Position position) {
        Component source = label(panel, position);
        Point point = new Point(source.getWidth() / 2, source.getHeight() / 2);
        send(source, MouseEvent.MOUSE_PRESSED, point, MouseEvent.BUTTON1);
        send(source, MouseEvent.MOUSE_RELEASED, point, MouseEvent.BUTTON1);
    }
    private static void send(Component source, int id, Point point, int button) {
        int modifiers = id == MouseEvent.MOUSE_DRAGGED ? MouseEvent.BUTTON1_DOWN_MASK : 0;
        source.dispatchEvent(new MouseEvent(source, id, System.currentTimeMillis(), modifiers,
                point.x, point.y, 1, false, button));
    }
    private static void layout(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) if (child instanceof Container) layout((Container) child);
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }
}
