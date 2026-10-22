
import utils.Utils;
import board.Position;
import core.Move;

/**
 * Tests the basic chess input utilities.
 * Uses standard Java without external testing libraries.
 *
 * Developer: Dev 3 (Sabid)
 */
public class UtilsTest {

    /** Counts the number of successful tests. */
    private static int passed = 0;

    /** Counts the number of failed tests. */
    private static int failed = 0;

    /**
     * Runs all utility tests and displays the results.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        testNormalize();
        testMoveFormat();
        testParsePosition();
        testParseMove();

        System.out.println("\nResults: " + passed
                + " passed, " + failed + " failed.");

        if (failed > 0) {
            throw new AssertionError(
                failed + " utility test(s) failed."
            );
        }
    }

    /**
     * Checks a condition and records its result.
     *
     * @param condition The condition to verify.
     * @param description Description of the test.
     */
    private static void check(
            boolean condition, String description) {

        if (condition) {
            passed++;
            System.out.println("PASS: " + description);
        } else {
            failed++;
            System.out.println("FAIL: " + description);
        }
    }

    /**
     * Verifies that an operation throws the expected exception.
     *
     * @param action Operation to execute.
     * @param description Description of the test.
     */
    private static void checkInvalid(
            Runnable action, String description) {

        try {
            action.run();
            check(false, description);
        } catch (IllegalArgumentException e) {
            check(true, description);
        }
    }

    /** Tests whitespace cleanup and uppercase conversion. */
    private static void testNormalize() {
        System.out.println("\nTesting normalize()");

        check("E2 E4".equals(Utils.normalize("e2 e4")),
                "Lowercase input");

        check("E2 E4".equals(Utils.normalize("  e2   e4  ")),
                "Extra whitespace");

        check("E2 E4".equals(Utils.normalize("e2\te4")),
                "Tab whitespace");

        check("".equals(Utils.normalize("")),
                "Empty input remains empty");

        checkInvalid(() -> Utils.normalize(null),
                "Null input throws exception");
    }

    /** Tests basic chess move format validation. */
    private static void testMoveFormat() {
        System.out.println("\nTesting isValidMoveFormat()");

        check(Utils.isValidMoveFormat("E2 E4"),
                "Valid move format");


        check(Utils.isValidMoveFormat("O-O"),
                "Kingside castling format accepted");

        check(Utils.isValidMoveFormat("O-O-O"),
                "Queenside castling format accepted");

        check(Utils.isValidMoveFormat("E7 E8=Q"),
                "Pawn promotion format accepted");

        check(!Utils.isValidMoveFormat("E7 E8=K"),
                "Invalid promotion piece rejected");

    }

    /** Tests conversion of coordinates into Position objects. */
    private static void testParsePosition() {
        System.out.println("\nTesting parsePosition()");

        Position a1 = Utils.parsePosition("A1");
        check(a1.getRow() == 0 && a1.getColumn() == 0,
                "A1 becomes row 0, column 0");

        Position e2 = Utils.parsePosition("E2");
        check(e2.getRow() == 1 && e2.getColumn() == 4,
                "E2 becomes row 1, column 4");

        Position h8 = Utils.parsePosition("H8");
        check(h8.getRow() == 7 && h8.getColumn() == 7,
                "H8 becomes row 7, column 7");

        checkInvalid(() -> Utils.parsePosition("E9"),
                "Invalid rank throws exception");

        checkInvalid(() -> Utils.parsePosition("e2"),
                "Lowercase position throws exception");

        checkInvalid(() -> Utils.parsePosition(null),
                "Null position throws exception");
    }

    /** Tests conversion of move strings into Move objects. */
    private static void testParseMove() {
        System.out.println("\nTesting parseMove()");
        Move kingside = Utils.parseMove("O-O");

        check(kingside.isCastling()
                && kingside.isKingsideCastle(),
                "Kingside castling parsed correctly");

        Move queenside = Utils.parseMove("O-O-O");

        check(queenside.isCastling()
                && !queenside.isKingsideCastle(),
                "Queenside castling parsed correctly");

        Move promotion = Utils.parseMove("e7 e8=q");

        check(promotion.getPromotionPiece() == 'Q'
                && promotion.getFrom().equals(new Position(6, 4))
                && promotion.getTo().equals(new Position(7, 4)),
                "Pawn promotion parsed correctly");

        checkInvalid(() -> Utils.parseMove("E7 E8=K"),
                "Invalid promotion notation rejected");
    }
}
