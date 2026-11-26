package test.gui;

import board.Board;
import board.Position;
import gui.ChessFrame;
import gui.GUIController;
import gui.HistoryEntry;
import gui.HistoryPanel;
import gui.board.BoardPanel;
import gui.board.BoardSize;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.util.List;
import javax.swing.SwingUtilities;
import test.TestUtils;

/** Tests history snapshots, reset, capture ordering and frame integration. */
public final class HistoryPanelTest {
    private HistoryPanelTest() { }
    public static void main(String[] args) {
        try { SwingUtilities.invokeAndWait(HistoryPanelTest::run); }
        catch (Exception e) { throw new AssertionError(e); }
    }
    private static void run() {
        checkNotationRoundTrip();
        Board board = new Board(); board.initialize();
        GUIController c = new GUIController(board, new BoardPanel(board));
        HistoryPanel panel = new HistoryPanel();
        c.addHistoryListener(panel::setHistory);
        check(0, panel.getTable().getRowCount(), "History starts empty");
        c.clickSquare(new Position(1,4)); c.clickSquare(new Position(3,4));
        check(1, panel.getTable().getRowCount(), "Click path records exactly one move");
        HistoryEntry first = c.getHistory().get(0);
        check(new HistoryEntry(1,"WHITE","Pawn","E2","E4",""), first, "Full move details recorded");
        List<HistoryEntry> snapshot = c.getHistory();
        boolean immutable = false;
        try { snapshot.clear(); } catch (UnsupportedOperationException expected) { immutable = true; }
        check(true, immutable, "History snapshot cannot be edited");
        c.movePiece(new Position(6,3), new Position(4,3)); // D7-D5
        int[] captures = {0};
        c.addCaptureListener((piece, position) -> {
            captures[0]++;
            check(3, panel.getTable().getRowCount(), "Capture appears before callback");
        });
        c.movePiece(new Position(3,4), new Position(4,3)); // E4xD5
        check(1, captures[0], "Capture notified once");
        check("BLACK Pawn", c.getHistory().get(2).captured(), "Captured color and type recorded");
        check("WHITE", c.getHistory().get(2).side(), "Capturing side recorded");
        check("E4", first.to(), "Earlier entry keeps original destination");
        check(1, snapshot.size(), "Earlier snapshot remains stable");
        c.movePiece(new Position(4,3),new Position(5,3)); // White attempts a second turn
        c.movePiece(null,new Position(0,4));
        c.refreshBoard();
        check(3,c.getHistory().size(), "Rejected moves and refresh do not create history");
        check(false,panel.getTable().isCellEditable(0,0), "History cells are read-only");
        check(2,panel.getTable().getSelectedRow(), "Latest entry selected");
        c.startNewGame();
        check(0,c.getHistory().size(), "New game clears controller history");
        check(0,panel.getTable().getRowCount(), "New game clears table");
        c.movePiece(new Position(1,0),new Position(3,0));
        check(1,c.getHistory().get(0).number(), "New game restarts numbering");
        Board replacement = new Board(); replacement.initialize(); c.setBoard(replacement);
        check(0,panel.getTable().getRowCount(), "Replacing model clears stale history");
        int[] notices = {0};
        GUIController.HistoryListener listener = entries -> notices[0]++;
        c.addHistoryListener(listener); c.addHistoryListener(listener); c.removeHistoryListener(listener);
        c.movePiece(new Position(1,0),new Position(3,0));
        check(1,notices[0], "Listener registration deduplicates and removal works");
        javax.swing.JRootPane root = new javax.swing.JRootPane();
        root.setContentPane(panel); root.setSize(400,250);
        java.util.ArrayList<HistoryEntry> many = new java.util.ArrayList<>();
        for (int i=1; i<=60; i++) many.add(new HistoryEntry(i,"WHITE","Pawn","A2","A3",""));
        panel.setHistory(many); layout(root); panel.setHistory(many);
        check(true, panel.getTable().getVisibleRect().intersects(panel.getTable().getCellRect(59,0,true)),
                "Long history scrolls last move into viewport");
        check(59,panel.getTable().getSelectedRow(), "Long history selects newest move");
        panel.setHistory(List.of());
        check(-1,panel.getTable().getSelectedRow(), "Reset clears latest-row selection");
        if (!GraphicsEnvironment.isHeadless()) {
            ChessFrame frame = new ChessFrame();
            try {
                check(frame.getHistoryPanel(), ((BorderLayout)frame.getContentPane().getLayout())
                        .getLayoutComponent(BorderLayout.EAST), "History occupies frame east region");
                frame.movePiece(new Position(1,4),new Position(3,4));
                check(1,frame.getHistoryPanel().getTable().getRowCount(), "Frame wires history observer");
                for (BoardSize size : BoardSize.values()) {
                    frame.applyBoardSize(size);
                    check(true,frame.getHistoryPanel().getWidth() >= HistoryPanel.PANEL_WIDTH,
                            "Settings preserve history width: " + size);
                    check(true,frame.getBoardPanel().getWidth() >= size.getDimension(),
                            "Settings preserve board space: " + size);
                }
            } finally { frame.dispose(); }
        }
    }
    private static void checkNotationRoundTrip() {
        Board board = new Board();
        board.initialize();
        GUIController controller = new GUIController(board, new BoardPanel(board));
        HistoryPanel panel = new HistoryPanel();
        controller.addHistoryListener(panel::setHistory);
        controller.movePiece(new Position(1,4), new Position(3,4)); // e4
        controller.movePiece(new Position(6,5), new Position(5,5)); // f6
        controller.movePiece(new Position(0,3), new Position(4,7)); // Qh5+
        controller.movePiece(new Position(6,6), new Position(5,6)); // g6 blocks
        check("Qh5+", panel.getTable().getValueAt(2,4), "Check stays on the original history row");
        check("g6", panel.getTable().getValueAt(3,4), "Blocking reply has no check marker");
        controller.movePiece(new Position(4,7), new Position(5,6)); // Qxg6+
        check("Qxg6+", panel.getTable().getValueAt(4,4), "Checking capture shows both symbols");
        try {
            java.nio.file.Path file = java.nio.file.Files.createTempFile("check-history-", ".chess");
            try {
                gui.GameFileService.save(file, gui.GameFileService.snapshot(controller));
                controller.undoLastMove();
                check(4, panel.getTable().getRowCount(), "Undo removes only the checking capture");
                var saved = gui.GameFileService.load(file);
                controller.restoreSession(saved.board(), saved.turn(), saved.history());
                check("Qh5+", panel.getTable().getValueAt(2,4), "Load restores earlier check");
                check("Qxg6+", panel.getTable().getValueAt(4,4), "Load restores checking capture");
                check("e4", panel.getTable().getValueAt(0,4), "Unmarked saved row stays unmarked");
            } finally {
                java.nio.file.Files.deleteIfExists(file);
            }
        } catch (java.io.IOException exception) {
            throw new AssertionError("Could not round-trip check history", exception);
        }
    }

    private static void layout(java.awt.Container container) {
        container.doLayout();
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof java.awt.Container) layout((java.awt.Container)child);
        }
    }
    private static void check(Object expected,Object actual,String message) {
        TestUtils.assertEquals(expected,actual,message);
    }
}
