package core;

import board.Board;
import board.Position;
import pieces.Bishop;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import test.TestUtils;

import java.util.List;

/**
 * Unit and integration tests for Dev 1's Milestone 1 legal move generation
 * and King safety simulation.
 *
 * @author Dev 1 (Rayed)
 */
public final class LegalMoveTest {

    /** Prevents instantiation of utility test class. */
    private LegalMoveTest() { }

    /**
     * Executes all legal move and King safety unit tests.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        testStartingBoardLegalMoves();
        testPinnedPieceProtection();
        testKingCannotMoveIntoCheck();
        testKingsCannotBeAdjacent();
        testKingCannotCaptureDefendedPiece();
        testInCheckMoveFiltering();
        testEnemyKingCannotBeCaptured();
        testWouldExposeKingPreservesBoardState();
        testOverloadedGetLegalMoves();
        Dev4M1RulesTest.main(args);
    }

    /**
     * Verifies legal moves on standard 32-piece initial board.
     * Only pawns (1 or 2 steps) and knights have legal moves;
     * pieces blocked by pawns have 0 legal moves.
     */
    private static void testStartingBoardLegalMoves() {
        Board board = new Board();
        board.initialize();

        // 1. White Pawn on E2 has exactly 2 legal moves: E3 and E4
        Position e2 = new Position(1, 4);
        List<Position> e2Moves = board.getLegalMoves(e2, Color.WHITE);
        TestUtils.assertEquals(2, e2Moves.size(), "Initial White Pawn on E2 has 2 legal moves");
        TestUtils.assertEquals(true, e2Moves.contains(new Position(2, 4)), "E2 Pawn can move to E3");
        TestUtils.assertEquals(true, e2Moves.contains(new Position(3, 4)), "E2 Pawn can move to E4");

        // 2. White Knight on B1 has 2 legal moves: A3 and C3
        Position b1 = new Position(0, 1);
        List<Position> b1Moves = board.getLegalMoves(b1, Color.WHITE);
        TestUtils.assertEquals(2, b1Moves.size(), "Initial White Knight on B1 has 2 legal moves");
        TestUtils.assertEquals(true, b1Moves.contains(new Position(2, 0)), "B1 Knight can move to A3");
        TestUtils.assertEquals(true, b1Moves.contains(new Position(2, 2)), "B1 Knight can move to C3");

        // 3. White Rook on A1 is blocked by pawns: 0 legal moves
        Position a1 = new Position(0, 0);
        List<Position> a1Moves = board.getLegalMoves(a1, Color.WHITE);
        TestUtils.assertEquals(0, a1Moves.size(), "Initial White Rook on A1 is blocked by pawns (0 moves)");

        // 4. White King on E1 is blocked by pieces: 0 legal moves
        Position e1 = new Position(0, 4);
        List<Position> e1Moves = board.getLegalMoves(e1, Color.WHITE);
        TestUtils.assertEquals(0, e1Moves.size(), "Initial White King on E1 has 0 legal moves");

        // 5. Querying Black piece on White's turn returns empty list
        Position e7 = new Position(6, 4);
        List<Position> e7WhiteTurn = board.getLegalMoves(e7, Color.WHITE);
        TestUtils.assertEquals(0, e7WhiteTurn.size(), "Querying Black pawn on White's turn returns empty list");

        // 6. Querying empty square returns empty list
        Position d4 = new Position(3, 3);
        List<Position> d4Moves = board.getLegalMoves(d4, Color.WHITE);
        TestUtils.assertEquals(0, d4Moves.size(), "Querying empty square returns empty list");

        // 7. Querying null or off-board returns empty list
        TestUtils.assertEquals(0, board.getLegalMoves(null, Color.WHITE).size(), "Null position returns empty list");
        TestUtils.assertEquals(0, board.getLegalMoves(new Position(-1, 0), Color.WHITE).size(), "Off-board returns empty list");
        TestUtils.assertEquals(0, board.getLegalMoves(e2, null).size(), "Null color returns empty list");
    }

