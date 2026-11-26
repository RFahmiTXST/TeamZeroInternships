package board;

import java.util.List;
import pieces.Color;
import pieces.King;
import pieces.Queen;
import pieces.Rook;

/**
 * Dev 4 Phase 3 M2 regression coverage for checkmate and stalemate detection.
 * This is a standalone test; no changes to teammates' test runners are required.
 */
public final class Dev4M2EndgameRulesTest {
    /** Number of assertions checked. */
    private static int assertions;

    /** Prevent instantiation. */
    private Dev4M2EndgameRulesTest() { }

    /**
     * Executes the standalone checks, failing with a nonzero exit code on error.
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        testInitialAndIncompleteBoards();
        testBlackCheckmate();
        testWhiteCheckmate();
        testBlackStalemate();
        testEscapableCheck();
        testQuietPosition();
        testScholarMate();
        System.out.println("Dev 4 Phase 3 M2 board: " + assertions + " assertions passed.");
    }

    /** Initial position and invalid setups are not finished games. */
    private static void testInitialAndIncompleteBoards() {
        Board board = new Board();
        board.initialize();
        check(!board.isCheckmate(Color.WHITE), "initial white not checkmated");
        check(!board.isCheckmate(Color.BLACK), "initial black not checkmated");
        check(!board.isStalemate(Color.WHITE), "initial white not stalemated");
        check(!board.isStalemate(Color.BLACK), "initial black not stalemated");
        check(!board.isCheckmate(null), "null side never checkmated");
        check(!board.isStalemate(null), "null side never stalemated");
        Board empty = new Board();
        check(!empty.isCheckmate(Color.WHITE), "missing white king is not checkmate");
        check(!empty.isStalemate(Color.WHITE), "missing white king is not stalemate");
        check(!empty.isCheckmate(Color.BLACK), "missing black king is not checkmate");
        check(!empty.isStalemate(Color.BLACK), "missing black king is not stalemate");
    }

    /** Qb7 and Kc6 checkmate the black king on a8. */
    private static void testBlackCheckmate() {
        Board board = new Board();
        placeKing(board, Color.BLACK, 7, 0);
        placeKing(board, Color.WHITE, 5, 2);
        placeQueen(board, Color.WHITE, 6, 1);
        check(board.isCheck(Color.BLACK), "black king at a8 is in check");
        check(board.getLegalMoves(pos(7, 0), Color.BLACK).isEmpty(), "black king has no legal square");
        check(board.isCheckmate(Color.BLACK), "black is checkmated");
        check(!board.isStalemate(Color.BLACK), "checkmate is not stalemate");
        check(!board.isCheckmate(Color.WHITE), "white is not checkmated");
        check(board.getCapturedPieces().isEmpty(), "mate queries do not capture pieces");
        check(board.getPiece(pos(6, 1)) instanceof Queen, "mate query preserves queen");
        check(board.getPiece(pos(7, 0)) instanceof King, "mate query preserves king");
    }

    /** Qb2 and Kc3 checkmate the white king on a1. */
    private static void testWhiteCheckmate() {
        Board board = new Board();
        placeKing(board, Color.WHITE, 0, 0);
        placeKing(board, Color.BLACK, 2, 2);
        placeQueen(board, Color.BLACK, 1, 1);
        check(board.isCheck(Color.WHITE), "white king at a1 is in check");
        check(board.isCheckmate(Color.WHITE), "white is checkmated");
        check(!board.isStalemate(Color.WHITE), "white mate is not stalemate");
        check(!board.isCheckmate(Color.BLACK), "black is not checkmated");
    }

    /** Qb6 and Kc6 stalemate the black king on a8. */
    private static void testBlackStalemate() {
        Board board = new Board();
        placeKing(board, Color.BLACK, 7, 0);
        placeKing(board, Color.WHITE, 5, 2);
        placeQueen(board, Color.WHITE, 5, 1);
        check(!board.isCheck(Color.BLACK), "black king a8 is not in check");
        check(board.getLegalMoves(pos(7, 0), Color.BLACK).isEmpty(), "black king has no legal moves");
        check(board.isStalemate(Color.BLACK), "black stalemate detected");
        check(!board.isCheckmate(Color.BLACK), "stalemate is not checkmate");
        check(board.getPiece(pos(5, 1)) instanceof Queen, "stalemate query preserves queen");
    }

