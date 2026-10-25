
package pieces;

import board.Position;
import test.TestUtils;

/**
 * Tests Pawn promotion piece creation.
 *
 * Developer: Dev 4
 */
public final class PawnPromotionTest {

    private PawnPromotionTest() { }

    /**
     * Checks that invalid promotions are rejected.
     *
     * @param pawn pawn being tested
     * @param code requested promotion piece
     * @param expected expected exception type
     * @return true when the expected exception occurs
     */
    private static boolean rejects(
            Pawn pawn,
            char code,
            Class<? extends RuntimeException> expected) {

        try {
            pawn.createPromotionPiece(code);
            return false;
        } catch (RuntimeException exception) {
            return expected.isInstance(exception);
        }
    }

    /**
     * Runs promotion tests.
     *
     * @param args unused
     */
    public static void main(String[] args) {

        System.out.println("Running Pawn promotion tests...\n");

        Pawn white = new Pawn(
                Color.WHITE, new Position(7, 4));

        Pawn black = new Pawn(
                Color.BLACK, new Position(0, 4));

        char[] codes = {'Q', 'R', 'B', 'N'};

        Class<?>[] types = {
            Queen.class,
            Rook.class,
            Bishop.class,
            Knight.class
        };

        for (int i = 0; i < codes.length; i++) {

            Piece whitePiece =
                    white.createPromotionPiece(codes[i]);

            Piece blackPiece =
                    black.createPromotionPiece(codes[i]);

            TestUtils.assertEquals(
                    true,
                    types[i].isInstance(whitePiece),
                    "White promotes to " + codes[i]
            );

            TestUtils.assertEquals(
                    "w" + codes[i],
                    whitePiece.toString(),
                    "White promotion symbol " + codes[i]
            );

            TestUtils.assertEquals(
                    true,
                    types[i].isInstance(blackPiece),
                    "Black promotes to " + codes[i]
            );

            TestUtils.assertEquals(
                    "b" + codes[i],
                    blackPiece.toString(),
                    "Black promotion symbol " + codes[i]
            );
        }

        Piece promoted = white.createPromotionPiece('q');

        TestUtils.assertEquals(
                "wQ",
                promoted.toString(),
                "Lowercase promotion code accepted"
        );

        TestUtils.assertEquals(
                Color.WHITE,
                promoted.getColor(),
                "Promotion preserves color"
        );

        TestUtils.assertEquals(
                new Position(7, 4),
                promoted.getPosition(),
                "Promotion preserves position"
        );

        TestUtils.assertEquals(
                "wp",
                white.toString(),
                "Original Pawn remains unchanged"
        );

        TestUtils.assertEquals(
                true,
                rejects(white, 'K', IllegalArgumentException.class),
                "Cannot promote to King"
        );

        TestUtils.assertEquals(
                true,
                rejects(
                    new Pawn(Color.WHITE, new Position(6, 4)),
                    'Q',
                    IllegalStateException.class
                ),
                "White cannot promote before rank 8"
        );

        TestUtils.assertEquals(
                true,
                rejects(
                    new Pawn(Color.BLACK, new Position(1, 4)),
                    'Q',
                    IllegalStateException.class
                ),
                "Black cannot promote before rank 1"
        );
    }
}
