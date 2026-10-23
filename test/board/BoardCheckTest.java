package board;

import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Rook;
import test.TestUtils;

/** Focused checks for Board.isCheck(). */
public final class BoardCheckTest {

    private BoardCheckTest() { }

    /** Runs Board.isCheck() checks. */
    public static void main(String[] args) {
        testRookAttack();
        testBlockingPiece();
        testKnightAttack();
        testMissingKing();
    }

    private static void testRookAttack() {
        Board board = new Board();
        board.setPiece(new Position(0, 4), new King(Color.WHITE, new Position(0, 4)));
        board.setPiece(new Position(0, 0), new Rook(Color.BLACK, new Position(0, 0)));

        TestUtils.assertEquals(true, board.isCheck(Color.WHITE),
                "Black rook attacks white king");
        TestUtils.assertEquals(false, board.isCheck(Color.BLACK),
                "Black king is not in check");
    }

    private static void testBlockingPiece() {
        Board board = new Board();
        board.setPiece(new Position(0, 4), new King(Color.WHITE, new Position(0, 4)));
        board.setPiece(new Position(0, 0), new Rook(Color.BLACK, new Position(0, 0)));
        board.setPiece(new Position(0, 2), new Rook(Color.WHITE, new Position(0, 2)));

        TestUtils.assertEquals(false, board.isCheck(Color.WHITE),
                "A friendly piece blocks the rook attack");
    }

    private static void testKnightAttack() {
        Board board = new Board();
        board.setPiece(new Position(3, 3), new King(Color.WHITE, new Position(3, 3)));
        board.setPiece(new Position(1, 2), new Knight(Color.BLACK, new Position(1, 2)));

        TestUtils.assertEquals(true, board.isCheck(Color.WHITE),
                "Black knight attacks white king");
    }

    private static void testMissingKing() {
        Board board = new Board();
        board.setPiece(new Position(1, 2), new Knight(Color.BLACK, new Position(1, 2)));

        TestUtils.assertEquals(false, board.isCheck(Color.WHITE),
                "A side without a king is not reported in check");
        TestUtils.assertEquals(false, board.isCheck(null),
                "Null color is not reported in check");
    }
}