    /** An attacked king with an available escape is not checkmated. */
    private static void testEscapableCheck() {
        Board board = new Board();
        placeKing(board, Color.BLACK, 7, 0);
        placeKing(board, Color.WHITE, 0, 7);
        board.setPiece(pos(0, 0), new Rook(Color.WHITE, pos(0, 0)));
        check(board.isCheck(Color.BLACK), "rook a1 checks black king a8");
        List<Position> moves = board.getLegalMoves(pos(7, 0), Color.BLACK);
        check(moves.contains(pos(7, 1)), "black king can escape to b8");
        check(!board.isCheckmate(Color.BLACK), "escapable check is not mate");
        check(!board.isStalemate(Color.BLACK), "escapable check is not stalemate");
    }

    /** Two widely separated kings may both move and the game is ongoing. */
    private static void testQuietPosition() {
        Board board = new Board();
        placeKing(board, Color.WHITE, 0, 0);
        placeKing(board, Color.BLACK, 7, 7);
        check(!board.isCheck(Color.WHITE) && !board.isCheck(Color.BLACK), "neither distant king is checked");
        check(!board.getLegalMoves(pos(0, 0), Color.WHITE).isEmpty(), "white has legal moves");
        check(!board.getLegalMoves(pos(7, 7), Color.BLACK).isEmpty(), "black has legal moves");
        check(!board.isStalemate(Color.WHITE), "white is not stalemated");
        check(!board.isStalemate(Color.BLACK), "black is not stalemated");
    }

    /** Real move sequence: 1.e4 e5 2.Bc4 Nc6 3.Qh5 Nf6 4.Qxf7#. */
    private static void testScholarMate() {
        Board board = new Board();
        board.initialize();
        move(board, Color.WHITE, 1, 4, 3, 4); // E2-E4
        move(board, Color.BLACK, 6, 4, 4, 4); // E7-E5
        move(board, Color.WHITE, 0, 5, 3, 2); // F1-C4
        move(board, Color.BLACK, 7, 1, 5, 2); // B8-C6
        move(board, Color.WHITE, 0, 3, 4, 7); // D1-H5
        move(board, Color.BLACK, 7, 6, 5, 5); // G8-F6
        move(board, Color.WHITE, 4, 7, 6, 5); // H5xF7
        check(board.isCheckmate(Color.BLACK), "Scholar's Mate checkmates Black");
        check(!board.isStalemate(Color.BLACK), "Scholar's Mate is not a draw");
        check(board.getPiece(pos(6, 5)) instanceof Queen, "white queen ends on f7");
        check(board.getCapturedPieces().size() == 1, "queen captured exactly one pawn");
    }

    /** Place a king in a test position. */
    private static void placeKing(Board board, Color side, int row, int column) {
        board.setPiece(pos(row, column), new King(side, pos(row, column)));
    }

    /** Place a queen in a test position. */
    private static void placeQueen(Board board, Color side, int row, int column) {
        board.setPiece(pos(row, column), new Queen(side, pos(row, column)));
    }

    /** Convert row/column to an immutable-equivalent coordinate value. */
    private static Position pos(int row, int col) { return new Position(row, col); }

    /** Execute a known legal move and fail if the engine does not generate it. */
    private static void move(Board b, Color side, int fr, int fc, int tr, int tc) {
        Position from = pos(fr, fc);
        Position to = pos(tr, tc);
        check(b.getLegalMoves(from, side).contains(to), side + " " + from + "-" + to + " is legal");
        b.movePiece(from, to);
    }

    /** Fail fast so a single bad expectation cannot be mistaken for a passing test. */
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError("Dev4 M2: " + message);
        assertions++;
        System.out.println("PASS: Dev4 M2: " + message);
    }
}
