package pieces;

import board.Position;
import test.TestUtils;
import board.Board;
import java.util.List;
import java.util.Set;

/** Tests the movement-related helpers of the Pawn class. */
public final class PawnTest {

    /** Prevents creation of a test instance. */
    private PawnTest() { }

    /**
     * Runs the Pawn helper tests.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        Pawn white = new Pawn(Color.WHITE, new Position(1, 4));
        Pawn black = new Pawn(Color.BLACK, new Position(6, 4));

        TestUtils.assertEquals(1, white.getForwardDirection(),
                "White pawn moves toward higher rows");

        TestUtils.assertEquals(-1, black.getForwardDirection(),
                "Black pawn moves toward lower rows");

        TestUtils.assertEquals(1, white.getStartRow(),
                "White pawn starts on row 1");

        TestUtils.assertEquals(6, black.getStartRow(),
                "Black pawn starts on row 6");

        TestUtils.assertEquals(7, white.getPromotionRow(),
                "White pawn promotes on row 7");

        TestUtils.assertEquals(0, black.getPromotionRow(),
                "Black pawn promotes on row 0");

        runMovementTests();
    }


        /**
         * Checks whether a pawn generates exactly the expected moves.
         *
         * @param label description of the test
         * @param pawn pawn being tested
         * @param board board containing the pieces
         * @param expected expected destination squares
         */
        private static void assertMoves(
                String label, Pawn pawn, Board board,
                Position... expected) {

        List<Position> actual = pawn.possibleMoves(board);

        TestUtils.assertEquals(
                expected.length,
                actual.size(),
                label + " - move count");

        TestUtils.assertEquals(
                Set.of(expected),
                Set.copyOf(actual),
                label + " - destinations");
        }

