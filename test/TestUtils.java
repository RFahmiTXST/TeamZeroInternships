
package test;

import java.util.Objects;

/** Shared assertion methods and test statistics for the chess project. */
public final class TestUtils {

    /** Number of assertions that passed. */
    private static int passed;

    /** Number of assertions that failed. */
    private static int failed;

    /** Prevents creation of a utility object. */
    private TestUtils() { }

    /**
     * Compares two values and records whether an assertion passes.
     *
     * @param expected the expected value
     * @param actual the observed value
     * @param message a description of the assertion
     */
    public static void assertEquals(Object expected, Object actual, String message) {
        if (Objects.equals(expected, actual)) {
            passed++;
            System.out.println("PASS: " + message);
        } else {
            failed++;
            System.err.println("FAIL: " + message
                    + " (expected " + expected + ", got " + actual + ")");
        }
    }

    /** Prints the number of passing and failing assertions. */
    public static void printSummary() {
        System.out.println("\nResults: " + passed + " passed, "
                + failed + " failed.");
    }

    /**
     * Returns the number of failed assertions.
     *
     * @return failure count
     */
    public static int getFailedCount() {
        return failed;
    }
}
