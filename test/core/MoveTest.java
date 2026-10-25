package core;

import board.Position;
import test.TestUtils;
import utils.Utils;

/** Regression coverage for move notation used by logs and console output. */
public final class MoveTest {
    private MoveTest() { }

    /** Runs notation checks.
     * @param args unused
     */
    public static void main(String[] args) {
        for (String notation : new String[]{"E2 E4", "A7 A8=Q", "B7 A8=R",
                "H2 H1=B", "G2 H1=N", "O-O", "O-O-O"}) {
            TestUtils.assertEquals(notation, Utils.parseMove(notation).toString(),
                    "Move notation round trip: " + notation);
        }
        TestUtils.assertEquals("E7 E8=Q", new Move(new Position(6, 4),
                new Position(7, 4), 'q').toString(), "Promotion symbol normalized");
    }
}