    /**
     * Verifies that pinned pieces cannot move off the pin line.
     */
    private static void testPinnedPieceProtection() {
        // Setup: White King at E1, White Rook at E2, Black Queen at E8
        Board board = new Board();
        Position e1 = new Position(0, 4);
        Position e2 = new Position(1, 4);
        Position e8 = new Position(7, 4);

        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(e2, new Rook(Color.WHITE, e2));
        board.setPiece(e8, new Queen(Color.BLACK, e8));

        // White Rook on E2 is pinned along file E.
        // It can move along file E (E3, E4, E5, E6, E7, E8 capture) but CANNOT move to D2, F2, C2, etc.
        List<Position> rookMoves = board.getLegalMoves(e2, Color.WHITE);
        TestUtils.assertEquals(6, rookMoves.size(), "Pinned Rook on E2 can only move along file E (6 squares)");
        TestUtils.assertEquals(true, rookMoves.contains(new Position(2, 4)), "Pinned Rook can move to E3");
        TestUtils.assertEquals(true, rookMoves.contains(e8), "Pinned Rook can capture pinner Queen on E8");
        TestUtils.assertEquals(false, rookMoves.contains(new Position(1, 3)), "Pinned Rook cannot move to D2");
        TestUtils.assertEquals(false, rookMoves.contains(new Position(1, 5)), "Pinned Rook cannot move to F2");

        // Setup: White King at E1, White Bishop at E2, Black Rook at E8
        Board board2 = new Board();
        board2.setPiece(e1, new King(Color.WHITE, e1));
        board2.setPiece(e2, new Bishop(Color.WHITE, e2));
        board2.setPiece(e8, new Rook(Color.BLACK, e8));

        // White Bishop cannot move diagonally because it would expose E1 King to E8 Rook!
        List<Position> bishopMoves = board2.getLegalMoves(e2, Color.WHITE);
        TestUtils.assertEquals(0, bishopMoves.size(), "Pinned Bishop on file E has 0 legal moves");

        // Setup: White King at E1, White Knight at E2, Black Queen at E8
        Board board3 = new Board();
        board3.setPiece(e1, new King(Color.WHITE, e1));
        board3.setPiece(e2, new Knight(Color.WHITE, e2));
        board3.setPiece(e8, new Queen(Color.BLACK, e8));

        // White Knight cannot jump because any move exposes King
        List<Position> knightMoves = board3.getLegalMoves(e2, Color.WHITE);
        TestUtils.assertEquals(0, knightMoves.size(), "Pinned Knight on file E has 0 legal moves");

        // Setup: White King at E1, White Bishop at D2, Black Queen at A5 (diagonal pin)
        Board board4 = new Board();
        Position d2 = new Position(1, 3);
        Position a5 = new Position(4, 0);
        board4.setPiece(e1, new King(Color.WHITE, e1));
        board4.setPiece(d2, new Bishop(Color.WHITE, d2));
        board4.setPiece(a5, new Queen(Color.BLACK, a5));

        List<Position> diagBishopMoves = board4.getLegalMoves(d2, Color.WHITE);
        // Can move along diagonal: C3 (2,2), B4 (3,1), A5 (4,0 capture)
        TestUtils.assertEquals(3, diagBishopMoves.size(), "Diagonally pinned Bishop has 3 legal moves along pin axis");
        TestUtils.assertEquals(true, diagBishopMoves.contains(new Position(2, 2)), "Bishop can move to C3");
        TestUtils.assertEquals(true, diagBishopMoves.contains(new Position(3, 1)), "Bishop can move to B4");
        TestUtils.assertEquals(true, diagBishopMoves.contains(a5), "Bishop can capture pinning Queen on A5");
        TestUtils.assertEquals(false, diagBishopMoves.contains(new Position(2, 4)), "Bishop cannot step off pin to E3");
    }

    /**
     * Verifies that the King cannot move into a square attacked by an enemy piece.
     */
    private static void testKingCannotMoveIntoCheck() {
        Board board = new Board();
        Position e1 = new Position(0, 4);
        Position a2 = new Position(1, 0); // Black Rook controls row 1 (rank 2)

        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(a2, new Rook(Color.BLACK, a2));

        List<Position> kingMoves = board.getLegalMoves(e1, Color.WHITE);
        // Possible moves for King on E1: D1, F1, D2, E2, F2.
        // Squares D2, E2, F2 are on row 1, attacked by Black Rook on A2.
        // Only D1 (0, 3) and F1 (0, 5) should be legal!
        TestUtils.assertEquals(2, kingMoves.size(), "King cannot move into check on rank 2; only 2 moves legal");
        TestUtils.assertEquals(true, kingMoves.contains(new Position(0, 3)), "King can move to D1");
        TestUtils.assertEquals(true, kingMoves.contains(new Position(0, 5)), "King can move to F1");
        TestUtils.assertEquals(false, kingMoves.contains(new Position(1, 4)), "King cannot move to E2 (attacked)");
        TestUtils.assertEquals(false, kingMoves.contains(new Position(1, 3)), "King cannot move to D2 (attacked)");
        TestUtils.assertEquals(false, kingMoves.contains(new Position(1, 5)), "King cannot move to F2 (attacked)");
    }

