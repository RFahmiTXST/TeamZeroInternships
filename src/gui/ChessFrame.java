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
import javax.swing.SwingUtilities;
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

    /** Mediates the active board model and its Swing view. */
    private final GUIController controller;
    /** The central visual board panel component. */
    private final BoardPanel boardPanel;
    /** The status label displayed in the southern footer panel. */
    private final JLabel statusLabel;

    /**
     * Constructs a new ChessFrame with a fresh, initialized standard chess board.
     *
     * @throws IllegalStateException if constructed outside the Swing EDT
     */
    public ChessFrame() {
        this(new Board());
        controller.startNewGame();
    }

    /**
     * Constructs a new ChessFrame with the specified Board model.
     *
     * @param board the domain Board model to display; must not be null
     * @throws IllegalArgumentException if the board is null
     * @throws IllegalStateException if constructed outside the Swing EDT
     */
    public ChessFrame(Board board) {
        super(DEFAULT_TITLE);
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("ChessFrame must be constructed on the Swing EDT.");
        }
        if (board == null) {
            throw new IllegalArgumentException("Board model must not be null.");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setPreferredSize(new Dimension(740, 780));
        setLayout(new BorderLayout());

        // Center: Chessboard panel
        this.boardPanel = new BoardPanel(board);
        this.controller = new GUIController(board, boardPanel);
        // Dev 4 M2: show winner and exit only when a King is captured.
        KingCaptureHandler kingCaptureHandler = new KingCaptureHandler(this);
        controller.addCaptureListener(kingCaptureHandler::onPieceCaptured);
        add(boardPanel, BorderLayout.CENTER);

        // South: Status bar panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(32, 34, 38));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        statusLabel = new JLabel(controller.getStatusMessage(), SwingConstants.LEFT);
        statusLabel.setForeground(new Color(220, 224, 230));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        add(statusPanel, BorderLayout.SOUTH);

        // Register controller status updates with the frame status bar
        controller.addStatusListener(this::setStatusMessage);

        // Top: Menu bar skeleton
        setJMenuBar(createMenuBarSkeleton());

        pack();
        setLocationRelativeTo(null);
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
        return controller.getBoard();
    }

    /**
     * Returns the controller for board initialization and model-view updates.
     *
     * @return the GUI controller
     */
    public GUIController getController() {
        return controller;
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
     *
     * @throws IllegalStateException if called outside the Swing EDT
     */
    public void refresh() {
        controller.refreshBoard();
    }

    /**
     * Gets the active player turn Color from the controller.
     *
     * @return current active player Color
     */
    public pieces.Color getCurrentTurn() {
        return controller.getCurrentTurn();
    }

    /**
     * Executes a move on the underlying board and view through the controller.
     *
     * @param from starting square
     * @param to destination square
     * @return true if the move was executed; false if rejected
     */
    public boolean movePiece(board.Position from, board.Position to) {
        return controller.movePiece(from, to);
    }
}

