
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
     * Runs all registered test classes.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Running Chess Project Tests...\n");


        // Developer 4: Pawn and King movement tests
        System.out.println("Running Pawn and King tests...\n");

        PawnTest.main(args);
        KingTest.main(args);

        System.out.println("\nPawn and King test summary:");
        TestUtils.printSummary();

        // Developer 3: Input validation and parsing tests
        System.out.println("\nRunning Utils tests...\n");

        UtilsTest.main(args);

        // Report failure to the operating system.
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
