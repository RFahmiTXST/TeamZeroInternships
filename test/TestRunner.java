import pieces.PawnTest;
import test.TestUtils;

/** Runs all chess test classes and reports the overall result. */
public final class TestRunner {

    /** Prevents creation of a runner instance. */
    private TestRunner() { }

    /**
     * Executes the project's tests.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        PawnTest.main(args);

        TestUtils.printSummary();

        if (TestUtils.getFailedCount() > 0) {
            System.exit(1);
        }
    }
}
