package pieces;

import board.Position;
import test.TestUtils;

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
    }
}
