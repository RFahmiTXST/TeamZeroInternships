package board;

import core.Move;
import core.Player;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import test.TestUtils;

/**
 * Integration tests for Board, Player, and Piece behavior.
 *
 * Developer: Dev 4
 */
public final class BoardIntegrationTest {

    /** Prevents construction of this test class. */
    private BoardIntegrationTest() { }

    /**
     * Executes board integration tests.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Running Board integration tests...\n");

        Board board = new Board();

        TestUtils.assertEquals(
                0, board.getPieces(Color.WHITE).size(),
                "New board is empty"
        );

        board.initialize();

        Position e1 = new Position(0, 4);
        Position e2 = new Position(1, 4);
        Position e4 = new Position(3, 4);
        Position e5 = new Position(4, 4);
        Position e7 = new Position(6, 4);
        Position e8 = new Position(7, 4);

        TestUtils.assertEquals(
                16, board.getPieces(Color.WHITE).size(),
                "White starts with 16 pieces"
        );

        TestUtils.assertEquals(
                16, board.getPieces(Color.BLACK).size(),
                "Black starts with 16 pieces"
        );

        TestUtils.assertEquals(
                "wK", board.getPiece(e1).toString(),
                "White King starts on E1"
        );

        TestUtils.assertEquals(
                "bK", board.getPiece(e8).toString(),
                "Black King starts on E8"
        );

        TestUtils.assertEquals(
                "wp", board.getPiece(e2).toString(),
                "White Pawn starts on E2"
        );

        TestUtils.assertEquals(
                "bp", board.getPiece(e7).toString(),
                "Black Pawn starts on E7"
        );

        TestUtils.assertEquals(
                true, board.render().contains("A   B   C   D"),
                "Rendered board contains file labels"
        );

        Piece whitePawn = board.getPiece(e2);
        Player whitePlayer = new Player(Color.WHITE);
        Player blackPlayer = new Player(Color.BLACK);

        TestUtils.assertEquals(
                false, whitePawn.hasMoved(),
                "Initial Pawn has not moved"
        );

        TestUtils.assertEquals(
                true, whitePlayer.makeMove(board, new Move(e2, e4)),
                "White Pawn moves from E2 to E4"
        );

        TestUtils.assertEquals(
                null, board.getPiece(e2),
                "E2 becomes empty after movement"
        );

        TestUtils.assertEquals(
                whitePawn, board.getPiece(e4),
                "E4 contains the original Pawn"
        );

        TestUtils.assertEquals(
                e4, whitePawn.getPosition(),
                "Pawn position updates to E4"
        );

        TestUtils.assertEquals(
                true, whitePawn.hasMoved(),
                "Pawn moved flag is updated"
        );

        String beforeInvalidMove = board.render();

        TestUtils.assertEquals(
                false,
                whitePlayer.makeMove(
                        board, new Move(e4, new Position(5, 4))
                ),
                "Moved Pawn cannot advance two squares"
        );

        TestUtils.assertEquals(
                beforeInvalidMove, board.render(),
                "Invalid move preserves board state"
        );

        TestUtils.assertEquals(
                true, blackPlayer.makeMove(board, new Move(e7, e5)),
                "Black Pawn moves from E7 to E5"
        );

        TestUtils.assertEquals(
                null, board.getPiece(e7),
                "E7 becomes empty"
        );

        TestUtils.assertEquals(
                "bp", board.getPiece(e5).toString(),
                "E5 contains the Black Pawn"
        );

        // Capture integration on a separate board.
        Board captureBoard = new Board();

        Position from = new Position(3, 4);
        Position to = new Position(4, 5);

        Piece attackingPawn = new Pawn(Color.WHITE, from);
        Piece capturedPawn = new Pawn(Color.BLACK, to);

        captureBoard.setPiece(from, attackingPawn);
        captureBoard.setPiece(to, capturedPawn);

        TestUtils.assertEquals(
                true,
                whitePlayer.makeMove(captureBoard, new Move(from, to)),
                "White Pawn captures diagonally"
        );

        TestUtils.assertEquals(
                null, captureBoard.getPiece(from),
                "Capture clears source square"
        );

        TestUtils.assertEquals(
                attackingPawn, captureBoard.getPiece(to),
                "Capturing Pawn occupies target square"
        );

        TestUtils.assertEquals(
                0, captureBoard.getPieces(Color.BLACK).size(),
                "Captured Black Pawn is no longer active"
        );

        // Verify game reset behavior.
        board.initialize();

        TestUtils.assertEquals(
                16, board.getPieces(Color.WHITE).size(),
                "Reinitialize restores White pieces"
        );

        TestUtils.assertEquals(
                16, board.getPieces(Color.BLACK).size(),
                "Reinitialize restores Black pieces"
        );

        TestUtils.assertEquals(
                null, board.getPiece(e4),
                "Reinitialize clears previous moves"
        );

        TestUtils.assertEquals(
                false, board.getPiece(e2).hasMoved(),
                "Reinitialize creates unmoved Pawns"
        );
    }
}
