
import pieces.PawnTest;
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

        PawnTest.main(args);

        TestUtils.printSummary();

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
