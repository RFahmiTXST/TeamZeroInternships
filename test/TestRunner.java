import pieces.PawnTest;
import pieces.KingTest;
import test.TestUtils;
import java.util.Objects;

/**
 * Runs automated tests for the chess project.
 *
 * Developer: Dev 4
 */
public class TestRunner {

    /**
     * Runs all registered test suites.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Running Chess Project Tests...\n");

        // Dev 4: Pawn and King movement tests
        System.out.println("Running Pawn and King tests...\n");
        PawnTest.main(args);
        KingTest.main(args);
        pieces.PawnPromotionTest.main(args);
        System.out.println("\nRunning special move integration tests...\n");
        core.SpecialMoveHandlerTest.main(args);

        // Dev 1: Rook, Bishop, Queen and Knight tests
        System.out.println("\nRunning other piece movement tests...\n");
        pieces.PieceMovementTest.main(args);

        // Dev 4: Board, Player, and Piece integration tests
        board.BoardIntegrationTest.main(args);

        // Dev 3: Player and console UI tests
        System.out.println("\nRunning Player and UI tests...\n");
        core.PlayerTest.main(args);
        ui.ConsoleUITest.main(args);

        // Summary for shared TestUtils assertions
        System.out.println("\nShared test summary:");
        TestUtils.printSummary();

        // Dev 3: Input validation and parsing tests
        // UtilsTest manages its own assertion counters.
        System.out.println("\nRunning Utils tests...\n");
        UtilsTest.main(args);

        // Ensure failures produce a non-zero exit code.
        if (TestUtils.getFailedCount() > 0) {
            System.exit(1);
        }
    }

    /**
     * Compares expected and actual values.
     * Retained for compatibility with existing tests.
     *
     * @param expected expected result
     * @param actual actual result
     * @param message assertion description
     */
    public static void assertEquals(
            Object expected, Object actual, String message) {

        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(
                    message + " - Expected: " + expected
                    + ", Actual: " + actual
            );
        }
    }
}