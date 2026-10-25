package pieces;

import board.Board;
import board.Position;
import java.util.List;
import java.util.Set;
import test.TestUtils;

/**
 * Tests King movement, captures, and board boundaries.
 *
 * Developer: Dev 4
 */
public final class KingTest {

    /** Prevents creation of a test instance. */
    private KingTest() { }

    /**
     * Verifies the number and destinations of King moves.
     *
     * @param label description of the test
     * @param king King being tested
     * @param board current board
     * @param expected expected destinations
     */
    private static void assertMoves(
            String label, King king, Board board,
            Position... expected) {

        List<Position> actual = king.possibleMoves(board);

        TestUtils.assertEquals(
                expected.length,
                actual.size(),
                label + " - move count");

        TestUtils.assertEquals(
                Set.of(expected),
                Set.copyOf(actual),
                label + " - destinations");
    }

    /**
     * Runs all King movement tests.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {

        // TEST 1: King at D4 on an empty board
        Board board1 = new Board();
        King king1 = new King(
                Color.WHITE, new Position(3, 3));

        board1.setPiece(king1.getPosition(), king1);

        assertMoves("King at center",
                king1, board1,
                new Position(2, 2),
                new Position(2, 3),
                new Position(2, 4),
                new Position(3, 2),
                new Position(3, 4),
                new Position(4, 2),
                new Position(4, 3),
                new Position(4, 4));

        // TEST 2: King at A1
        Board board2 = new Board();
        King king2 = new King(
                Color.WHITE, new Position(0, 0));

        board2.setPiece(king2.getPosition(), king2);

        assertMoves("King at bottom-left corner",
                king2, board2,
                new Position(0, 1),
                new Position(1, 0),
                new Position(1, 1));

        // TEST 3: King at H8
        Board board3 = new Board();
        King king3 = new King(
                Color.BLACK, new Position(7, 7));

        board3.setPiece(king3.getPosition(), king3);

        assertMoves("King at top-right corner",
                king3, board3,
                new Position(6, 6),
                new Position(6, 7),
                new Position(7, 6));

        // TEST 4: All adjacent squares have friendly pieces
        Board board4 = new Board();
        King king4 = new King(
                Color.WHITE, new Position(3, 3));

        board4.setPiece(king4.getPosition(), king4);

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dr == 0 && dc == 0) {
                    continue;
                }

                Position pos = new Position(3 + dr, 3 + dc);
                board4.setPiece(pos,
                        new Pawn(Color.WHITE, pos));
            }
        }

        assertMoves("King surrounded by friendly pieces",
                king4, board4);

        // TEST 5: King may capture enemy pieces
        Board board5 = new Board();
        King king5 = new King(
                Color.WHITE, new Position(3, 3));

        board5.setPiece(king5.getPosition(), king5);

        Position enemyPos = new Position(4, 4);
        board5.setPiece(enemyPos,
                new Pawn(Color.BLACK, enemyPos));

        assertMoves("King can capture opponent",
                king5, board5,
                new Position(2, 2),
                new Position(2, 3),
                new Position(2, 4),
                new Position(3, 2),
                new Position(3, 4),
                new Position(4, 2),
                new Position(4, 3),
                new Position(4, 4));

        // TEST 6: Mixed friendly and enemy pieces
        Board board6 = new Board();
        King king6 = new King(
                Color.WHITE, new Position(3, 3));

        board6.setPiece(king6.getPosition(), king6);

        Position friend1 = new Position(4, 3);
        Position friend2 = new Position(3, 2);
        Position enemy1 = new Position(4, 2);
        Position enemy2 = new Position(2, 4);

        board6.setPiece(friend1,
                new Pawn(Color.WHITE, friend1));
        board6.setPiece(friend2,
                new Pawn(Color.WHITE, friend2));
        board6.setPiece(enemy1,
                new Pawn(Color.BLACK, enemy1));
        board6.setPiece(enemy2,
                new Pawn(Color.BLACK, enemy2));

        assertMoves("King with mixed neighboring pieces",
                king6, board6,
                new Position(2, 2),
                new Position(2, 3),
                new Position(2, 4),
                new Position(3, 4),
                new Position(4, 2),
                new Position(4, 4));

        // TEST 7: Black King captures white but not black
        Board board7 = new Board();
        King king7 = new King(
                Color.BLACK, new Position(4, 4));

        board7.setPiece(king7.getPosition(), king7);

        Position blackFriend = new Position(5, 4);
        Position whiteEnemy = new Position(4, 5);

        board7.setPiece(blackFriend,
                new Pawn(Color.BLACK, blackFriend));
        board7.setPiece(whiteEnemy,
                new Pawn(Color.WHITE, whiteEnemy));

        assertMoves("Black King capture and blocking",
                king7, board7,
                new Position(3, 3),
                new Position(3, 4),
                new Position(3, 5),
                new Position(4, 3),
                new Position(4, 5),
                new Position(5, 3),
                new Position(5, 5));

        // TEST 8: King symbol
        TestUtils.assertEquals(
                'K', king1.getSymbol(),
                "King symbol is K");
    }
}
