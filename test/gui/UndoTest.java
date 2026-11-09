package test.gui;

import board.Board;
import board.Position;
import gui.GUIController;
import gui.ChessFrame;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;
import gui.board.BoardPanel;
import gui.board.CapturedPiecesPanel;
import gui.pieces.PieceIconManager;
import gui.pieces.PieceStyle;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import pieces.Color;
import pieces.Piece;
import test.TestUtils;

/** Integration checks for undo, captured pieces and move history. */
public final class UndoTest {
    /** Prevents instantiation. */
    private UndoTest() { }

    /** Runs the checks on the Swing event dispatch thread. @param args unused */
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(UndoTest::checkUndo);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    /** Exercises multi-step undo, capture restoration and session resets. */
    private static void checkUndo() {
        Board board = new Board();
        board.initialize();
        BoardPanel view = new BoardPanel(board);
        GUIController controller = new GUIController(board, view);
        CapturedPiecesPanel rack = new CapturedPiecesPanel();
        List<Integer> sizes = new ArrayList<>();
        controller.addHistoryListener(history -> {
            sizes.add(history.size());
            rack.refresh(controller.getBoard());
        });
        Position e2 = new Position(1, 4), e4 = new Position(3, 4);
        Position d7 = new Position(6, 3), d5 = new Position(4, 3);
        Piece pawn = board.getPiece(e2);
        Piece opponent = board.getPiece(d7);
        TestUtils.assertEquals(false, controller.undoLastMove(), "Undo on empty history is harmless");
        controller.movePiece(e2, e4);
        controller.movePiece(d7, d5);
        controller.movePiece(e4, d5);
        TestUtils.assertEquals(3, controller.getHistory().size(), "Three moves recorded");
        TestUtils.assertEquals("Captured Black (1)", ((JLabel) rack.getComponent(2)).getText(), "Rack shows capture");
        TestUtils.assertEquals(true, controller.undoLastMove(), "Undo capture succeeds");
        TestUtils.assertEquals(pawn, board.getPiece(e4), "Undo returns moving piece");
        TestUtils.assertEquals(opponent, board.getPiece(d5), "Undo restores captured piece");
        TestUtils.assertEquals(e4, pawn.getPosition(), "Undo restores moved piece coordinates");
        TestUtils.assertEquals(true, pawn.hasMoved(), "Earlier moved flag preserved");
        TestUtils.assertEquals(0, board.getCapturedPieces().size(), "Undo removes captured piece from rack model");
        TestUtils.assertEquals("Captured Black (0)", ((JLabel) rack.getComponent(2)).getText(), "Rack refreshes after undo");
        TestUtils.assertEquals(Color.WHITE, controller.getCurrentTurn(), "Undo restores prior turn");
        TestUtils.assertEquals(2, controller.getHistory().size(), "Undo removes history entry");
        TestUtils.assertEquals(opponent, view.getSquarePanel(d5).getPiece(), "Undo refreshes destination view");
        controller.undoLastMove();
        TestUtils.assertEquals(opponent, board.getPiece(d7), "Second undo restores black pawn");
        TestUtils.assertEquals(false, opponent.hasMoved(), "Undo restores original moved flag");
        controller.undoLastMove();
        TestUtils.assertEquals(pawn, board.getPiece(e2), "Third undo restores white pawn");
        TestUtils.assertEquals(false, pawn.hasMoved(), "Original unmoved state restored");
        TestUtils.assertEquals(0, controller.getHistory().size(), "All history cleared after three undos");
        TestUtils.assertEquals(0, sizes.get(sizes.size() - 1), "History observer receives undo");
        controller.movePiece(e2, e4);
        controller.startNewGame();
        TestUtils.assertEquals(false, controller.undoLastMove(), "New game clears undo state");
        controller.movePiece(e2, e4);
        controller.capturePiece(new Position(6, 0));
        TestUtils.assertEquals(false, controller.canUndo(), "Direct capture invalidates older undo records");
        TestUtils.assertEquals(false, controller.undoLastMove(), "Undo after direct capture is harmless");
        TestUtils.assertEquals(1, controller.getHistory().size(), "Direct capture preserves prior move history");
        controller.setBoard(new Board());
        TestUtils.assertEquals(false, controller.undoLastMove(), "Board replacement clears undo state");
        if (!GraphicsEnvironment.isHeadless()) checkFrameButton();
    }

    /** Verifies the actual sidebar button reverses a move in the frame. */
    private static void checkFrameButton() {
        ChessFrame frame = new ChessFrame();
        try {
            JButton button = findUndo(frame);
            TestUtils.assertEquals(true, button != null, "Undo button is in the frame");
            if (button == null) return;
            TestUtils.assertEquals(false, button.isEnabled(), "Undo button starts disabled");
            Position from = new Position(1, 4), to = new Position(3, 4);
            Piece piece = frame.getBoard().getPiece(from);
            frame.getController().movePiece(from, to);
            TestUtils.assertEquals(true, button.isEnabled(), "Move enables undo button");
            button.doClick();
            TestUtils.assertEquals(piece, frame.getBoard().getPiece(from), "Button restores piece");
            TestUtils.assertEquals(false, button.isEnabled(), "Undo button disables after final undo");
            frame.getController().movePiece(from, new Position(6, 3));
            frame.getController().capturePiece(new Position(6, 0));
            TestUtils.assertEquals(false, button.isEnabled(), "Direct capture disables stale undo button");
            CapturedPiecesPanel rack = findRack(frame);
            TestUtils.assertEquals(true, rack != null, "Captured rack is in the frame");
            if (rack != null) {
                JPanel blackRack = (JPanel) rack.getComponent(3);
                JLabel icon = (JLabel) blackRack.getComponent(0);
                Piece captured = frame.getBoard().getCapturedPieces(Color.BLACK).get(0);
                frame.getSettingsDialog().getPieceStyleComboBox().setSelectedItem(PieceStyle.MINIMALIST);
                frame.getSettingsDialog().applySettings();
                JLabel updated = (JLabel) blackRack.getComponent(0);
                TestUtils.assertEquals(PieceIconManager.getPieceIcon(captured, 28, PieceStyle.MINIMALIST),
                        updated.getIcon(), "Settings immediately refresh captured piece icons");
                TestUtils.assertEquals(false, icon == updated, "Rack rebuilt after style change");
            }
        } finally {
            PieceIconManager.setPieceStyle(PieceStyle.CLASSIC_VECTOR);
            frame.dispose();
        }
    }

    /** Finds the capture rack. @param component root @return rack or null */
    private static CapturedPiecesPanel findRack(Component component) {
        if (component instanceof CapturedPiecesPanel) return (CapturedPiecesPanel) component;
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                CapturedPiecesPanel found = findRack(child);
                if (found != null) return found;
            }
        }
        return null;
    }

    /** Finds the undo button in the component tree. @param component root @return button or null */
    private static JButton findUndo(Component component) {
        if (component instanceof JButton && "Undo".equals(((JButton) component).getText())) {
            return (JButton) component;
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                JButton found = findUndo(child);
                if (found != null) return found;
            }
        }
        return null;
    }
}