        /** Tests basic pawn movement, blocking, and captures. */
        private static void runMovementTests() {

        // TEST 1: White pawn moves one or two squares
        Board board1 = new Board();
        Pawn white1 = new Pawn(Color.WHITE, new Position(1, 4));
        board1.setPiece(white1.getPosition(), white1);

        assertMoves("White pawn initial movement",
                white1, board1,
                new Position(2, 4),
                new Position(3, 4));

        // TEST 2: Black pawn moves one or two squares
        Board board2 = new Board();
        Pawn black1 = new Pawn(Color.BLACK, new Position(6, 4));
        board2.setPiece(black1.getPosition(), black1);

        assertMoves("Black pawn initial movement",
                black1, board2,
                new Position(5, 4),
                new Position(4, 4));

        // TEST 3: Friendly piece blocks forward movement
        Board board3 = new Board();
        Pawn white2 = new Pawn(Color.WHITE, new Position(1, 4));
        board3.setPiece(white2.getPosition(), white2);
        board3.setPiece(new Position(2, 4),
                new Pawn(Color.WHITE, new Position(2, 4)));

        assertMoves("Pawn blocked directly ahead",
                white2, board3);

        // TEST 4: Second square is occupied
        Board board4 = new Board();
        Pawn white3 = new Pawn(Color.WHITE, new Position(1, 4));
        board4.setPiece(white3.getPosition(), white3);
        board4.setPiece(new Position(3, 4),
                new Pawn(Color.BLACK, new Position(3, 4)));

        assertMoves("Pawn double move blocked",
                white3, board4,
                new Position(2, 4));

        // TEST 5: Enemy pieces can be captured diagonally
        Board board5 = new Board();
        Pawn white4 = new Pawn(Color.WHITE, new Position(1, 4));
        board5.setPiece(white4.getPosition(), white4);

        board5.setPiece(new Position(2, 3),
                new Pawn(Color.BLACK, new Position(2, 3)));

        board5.setPiece(new Position(2, 5),
                new Pawn(Color.BLACK, new Position(2, 5)));

        assertMoves("Pawn diagonal captures",
                white4, board5,
                new Position(2, 4),
                new Position(3, 4),
                new Position(2, 3),
                new Position(2, 5));

        // TEST 6: Friendly pieces cannot be captured
        Board board6 = new Board();
        Pawn white5 = new Pawn(Color.WHITE, new Position(1, 4));
        board6.setPiece(white5.getPosition(), white5);

        board6.setPiece(new Position(2, 3),
                new Pawn(Color.WHITE, new Position(2, 3)));

        board6.setPiece(new Position(2, 5),
                new Pawn(Color.WHITE, new Position(2, 5)));

        assertMoves("Pawn cannot capture friendly pieces",
                white5, board6,
                new Position(2, 4),
                new Position(3, 4));

        // TEST 7: White pawn away from starting row
        Board board7 = new Board();
        Pawn white6 = new Pawn(Color.WHITE, new Position(3, 4));
        board7.setPiece(white6.getPosition(), white6);

        assertMoves("White pawn away from starting row",
                white6, board7,
                new Position(4, 4));

        // TEST 8: Black pawn away from starting row
        Board board8 = new Board();
        Pawn black2 = new Pawn(Color.BLACK, new Position(4, 4));
        board8.setPiece(black2.getPosition(), black2);

        assertMoves("Black pawn away from starting row",
                black2, board8,
                new Position(3, 4));

        // TEST 9: Enemy directly ahead cannot be captured
        Board board9 = new Board();
        Pawn white7 = new Pawn(Color.WHITE, new Position(1, 4));
        board9.setPiece(white7.getPosition(), white7);

        board9.setPiece(new Position(2, 4),
                new Pawn(Color.BLACK, new Position(2, 4)));

        board9.setPiece(new Position(2, 3),
                new Pawn(Color.BLACK, new Position(2, 3)));

        assertMoves("Enemy blocks forward but diagonal capture works",
                white7, board9,
                new Position(2, 3));

        // TEST 10: White pawn on left edge (A-file)
        Board board10 = new Board();
        Pawn white8 = new Pawn(Color.WHITE, new Position(1, 0));
        board10.setPiece(white8.getPosition(), white8);

        board10.setPiece(new Position(2, 1),
                new Pawn(Color.BLACK, new Position(2, 1)));

        assertMoves("White pawn on left edge",
                white8, board10,
                new Position(2, 0),
                new Position(3, 0),
                new Position(2, 1));

        // TEST 11: Black pawn on right edge (H-file)
        Board board11 = new Board();
        Pawn black3 = new Pawn(Color.BLACK, new Position(6, 7));
        board11.setPiece(black3.getPosition(), black3);

        board11.setPiece(new Position(5, 6),
        new Pawn(Color.WHITE, new Position(5, 6)));

        assertMoves("Black pawn on right edge",
        black3, board11,
        new Position(5, 7),
        new Position(4, 7),
        new Position(5, 6));

        // TEST 12: Pawns cannot move beyond promotion rank
        Board board12 = new Board();
        Pawn white9 = new Pawn(Color.WHITE, new Position(7, 4));
        board12.setPiece(white9.getPosition(), white9);

        assertMoves("White pawn at final rank",
        white9, board12);

        Board board13 = new Board();
        Pawn black4 = new Pawn(Color.BLACK, new Position(0, 4));
        board13.setPiece(black4.getPosition(), black4);

        assertMoves("Black pawn at final rank",
        black4, board13);

        // TEST 13: A pawn marked as moved cannot advance two squares
        // Keep it on the starting row to isolate the hasMoved() condition.
        Board board14 = new Board();
        Pawn movedPawn = new Pawn(Color.WHITE, new Position(1, 4));

        // Simulate the moved state without changing the test position.
        movedPawn.move(new Position(1, 4));

        board14.setPiece(movedPawn.getPosition(), movedPawn);

        assertMoves("Previously moved pawn cannot double move",
        movedPawn, board14,
        new Position(2, 4));
        }
}
