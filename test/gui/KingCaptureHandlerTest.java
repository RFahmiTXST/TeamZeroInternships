package test.gui;

import board.Board;
import board.Position;
import gui.KingCaptureHandler;
import pieces.Color;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import test.TestUtils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Headless regression tests for Phase 2 King-capture announcements and exit.
 * These checks never open a dialog or terminate the test process.
 *
 * @author Dev 4 (Towsif)
 */
public final class KingCaptureHandlerTest {

    /** Prevents instantiation of test utility. */
    private KingCaptureHandlerTest() { }

    /**
     * Runs the Swing-related checks on the event dispatch thread.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Running Dev 4 Phase 2 King-capture tests...");
        try {
            SwingUtilities.invokeAndWait(() -> {
                testIdentificationAndWinnerMessage();
                testOrdinaryCaptureDoesNotEndGame();
                testKingCaptureDialogAndExit();
                testBoardMoveIntegration();
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("King-capture tests interrupted", e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("King-capture tests failed", e.getCause());
        }
    }

    /** Tests detection and winning-side identification for both King colors. */
    private static void testIdentificationAndWinnerMessage() {
        King black = new King(Color.BLACK, new Position(7, 4));
        King white = new King(Color.WHITE, new Position(0, 4));
        Pawn pawn = new Pawn(Color.BLACK, new Position(6, 4));
        TestUtils.assertEquals(true, KingCaptureHandler.isKingCapture(black), "Black King recognized");
        TestUtils.assertEquals(true, KingCaptureHandler.isKingCapture(white), "White King recognized");
        TestUtils.assertEquals(false, KingCaptureHandler.isKingCapture(pawn), "Pawn is not a King");
        TestUtils.assertEquals(false, KingCaptureHandler.isKingCapture(null), "Null is not a King");
        TestUtils.assertEquals("Game Over! White captured Black's King and wins!",
                KingCaptureHandler.winnerMessage(black), "White wins after Black King capture");
        TestUtils.assertEquals("Game Over! Black captured White's King and wins!",
                KingCaptureHandler.winnerMessage(white), "Black wins after White King capture");
        boolean rejected = false;
        try {
            KingCaptureHandler.winnerMessage(pawn);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        TestUtils.assertEquals(true, rejected, "Non-King winner request is rejected");
    }

    /** Tests that ordinary captures and empty destinations do not end the game. */
    private static void testOrdinaryCaptureDoesNotEndGame() {
        AtomicInteger dialogs = new AtomicInteger();
        AtomicInteger exits = new AtomicInteger();
        KingCaptureHandler handler = new KingCaptureHandler(null,
                (parent, message, dismiss) -> dialogs.incrementAndGet(), exits::incrementAndGet);
        handler.onPieceCaptured(null, new Position(3, 3));
        handler.onPieceCaptured(new Pawn(Color.WHITE, new Position(1, 1)), new Position(1, 1));
        TestUtils.assertEquals(false, handler.hasEnded(), "Ordinary capture keeps game running");
        TestUtils.assertEquals(0, dialogs.get(), "No dialog for ordinary capture");
        TestUtils.assertEquals(0, exits.get(), "No exit for ordinary capture");
    }

    /** Tests one dialog, accurate message, and an exit only upon dismissal. */
    private static void testKingCaptureDialogAndExit() {
        AtomicInteger dialogs = new AtomicInteger();
        AtomicInteger exits = new AtomicInteger();
        String[] lastMessage = new String[1];
        Runnable[] dismissAction = new Runnable[1];
        KingCaptureHandler handler = new KingCaptureHandler(null,
                (parent, message, dismiss) -> {
                    dialogs.incrementAndGet();
                    lastMessage[0] = message;
                    dismissAction[0] = dismiss;
                }, exits::incrementAndGet);
        Piece blackKing = new King(Color.BLACK, new Position(7, 4));
        handler.onPieceCaptured(blackKing, new Position(7, 4));
        TestUtils.assertEquals(true, handler.hasEnded(), "King capture ends game state");
        TestUtils.assertEquals(1, dialogs.get(), "Exactly one modal announcement requested");
        TestUtils.assertEquals("Game Over! White captured Black's King and wins!",
                lastMessage[0], "Dialog shows correct winner");
        TestUtils.assertEquals(0, exits.get(), "Exit waits for dialog dismissal");
        TestUtils.assertEquals(true, dismissAction[0] != null, "Dismissal callback provided");
        handler.onPieceCaptured(blackKing, new Position(7, 4));
        TestUtils.assertEquals(1, dialogs.get(), "Duplicate capture cannot show second dialog");
        dismissAction[0].run();
        TestUtils.assertEquals(1, exits.get(), "Application exit requested after dismissal");
    }

    /** Runs a real Board capture and passes its returned piece to the handler. */
    private static void testBoardMoveIntegration() {
        Board board = new Board();
        Position from = new Position(0, 3);
        Position to = new Position(7, 4);
        Queen queen = new Queen(Color.WHITE, from);
        King king = new King(Color.BLACK, to);
        board.setPiece(from, queen);
        board.setPiece(to, king);
        Piece captured = board.movePiece(from, to);
        TestUtils.assertEquals(king, captured, "Board returns the captured King");
        TestUtils.assertEquals(null, board.getPiece(from), "Captured move clears source square");
        TestUtils.assertEquals(queen, board.getPiece(to), "Winner occupies captured square");
        AtomicInteger dialogs = new AtomicInteger();
        KingCaptureHandler handler = new KingCaptureHandler(null,
                (parent, message, dismiss) -> dialogs.incrementAndGet(), () -> { });
        handler.onPieceCaptured(captured, to);
        TestUtils.assertEquals(true, handler.hasEnded(), "Capture event reaches endgame handler");
        TestUtils.assertEquals(1, dialogs.get(), "Endgame announcement triggered from Board capture");
    }
}
