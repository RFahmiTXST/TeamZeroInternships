package core;

import board.Board;
import board.Position;
import pieces.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import test.TestUtils;
import ui.ConsoleUI;

/** Exercises the real game, input parser, board, and pieces using scripted input. */
public final class GameTest {
    /** Prevents utility construction. */
    private GameTest() { }

    /** Real console UI with observations for integration assertions. */
    private static final class ObservedUI extends ConsoleUI {
        /** Turns shown before each prompt. */
        private final List<Color> turns = new ArrayList<>();
        /** Rendered board at each prompt. */
        private final List<String> snapshots = new ArrayList<>();
        /** Board supplied by Game. */
        private Board board;
        /** Number of rejected inputs. */
        private int errors;
        /** Number of termination messages. */
        private int endings;

        /** Creates an observed console. @param scanner scripted input */
        private ObservedUI(Scanner scanner) { super(scanner); }

        @Override public void showBoard(Board value) {
            board = value;
            snapshots.add(value.render());
            super.showBoard(value);
        }

        @Override public void showTurn(Color color) {
            turns.add(color);
            super.showTurn(color);
        }

        @Override public void showError(String error) {
            errors++;
            super.showError(error);
        }

        @Override public void showResult(String result) {
            endings++;
            super.showResult(result);
        }
    }

    /** Runs a game while suppressing its transcript. @param game game to start */
    private static void run(Game game) {
        PrintStream original = System.out;
        try (PrintStream output = new PrintStream(new ByteArrayOutputStream())) {
            System.setOut(output);
            game.start();
        } finally {
            System.setOut(original);
        }
    }

    /** Runs turn, capture, reset, and termination tests. @param args unused */
    public static void main(String[] args) {
        String script = "hello\nE2 E5\nE7 E5\n  e2   e4  \nE7 E5\nG1 F3\n"
                + "B8 C6\nF1 B5\nA7 A6\nB5 C6\n ExIt \nquit\n";
        try (Scanner scanner = new Scanner(script)) {
            ObservedUI ui = new ObservedUI(scanner);
            Game game = new Game(ui);
            run(game);
            TestUtils.assertEquals(Arrays.asList(Color.WHITE, Color.WHITE, Color.WHITE,
                    Color.WHITE, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE,
                    Color.BLACK, Color.WHITE, Color.BLACK), ui.turns, "Only valid moves switch turns");
            TestUtils.assertEquals(3, ui.errors, "Format, illegal movement, and wrong owner rejected");
            TestUtils.assertEquals(true, ui.snapshots.subList(0, 4).stream()
                    .allMatch(ui.snapshots.get(0)::equals), "Rejected moves preserve starting board");
            TestUtils.assertEquals(null, ui.board.getPiece(new Position(1, 4)), "E2 cleared");
            TestUtils.assertEquals("wp", ui.board.getPiece(new Position(3, 4)).toString(), "White pawn reaches E4");
            TestUtils.assertEquals("bp", ui.board.getPiece(new Position(4, 4)).toString(), "Black pawn reaches E5");
            TestUtils.assertEquals("wB", ui.board.getPiece(new Position(5, 2)).toString(), "Bishop captures knight on C6");
            TestUtils.assertEquals(15, ui.board.getPieces(Color.BLACK).size(), "Capture removes one black piece");
            TestUtils.assertEquals(16, ui.board.getPieces(Color.WHITE).size(), "White piece count preserved");
            TestUtils.assertEquals(1, ui.endings, "Exit ends once");
            TestUtils.assertEquals(true, scanner.hasNextLine(), "Exit stops consuming input");
            run(game);
            TestUtils.assertEquals(Color.WHITE, ui.turns.get(ui.turns.size() - 1), "Restart begins with White");
            TestUtils.assertEquals(16, ui.board.getPieces(Color.BLACK).size(), "Restart restores captured pieces");
            TestUtils.assertEquals(false, ui.board.getPiece(new Position(1, 4)).hasMoved(), "Restart resets moved flags");
            TestUtils.assertEquals(2, ui.endings, "Quit ends restarted game once");
        }
        try (Scanner scanner = new Scanner("O-O\nE2 E4\nE7 E5\nG1 F3\nB8 C6\nF1 C4\nG8 F6\nO-O\nquit\n")) {
            ObservedUI ui = new ObservedUI(scanner);
            run(new Game(ui));
            TestUtils.assertEquals(1, ui.errors, "Blocked castling rejected without losing turn");
            TestUtils.assertEquals(Color.WHITE, ui.turns.get(1), "Rejected castle preserves White turn");
            TestUtils.assertEquals("wK", ui.board.getPiece(new Position(0, 6)).toString(), "Game castles king to G1");
            TestUtils.assertEquals("wR", ui.board.getPiece(new Position(0, 5)).toString(), "Game castles rook to F1");
            TestUtils.assertEquals(null, ui.board.getPiece(new Position(0, 4)), "Castling clears E1");
            TestUtils.assertEquals(Color.BLACK, ui.turns.get(ui.turns.size() - 1), "Successful castle switches turn");
        }
        for (String scriptEnd : new String[]{"", "\n", " QuIt \n", "E2 E4\n"}) {
            try (Scanner scanner = new Scanner(scriptEnd)) {
                ObservedUI ui = new ObservedUI(scanner);
                run(new Game(ui));
                TestUtils.assertEquals(1, ui.endings, "Quit or EOF ends cleanly: " + scriptEnd.replace('\n', '/'));
                TestUtils.assertEquals(scriptEnd.equals("\n") ? 1 : 0, ui.errors, "Blank input rejected; EOF is not an error");
            }
        }
    }
}
