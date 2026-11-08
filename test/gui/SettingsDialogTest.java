package test.gui;

import board.Board;
import board.Position;
import gui.ChessFrame;
import gui.board.BoardPanel;
import gui.board.BoardSize;
import gui.board.BoardTheme;
import gui.board.SquarePanel;
import gui.dialogs.SettingsDialog;
import gui.pieces.PieceIconManager;
import gui.pieces.PieceStyle;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import test.TestUtils;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;

/**
 * Verification test suite for Dev 1's Phase 2 Milestone 3 deliverables:
 * Settings window for real-time appearance and customization (SettingsDialog),
 * board background color/style selector, chess piece style/color selector,
 * board size selector, and dynamic real-time apply preserving ongoing game state.
 *
 * @author Dev 1 (Rayed)
 */
public final class SettingsDialogTest {

    /** Private constructor to prevent instantiation. */
    private SettingsDialogTest() { }

    /**
     * Executes the test suite on the Event Dispatch Thread with headless safety.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("Running Dev 1 Phase 2 Milestone 3 Settings & Customization tests...");
        try {
            SwingUtilities.invokeAndWait(() -> {
                testBoardThemeEnum();
                testPieceStyleEnum();
                testBoardSizeEnum();
                testPieceIconManagerStyleIntegration();
                testBoardPanelThemeAndSizeIntegration();
                testDirectApplyPreservesGameState();
                testSettingsDialogUIAndDefaults();
                testSettingsDialogApplyPreservesGameState();
                testSettingsDialogResetToDefaults();
                testChessFrameSettingsIntegration();
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("SettingsDialogTest interrupted", e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("SettingsDialogTest failed", e.getCause());
        }
        System.out.println("Dev 1 Phase 2 Milestone 3 tests completed successfully.");
    }

    /** Verifies the BoardTheme enum values, square colors, and lookup helpers. */
    private static void testBoardThemeEnum() {
        TestUtils.assertEquals(4, BoardTheme.values().length, "Four BoardTheme presets exist");
        TestUtils.assertEquals("Classic Wood", BoardTheme.CLASSIC_WOOD.getDisplayName(), "Classic Wood display name");
        TestUtils.assertEquals("Modern Gray", BoardTheme.MODERN_GRAY.getDisplayName(), "Modern Gray display name");
        TestUtils.assertEquals("Slate Blue", BoardTheme.SLATE_BLUE.getDisplayName(), "Slate Blue display name");
        TestUtils.assertEquals("Forest Green", BoardTheme.FOREST_GREEN.getDisplayName(), "Forest Green display name");

        for (BoardTheme theme : BoardTheme.values()) {
            TestUtils.assertEquals(true, theme.getLightColor() != null, theme + " light color is defined");
            TestUtils.assertEquals(true, theme.getDarkColor() != null, theme + " dark color is defined");
            TestUtils.assertEquals(false, theme.getLightColor().equals(theme.getDarkColor()),
                    theme + " light and dark colors are distinct");
            TestUtils.assertEquals(theme, BoardTheme.fromDisplayName(theme.getDisplayName()),
                    theme + " lookup from display name succeeds");
            TestUtils.assertEquals(theme, BoardTheme.fromDisplayName(theme.getDisplayName().toLowerCase()),
                    theme + " lookup is case-insensitive");
        }

        TestUtils.assertEquals(null, BoardTheme.fromDisplayName(null), "Null theme name returns null");
        TestUtils.assertEquals(null, BoardTheme.fromDisplayName("Unknown Theme"), "Unknown theme name returns null");
    }

    /** Verifies the PieceStyle enum values, descriptions, and lookup helpers. */
    private static void testPieceStyleEnum() {
        TestUtils.assertEquals(3, PieceStyle.values().length, "Three PieceStyle presets exist");
        TestUtils.assertEquals("Classic Vector", PieceStyle.CLASSIC_VECTOR.getDisplayName(), "Classic Vector display name");
        TestUtils.assertEquals("Minimalist", PieceStyle.MINIMALIST.getDisplayName(), "Minimalist display name");
        TestUtils.assertEquals("High-Contrast", PieceStyle.HIGH_CONTRAST.getDisplayName(), "High-Contrast display name");

        for (PieceStyle style : PieceStyle.values()) {
            TestUtils.assertEquals(true, style.getDescription() != null && !style.getDescription().isEmpty(),
                    style + " description is non-empty");
            TestUtils.assertEquals(style, PieceStyle.fromDisplayName(style.getDisplayName()),
                    style + " lookup from display name succeeds");
            TestUtils.assertEquals(style, PieceStyle.fromDisplayName(style.getDisplayName().toLowerCase()),
                    style + " lookup is case-insensitive");
        }

        TestUtils.assertEquals(null, PieceStyle.fromDisplayName(null), "Null style name returns null");
        TestUtils.assertEquals(null, PieceStyle.fromDisplayName("NonExistent"), "Unknown style name returns null");
    }

