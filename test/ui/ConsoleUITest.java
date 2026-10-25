package ui;

import board.Board;
import pieces.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import test.TestUtils;

/** Tests console output and raw input without interactive prompts. */
public final class ConsoleUITest {
    private ConsoleUITest() { }

    /** Captures output and always restores the console. */
    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream stream = new PrintStream(output)) {
            System.setOut(stream);
            action.run();
        } finally {
            System.setOut(original);
        }
        return output.toString();
    }

    /** Runs UI checks. @param args unused */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(" e2 e4 \nquit\n")) {
            ConsoleUI ui = new ConsoleUI(scanner);
            String welcome = capture(ui::showWelcome);
            TestUtils.assertEquals(true, welcome.contains("E2 E4") && welcome.contains("quit")
                    && welcome.contains("exit"), "Welcome explains input and exit");
            Board board = new Board() {
                @Override public String render() { return "board snapshot"; }
            };
            TestUtils.assertEquals("board snapshot" + System.lineSeparator(),
                    capture(() -> ui.showBoard(board)), "UI prints Board rendering");
            TestUtils.assertEquals("White's turn." + System.lineSeparator(),
                    capture(() -> ui.showTurn(Color.WHITE)), "White turn displayed");
            TestUtils.assertEquals("Black's turn." + System.lineSeparator(),
                    capture(() -> ui.showTurn(Color.BLACK)), "Black turn displayed");
            String[] input = new String[1];
            String prompt = capture(() -> input[0] = ui.promptMove(Color.WHITE));
            TestUtils.assertEquals(" e2 e4 ", input[0], "Raw move input preserved");
            TestUtils.assertEquals(true, prompt.contains("WHITE") && prompt.contains("E2 E4"), "Prompt identifies player and format");
            capture(() -> input[0] = ui.promptMove(Color.BLACK));
            TestUtils.assertEquals("quit", input[0], "Quit input preserved");
            capture(() -> input[0] = ui.promptMove(Color.WHITE));
            TestUtils.assertEquals(null, input[0], "End of input returns null");
            TestUtils.assertEquals("Game ended" + System.lineSeparator(),
                    capture(() -> ui.showResult("Game ended")), "Result displayed");
            TestUtils.assertEquals("Error: Invalid move" + System.lineSeparator(),
                    capture(() -> ui.showError("Invalid move")), "Error displayed");
        }
    }
}
