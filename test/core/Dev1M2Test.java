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
 * Unit tests for Dev 1's Milestone 2 deliverables: lightweight King safety
 * simulation using {@link AttackDetector#isSquareAttacked(Board, Position, Color)}
 * and comprehensive pinned-piece protection across all axes and piece types.
 *
 * @author Dev 1 (Rayed)
 */
public final class Dev1M2Test {

    /** Prevents instantiation of utility test class. */
    private Dev1M2Test() { }

    /**
     * Executes all Milestone 2 King safety and pin protection checks.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        testPlanPinnedRookVsBlackQueen();
        testDiagonalPinsAndPinnedPawnCapture();
        testHorizontalRankPin();
        testXRayCheckPreventsKingRetreatAlongRay();
        testDoubleCheckForcesKingMoveOnly();
        testCheckEvasionBlocksUnrelatedMoves();
        testSimulationRollbackOnCaptureAndGuards();
        testBlackKingSafetyAndPins();
    }

    /**
     * Verifies the Phase 3 Plan Milestone 2 Pin Test:
     * White King on E1, White Rook on E2, Black Queen on E7.
     */
    private static void testPlanPinnedRookVsBlackQueen() {
        Board board = new Board();
        Position e1 = pos("E1");
        Position e2 = pos("E2");
        Position e7 = pos("E7");
        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(e2, new Rook(Color.WHITE, e2));
        board.setPiece(e7, new Queen(Color.BLACK, e7));

        TestUtils.assertEquals(true, board.wouldExposeKing(e2, pos("D2"), Color.WHITE),
                "Pinned Rook moving horizontally to D2 exposes White King");
        TestUtils.assertEquals(true, board.wouldExposeKing(e2, pos("F2"), Color.WHITE),
                "Pinned Rook moving horizontally to F2 exposes White King");
        TestUtils.assertEquals(false, board.wouldExposeKing(e2, pos("E4"), Color.WHITE),
                "Pinned Rook staying on file E (E4) keeps King safe");
        TestUtils.assertEquals(false, board.wouldExposeKing(e2, e7, Color.WHITE),
                "Pinned Rook capturing pinner Queen on E7 keeps King safe");

        List<Position> rookMoves = board.getLegalMoves(e2, Color.WHITE);
        TestUtils.assertEquals(5, rookMoves.size(),
                "Pinned Rook on E2 has exactly 5 file-E moves (E3-E7)");
        TestUtils.assertEquals(false, rookMoves.contains(pos("D2")),
                "Legal moves exclude D2");
        TestUtils.assertEquals(false, rookMoves.contains(pos("F2")),
                "Legal moves exclude F2");
        TestUtils.assertEquals(true, rookMoves.contains(e7),
                "Legal moves include capturing pinner on E7");
    }

    /**
     * Verifies diagonal pins for Bishop, Rook, and Pawn (including legal capture along the pin).
     */
    private static void testDiagonalPinsAndPinnedPawnCapture() {
        // 1. Pinned Rook on diagonal has 0 legal moves
        Board rookPin = new Board();
        rookPin.setPiece(pos("E1"), new King(Color.WHITE, pos("E1")));
        rookPin.setPiece(pos("D2"), new Rook(Color.WHITE, pos("D2")));
        rookPin.setPiece(pos("A5"), new Bishop(Color.BLACK, pos("A5")));
        TestUtils.assertEquals(0, rookPin.getLegalMoves(pos("D2"), Color.WHITE).size(),
                "Diagonally pinned Rook has zero legal moves");

        // 2. Pinned Pawn cannot push forward off diagonal, but CAN capture pinner on pin vector
        Board pawnPin = new Board();
        pawnPin.setPiece(pos("E1"), new King(Color.WHITE, pos("E1")));
        pawnPin.setPiece(pos("D2"), new Pawn(Color.WHITE, pos("D2")));
        pawnPin.setPiece(pos("C3"), new Queen(Color.BLACK, pos("C3")));
        pawnPin.setPiece(pos("E3"), new Knight(Color.BLACK, pos("E3")));

        List<Position> pawnMoves = pawnPin.getLegalMoves(pos("D2"), Color.WHITE);
        TestUtils.assertEquals(1, pawnMoves.size(),
                "Diagonally pinned Pawn has only 1 legal move (capturing pinner along pin diagonal)");
        TestUtils.assertEquals(true, pawnMoves.contains(pos("C3")),
                "Pinned Pawn can capture pinner Queen on C3 along pin axis");
        TestUtils.assertEquals(false, pawnMoves.contains(pos("D3")),
                "Pinned Pawn cannot push forward to D3 off diagonal pin");
        TestUtils.assertEquals(false, pawnMoves.contains(pos("E3")),
                "Pinned Pawn cannot capture onto E3 off diagonal pin");
    }

    /**
     * Verifies horizontal rank pins restrict Queens to the rank and immobilize Knights.
     */
    private static void testHorizontalRankPin() {
        Board board = new Board();
        board.setPiece(pos("A1"), new King(Color.WHITE, pos("A1")));
        board.setPiece(pos("D1"), new Queen(Color.WHITE, pos("D1")));
        board.setPiece(pos("H1"), new Rook(Color.BLACK, pos("H1")));

        List<Position> queenMoves = board.getLegalMoves(pos("D1"), Color.WHITE);
        TestUtils.assertEquals(6, queenMoves.size(),
                "Horizontally pinned Queen can move only along rank 1 (B1, C1, E1-H1)");
        TestUtils.assertEquals(true, queenMoves.contains(pos("H1")),
                "Pinned Queen can capture pinning Rook on H1");
        TestUtils.assertEquals(false, queenMoves.contains(pos("D2")),
                "Pinned Queen cannot move vertically off rank 1");
        TestUtils.assertEquals(false, queenMoves.contains(pos("E2")),
                "Pinned Queen cannot move diagonally off rank 1");

        // Replace Queen with Knight on D1: Knight cannot stay on rank 1 -> 0 legal moves
        board.setPiece(pos("D1"), new Knight(Color.WHITE, pos("D1")));
        TestUtils.assertEquals(0, board.getLegalMoves(pos("D1"), Color.WHITE).size(),
                "Horizontally pinned Knight has 0 legal moves");
    }

    /**
     * Ensures trial simulation removes the King from its origin square before checking attacks,
     * preventing the King from falsely shielding its own retreat square along a check ray.
     */
    private static void testXRayCheckPreventsKingRetreatAlongRay() {
        Board board = new Board();
        Position e2 = pos("E2");
        Position e1 = pos("E1");
        Position e3 = pos("E3");
        board.setPiece(e2, new King(Color.WHITE, e2));
        board.setPiece(pos("E8"), new Rook(Color.BLACK, pos("E8")));

        TestUtils.assertEquals(true, board.wouldExposeKing(e2, e1, Color.WHITE),
                "King retreating along check ray from E2 to E1 remains exposed to E8 Rook");
        TestUtils.assertEquals(true, board.wouldExposeKing(e2, e3, Color.WHITE),
                "King stepping into check ray from E2 to E3 remains exposed to E8 Rook");

        List<Position> kingMoves = board.getLegalMoves(e2, Color.WHITE);
        TestUtils.assertEquals(6, kingMoves.size(),
                "King on E2 under E8 Rook check has 6 safe adjacent squares (D/F files)");
        TestUtils.assertEquals(false, kingMoves.contains(e1), "King cannot step back to E1");
        TestUtils.assertEquals(false, kingMoves.contains(e3), "King cannot step forward to E3");
    }

    /**
     * Ensures that under double check, blocking or capturing one checker is still rejected,
     * leaving only King evasions legal.
     */
    private static void testDoubleCheckForcesKingMoveOnly() {
        Board board = new Board();
        board.setPiece(pos("E1"), new King(Color.WHITE, pos("E1")));
        board.setPiece(pos("E8"), new Rook(Color.BLACK, pos("E8")));
        board.setPiece(pos("B4"), new Bishop(Color.BLACK, pos("B4")));
        board.setPiece(pos("G3"), new Knight(Color.WHITE, pos("G3")));
        board.setPiece(pos("A8"), new Rook(Color.WHITE, pos("A8")));

        // Blocking Rook on E2 still leaves King exposed to B4 Bishop
        TestUtils.assertEquals(true, board.wouldExposeKing(pos("G3"), pos("E2"), Color.WHITE),
                "Blocking E8 Rook leaves King in check from B4 Bishop");
        // Capturing E8 Rook still leaves King exposed to B4 Bishop
        TestUtils.assertEquals(true, board.wouldExposeKing(pos("A8"), pos("E8"), Color.WHITE),
                "Capturing E8 Rook leaves King in check from B4 Bishop");

        TestUtils.assertEquals(0, board.getLegalMoves(pos("G3"), Color.WHITE).size(),
                "Knight has 0 legal moves in double check");
        TestUtils.assertEquals(0, board.getLegalMoves(pos("A8"), Color.WHITE).size(),
                "Rook has 0 legal moves in double check");

        List<Position> kingMoves = board.getLegalMoves(pos("E1"), Color.WHITE);
        TestUtils.assertEquals(3, kingMoves.size(),
                "King in double check can only escape to D1, F1, or F2");
        TestUtils.assertEquals(false, kingMoves.contains(pos("D2")),
                "D2 remains attacked by B4 Bishop");
        TestUtils.assertEquals(false, kingMoves.contains(pos("E2")),
                "E2 remains attacked by E8 Rook");
        TestUtils.assertEquals(true, kingMoves.contains(pos("D1")),
                "King can escape to D1");
        TestUtils.assertEquals(true, kingMoves.contains(pos("F1")),
                "King can escape to F1");
        TestUtils.assertEquals(true, kingMoves.contains(pos("F2")),
                "King can escape to F2");
    }

    /**
     * Verifies Plan Milestone 2 Check Evasion scenario: unrelated pawn rejected,
     * while blocking the check ray or evading with King is legal.
     */
    private static void testCheckEvasionBlocksUnrelatedMoves() {
        Board board = new Board();
        board.setPiece(pos("E1"), new King(Color.WHITE, pos("E1")));
        board.setPiece(pos("A2"), new Pawn(Color.WHITE, pos("A2")));
        board.setPiece(pos("C4"), new Bishop(Color.WHITE, pos("C4")));
        board.setPiece(pos("E8"), new Rook(Color.BLACK, pos("E8")));

        TestUtils.assertEquals(true, board.isCheck(Color.WHITE),
                "White King is in check from Black Rook on E8");
        TestUtils.assertEquals(0, board.getLegalMoves(pos("A2"), Color.WHITE).size(),
                "Unrelated A2 pawn has 0 legal moves while King is in check");

        List<Position> bishopMoves = board.getLegalMoves(pos("C4"), Color.WHITE);
        TestUtils.assertEquals(2, bishopMoves.size(),
                "Bishop on C4 can only interpose on E2 or E6");
        TestUtils.assertEquals(true, bishopMoves.contains(pos("E2")),
                "Bishop can interpose on E2");
        TestUtils.assertEquals(true, bishopMoves.contains(pos("E6")),
                "Bishop can interpose on E6");

        List<Position> kingMoves = board.getLegalMoves(pos("E1"), Color.WHITE);
        TestUtils.assertEquals(4, kingMoves.size(),
                "King on E1 can evade to D1, F1, D2, F2");
    }

    /**
     * Verifies rollback integrity during capture simulation and invalid-input guards
     * including friendly-occupied destinations and missing Kings.
     */
    private static void testSimulationRollbackOnCaptureAndGuards() {
        Board board = new Board();
        Position e1 = pos("E1");
        Position d2 = pos("D2");
        Position c3 = pos("C3");
        Position a5 = pos("A5");

        Bishop whiteBishop = new Bishop(Color.WHITE, d2);
        whiteBishop.move(d2); // mark hasMoved() = true to confirm preservation
        Knight blackKnight = new Knight(Color.BLACK, c3);
        blackKnight.move(c3);

        board.setPiece(e1, new King(Color.WHITE, e1));
        board.setPiece(d2, whiteBishop);
        board.setPiece(c3, blackKnight);
        board.setPiece(a5, new Queen(Color.BLACK, a5));

        TestUtils.assertEquals(false, board.wouldExposeKing(d2, c3, Color.WHITE),
                "Capturing on pin vector C3 keeps King safe");
        TestUtils.assertEquals(whiteBishop, board.getPiece(d2),
                "Moving Bishop restored to D2 after capture simulation");
        TestUtils.assertEquals(d2, whiteBishop.getPosition(),
                "Bishop internal coordinate preserved");
        TestUtils.assertEquals(true, whiteBishop.hasMoved(),
                "Bishop moved flag preserved as true");
        TestUtils.assertEquals(blackKnight, board.getPiece(c3),
                "Temporarily captured Black Knight restored to C3");
        TestUtils.assertEquals(0, board.getCapturedPieces().size(),
                "Trial capture does not pollute capturedPieces list");

        // Invalid / friendly-target guards
        TestUtils.assertEquals(true, board.wouldExposeKing(d2, d2, Color.WHITE),
                "Same-square move treated as invalid/exposed");
        TestUtils.assertEquals(true, board.wouldExposeKing(d2, e1, Color.WHITE),
                "Moving onto friendly King square is rejected");
        TestUtils.assertEquals(true, board.wouldExposeKing(c3, pos("B5"), Color.WHITE),
                "Wrong-color source piece rejected");
        TestUtils.assertEquals(true, board.wouldExposeKing(pos("H8"), pos("H7"), Color.WHITE),
                "Empty source square rejected");

        // Synthetic board without friendly King returns false cleanly
        Board kingless = new Board();
        kingless.setPiece(pos("D1"), new Rook(Color.WHITE, pos("D1")));
        TestUtils.assertEquals(false, kingless.wouldExposeKing(pos("D1"), pos("D5"), Color.WHITE),
                "Board without friendly King does not falsely report check");
    }

    /**
     * Verifies King safety, pawn check attacks, and pinned pieces symmetrically for Black.
     */
    private static void testBlackKingSafetyAndPins() {
        Board board = new Board();
        board.setPiece(pos("E8"), new King(Color.BLACK, pos("E8")));
        board.setPiece(pos("D7"), new Bishop(Color.BLACK, pos("D7")));
        board.setPiece(pos("A4"), new Queen(Color.WHITE, pos("A4")));
        board.setPiece(pos("F7"), new Pawn(Color.WHITE, pos("F7")));

        // F7 White pawn attacks E8 and G8; E8 is currently in check by F7 pawn.
        TestUtils.assertEquals(true, board.isCheck(Color.BLACK),
                "White Pawn on F7 puts Black King on E8 in check");
        // Black Bishop on D7 cannot resolve the pawn check -> 0 legal moves
        TestUtils.assertEquals(0, board.getLegalMoves(pos("D7"), Color.BLACK).size(),
                "Pinned Black Bishop cannot resolve pawn check");

        // Remove F7 pawn so only diagonal pin remains
        board.setPiece(pos("F7"), null);
        List<Position> blackBishopMoves = board.getLegalMoves(pos("D7"), Color.BLACK);
        TestUtils.assertEquals(3, blackBishopMoves.size(),
                "Pinned Black Bishop can only move to C6, B5, A4 along pin axis");
        TestUtils.assertEquals(true, blackBishopMoves.contains(pos("A4")),
                "Pinned Black Bishop can capture pinning White Queen on A4");
        TestUtils.assertEquals(false, blackBishopMoves.contains(pos("C8")),
                "Pinned Black Bishop cannot move off pin axis to C8");
    }

    /** Converts algebraic coordinate (e.g., "E1") to a {@link Position}. */
    private static Position pos(String square) {
        return new Position(square.charAt(1) - '1', square.charAt(0) - 'A');
    }
}