    /** Verifies the BoardSize enum values, dimensions, and lookup helpers. */
    private static void testBoardSizeEnum() {
        TestUtils.assertEquals(3, BoardSize.values().length, "Three BoardSize presets exist");
        TestUtils.assertEquals(480, BoardSize.SMALL.getDimension(), "Small board size is 480");
        TestUtils.assertEquals(640, BoardSize.MEDIUM.getDimension(), "Medium board size is 640");
        TestUtils.assertEquals(800, BoardSize.LARGE.getDimension(), "Large board size is 800");

        for (BoardSize size : BoardSize.values()) {
            TestUtils.assertEquals(size, BoardSize.fromDisplayName(size.getDisplayName()),
                    size + " lookup from display name succeeds");
        }

        TestUtils.assertEquals(null, BoardSize.fromDisplayName(null), "Null size name returns null");
        TestUtils.assertEquals(null, BoardSize.fromDisplayName("Invalid"), "Unknown size name returns null");
    }

    /** Verifies PieceIconManager style switching, caching, and custom style rendering. */
    private static void testPieceIconManagerStyleIntegration() {
        TestUtils.assertEquals(PieceStyle.CLASSIC_VECTOR, PieceIconManager.getPieceStyle(),
                "Default piece style is Classic Vector");

        Icon classicIcon = PieceIconManager.getPieceIcon(Color.WHITE, 'N', 64);
        TestUtils.assertEquals(true, classicIcon != null, "Classic vector icon renders");

        // Switch to Minimalist
        PieceIconManager.setPieceStyle(PieceStyle.MINIMALIST);
        TestUtils.assertEquals(PieceStyle.MINIMALIST, PieceIconManager.getPieceStyle(),
                "Active style updated to Minimalist");
        Icon minIcon = PieceIconManager.getPieceIcon(Color.WHITE, 'N', 64);
        TestUtils.assertEquals(true, minIcon != null, "Minimalist icon renders");

        // Switch to High-Contrast
        PieceIconManager.setPieceStyle(PieceStyle.HIGH_CONTRAST);
        TestUtils.assertEquals(PieceStyle.HIGH_CONTRAST, PieceIconManager.getPieceStyle(),
                "Active style updated to High-Contrast");
        Icon hcIcon = PieceIconManager.getPieceIcon(Color.BLACK, 'K', 64);
        TestUtils.assertEquals(true, hcIcon != null, "High-Contrast icon renders");

        // Explicit style request
        Icon explicitClassic = PieceIconManager.getPieceIcon(Color.WHITE, 'Q', 64, PieceStyle.CLASSIC_VECTOR);
        TestUtils.assertEquals(true, explicitClassic != null, "Explicit style icon renders");

        // Null style validation
        boolean threw = false;
        try {
            PieceIconManager.setPieceStyle(null);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        TestUtils.assertEquals(true, threw, "Setting null PieceStyle throws IllegalArgumentException");

        // Restore default
        PieceIconManager.setPieceStyle(PieceStyle.CLASSIC_VECTOR);
    }

    /** Verifies BoardPanel theme and size updates. */
    private static void testBoardPanelThemeAndSizeIntegration() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);

        TestUtils.assertEquals(BoardTheme.CLASSIC_WOOD, panel.getTheme(), "Initial panel theme is Classic Wood");
        TestUtils.assertEquals(BoardSize.MEDIUM, panel.getBoardSize(), "Initial panel size is Medium");

        // Apply Modern Gray theme
        panel.setTheme(BoardTheme.MODERN_GRAY);
        TestUtils.assertEquals(BoardTheme.MODERN_GRAY, panel.getTheme(), "Panel theme is Modern Gray");
        SquarePanel a1 = panel.getSquarePanel(0, 0); // Dark square
        SquarePanel b1 = panel.getSquarePanel(0, 1); // Light square
        TestUtils.assertEquals(BoardTheme.MODERN_GRAY.getDarkColor(), a1.getBackground(), "A1 has Modern Gray dark color");
        TestUtils.assertEquals(BoardTheme.MODERN_GRAY.getLightColor(), b1.getBackground(), "B1 has Modern Gray light color");

