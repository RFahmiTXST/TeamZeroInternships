package gui;

import board.Board;
import gui.board.BoardPanel;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Main application window for the Phase 2 Graphical User Interface (GUI).
 *
 * <p>Houses the 8x8 chessboard view, status bar, and menu bar. Configured with
 * standard window-closing behavior, adaptive minimum dimensions, and clean
 * {@link BorderLayout} separation between visual panels.</p>
 *
 * @author Dev 1 (Rayed)
 */
public class ChessFrame extends JFrame {

    /** Generated serial version UID for Swing serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /** Default application window title. */
    public static final String DEFAULT_TITLE = "Team Zero Chess - Phase 2 GUI";
    /** Minimum allowed window width in pixels. */
    public static final int MIN_WIDTH = 680;
    /** Minimum allowed window height in pixels. */
    public static final int MIN_HEIGHT = 720;

    /** The underlying domain board model. */
    private final Board board;
    /** The central visual board panel component. */
    private final BoardPanel boardPanel;
    /** The status label displayed in the southern footer panel. */
    private final JLabel statusLabel;

    /**
     * Constructs a new ChessFrame with a fresh, initialized standard chess board.
     */
    public ChessFrame() {
        this(createDefaultBoard());
    }

    /**
     * Constructs a new ChessFrame with the specified Board model.
     *
     * @param board the domain Board model to display; must not be null
     */
    public ChessFrame(Board board) {
        super(DEFAULT_TITLE);
        if (board == null) {
            throw new IllegalArgumentException("Board model must not be null.");
        }
        this.board = board;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setPreferredSize(new Dimension(740, 780));
        setLayout(new BorderLayout());

        // Center: Chessboard panel
        this.boardPanel = new BoardPanel(board);
        add(boardPanel, BorderLayout.CENTER);

        // South: Status bar panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(32, 34, 38));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        statusLabel = new JLabel("Team Zero Chess | Phase 2 GUI Initialized | White to move", SwingConstants.LEFT);
        statusLabel.setForeground(new Color(220, 224, 230));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        add(statusPanel, BorderLayout.SOUTH);

        // Top: Menu bar skeleton
        setJMenuBar(createMenuBarSkeleton());

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Helper factory to create and populate a default initialized Board.
     *
     * @return initialized Board
     */
    private static Board createDefaultBoard() {
        Board b = new Board();
        b.initialize();
        return b;
    }

    /**
     * Creates an initial MenuBar skeleton for the window.
     *
     * @return configured JMenuBar
     */
    private JMenuBar createMenuBarSkeleton() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);
        return menuBar;
    }

    /**
     * Gets the domain Board model associated with this frame.
     *
     * @return the Board model
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the central BoardPanel component.
     *
     * @return the BoardPanel instance
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * Gets the footer status label component.
     *
     * @return the JLabel status label
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    /**
     * Updates the status bar message displayed at the bottom of the window.
     *
     * @param message text message to display
     */
    public void setStatusMessage(String message) {
        if (message != null) {
            statusLabel.setText(message);
        }
    }

    /**
     * Refreshes the board visual components from the domain model.
     */
    public void refresh() {
        boardPanel.updateFromBoard();
    }
}

