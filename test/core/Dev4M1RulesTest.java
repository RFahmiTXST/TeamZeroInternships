package core;

import board.Board;
import board.Position;
import pieces.Bishop;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;
import test.TestUtils;

import java.util.List;

/**
 * Dev 4 Phase 3 M1 regression tests for piece geometry, blockers, and captures.
 * These checks use the real Board.getLegalMoves API and preserve Dev 1's suite.
 */
public final class Dev4M1RulesTest {
    /** Total assertions performed by this suite. */
    private static int checks;

    /** Test-only utility class. */
    private Dev4M1RulesTest() { }

    /**
     * Runs the piece-movement regression suite.
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        int oldFailures = TestUtils.getFailedCount();
        checks = 0;
        initialPosition();
        rookBlockers();
        bishopBlockers();
        queenBlockers();
        knightJump();
        whitePawn();
        blackPawn();
        colorGuard();
        System.out.println("Dev 4 Phase 3 M1 rules: " + checks + " assertions executed.");
        if (TestUtils.getFailedCount() != oldFailures) {
            throw new AssertionError("Dev 4 M1 rules regression failed.");
        }
    }

    /** Initial position checks verify that slider paths do not pass friendly pawns. */
    private static void initialPosition() {
        Board board = new Board();
        board.initialize();
        check(0, moves(board, "A1", Color.WHITE).size(), "Initial rook A1 blocked");
        check(0, moves(board, "C1", Color.WHITE).size(), "Initial bishop C1 blocked");
        check(0, moves(board, "D1", Color.WHITE).size(), "Initial queen D1 blocked");
        check(true, moves(board, "B1", Color.WHITE).contains(at("A3")), "B1 knight jumps to A3");
        check(true, moves(board, "B1", Color.WHITE).contains(at("C3")), "B1 knight jumps to C3");
        check(2, moves(board, "E2", Color.WHITE).size(), "E2 pawn initially has two moves");
    }

    /** Rooks stop before friends and at captured enemies. */
    private static void rookBlockers() {
        Board board = bareBoard();
        put(board, "D4", new Rook(Color.WHITE, at("D4")));
        put(board, "D6", new Pawn(Color.WHITE, at("D6")));
        put(board, "B4", new Pawn(Color.BLACK, at("B4")));
        List<Position> legal = moves(board, "D4", Color.WHITE);
        check(true, legal.contains(at("D5")), "Rook can approach friendly blocker");
        check(false, legal.contains(at("D6")), "Rook cannot capture friendly blocker");
        check(false, legal.contains(at("D7")), "Rook cannot jump over friendly blocker");
        check(true, legal.contains(at("C4")), "Rook can approach opposing blocker");
        check(true, legal.contains(at("B4")), "Rook can capture opposing blocker");
        check(false, legal.contains(at("A4")), "Rook cannot move past opposing blocker");
    }

    /** Bishops use diagonals and stop at the first occupied square. */
    private static void bishopBlockers() {
        Board board = bareBoard();
        put(board, "C3", new Bishop(Color.WHITE, at("C3")));
        put(board, "E5", new Pawn(Color.WHITE, at("E5")));
        put(board, "A5", new Pawn(Color.BLACK, at("A5")));
        List<Position> legal = moves(board, "C3", Color.WHITE);
        check(true, legal.contains(at("D4")), "Bishop can approach friendly blocker");
        check(false, legal.contains(at("E5")), "Bishop cannot capture friendly piece");
        check(false, legal.contains(at("F6")), "Bishop cannot cross friendly blocker");
        check(true, legal.contains(at("B4")), "Bishop can approach opposing blocker");
        check(true, legal.contains(at("A5")), "Bishop can capture diagonal opponent");
        check(false, legal.contains(at("C4")), "Bishop cannot move vertically");
    }

    /** Queens must obey rook and bishop blockers. */
    private static void queenBlockers() {
        Board board = bareBoard();
        put(board, "D4", new Queen(Color.WHITE, at("D4")));
        put(board, "D6", new Pawn(Color.WHITE, at("D6")));
        put(board, "F4", new Pawn(Color.BLACK, at("F4")));
        List<Position> legal = moves(board, "D4", Color.WHITE);
        check(true, legal.contains(at("D5")), "Queen can approach own pawn");
        check(false, legal.contains(at("D6")), "Queen cannot capture own pawn");
        check(false, legal.contains(at("D7")), "Queen cannot cross own pawn");
        check(true, legal.contains(at("E4")), "Queen can approach opponent");
        check(true, legal.contains(at("F4")), "Queen can capture opponent");
        check(false, legal.contains(at("G4")), "Queen stops after capture square");
        check(true, legal.contains(at("E5")), "Queen may move diagonally");
    }

    /** Knights jump over intermediate pawns but cannot land on friends. */
    private static void knightJump() {
        Board board = bareBoard();
        put(board, "B1", new Knight(Color.WHITE, at("B1")));
        put(board, "A2", new Pawn(Color.WHITE, at("A2")));
        put(board, "C2", new Pawn(Color.WHITE, at("C2")));
        List<Position> legal = moves(board, "B1", Color.WHITE);
        check(true, legal.contains(at("A3")), "Knight jumps over A2");
        check(true, legal.contains(at("C3")), "Knight jumps over C2");
        put(board, "A3", new Pawn(Color.BLACK, at("A3")));
        put(board, "C3", new Pawn(Color.WHITE, at("C3")));
        legal = moves(board, "B1", Color.WHITE);
        check(true, legal.contains(at("A3")), "Knight can capture on landing square");
        check(false, legal.contains(at("C3")), "Knight cannot land on own piece");
        check(false, legal.contains(at("B3")), "Knight cannot move like a rook");
    }

