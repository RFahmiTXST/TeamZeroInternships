package test.gui;

import board.Board;
import board.Position;
import gui.CheckmateHandler;
import gui.GUIController;
import gui.GameFileService;
import gui.HistoryEntry;
import gui.board.BoardPanel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import pieces.Color;
import pieces.King;
import pieces.Queen;

/** Headless-compatible integration tests for Dev 4 Phase 3 M2 endgame flow. */
public final class Dev4M2EndgameTest {
    /** Assertion count. */
    private static int assertions;

    /** Prevent construction. */
    private Dev4M2EndgameTest() { }

    /**
     * Runs all Swing checks on the EDT without showing physical windows.
     * @param args unused command-line arguments
     * @throws Exception if a Swing test fails or the EDT is interrupted
     */
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(Dev4M2EndgameTest::runAll);
        System.out.println("Dev 4 Phase 3 M2 GUI: " + assertions + " assertions passed.");
    }

    /** Exercises mate, draw, board locking, reset and persistent history. */
    private static void runAll() {
        check("Checkmate! White wins by checkmate!".equals(
                CheckmateHandler.resultMessage(GUIController.GameOutcome.CHECKMATE, Color.WHITE)),
                "white winner message");
        check("Checkmate! Black wins by checkmate!".equals(
                CheckmateHandler.resultMessage(GUIController.GameOutcome.CHECKMATE, Color.BLACK)),
                "black winner message");
        check("Stalemate! Draw!".equals(
                CheckmateHandler.resultMessage(GUIController.GameOutcome.STALEMATE, null)),
                "stalemate message");
        boolean invalid = false;
        try {
            CheckmateHandler.resultMessage(GUIController.GameOutcome.ONGOING, null);
        } catch (IllegalArgumentException expected) {
            invalid = true;
        }
        check(invalid, "ongoing result is not an endgame");
        testScholarMateAndPersistence();
        testStalemateAndReset();
        testBlackWins();
    }

    /** A real seven-ply sequence must end in mate without any king capture. */
    private static void testScholarMateAndPersistence() {
        Board board = new Board();
        board.initialize();
        GUIController controller = new GUIController(board, new BoardPanel(board));
        int[] dialogs = {0};
        String[] shown = {null};
        new CheckmateHandler(null, controller, (parent, message) -> {
            dialogs[0]++;
            shown[0] = message;
        });
        check(!controller.isGameOver(), "starting game ongoing");
        play(controller, 1, 4, 3, 4); // E2-E4
        play(controller, 6, 4, 4, 4); // E7-E5
        play(controller, 0, 5, 3, 2); // F1-C4
        play(controller, 7, 1, 5, 2); // B8-C6
        play(controller, 0, 3, 4, 7); // D1-H5
        play(controller, 7, 6, 5, 5); // G8-F6
        check(dialogs[0] == 0, "no premature dialog before mating move");
        play(controller, 4, 7, 6, 5); // H5xF7#
        check(controller.isGameOver(), "game locked after checkmate");
        check(controller.getGameOutcome() == GUIController.GameOutcome.CHECKMATE,
                "checkmate outcome exposed");
        check(controller.getWinner() == Color.WHITE, "white wins Scholar's Mate");
        check(dialogs[0] == 1, "one endgame dialog requested");
        check("Checkmate! White wins by checkmate!".equals(shown[0]),
                "dialog announces correct winner");
        check(controller.getHistory().size() == 7, "all seven moves remain visible");
        HistoryEntry mate = controller.getHistory().get(6);
        check("F7#".equals(mate.to()), "mating move has # suffix");
        check("H5".equals(mate.from()), "mating move starts on H5");
        check(board.getPiece(new Position(7, 4)) instanceof King,
                "black king remains on E8");
        check(!controller.canUndo() && !controller.undoLastMove(),
                "Undo cannot reopen a finished game");
        check(!controller.movePiece(new Position(6, 0), new Position(5, 0)),
                "moves rejected after checkmate");
        controller.clickSquare(new Position(6, 0));
        check(controller.getSelected() == null, "selection disabled after checkmate");
        check(controller.getLegalMovesForSelected().isEmpty(), "no hints shown after checkmate");
        check(controller.getHistory().size() == 7 && dialogs[0] == 1,
                "rejected postgame gestures leave history and dialog count unchanged");

        Path file = null;
        try {
            file = Files.createTempFile("teamzero-phase3-m2-", ".chess");
            GameFileService.save(file, GameFileService.snapshot(controller));
            GameFileService.GameState loaded = GameFileService.load(file);
            check("F7#".equals(loaded.history().get(6).to()),
                    "Save/Load preserves checkmate notation");
            controller.startNewGame();
            check(!controller.isGameOver(), "New Game unlocks board");
            check(controller.canUndo() == false, "New Game clears Undo");
            controller.restoreSession(loaded.board(), loaded.turn(), loaded.history());
            check(controller.isGameOver(), "loading finished position keeps board locked");
            check(controller.getGameOutcome() == GUIController.GameOutcome.CHECKMATE,
                    "loaded position re-evaluated as checkmate");
            check(dialogs[0] == 1, "load does not announce a duplicate modal");
            check(controller.getHistory().size() == 7, "loaded history retained");
        } catch (IOException exception) {
            throw new AssertionError("Saving/loading checkmate result failed", exception);
        } finally {
            if (file != null) {
                try {
                    Files.deleteIfExists(file);
                } catch (IOException exception) {
                    throw new AssertionError("Could not delete temporary save", exception);
                }
            }
        }
        controller.startNewGame();
        check(!controller.isGameOver(), "New Game after load restores normal play");
        check(controller.movePiece(new Position(1, 4), new Position(3, 4)),
                "normal move works after reset");
    }

    /** A legal queen move can create stalemate without a winner or a # suffix. */
    private static void testStalemateAndReset() {
        Board b = new Board();
        b.setPiece(pos(7, 0), new King(Color.BLACK, pos(7, 0))); // A8
        b.setPiece(pos(5, 2), new King(Color.WHITE, pos(5, 2))); // C6
        b.setPiece(pos(6, 1), new Queen(Color.WHITE, pos(6, 1))); // B7
        GUIController c = new GUIController(b, new BoardPanel(b));
        int[] dialogs = {0};
        String[] message = {null};
        new CheckmateHandler(null, c, (parent, text) -> {
            dialogs[0]++;
            message[0] = text;
        });
        check(c.movePiece(pos(6, 1), pos(5, 1)), "queen moves from B7 to B6");
        check(b.isStalemate(Color.BLACK), "black is stalemated after Qb6");
        check(c.isGameOver(), "stalemate locks the board");
        check(c.getGameOutcome() == GUIController.GameOutcome.STALEMATE,
                "stalemate outcome exposed");
        check(c.getWinner() == null, "stalemate has no winner");
        check("Stalemate! Draw!".equals(message[0]) && dialogs[0] == 1,
                "draw dialog shown once");
        check("B6".equals(c.getHistory().get(0).to()),
                "stalemate notation contains no mate suffix");
        check(!c.movePiece(pos(7, 0), pos(6, 0)), "stalemated side cannot move");
        check(!c.canUndo(), "stalemate prevents Undo");
        c.startNewGame();
        check(!c.isGameOver(), "New Game clears stalemate");
    }

    /** Black mating White must report the opposite winner. */
    private static void testBlackWins() {
        Board b = new Board();
        b.setPiece(pos(0, 0), new King(Color.WHITE, pos(0, 0))); // A1
        b.setPiece(pos(2, 2), new King(Color.BLACK, pos(2, 2))); // C3
        b.setPiece(pos(2, 1), new Queen(Color.BLACK, pos(2, 1))); // B3
        GUIController c = new GUIController(b, new BoardPanel(b));
        c.setCurrentTurn(Color.BLACK);
        String[] result = {null};
        new CheckmateHandler(null, c, (parent, text) -> result[0] = text);
        check(c.movePiece(pos(2, 1), pos(1, 1)), "black queen plays B3-B2");
        check(c.getGameOutcome() == GUIController.GameOutcome.CHECKMATE,
                "white is checkmated");
        check(c.getWinner() == Color.BLACK, "black is recorded as winner");
        check("B2#".equals(c.getHistory().get(0).to()), "black mating move annotated");
        check("Checkmate! Black wins by checkmate!".equals(result[0]),
                "black endgame modal text");
    }

    /** Move coordinates. */
    private static Position pos(int row, int col) { return new Position(row, col); }

    /** Execute an expected legal move. */
    private static void play(GUIController c, int sr, int sc, int dr, int dc) {
        check(c.movePiece(pos(sr, sc), pos(dr, dc)),
                "legal move " + pos(sr, sc) + "-" + pos(dr, dc));
    }

    /** Assertions fail immediately for reliable automation exit status. */
    private static void check(boolean correct, String message) {
        if (!correct) throw new AssertionError("Dev4 M2 GUI: " + message);
        assertions++;
        System.out.println("PASS: Dev4 M2 GUI: " + message);
    }
}
