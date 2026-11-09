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
        Board board = new Board(); board.initialize();
        GUIController c = new GUIController(board, new BoardPanel(board));
        HistoryPanel panel = new HistoryPanel();
        c.addHistoryListener(panel::setHistory);
        check(0, panel.getTable().getRowCount(), "History starts empty");
        c.clickSquare(new Position(1,4)); c.clickSquare(new Position(4,4));
        check(1, panel.getTable().getRowCount(), "Click path records exactly one move");
        HistoryEntry first = c.getHistory().get(0);
        check(new HistoryEntry(1,"WHITE","Pawn","E2","E5",""), first, "Full move details recorded");
        List<HistoryEntry> snapshot = c.getHistory();
        boolean immutable = false;
        try { snapshot.clear(); } catch (UnsupportedOperationException expected) { immutable = true; }
        check(true, immutable, "History snapshot cannot be edited");
        c.movePiece(new Position(4,4),new Position(6,4));
        check(2, panel.getTable().getRowCount(), "Capture adds one row");
        check("BLACK Pawn", c.getHistory().get(1).captured(), "Captured color and type recorded");
        check("WHITE", c.getHistory().get(1).side(), "Actual piece color recorded despite turn indicator");
        check("E5", first.to(), "Earlier entry does not follow mutable piece");
        check(1, snapshot.size(), "Earlier snapshot remains stable");
        c.movePiece(new Position(6,4),new Position(0,4));
        c.movePiece(null,new Position(0,4));
        c.refreshBoard(); c.switchTurn();
        check(2,c.getHistory().size(), "Rejected moves and refresh do not create history");
        check(false,panel.getTable().isCellEditable(0,0), "History cells are read-only");
        check(1,panel.getTable().getSelectedRow(), "Latest entry selected");
        c.addCaptureListener((piece, position) -> check(3,panel.getTable().getRowCount(),
                "Winning move appears before endgame callback"));
        c.movePiece(new Position(6,4),new Position(7,4));
        check("BLACK King", c.getHistory().get(2).captured(), "King capture recorded");
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