    /** White pawns move straight into empty squares and capture only diagonally. */
    private static void whitePawn() {
        Board board = bareBoard();
        put(board, "E2", new Pawn(Color.WHITE, at("E2")));
        List<Position> legal = moves(board, "E2", Color.WHITE);
        check(true, legal.contains(at("E3")), "White pawn can advance one");
        check(true, legal.contains(at("E4")), "White pawn can advance two initially");
        check(false, legal.contains(at("E5")), "White pawn cannot advance three");
        check(false, legal.contains(at("D3")), "White pawn cannot move diagonally when empty");

        put(board, "E3", new Pawn(Color.BLACK, at("E3")));
        put(board, "D3", new Pawn(Color.BLACK, at("D3")));
        put(board, "F3", new Pawn(Color.WHITE, at("F3")));
        legal = moves(board, "E2", Color.WHITE);
        check(false, legal.contains(at("E3")), "White pawn cannot capture straight forward");
        check(false, legal.contains(at("E4")), "Blocked white pawn cannot double-step");
        check(true, legal.contains(at("D3")), "White pawn captures diagonally left");
        check(false, legal.contains(at("F3")), "White pawn cannot capture friendly diagonal");

        Board moved = bareBoard();
        put(moved, "E2", new Pawn(Color.WHITE, at("E2")));
        moved.movePiece(at("E2"), at("E3"));
        legal = moves(moved, "E3", Color.WHITE);
        check(true, legal.contains(at("E4")), "Moved white pawn may advance one");
        check(false, legal.contains(at("E5")), "Moved white pawn cannot double-step");
    }

    /** Black pawns have mirrored direction and capture rules. */
    private static void blackPawn() {
        Board board = bareBoard();
        put(board, "E7", new Pawn(Color.BLACK, at("E7")));
        List<Position> legal = moves(board, "E7", Color.BLACK);
        check(true, legal.contains(at("E6")), "Black pawn advances toward rank one");
        check(true, legal.contains(at("E5")), "Black pawn advances two initially");
        check(false, legal.contains(at("E4")), "Black pawn cannot advance three");
        check(false, legal.contains(at("D6")), "Black pawn cannot capture empty diagonal");

        put(board, "E6", new Pawn(Color.WHITE, at("E6")));
        put(board, "D6", new Pawn(Color.WHITE, at("D6")));
        put(board, "F6", new Pawn(Color.BLACK, at("F6")));
        legal = moves(board, "E7", Color.BLACK);
        check(false, legal.contains(at("E6")), "Black pawn cannot capture straight forward");
        check(false, legal.contains(at("E5")), "Blocked black pawn cannot double-step");
        check(true, legal.contains(at("D6")), "Black pawn captures diagonally left");
        check(false, legal.contains(at("F6")), "Black pawn cannot capture friendly diagonal");

        Board moved = bareBoard();
        put(moved, "E7", new Pawn(Color.BLACK, at("E7")));
        moved.movePiece(at("E7"), at("E6"));
        legal = moves(moved, "E6", Color.BLACK);
        check(true, legal.contains(at("E5")), "Moved black pawn may advance one");
        check(false, legal.contains(at("E4")), "Moved black pawn cannot double-step");
    }

    /** Legal-move queries must not grant access to the opposing player's pieces. */
    private static void colorGuard() {
        Board board = new Board();
        board.initialize();
        check(true, moves(board, "E7", Color.WHITE).isEmpty(),
                "White cannot query Black pawn moves");
        check(true, moves(board, "E2", Color.BLACK).isEmpty(),
                "Black cannot query White pawn moves");
        check(true, moves(board, "E2", Color.WHITE).contains(at("E4")),
                "White may query own pawn moves");
        check(true, moves(board, "E7", Color.BLACK).contains(at("E5")),
                "Black may query own pawn moves");
    }

    /** Keeps both Kings on the board so checks use real King-safety filtering. */
    private static Board bareBoard() {
        Board board = new Board();
        put(board, "E1", new King(Color.WHITE, at("E1")));
        put(board, "H8", new King(Color.BLACK, at("H8")));
        return board;
    }

    /** Places a piece without changing its internal coordinates. */
    private static void put(Board board, String square, pieces.Piece piece) {
        board.setPiece(at(square), piece);
    }

    /** Converts algebraic notation to zero-based coordinates. */
    private static Position at(String square) {
        return new Position(square.charAt(1) - '1', square.charAt(0) - 'A');
    }

    /** Retrieves King-safe candidate destinations for a source square. */
    private static List<Position> moves(Board board, String from, Color color) {
        return board.getLegalMoves(at(from), color);
    }

    /** Records an assertion in the project's shared test framework. */
    private static void check(Object expected, Object actual, String label) {
        checks++;
        TestUtils.assertEquals(expected, actual, "Dev4 M1: " + label);
    }
}
