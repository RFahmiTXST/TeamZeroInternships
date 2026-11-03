package gui.board;

import board.Board;
import board.Position;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Renders the 8x8 chessboard grid with perimeter coordinate labels and square components.
 *
 * <p>Orchestrates 64 {@link SquarePanel} instances arranged in standard chess orientation:
 * White back rank at the bottom (rank 1), Black back rank at the top (rank 8), with files
 * A through H spanning left to right. Synchronizes piece placement directly with the
 * underlying {@link Board} domain model.</p>
 *
 * @author Dev 1 (Rayed) &amp; Dev 2 (Abid)
 */
public class BoardPanel extends JPanel {

    /** Generated serial version UID for Swing serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /** Number of rows and columns on the board. */
    public static final int BOARD_SIZE = 8;

    /** 2D matrix of square panels indexed by [row][column]. */
    private final SquarePanel[][] squarePanels;

    /** Panel holding the 8x8 squares. */
    private final JPanel gridContainer;

    /** The current domain board model. */
    private Board board;

    /**
     * Constructs a BoardPanel initialized with the given Board model.
     *
     * @param board the domain Board model to display; must not be null
     */
    public BoardPanel(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board must not be null.");
        }
        this.board = board;
        this.squarePanels = new SquarePanel[BOARD_SIZE][BOARD_SIZE];

        setLayout(new BorderLayout());
        setBackground(new Color(40, 42, 48));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gridContainer = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE, 0, 0));
        gridContainer.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 55), 2));

        // Create the 64 squares.
        // Screen row 0 is rank 8 (row index 7), screen row 7 is rank 1 (row index 0).
        for (int screenRow = 0; screenRow < BOARD_SIZE; screenRow++) {
            int boardRow = (BOARD_SIZE - 1) - screenRow;
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position pos = new Position(boardRow, col);
                SquarePanel square = new SquarePanel(pos);
                squarePanels[boardRow][col] = square;
                gridContainer.add(square);
            }
        }

        add(gridContainer, BorderLayout.CENTER);
        add(createFileLabelsPanel(), BorderLayout.SOUTH);
        add(createRankLabelsPanel(), BorderLayout.WEST);

        updateFromBoard();
    }

    /**
     * Creates the horizontal panel displaying file labels ('A' through 'H').
     *
     * @return JPanel containing file coordinate labels
     */
    private JPanel createFileLabelsPanel() {
        JPanel filePanel = new JPanel(new GridLayout(1, BOARD_SIZE));
        filePanel.setOpaque(false);
        filePanel.setPreferredSize(new Dimension(0, 24));
        filePanel.setBorder(BorderFactory.createEmptyBorder(4, 24, 0, 0));

        Font labelFont = new Font("SansSerif", Font.BOLD, 13);
        for (int col = 0; col < BOARD_SIZE; col++) {
            char fileChar = (char) ('A' + col);
            JLabel label = new JLabel(String.valueOf(fileChar), SwingConstants.CENTER);
            label.setFont(labelFont);
            label.setForeground(new Color(180, 185, 195));
            filePanel.add(label);
        }
        return filePanel;
    }

    /**
     * Creates the vertical panel displaying rank labels ('8' down to '1').
     *
     * @return JPanel containing rank coordinate labels
     */
    private JPanel createRankLabelsPanel() {
        JPanel rankPanel = new JPanel(new GridLayout(BOARD_SIZE, 1));
        rankPanel.setOpaque(false);
        rankPanel.setPreferredSize(new Dimension(24, 0));
        rankPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        Font labelFont = new Font("SansSerif", Font.BOLD, 13);
        for (int screenRow = 0; screenRow < BOARD_SIZE; screenRow++) {
            int rankNum = BOARD_SIZE - screenRow;
            JLabel label = new JLabel(String.valueOf(rankNum), SwingConstants.CENTER);
            label.setFont(labelFont);
            label.setForeground(new Color(180, 185, 195));
            rankPanel.add(label);
        }
        return rankPanel;
    }

    /**
     * Synchronizes all square views with the current state of the domain board model.
     */
    public void updateFromBoard() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Position pos = new Position(r, c);
                squarePanels[r][c].setPiece(board.getPiece(pos));
            }
        }
        repaint();
    }

    /**
     * Replaces the active domain board model and repaints the board.
     *
     * @param newBoard the new Board model to display
     */
    public void setBoard(Board newBoard) {
        if (newBoard != null) {
            this.board = newBoard;
            updateFromBoard();
        }
    }

    /**
     * Gets the domain Board currently displayed.
     *
     * @return the Board model
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Retrieves the SquarePanel component at the given coordinate position.
     *
     * @param pos the board Position
     * @return the corresponding SquarePanel, or null if off-board
     */
    public SquarePanel getSquarePanel(Position pos) {
        if (pos == null || !pos.isOnBoard()) {
            return null;
        }
        return squarePanels[pos.getRow()][pos.getColumn()];
    }

    /**
     * Retrieves the SquarePanel component at the specified row and column indices.
     *
     * @param row zero-based row index (0 = rank 1)
     * @param column zero-based column index (0 = file A)
     * @return the corresponding SquarePanel, or null if indices are out of bounds
     */
    public SquarePanel getSquarePanel(int row, int column) {
        if (row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE) {
            return null;
        }
        return squarePanels[row][column];
    }

    /**
     * Configures the board theme colors for light and dark squares across the grid.
     *
     * @param lightColor background color for light squares
     * @param darkColor background color for dark squares
     */
    public void setThemeColors(Color lightColor, Color darkColor) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                squarePanels[r][c].setSquareColors(lightColor, darkColor);
            }
        }
        repaint();
    }

    /**
     * Clears all selection and highlight states across all squares on the board.
     */
    public void clearHighlights() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                squarePanels[r][c].setSelected(false);
                squarePanels[r][c].setHighlighted(false);
            }
        }
    }
}

