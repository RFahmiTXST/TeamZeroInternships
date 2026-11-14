package test.gui;

import board.Board;
import board.Position;
import gui.GameFileService;
import gui.GUIController;
import gui.HistoryEntry;
import gui.board.BoardPanel;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import test.TestUtils;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Headless GUI-session save/load regression tests for Dev 4 Phase 2 M3. */
public final class GameFileServiceTest {
    /** Prevents instantiation. */
    private GameFileServiceTest() { }

    /**
     * Runs file round-trip and controller restoration tests on the Swing EDT.
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println("Running Dev 4 Phase 2 M3 save/load tests...");
        try {
            SwingUtilities.invokeAndWait(GameFileServiceTest::checks);
        } catch (Exception exception) {
            throw new AssertionError("Save/load tests failed", exception);
        }
    }

    /** Exercises save/load, captured lists, history, moved flags and error handling. */
    private static void checks() {
        Path file = null;
        try {
            file = Files.createTempFile("teamzero-m3-", ".chess");
            Board board = new Board();
            board.initialize();
            GUIController controller = new GUIController(board, new BoardPanel(board));
            gui.board.CapturedPiecesPanel rack = new gui.board.CapturedPiecesPanel();
            controller.addHistoryListener(entries -> rack.refresh(controller.getBoard()));
            List<List<HistoryEntry>> updates = new ArrayList<>();
            controller.addHistoryListener(updates::add);

            // Make three unrestricted moves, including a capture.
            check(true, controller.movePiece(new Position(1, 4), new Position(3, 4)), "White pawn moves");
            check(true, controller.movePiece(new Position(6, 4), new Position(4, 4)), "Black pawn moves");
            check(true, controller.movePiece(new Position(3, 4), new Position(4, 4)), "White pawn captures");
            check(Color.BLACK, controller.getCurrentTurn(), "Turn is Black before saving");
            check(3, controller.getHistory().size(), "Three moves recorded");
            check(1, board.getCapturedPieces().size(), "One piece captured before saving");

            Piece moving = board.getPiece(new Position(4, 4));
            check(true, moving.hasMoved(), "Moving pawn marked moved before saving");
            Piece captured = board.getCapturedPieces().get(0);
            check(true, captured.hasMoved(), "Captured pawn moved flag retained before saving");
            List<HistoryEntry> expectedHistory = controller.getHistory();
            GameFileService.save(file, GameFileService.snapshot(controller));
            check(true, Files.size(file) > 0, "Save file created");

            GameFileService.GameState loaded = GameFileService.load(file);
            check(Color.BLACK, loaded.turn(), "Active turn restored from file");
            check(3, loaded.history().size(), "Full history restored from file");
            check(expectedHistory, loaded.history(), "All move details preserved");
            check(1, loaded.board().getCapturedPieces().size(), "Captured-piece list restored");
            check(Color.BLACK, loaded.board().getCapturedPieces().get(0).getColor(),
                    "Captured-piece color restored");
            check(true, loaded.board().getCapturedPieces().get(0).hasMoved(),
                    "Captured-piece moved flag restored");
            check(true, loaded.board().getPiece(new Position(4, 4)) instanceof Pawn,
                    "Final square has saved Pawn");
            check(null, loaded.board().getPiece(new Position(1, 4)),
                    "Original pawn square remains empty");
            check(true, loaded.board().getPiece(new Position(4, 4)).hasMoved(),
                    "Active piece moved flag restored");
            check(false, loaded.board().getPiece(new Position(0, 0)).hasMoved(),
                    "Unmoved rook remains unmoved");
            check(16, loaded.board().getPieces(Color.WHITE).size(),
                    "All sixteen active white pieces preserved");
            check(15, loaded.board().getPieces(Color.BLACK).size(),
                    "One black captured piece excluded from active list");

            // Replace the live game through the controller without firing capture events.
            controller.startNewGame();
            check(0, controller.getHistory().size(), "New Game clears history");
            check(0, controller.getBoard().getCapturedPieces().size(), "New Game clears captures");
            check(Color.WHITE, controller.getCurrentTurn(), "New Game resets turn");
            check(true, controller.movePiece(new Position(1, 0), new Position(3, 0)),
                    "Old session has a reversible move before Load");
            check(true, controller.canUndo(), "Old session undo record exists");
            int[] captureEvents = {0};
            controller.addCaptureListener((piece, position) -> captureEvents[0]++);
            controller.restoreSession(loaded.board(), loaded.turn(), loaded.history());
            check(Color.BLACK, controller.getCurrentTurn(), "Load restores live turn");
            check(expectedHistory, controller.getHistory(), "Load restores live history");
            check(3, updates.get(updates.size() - 1).size(), "History listener receives restored rows");
            check(1, controller.getBoard().getCapturedPieces().size(), "Load restores live captured list");
            check("Captured Black (1)",
                    ((javax.swing.JLabel) rack.getComponent(2)).getText(),
                    "Captured rack refreshes after Load");
            check(true, controller.getBoardPanel().getSquarePanel(new Position(4, 4))
                    .getPiece() instanceof Pawn, "Board panel displays loaded piece");
            check(0, captureEvents[0], "Loading does not trigger capture/endgame callbacks");
            check(false, controller.canUndo(), "Load clears prior undo records");
            check(false, controller.undoLastMove(), "Stale Undo cannot modify loaded game");
            check(expectedHistory, controller.getHistory(),
                    "Rejected stale Undo preserves loaded history");

            // Reject invalid files without mutating the active session.
            Files.writeString(file, "format=UNKNOWN\n", StandardCharsets.UTF_8);
            boolean rejected = false;
            try {
                GameFileService.load(file);
            } catch (IOException expected) {
                rejected = true;
            }
            check(true, rejected, "Malformed save file rejected");
            check(Color.BLACK, controller.getCurrentTurn(), "Invalid load preserves active turn");
            check(expectedHistory, controller.getHistory(), "Invalid load preserves history");
            check(true, controller.getBoardPanel().getSquarePanel(new Position(4, 4))
                    .getPiece() instanceof Pawn, "Invalid load preserves board");
        } catch (IOException exception) {
            throw new AssertionError("Unexpected file I/O failure", exception);
        } finally {
            if (file != null) {
                try { Files.deleteIfExists(file); } catch (IOException ignored) { }
            }
        }
    }

    /** Records one assertion in the existing shared test framework. */
    private static void check(Object expected, Object actual, String message) {
        TestUtils.assertEquals(expected, actual, message);
    }
}
