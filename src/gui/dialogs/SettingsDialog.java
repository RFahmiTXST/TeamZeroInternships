package gui.dialogs;

import gui.ChessFrame;
import gui.board.BoardPanel;
import gui.board.BoardSize;
import gui.board.BoardTheme;
import gui.pieces.PieceIconManager;
import gui.pieces.PieceStyle;
import pieces.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * Customization settings dialog for the Java Swing Chess GUI.
 *
 * <p>Provides real-time interactive selection of:</p>
 * <ul>
 *   <li><b>Board Theme &amp; Background Color:</b> Classic Wood, Modern Gray, Slate Blue, Forest Green.</li>
 *   <li><b>Chess Piece Style:</b> Classic Vector, Minimalist, High-Contrast.</li>
 *   <li><b>Board Dimensions:</b> Small (480x480), Medium (640x640), Large (800x800).</li>
 * </ul>
 * <p>Features an interactive live 2x2 preview panel and an <b>Apply</b> action that dynamically
 * updates colors, vector piece icons, and window dimensions in real time without restarting
 * the application or losing ongoing game moves or board state.</p>
 *
 * @author Dev 1 (Rayed)
 */
public final class SettingsDialog extends JDialog {

    /** Generated serial version UID for Swing serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /** Dialog window title. */
    public static final String DIALOG_TITLE = "Chess Appearance & Settings";

    /** Target BoardPanel to customize. */
    private final BoardPanel boardPanel;

    /** Parent owner window. */
    private final Window ownerWindow;

    /** Dropdown selector for board background themes. */
    private final JComboBox<BoardTheme> themeComboBox;

    /** Dropdown selector for piece visual styles. */
    private final JComboBox<PieceStyle> pieceStyleComboBox;

    /** Dropdown selector for board sizing presets. */
    private final JComboBox<BoardSize> boardSizeComboBox;

    /** Action button to apply settings in real-time. */
    private final JButton applyButton;

    /** Action button to apply settings and dismiss dialog. */
    private final JButton okButton;

    /** Action button to cancel and dismiss dialog. */
    private final JButton cancelButton;

    /** Action button to reset selections to default values. */
    private final JButton resetButton;

    /** Mini 2x2 preview square panels [row][col]. */
    private final JPanel[][] previewSquares = new JPanel[2][2];

    /** Labels displaying preview piece icons inside preview squares. */
    private final JLabel[][] previewLabels = new JLabel[2][2];

    /** Description label explaining selected piece style. */
    private final JLabel pieceStyleDescLabel;

    /**
     * Constructs a non-modal SettingsDialog for the given ChessFrame.
     *
     * @param frame the main ChessFrame application window; may be null
     */
    public SettingsDialog(ChessFrame frame) {
        this(frame, frame != null ? frame.getBoardPanel() : null, false);
    }

    /**
     * Constructs a non-modal SettingsDialog with a parent window and board panel.
     *
     * @param owner the parent Window; may be null
     * @param boardPanel the BoardPanel component to configure; may be null
     */
    public SettingsDialog(Window owner, BoardPanel boardPanel) {
        this(owner, boardPanel, false);
    }

