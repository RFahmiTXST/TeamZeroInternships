package pieces;

import board.Board;
import board.Position;
import test.TestUtils;
import java.util.List;

/**
 * Tests movement and behavior for Rook, Bishop, Queen, and Knight (Dev 1).
 */
public final class PieceMovementTest {

    private PieceMovementTest() { }

    /**
     * Executes all piece movement tests.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        testRook();
        testBishop();
        testQueen();
        testKnight();
    }

    private static void testRook() {
        Board board = new Board();
        Position d4 = new Position(3, 3);
        Rook whiteRook = new Rook(Color.WHITE, d4);
        board.setPiece(d4, whiteRook);

        TestUtils.assertEquals('R', whiteRook.getSymbol(), "Rook symbol is 'R'");
        TestUtils.assertEquals("wR", whiteRook.toString(), "White rook toString is 'wR'");
        TestUtils.assertEquals("bR", new Rook(Color.BLACK, d4).toString(), "Black rook toString is 'bR'");

        List<Position> emptyMoves = whiteRook.possibleMoves(board);
        TestUtils.assertEquals(14, emptyMoves.size(), "Rook on empty board at D4 has 14 moves");

        // Place a friendly piece to the right at D6 (row 3, col 5)
        Position d6 = new Position(3, 5);
        board.setPiece(d6, new Rook(Color.WHITE, d6));

        // Place an opponent piece above at F4 (row 5, col 3)
        Position f4 = new Position(5, 3);
        board.setPiece(f4, new Rook(Color.BLACK, f4));

        List<Position> blockedMoves = whiteRook.possibleMoves(board);
        // Right: D5 (3, 4) is valid; D6 (3, 5) friendly blocked; D7 (3, 6) unreachable
        TestUtils.assertEquals(true, blockedMoves.contains(new Position(3, 4)), "Rook can move right to open D5");
        TestUtils.assertEquals(false, blockedMoves.contains(d6), "Rook cannot land on friendly D6");
        TestUtils.assertEquals(false, blockedMoves.contains(new Position(3, 6)), "Rook cannot pass through friendly D6");

        // Up: E4 (4, 3) is valid; F4 (5, 3) is enemy capture; G4 (6, 3) unreachable
        TestUtils.assertEquals(true, blockedMoves.contains(new Position(4, 3)), "Rook can move up to open E4");
        TestUtils.assertEquals(true, blockedMoves.contains(f4), "Rook can capture enemy at F4");
        TestUtils.assertEquals(false, blockedMoves.contains(new Position(6, 3)), "Rook cannot pass through enemy at F4");
    }

    private static void testBishop() {
        Board board = new Board();
        Position d4 = new Position(3, 3);
        Bishop whiteBishop = new Bishop(Color.WHITE, d4);
        board.setPiece(d4, whiteBishop);

        TestUtils.assertEquals('B', whiteBishop.getSymbol(), "Bishop symbol is 'B'");
        TestUtils.assertEquals("wB", whiteBishop.toString(), "White bishop toString is 'wB'");
        TestUtils.assertEquals("bB", new Bishop(Color.BLACK, d4).toString(), "Black bishop toString is 'bB'");

        List<Position> emptyMoves = whiteBishop.possibleMoves(board);
        TestUtils.assertEquals(13, emptyMoves.size(), "Bishop on empty board at D4 has 13 moves");

        // Corner test at A1 (0, 0)
        Board cornerBoard = new Board();
        Position a1 = new Position(0, 0);
        Bishop cornerBishop = new Bishop(Color.WHITE, a1);
        cornerBoard.setPiece(a1, cornerBishop);
        TestUtils.assertEquals(7, cornerBishop.possibleMoves(cornerBoard).size(), "Bishop at A1 corner has 7 moves");

        // Friendly block at F6 (5, 5)
        Position f6 = new Position(5, 5);
        board.setPiece(f6, new Bishop(Color.WHITE, f6));

        // Enemy block at B6 (5, 1)
        Position b6 = new Position(5, 1);
        board.setPiece(b6, new Bishop(Color.BLACK, b6));

        List<Position> blockedMoves = whiteBishop.possibleMoves(board);
        TestUtils.assertEquals(true, blockedMoves.contains(new Position(4, 4)), "Bishop can move to E5 before friendly F6");
        TestUtils.assertEquals(false, blockedMoves.contains(f6), "Bishop cannot land on friendly F6");
        TestUtils.assertEquals(false, blockedMoves.contains(new Position(6, 6)), "Bishop cannot jump past friendly F6");

        TestUtils.assertEquals(true, blockedMoves.contains(new Position(4, 2)), "Bishop can move to C5 before enemy B6");
        TestUtils.assertEquals(true, blockedMoves.contains(b6), "Bishop can capture enemy at B6");
        TestUtils.assertEquals(false, blockedMoves.contains(new Position(6, 0)), "Bishop cannot jump past enemy B6");
    }

    private static void testQueen() {
        Board board = new Board();
        Position d4 = new Position(3, 3);
        Queen whiteQueen = new Queen(Color.WHITE, d4);
        board.setPiece(d4, whiteQueen);

        TestUtils.assertEquals('Q', whiteQueen.getSymbol(), "Queen symbol is 'Q'");
        TestUtils.assertEquals("wQ", whiteQueen.toString(), "White queen toString is 'wQ'");
        TestUtils.assertEquals("bQ", new Queen(Color.BLACK, d4).toString(), "Black queen toString is 'bQ'");

        List<Position> emptyMoves = whiteQueen.possibleMoves(board);
        TestUtils.assertEquals(27, emptyMoves.size(), "Queen on empty board at D4 has 27 moves (14 straight + 13 diagonal)");

        // Corner test at A1 (0, 0)
        Board cornerBoard = new Board();
        Position a1 = new Position(0, 0);
        Queen cornerQueen = new Queen(Color.WHITE, a1);
        cornerBoard.setPiece(a1, cornerQueen);
        TestUtils.assertEquals(21, cornerQueen.possibleMoves(cornerBoard).size(), "Queen at A1 corner has 21 moves (14 straight + 7 diagonal)");
    }

    private static void testKnight() {
        Board board = new Board();
        Position d4 = new Position(3, 3);
        Knight whiteKnight = new Knight(Color.WHITE, d4);
        board.setPiece(d4, whiteKnight);

        TestUtils.assertEquals('N', whiteKnight.getSymbol(), "Knight symbol is 'N'");
        TestUtils.assertEquals("wN", whiteKnight.toString(), "White knight toString is 'wN'");
        TestUtils.assertEquals("bN", new Knight(Color.BLACK, d4).toString(), "Black knight toString is 'bN'");

        List<Position> centerMoves = whiteKnight.possibleMoves(board);
        TestUtils.assertEquals(8, centerMoves.size(), "Knight in center at D4 has exactly 8 moves");

        // Corner test at A1 (0, 0)
        Board cornerBoard = new Board();
        Position a1 = new Position(0, 0);
        Knight cornerKnight = new Knight(Color.WHITE, a1);
        cornerBoard.setPiece(a1, cornerKnight);
        List<Position> cornerMoves = cornerKnight.possibleMoves(cornerBoard);
        TestUtils.assertEquals(2, cornerMoves.size(), "Knight in corner at A1 has 2 moves");
        TestUtils.assertEquals(true, cornerMoves.contains(new Position(1, 2)), "Corner knight can move to C2 (1, 2)");
        TestUtils.assertEquals(true, cornerMoves.contains(new Position(2, 1)), "Corner knight can move to B3 (2, 1)");

        // Surround knight at D4 with pieces to verify jumping capability
        board.setPiece(new Position(4, 3), new Rook(Color.WHITE, new Position(4, 3))); // D5
        board.setPiece(new Position(2, 3), new Rook(Color.WHITE, new Position(2, 3))); // D3
        board.setPiece(new Position(3, 4), new Rook(Color.WHITE, new Position(3, 4))); // E4
        board.setPiece(new Position(3, 2), new Rook(Color.WHITE, new Position(3, 2))); // C4
        TestUtils.assertEquals(8, whiteKnight.possibleMoves(board).size(), "Knight jumps over adjacent surrounding pieces");

        // Place friendly piece at B5 (row 4, col 1) -> offset {1, -2}
        Position b5 = new Position(4, 1);
        board.setPiece(b5, new Knight(Color.WHITE, b5));

        // Place opponent piece at F5 (row 4, col 5) -> offset {1, 2}
        Position f5 = new Position(4, 5);
        board.setPiece(f5, new Knight(Color.BLACK, f5));

        List<Position> blockedMoves = whiteKnight.possibleMoves(board);
        TestUtils.assertEquals(7, blockedMoves.size(), "Knight cannot land on friendly B5 but can capture enemy F5 (total 7)");
        TestUtils.assertEquals(false, blockedMoves.contains(b5), "Knight cannot move to friendly square B5");
        TestUtils.assertEquals(true, blockedMoves.contains(f5), "Knight can capture enemy on F5");
    }
}