    /**
     * Verifies that Kings can never move into adjacent squares (1-square radius of enemy King).
     */
    private static void testKingsCannotBeAdjacent() {
        Board board = new Board();
        Position e4 = new Position(3, 4);
        Position e6 = new Position(5, 4);

        board.setPiece(e4, new King(Color.WHITE, e4));
        board.setPiece(e6, new King(Color.BLACK, e6));

        List<Position> whiteKingMoves = board.getLegalMoves(e4, Color.WHITE);
        // Squares D5 (4,3), E5 (4,4), F5 (4,5) are adjacent to Black King on E6
        TestUtils.assertEquals(false, whiteKingMoves.contains(new Position(4, 4)), "White King cannot move to E5 (adjacent to Black King)");
        TestUtils.assertEquals(false, whiteKingMoves.contains(new Position(4, 3)), "White King cannot move to D5 (adjacent to Black King)");
        TestUtils.assertEquals(false, whiteKingMoves.contains(new Position(4, 5)), "White King cannot move to F5 (adjacent to Black King)");
        // Safe squares on row 2 or row 3: D4 (3,3), F4 (3,5), D3 (2,3), E3 (2,4), F3 (2,5)
        TestUtils.assertEquals(true, whiteKingMoves.contains(new Position(3, 3)), "White King can move to D4");
        TestUtils.assertEquals(true, whiteKingMoves.contains(new Position(2, 4)), "White King can move to E3");
    }

    /**
     * Verifies that the King cannot capture an enemy piece that is protected by another piece.
     */
    private static void testKingCannotCaptureDefendedPiece() {
        Board board = new Board();
        Position e1 = new Position(0, 4);
        Position e2 = new Position(1, 4); // Black Rook
        Position d3 = new Position(2, 3); // Black Bishop protecting E2

        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(e2, new Rook(Color.BLACK, e2));
        board.setPiece(d3, new Bishop(Color.BLACK, d3));

        List<Position> kingMoves = board.getLegalMoves(e1, Color.WHITE);
        TestUtils.assertEquals(false, kingMoves.contains(e2), "King cannot capture defended Rook on E2");

        // Now remove the defender Bishop
        board.setPiece(d3, null);
        List<Position> kingMovesAfter = board.getLegalMoves(e1, Color.WHITE);
        TestUtils.assertEquals(true, kingMovesAfter.contains(e2), "King CAN capture undefended Rook on E2");
    }

    /**
     * Verifies that when a player is in check, only moves that resolve the check are legal.
     */
    private static void testInCheckMoveFiltering() {
        Board board = new Board();
        Position e1 = new Position(0, 4); // White King
        Position e8 = new Position(7, 4); // Black Rook delivering check on file E
        Position c3 = new Position(2, 2); // White Knight
        Position h5 = new Position(4, 7); // White Bishop

        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(e8, new Rook(Color.BLACK, e8));
        board.setPiece(c3, new Knight(Color.WHITE, c3));
        board.setPiece(h5, new Bishop(Color.WHITE, h5));

        // White is in check!
        TestUtils.assertEquals(true, board.isCheck(Color.WHITE), "White is currently in check from Black Rook on E8");

        // 1. Knight on C3:
        // Possible moves: A4, A2, B1, B5, D1, D5, E2, E4.
        // Only E2 (1, 4) and E4 (3, 4) block file E! Other moves leave King in check!
        List<Position> knightMoves = board.getLegalMoves(c3, Color.WHITE);
        TestUtils.assertEquals(2, knightMoves.size(), "Knight has exactly 2 legal moves to block check (E2 and E4)");
        TestUtils.assertEquals(true, knightMoves.contains(new Position(1, 4)), "Knight can jump to E2 to block check");
        TestUtils.assertEquals(true, knightMoves.contains(new Position(3, 4)), "Knight can jump to E4 to block check");
        TestUtils.assertEquals(false, knightMoves.contains(new Position(3, 0)), "Knight cannot jump to A4 (does not resolve check)");

        // 2. Bishop on H5:
        // Bishop can capture checking Rook on E8 (7, 4) OR interpose on E2 (1, 4) to block the ray!
        List<Position> bishopMoves = board.getLegalMoves(h5, Color.WHITE);
        TestUtils.assertEquals(2, bishopMoves.size(), "Bishop has 2 legal moves to resolve check (capture on E8 or block on E2)");
        TestUtils.assertEquals(true, bishopMoves.contains(e8), "Bishop can capture checking Rook on E8");
        TestUtils.assertEquals(true, bishopMoves.contains(new Position(1, 4)), "Bishop can interpose on E2 to block check");

        // 3. King on E1:
        // King can evade to D1 (0, 3), F1 (0, 5), D2 (1, 3), F2 (1, 5).
        // King CANNOT move to E2 (1, 4) because it remains on file E under attack!
        List<Position> kingMoves = board.getLegalMoves(e1, Color.WHITE);
        TestUtils.assertEquals(false, kingMoves.contains(new Position(1, 4)), "King cannot evade to E2 along check ray");
        TestUtils.assertEquals(true, kingMoves.contains(new Position(0, 3)), "King can evade to D1");
        TestUtils.assertEquals(true, kingMoves.contains(new Position(0, 5)), "King can evade to F1");
    }