    /**
     * Constructs a SettingsDialog with custom modality.
     *
     * @param owner the parent Window; may be null
     * @param boardPanel the BoardPanel component to configure; may be null
     * @param modal true for modal dialog, false for modeless
     */
    public SettingsDialog(Window owner, BoardPanel boardPanel, boolean modal) {
        super(owner, DIALOG_TITLE, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        this.ownerWindow = owner;
        this.boardPanel = boardPanel;

        // Determine initial settings from active panel or defaults
        BoardTheme initialTheme = boardPanel != null ? boardPanel.getTheme() : BoardTheme.CLASSIC_WOOD;
        BoardSize initialSize = boardPanel != null ? boardPanel.getBoardSize() : BoardSize.MEDIUM;
        PieceStyle initialStyle = PieceIconManager.getPieceStyle();

        // Initialize selectors
        this.themeComboBox = new JComboBox<>(BoardTheme.values());
        this.themeComboBox.setSelectedItem(initialTheme);
        this.themeComboBox.setRenderer(new ThemeCellRenderer());

        this.pieceStyleComboBox = new JComboBox<>(PieceStyle.values());
        this.pieceStyleComboBox.setSelectedItem(initialStyle);

        this.boardSizeComboBox = new JComboBox<>(BoardSize.values());
        this.boardSizeComboBox.setSelectedItem(initialSize);

        this.pieceStyleDescLabel = new JLabel(initialStyle.getDescription());
        this.pieceStyleDescLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        this.pieceStyleDescLabel.setForeground(new java.awt.Color(160, 165, 175));

        // Initialize action buttons
        this.applyButton = new JButton("Apply");
        this.applyButton.setMnemonic(KeyEvent.VK_A);
        this.applyButton.setToolTipText("Apply selections immediately to the active game without closing dialog");

        this.okButton = new JButton("OK");
        this.okButton.setMnemonic(KeyEvent.VK_O);
        this.okButton.setToolTipText("Apply selections and close dialog");

        this.cancelButton = new JButton("Cancel");
        this.cancelButton.setMnemonic(KeyEvent.VK_C);
        this.cancelButton.setToolTipText("Discard changes and close dialog");

        this.resetButton = new JButton("Reset Defaults");
        this.resetButton.setMnemonic(KeyEvent.VK_R);
        this.resetButton.setToolTipText("Reset options to Classic Wood, Classic Vector, and Medium");

        // Build UI structure
        initUI();

        // Wire event handlers
        this.themeComboBox.addActionListener(e -> updatePreview());
        this.pieceStyleComboBox.addActionListener(e -> {
            PieceStyle sel = (PieceStyle) pieceStyleComboBox.getSelectedItem();
            if (sel != null) {
                pieceStyleDescLabel.setText(sel.getDescription());
            }
            updatePreview();
        });
        this.boardSizeComboBox.addActionListener(e -> updatePreview());

        this.applyButton.addActionListener(e -> applySettings());
        this.okButton.addActionListener(e -> {
            applySettings();
            dispose();
        });
        this.cancelButton.addActionListener(e -> dispose());
        this.resetButton.addActionListener(e -> resetToDefaults());

        // Update preview immediately
        updatePreview();

        pack();
        setResizable(false);
        if (owner != null) {
            setLocationRelativeTo(owner);
        }
    }

    /**
     * Initializes the Swing component layout and dark styling.
     */
    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(new java.awt.Color(36, 38, 44));
        root.setBorder(new EmptyBorder(14, 16, 14, 16));

        // Top Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Chess Customization");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(new java.awt.Color(240, 242, 245));
        JLabel subtitleLabel = new JLabel("Customize board colors, piece rendering, and window sizing in real time.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(new java.awt.Color(170, 175, 185));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        root.add(headerPanel, BorderLayout.NORTH);

        // Center: Settings Form on Left, Live Preview on Right
        JPanel centerPanel = new JPanel(new BorderLayout(16, 0));
        centerPanel.setOpaque(false);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(createStyledTitledBorder("Options"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Board Theme
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(createLabel("Board Style:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(themeComboBox, gbc);

        // Row 1: Piece Style
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(createLabel("Piece Style:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(pieceStyleComboBox, gbc);

        // Row 2: Piece Style Description
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 8, 8, 8);
        formPanel.add(pieceStyleDescLabel, gbc);
        gbc.insets = new Insets(6, 8, 6, 8);

        // Row 3: Board Size
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(createLabel("Board Size:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(boardSizeComboBox, gbc);

        centerPanel.add(formPanel, BorderLayout.CENTER);

        // Preview Panel on East
        JPanel previewContainer = new JPanel(new BorderLayout(0, 6));
        previewContainer.setOpaque(false);
        previewContainer.setBorder(createStyledTitledBorder("Live Preview"));

        JPanel previewBoard = new JPanel(new GridLayout(2, 2, 0, 0));
        previewBoard.setPreferredSize(new Dimension(140, 140));
        previewBoard.setBorder(BorderFactory.createLineBorder(new java.awt.Color(60, 64, 72), 2));

        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                JPanel sq = new JPanel(new BorderLayout());
                JLabel iconLbl = new JLabel("", SwingConstants.CENTER);
                sq.add(iconLbl, BorderLayout.CENTER);
                previewSquares[r][c] = sq;
                previewLabels[r][c] = iconLbl;
                previewBoard.add(sq);
            }
        }
        previewContainer.add(previewBoard, BorderLayout.CENTER);

        JLabel previewNote = new JLabel("Instant preview", SwingConstants.CENTER);
        previewNote.setFont(new Font("SansSerif", Font.PLAIN, 10));
        previewNote.setForeground(new java.awt.Color(150, 155, 165));
        previewContainer.add(previewNote, BorderLayout.SOUTH);

        centerPanel.add(previewContainer, BorderLayout.EAST);
        root.add(centerPanel, BorderLayout.CENTER);

        // Bottom Button Bar
        JPanel buttonBar = new JPanel(new BorderLayout());
        buttonBar.setOpaque(false);
        buttonBar.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftButtons.setOpaque(false);
        leftButtons.add(resetButton);
        buttonBar.add(leftButtons, BorderLayout.WEST);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightButtons.setOpaque(false);
        rightButtons.add(applyButton);
        rightButtons.add(okButton);
        rightButtons.add(cancelButton);
        buttonBar.add(rightButtons, BorderLayout.EAST);

        root.add(buttonBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    /**
     * Updates the live 2x2 interactive preview board inside the dialog.
     */
    private void updatePreview() {
        BoardTheme theme = (BoardTheme) themeComboBox.getSelectedItem();
        PieceStyle style = (PieceStyle) pieceStyleComboBox.getSelectedItem();

        if (theme == null) {
            theme = BoardTheme.CLASSIC_WOOD;
        }
        if (style == null) {
            style = PieceStyle.CLASSIC_VECTOR;
        }

        java.awt.Color light = theme.getLightColor();
        java.awt.Color dark = theme.getDarkColor();

        // 2x2 mini board:
        // [0][0] = Light (White Knight)
        // [0][1] = Dark  (Black Pawn)
        // [1][0] = Dark  (White Queen)
        // [1][1] = Light (Black King)
        previewSquares[0][0].setBackground(light);
        previewSquares[0][1].setBackground(dark);
        previewSquares[1][0].setBackground(dark);
        previewSquares[1][1].setBackground(light);

        int iconSize = 48;
        previewLabels[0][0].setIcon(PieceIconManager.getPieceIcon(Color.WHITE, 'N', iconSize, style));
        previewLabels[0][1].setIcon(PieceIconManager.getPieceIcon(Color.BLACK, 'P', iconSize, style));
        previewLabels[1][0].setIcon(PieceIconManager.getPieceIcon(Color.WHITE, 'Q', iconSize, style));
        previewLabels[1][1].setIcon(PieceIconManager.getPieceIcon(Color.BLACK, 'K', iconSize, style));

        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                previewSquares[r][c].repaint();
            }
        }
    }

    /**
     * Applies the selected customization settings in real time to the active application.
     *
     * <p>Dynamically updates board theme colors, procedural piece icons, and board dimensions
     * without restarting the application or resetting ongoing piece moves or board state.</p>
     */
    public void applySettings() {
        BoardTheme selectedTheme = (BoardTheme) themeComboBox.getSelectedItem();
        PieceStyle selectedStyle = (PieceStyle) pieceStyleComboBox.getSelectedItem();
        BoardSize selectedSize = (BoardSize) boardSizeComboBox.getSelectedItem();

        if (selectedStyle != null) {
            PieceIconManager.setPieceStyle(selectedStyle);
        }

        if (boardPanel != null) {
            if (selectedTheme != null) {
                boardPanel.setTheme(selectedTheme);
            }
            if (selectedSize != null) {
                boardPanel.setBoardSize(selectedSize);
            }
            boardPanel.updateFromBoard();
        }

        if (ownerWindow instanceof ChessFrame) {
            ChessFrame frame = (ChessFrame) ownerWindow;
            frame.applyBoardSize(selectedSize);
            frame.refreshCapturedPieces();
        } else if (ownerWindow != null) {
            ownerWindow.pack();
        }
    }

    /**
     * Resets combobox selections to application default presets and refreshes preview.
     */
    public void resetToDefaults() {
        themeComboBox.setSelectedItem(BoardTheme.CLASSIC_WOOD);
        pieceStyleComboBox.setSelectedItem(PieceStyle.CLASSIC_VECTOR);
        boardSizeComboBox.setSelectedItem(BoardSize.MEDIUM);
        updatePreview();
    }

    /**
     * Helper to create styled form labels.
     *
     * @param text label text
     * @return styled JLabel
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(new java.awt.Color(220, 224, 230));
        return label;
    }

    /**
     * Helper to create styled titled borders.
     *
     * @param title section title
     * @return TitledBorder
     */
    private TitledBorder createStyledTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new java.awt.Color(55, 60, 68), 1),
                title
        );
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        border.setTitleColor(new java.awt.Color(190, 195, 205));
        return border;
    }

    // --- Component Getters for Automated Verification & Testing ---

    /**
     * Gets the dropdown combobox for BoardTheme selection.
     *
     * @return JComboBox of BoardTheme
     */
    public JComboBox<BoardTheme> getThemeComboBox() {
        return themeComboBox;
    }

    /**
     * Gets the dropdown combobox for PieceStyle selection.
     *
     * @return JComboBox of PieceStyle
     */
    public JComboBox<PieceStyle> getPieceStyleComboBox() {
        return pieceStyleComboBox;
    }

    /**
     * Gets the dropdown combobox for BoardSize selection.
     *
     * @return JComboBox of BoardSize
     */
    public JComboBox<BoardSize> getBoardSizeComboBox() {
        return boardSizeComboBox;
    }

    /**
     * Gets the currently selected BoardTheme in the combobox.
     *
     * @return selected BoardTheme
     */
    public BoardTheme getSelectedTheme() {
        return (BoardTheme) themeComboBox.getSelectedItem();
    }

    /**
     * Programmatically sets the selected BoardTheme in the combobox.
     *
     * @param theme target BoardTheme
     */
    public void setSelectedTheme(BoardTheme theme) {
        themeComboBox.setSelectedItem(theme);
    }

    /**
     * Gets the currently selected PieceStyle in the combobox.
     *
     * @return selected PieceStyle
     */
    public PieceStyle getSelectedPieceStyle() {
        return (PieceStyle) pieceStyleComboBox.getSelectedItem();
    }

    /**
     * Programmatically sets the selected PieceStyle in the combobox.
     *
     * @param style target PieceStyle
     */
    public void setSelectedPieceStyle(PieceStyle style) {
        pieceStyleComboBox.setSelectedItem(style);
    }

    /**
     * Gets the currently selected BoardSize in the combobox.
     *
     * @return selected BoardSize
     */
    public BoardSize getSelectedBoardSize() {
        return (BoardSize) boardSizeComboBox.getSelectedItem();
    }

    /**
     * Programmatically sets the selected BoardSize in the combobox.
     *
     * @param size target BoardSize
     */
    public void setSelectedBoardSize(BoardSize size) {
        boardSizeComboBox.setSelectedItem(size);
    }

    /**
     * Gets the Apply action button.
     *
     * @return JButton apply button
     */
    public JButton getApplyButton() {
        return applyButton;
    }

    /**
     * Gets the OK action button.
     *
     * @return JButton OK button
     */
    public JButton getOkButton() {
        return okButton;
    }

    /**
     * Gets the Cancel action button.
     *
     * @return JButton cancel button
     */
    public JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Gets the Reset Defaults action button.
     *
     * @return JButton reset button
     */
    public JButton getResetButton() {
        return resetButton;
    }

    /**
     * Custom ListCellRenderer for BoardTheme displaying color swatches next to theme names.
     */
    private static class ThemeCellRenderer extends javax.swing.DefaultListCellRenderer {

        /** Generated serial version UID for Swing serialization compatibility. */
        private static final long serialVersionUID = 1L;

        /** Constructs a new ThemeCellRenderer. */
        ThemeCellRenderer() { }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof BoardTheme) {
                BoardTheme theme = (BoardTheme) value;
                label.setText(theme.getDisplayName());
                label.setIcon(new ThemeColorSwatch(theme.getLightColor(), theme.getDarkColor()));
            }
            return label;
        }
    }

    /**
     * Tiny two-tone color swatch icon representing alternating board squares.
     */
    private static class ThemeColorSwatch implements javax.swing.Icon {

        /** Light square swatch color. */
        private final java.awt.Color light;

        /** Dark square swatch color. */
        private final java.awt.Color dark;

        /**
         * Constructs a ThemeColorSwatch with light and dark square colors.
         *
         * @param light light square color
         * @param dark dark square color
         */
        ThemeColorSwatch(java.awt.Color light, java.awt.Color dark) {
            this.light = light;
            this.dark = dark;
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(light);
            g2.fillRect(x, y, 9, 14);
            g2.setColor(dark);
            g2.fillRect(x + 9, y, 9, 14);
            g2.setColor(new java.awt.Color(60, 60, 60));
            g2.drawRect(x, y, 17, 13);
            g2.dispose();
        }
    }
}