        // Apply Forest Green theme
        panel.setTheme(BoardTheme.FOREST_GREEN);
        TestUtils.assertEquals(BoardTheme.FOREST_GREEN.getDarkColor(), a1.getBackground(), "A1 has Forest Green dark color");
        TestUtils.assertEquals(BoardTheme.FOREST_GREEN.getLightColor(), b1.getBackground(), "B1 has Forest Green light color");

        // Apply Small size
        panel.setBoardSize(BoardSize.SMALL);
        TestUtils.assertEquals(BoardSize.SMALL, panel.getBoardSize(), "Panel size is Small");

        // Apply Large size
        panel.setBoardSize(BoardSize.LARGE);
        TestUtils.assertEquals(BoardSize.LARGE, panel.getBoardSize(), "Panel size is Large");

        // Null argument validation
        boolean threwTheme = false;
        try {
            panel.setTheme(null);
        } catch (IllegalArgumentException e) {
            threwTheme = true;
        }
        TestUtils.assertEquals(true, threwTheme, "Setting null BoardTheme throws IllegalArgumentException");

        boolean threwSize = false;
        try {
            panel.setBoardSize(null);
        } catch (IllegalArgumentException e) {
            threwSize = true;
        }
        TestUtils.assertEquals(true, threwSize, "Setting null BoardSize throws IllegalArgumentException");

