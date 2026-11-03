package test.gui;

import board.Board;
import board.Position;
import gui.ChessFrame;
import gui.board.BoardPanel;
import gui.board.SquarePanel;
import gui.pieces.PieceIconManager;
import gui.pieces.PieceView;
import pieces.Bishop;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import test.TestUtils;

import javax.swing.Icon;

/**
 * Unit and integration tests for Phase 2 GUI components (Dev 1 Milestone 1).
 *
 * @author Dev 1 (Rayed)
 */
public class PieceIconManagerTest {

    /**
     * Executes all tests for GUI piece rendering, board panel, and window frame.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Running PieceIconManager and GUI Tests...");

        testPieceIconGeneration();
        testPieceScaling();
        testIconCaching();
        testPieceView();
        testBoardPanelStructure();
        testChessFrameInitialization();

        System.out.println("PieceIconManager and GUI Tests completed.");
    }

    /**
     * Verifies that procedural icon rendering produces non-null icons for all pieces of both colors.
     */
    private static void testPieceIconGeneration() {
        char[] symbols = {'P', 'R', 'N', 'B', 'Q', 'K'};

        for (Color color : new Color[]{Color.WHITE, Color.BLACK}) {
            for (char sym : symbols) {
                Icon icon = PieceIconManager.getPieceIcon(color, sym);
                TestUtils.assertEquals(true, icon != null,
                        "Icon exists for " + color + " " + sym);
                if (icon != null) {
                    TestUtils.assertEquals(PieceIconManager.DEFAULT_SIZE, icon.getIconWidth(),
                            "Icon width matches default size for " + color + " " + sym);
                    TestUtils.assertEquals(PieceIconManager.DEFAULT_SIZE, icon.getIconHeight(),
                            "Icon height matches default size for " + color + " " + sym);
                }
            }
        }
    }

    /**
     * Verifies that custom sizing works dynamically.
     */
    private static void testPieceScaling() {
        int customSize = 48;
        Icon icon48 = PieceIconManager.getPieceIcon(Color.WHITE, 'Q', customSize);
        TestUtils.assertEquals(true, icon48 != null, "Scaled Queen icon is generated");
        if (icon48 != null) {
            TestUtils.assertEquals(customSize, icon48.getIconWidth(), "Scaled icon width is 48");
            TestUtils.assertEquals(customSize, icon48.getIconHeight(), "Scaled icon height is 48");
        }

        int largeSize = 80;
        Icon icon80 = PieceIconManager.getPieceIcon(Color.BLACK, 'K', largeSize);
        TestUtils.assertEquals(true, icon80 != null, "Large King icon is generated");
        if (icon80 != null) {
            TestUtils.assertEquals(largeSize, icon80.getIconWidth(), "Large icon width is 80");
        }
    }

    /**
     * Verifies that cached icon lookups return the same instance without redundant generation.
     */
    private static void testIconCaching() {
        Icon first = PieceIconManager.getPieceIcon(Color.WHITE, 'R', 64);
        Icon second = PieceIconManager.getPieceIcon(Color.WHITE, 'R', 64);
        TestUtils.assertEquals(true, first == second, "Cached icons return identical reference");
    }

    /**
     * Verifies PieceView wrapper methods.
     */
    private static void testPieceView() {
        Position pos = new Position(0, 4);
        King whiteKing = new King(Color.WHITE, pos);
        PieceView view = new PieceView(whiteKing);

        TestUtils.assertEquals(true, view.hasPiece(), "PieceView reports active piece");
        TestUtils.assertEquals(Color.WHITE, view.getColor(), "PieceView reports White color");
        TestUtils.assertEquals("White King", view.getDisplayName(), "PieceView display name is White King");
        TestUtils.assertEquals(true, view.getIcon() != null, "PieceView resolves piece icon");

        PieceView emptyView = new PieceView(null);
        TestUtils.assertEquals(false, emptyView.hasPiece(), "Empty PieceView has no piece");
        TestUtils.assertEquals("Empty", emptyView.getDisplayName(), "Empty PieceView display name is Empty");
    }

    /**
     * Verifies that BoardPanel correctly maps all 64 squares and populates standard pieces.
     */
    private static void testBoardPanelStructure() {
        Board board = new Board();
        board.initialize();
        BoardPanel panel = new BoardPanel(board);

        TestUtils.assertEquals(board, panel.getBoard(), "BoardPanel references correct Board model");

        // Verify corner pieces:
        // A1 (row 0, col 0) should have White Rook
        SquarePanel a1 = panel.getSquarePanel(new Position(0, 0));
        TestUtils.assertEquals(true, a1 != null, "Square A1 panel exists");
        if (a1 != null) {
            TestUtils.assertEquals(false, a1.isLightSquare(), "A1 is dark square");
            TestUtils.assertEquals(true, a1.getPiece() instanceof Rook, "A1 contains Rook");
            TestUtils.assertEquals(Color.WHITE, a1.getPiece().getColor(), "A1 Rook is White");
        }

        // E1 (row 0, col 4) should have White King
        SquarePanel e1 = panel.getSquarePanel(new Position(0, 4));
        if (e1 != null) {
            TestUtils.assertEquals(true, e1.getPiece() instanceof King, "E1 contains White King");
        }

        // E8 (row 7, col 4) should have Black King
        SquarePanel e8 = panel.getSquarePanel(new Position(7, 4));
        if (e8 != null) {
            TestUtils.assertEquals(true, e8.getPiece() instanceof King, "E8 contains Black King");
        }

        // D4 (row 3, col 3) is initially empty
        SquarePanel d4 = panel.getSquarePanel(new Position(3, 3));
        if (d4 != null) {
            TestUtils.assertEquals(true, d4.getPiece() == null, "D4 is initially empty");
        }
    }

    /**
     * Verifies that ChessFrame initializes cleanly.
     * When running in a headless environment, window creation is bypassed to avoid HeadlessException.
     */
    private static void testChessFrameInitialization() {
        TestUtils.assertEquals("Team Zero Chess - Phase 2 GUI", ChessFrame.DEFAULT_TITLE, "ChessFrame has correct title");
        TestUtils.assertEquals(true, ChessFrame.MIN_WIDTH >= 600, "ChessFrame enforces minimum width");
        TestUtils.assertEquals(true, ChessFrame.MIN_HEIGHT >= 600, "ChessFrame enforces minimum height");

        if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println("PASS: (Headless) ChessFrame display window creation skipped in headless environment.");
            return;
        }

        ChessFrame frame = new ChessFrame();
        TestUtils.assertEquals(ChessFrame.DEFAULT_TITLE, frame.getTitle(), "ChessFrame window title matches");
        TestUtils.assertEquals(true, frame.getBoard() != null, "ChessFrame has initialized Board");
        TestUtils.assertEquals(true, frame.getBoardPanel() != null, "ChessFrame has BoardPanel");
        TestUtils.assertEquals(true, frame.getStatusLabel() != null, "ChessFrame has status label");
        frame.dispose();
    }
}

