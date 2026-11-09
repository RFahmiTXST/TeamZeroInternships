package gui.board;

import board.Board;
import gui.pieces.PieceIconManager;
import pieces.Color;
import pieces.Piece;

import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Displays captured white and black pieces from the active board. */
public final class CapturedPiecesPanel extends JPanel {
    /** Serialization identifier for Swing. */
    private static final long serialVersionUID = 1L;
    /** Displays captured white pieces. */
    private final JPanel whiteRack = new JPanel(new GridLayout(0, 8, 2, 2));
    /** Displays captured black pieces. */
    private final JPanel blackRack = new JPanel(new GridLayout(0, 8, 2, 2));
    /** Count of captured white pieces. */
    private final JLabel whiteCount = new JLabel();
    /** Count of captured black pieces. */
    private final JLabel blackCount = new JLabel();

    /** Creates an empty captured-pieces display. */
    public CapturedPiecesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(whiteCount);
        add(whiteRack);
        add(blackCount);
        add(blackRack);
    }

    /**
     * Rebuilds the racks using the current captured pieces and piece style.
     *
     * @param board active board, whose capture lists are read without modification
     * @throws NullPointerException if board is null
     */
    public void refresh(Board board) {
        Objects.requireNonNull(board, "board");
        refreshRack(whiteRack, whiteCount, "White", board, Color.WHITE);
        refreshRack(blackRack, blackCount, "Black", board, Color.BLACK);
        revalidate();
        repaint();
    }

    /**
     * Replaces one rack with icons and its current count.
     *
     * @param rack panel to update
     * @param count count label to update
     * @param name human-readable color
     * @param board active board
     * @param color color of captured pieces to show
     */
    private static void refreshRack(JPanel rack, JLabel count, String name,
                                    Board board, Color color) {
        java.util.List<Piece> captured = board.getCapturedPieces(color);
        count.setText("Captured " + name + " (" + captured.size() + ")");
        rack.removeAll();
        for (Piece piece : captured) {
            JLabel icon = new JLabel(PieceIconManager.getPieceIcon(piece, 28));
            icon.setToolTipText(name + " " + piece.getClass().getSimpleName());
            rack.add(icon);
        }
    }
}