    /**
     * Verifies that the opponent King's square is never returned as a legal move.
     */
    private static void testEnemyKingCannotBeCaptured() {
        Board board = new Board();
        Position e1 = new Position(0, 4);
        Position e7 = new Position(6, 4);
        Position e8 = new Position(7, 4);

        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(e7, new Rook(Color.WHITE, e7));
        board.setPiece(e8, new King(Color.BLACK, e8));

        // White Rook on E7 has enemy King on E8 directly ahead.
        // In chess, an enemy King can never be captured.
        List<Position> rookMoves = board.getLegalMoves(e7, Color.WHITE);
        TestUtils.assertEquals(false, rookMoves.contains(e8), "Legal moves must never include enemy King square");
    }

    /**
     * Verifies that wouldExposeKing does not mutate board state, pieces, or capture lists.
     */
    private static void testWouldExposeKingPreservesBoardState() {
        Board board = new Board();
        board.initialize();

        int whitePiecesBefore = board.getPieces(Color.WHITE).size();
        int blackPiecesBefore = board.getPieces(Color.BLACK).size();
        int capturedBefore = board.getCapturedPieces().size();

        Position e2 = new Position(1, 4);
        Position e4 = new Position(3, 4);

        boolean exposes = board.wouldExposeKing(e2, e4, Color.WHITE);
        boolean leavesInCheck = board.wouldLeaveInCheck(e2, e4, Color.WHITE);

        TestUtils.assertEquals(false, exposes, "1. e4 does not expose White King to check");
        TestUtils.assertEquals(exposes, leavesInCheck, "wouldLeaveInCheck matches wouldExposeKing");

        // Verify total state preservation
        TestUtils.assertEquals(whitePiecesBefore, board.getPieces(Color.WHITE).size(), "White pieces count unmutated");
        TestUtils.assertEquals(blackPiecesBefore, board.getPieces(Color.BLACK).size(), "Black pieces count unmutated");
        TestUtils.assertEquals(capturedBefore, board.getCapturedPieces().size(), "Captured pieces count unmutated");
        TestUtils.assertEquals(e2, board.getPiece(e2).getPosition(), "E2 piece position restored to E2");
        TestUtils.assertEquals(false, board.getPiece(e2).hasMoved(), "E2 piece moved flag preserved as false");
        TestUtils.assertEquals(null, board.getPiece(e4), "E4 remains empty after simulation");
    }

    /**
     * Verifies overloaded getLegalMoves(Position) defaults to piece's color.
     */
    private static void testOverloadedGetLegalMoves() {
        Board board = new Board();
        board.initialize();

        Position e2 = new Position(1, 4);
        List<Position> overloadedMoves = board.getLegalMoves(e2);
        List<Position> explicitMoves = board.getLegalMoves(e2, Color.WHITE);

        TestUtils.assertEquals(explicitMoves.size(), overloadedMoves.size(), "Overloaded getLegalMoves matches explicit");
        TestUtils.assertEquals(0, board.getLegalMoves(new Position(3, 3)).size(), "Empty square returns empty list");
        TestUtils.assertEquals(0, board.getLegalMoves(null).size(), "Null position returns empty list");
    }
}