        // Restore defaults
        panel.setTheme(BoardTheme.CLASSIC_WOOD);
        panel.setBoardSize(BoardSize.MEDIUM);
    }

    /**
     * Verifies that dynamically updating board theme, piece style, and board dimensions
     * directly preserves ongoing moves, turns, captures, and active board state in headless mode.
     */
    private static void testDirectApplyPreservesGameState() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        gui.GUIController controller = new gui.GUIController(board, panel);

        // Make moves on the board
        Position e2 = new Position(1, 4);
        Position e4 = new Position(3, 4);
        controller.movePiece(e2, e4);

        Position d7 = new Position(6, 3);
        Position d5 = new Position(4, 3);
        controller.movePiece(d7, d5);

        // White Pawn captures Black Pawn
        controller.movePiece(e4, d5);

        Piece pawnOnD5 = board.getPiece(d5);
        TestUtils.assertEquals(true, pawnOnD5 instanceof Pawn, "Pawn occupies D5 after capture");
        TestUtils.assertEquals(Color.WHITE, pawnOnD5.getColor(), "White pawn occupies D5");
        TestUtils.assertEquals(1, board.getCapturedPieces().size(), "One piece captured in game");
        Color turnBeforeApply = controller.getCurrentTurn();

        // Apply theme, style, and size changes
        panel.setTheme(BoardTheme.FOREST_GREEN);
        PieceIconManager.setPieceStyle(PieceStyle.MINIMALIST);
        panel.setBoardSize(BoardSize.LARGE);
        panel.updateFromBoard();

        // Verify customizations applied
        TestUtils.assertEquals(BoardTheme.FOREST_GREEN, panel.getTheme(), "Board theme updated to Forest Green");
        TestUtils.assertEquals(PieceStyle.MINIMALIST, PieceIconManager.getPieceStyle(), "Piece style updated to Minimalist");
        TestUtils.assertEquals(BoardSize.LARGE, panel.getBoardSize(), "Board size updated to Large");

        // Verify ongoing game state is preserved
        TestUtils.assertEquals(pawnOnD5, board.getPiece(d5), "D5 piece is preserved without reset");
        TestUtils.assertEquals(null, board.getPiece(e2), "E2 remains empty after settings apply");
        TestUtils.assertEquals(null, board.getPiece(e4), "E4 remains empty after settings apply");
        TestUtils.assertEquals(1, board.getCapturedPieces().size(), "Captured pieces list is preserved");
        TestUtils.assertEquals(turnBeforeApply, controller.getCurrentTurn(), "Player turn state is preserved");
        TestUtils.assertEquals(pawnOnD5, panel.getSquarePanel(d5).getPiece(), "Visual SquarePanel piece is preserved");

        // Restore defaults
        panel.setTheme(BoardTheme.CLASSIC_WOOD);
        panel.setBoardSize(BoardSize.MEDIUM);
        PieceIconManager.setPieceStyle(PieceStyle.CLASSIC_VECTOR);
    }

    /** Verifies SettingsDialog initialization, initial component states, and default values. */
    private static void testSettingsDialogUIAndDefaults() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("PASS: (Headless) SettingsDialog UI testing skipped in headless mode.");
            return;
        }

        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        SettingsDialog dialog = new SettingsDialog(null, panel);

        TestUtils.assertEquals(SettingsDialog.DIALOG_TITLE, dialog.getTitle(), "SettingsDialog title matches");
        TestUtils.assertEquals(BoardTheme.CLASSIC_WOOD, dialog.getSelectedTheme(), "Default selected theme is Classic Wood");
        TestUtils.assertEquals(PieceStyle.CLASSIC_VECTOR, dialog.getSelectedPieceStyle(), "Default selected piece style is Classic Vector");
        TestUtils.assertEquals(BoardSize.MEDIUM, dialog.getSelectedBoardSize(), "Default selected size is Medium");

        TestUtils.assertEquals(true, dialog.getApplyButton() != null, "Apply button exists");
        TestUtils.assertEquals(true, dialog.getOkButton() != null, "OK button exists");
        TestUtils.assertEquals(true, dialog.getCancelButton() != null, "Cancel button exists");
        TestUtils.assertEquals(true, dialog.getResetButton() != null, "Reset button exists");

        TestUtils.assertEquals(4, dialog.getThemeComboBox().getItemCount(), "Theme combobox has 4 choices");
        TestUtils.assertEquals(3, dialog.getPieceStyleComboBox().getItemCount(), "Piece style combobox has 3 choices");
        TestUtils.assertEquals(3, dialog.getBoardSizeComboBox().getItemCount(), "Board size combobox has 3 choices");

        dialog.dispose();
    }

    /**
     * Verifies that applying settings dynamically updates colors, piece icons, and dimensions
     * without resetting ongoing moves, turns, or active board state.
     */
    private static void testSettingsDialogApplyPreservesGameState() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("PASS: (Headless) SettingsDialog apply testing skipped in headless mode.");
            return;
        }

        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        gui.GUIController controller = new gui.GUIController(board, panel);

        // Make moves on the board
        Position e2 = new Position(1, 4);
        Position e4 = new Position(3, 4);
        controller.movePiece(e2, e4);

        Position d7 = new Position(6, 3);
        Position d5 = new Position(4, 3);
        controller.movePiece(d7, d5);

        // Execute a capture: White Pawn on E4 captures Black Pawn on D5
        controller.movePiece(e4, d5);

        Piece pawnOnD5 = board.getPiece(d5);
        TestUtils.assertEquals(true, pawnOnD5 instanceof Pawn, "Pawn occupies D5 after capture");
        TestUtils.assertEquals(Color.WHITE, pawnOnD5.getColor(), "White pawn occupies D5");
        TestUtils.assertEquals(1, board.getCapturedPieces().size(), "One piece captured in game");
        Color turnBeforeApply = controller.getCurrentTurn();

        // Open SettingsDialog and select custom settings
        SettingsDialog dialog = new SettingsDialog(null, panel);
        dialog.setSelectedTheme(BoardTheme.FOREST_GREEN);
        dialog.setSelectedPieceStyle(PieceStyle.MINIMALIST);
        dialog.setSelectedBoardSize(BoardSize.LARGE);

        // Click Apply
        dialog.applySettings();

        // 1. Verify customizations applied
        TestUtils.assertEquals(BoardTheme.FOREST_GREEN, panel.getTheme(), "Board theme updated to Forest Green");
        TestUtils.assertEquals(PieceStyle.MINIMALIST, PieceIconManager.getPieceStyle(), "Piece style updated to Minimalist");
        TestUtils.assertEquals(BoardSize.LARGE, panel.getBoardSize(), "Board size updated to Large");

        // 2. Verify ongoing game state is 100% PRESERVED
        TestUtils.assertEquals(pawnOnD5, board.getPiece(d5), "D5 piece is preserved without reset");
        TestUtils.assertEquals(null, board.getPiece(e2), "E2 remains empty after settings apply");
        TestUtils.assertEquals(null, board.getPiece(e4), "E4 remains empty after settings apply");
        TestUtils.assertEquals(1, board.getCapturedPieces().size(), "Captured pieces list is preserved without reset");
        TestUtils.assertEquals(turnBeforeApply, controller.getCurrentTurn(), "Player turn state is preserved without reset");
        TestUtils.assertEquals(pawnOnD5, panel.getSquarePanel(d5).getPiece(), "Visual SquarePanel piece is preserved");

        dialog.dispose();

        // Restore defaults
        panel.setTheme(BoardTheme.CLASSIC_WOOD);
        panel.setBoardSize(BoardSize.MEDIUM);
        PieceIconManager.setPieceStyle(PieceStyle.CLASSIC_VECTOR);
    }

    /** Verifies that the Reset Defaults button restores standard options. */
    private static void testSettingsDialogResetToDefaults() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("PASS: (Headless) SettingsDialog reset testing skipped in headless mode.");
            return;
        }

        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);
        SettingsDialog dialog = new SettingsDialog(null, panel);

        dialog.setSelectedTheme(BoardTheme.SLATE_BLUE);
        dialog.setSelectedPieceStyle(PieceStyle.HIGH_CONTRAST);
        dialog.setSelectedBoardSize(BoardSize.SMALL);

        TestUtils.assertEquals(BoardTheme.SLATE_BLUE, dialog.getSelectedTheme(), "Theme changed to Slate Blue");
        TestUtils.assertEquals(PieceStyle.HIGH_CONTRAST, dialog.getSelectedPieceStyle(), "Style changed to High-Contrast");
        TestUtils.assertEquals(BoardSize.SMALL, dialog.getSelectedBoardSize(), "Size changed to Small");

        // Trigger Reset Defaults
        dialog.resetToDefaults();

        TestUtils.assertEquals(BoardTheme.CLASSIC_WOOD, dialog.getSelectedTheme(), "Reset restored Classic Wood");
        TestUtils.assertEquals(PieceStyle.CLASSIC_VECTOR, dialog.getSelectedPieceStyle(), "Reset restored Classic Vector");
        TestUtils.assertEquals(BoardSize.MEDIUM, dialog.getSelectedBoardSize(), "Reset restored Medium size");

        dialog.dispose();
    }

    /** Verifies ChessFrame menu bar, toolbar gear button, and settings dialog access. */
    private static void testChessFrameSettingsIntegration() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("PASS: (Headless) ChessFrame display window testing skipped in headless mode.");
            return;
        }

        ChessFrame frame = new ChessFrame();

        // Check Toolbar Gear Button
        JButton gearButton = frame.getSettingsButton();
        TestUtils.assertEquals(true, gearButton != null, "ChessFrame has settings toolbar button");
        if (gearButton != null) {
            TestUtils.assertEquals(true, gearButton.getText().contains("Settings"), "Toolbar button text has 'Settings'");
        }

        // Check MenuBar items
        JMenuBar menuBar = frame.getJMenuBar();
        TestUtils.assertEquals(true, menuBar != null, "ChessFrame has menu bar");
        if (menuBar != null) {
            boolean hasSettingsItem = false;
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                if (menu != null) {
                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem item = menu.getItem(j);
                        if (item != null && item.getText() != null && item.getText().contains("Settings")) {
                            hasSettingsItem = true;
                        }
                    }
                }
            }
            TestUtils.assertEquals(true, hasSettingsItem, "Menu bar contains Settings menu item");
        }

        // Test openSettingsDialog and applyBoardSize
        frame.openSettingsDialog();
        SettingsDialog dialog = frame.getSettingsDialog();
        TestUtils.assertEquals(true, dialog != null, "ChessFrame instantiates SettingsDialog");
        if (dialog != null) {
            TestUtils.assertEquals(true, dialog.isVisible(), "SettingsDialog is visible when opened");
            dialog.dispose();
        }

        // Test applyBoardSize on frame
        frame.applyBoardSize(BoardSize.SMALL);
        TestUtils.assertEquals(BoardSize.SMALL, frame.getBoardPanel().getBoardSize(), "Frame applied Small board size");

        frame.applyBoardSize(BoardSize.LARGE);
        TestUtils.assertEquals(BoardSize.LARGE, frame.getBoardPanel().getBoardSize(), "Frame applied Large board size");

        // Restore Medium
        frame.applyBoardSize(BoardSize.MEDIUM);
        frame.getBoardPanel().setTheme(BoardTheme.CLASSIC_WOOD);
        PieceIconManager.setPieceStyle(PieceStyle.CLASSIC_VECTOR);

        frame.dispose();
    }
}
