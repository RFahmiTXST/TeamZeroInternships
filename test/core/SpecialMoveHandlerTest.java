
package core;

import board.Board;
import board.Position;
import pieces.Bishop;
import pieces.Color;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;
import test.TestUtils;

/**
 * Tests special move execution.
 *
 * Developer: Dev 4
 */
public final class SpecialMoveHandlerTest {

    private SpecialMoveHandlerTest() { }

    /** Runs Pawn promotion integration tests. */
    public static void main(String[] args) {

        System.out.println(
                "Running Pawn promotion integration tests...\n"
        );

        // White promotes to Queen.
        Board whiteBoard = new Board();
        Position e7 = new Position(6, 4);
        Position e8 = new Position(7, 4);

        whiteBoard.setPiece(
                e7, new Pawn(Color.WHITE, e7)
        );

        TestUtils.assertEquals(
                true,
                SpecialMoveHandler.executePromotion(
                        whiteBoard, new Move(e7, e8, 'Q'),
                        Color.WHITE
                ),
                "White promotion accepted"
        );

        TestUtils.assertEquals(
                null, whiteBoard.getPiece(e7),
                "Promotion clears source square"
        );

        TestUtils.assertEquals(
                true, whiteBoard.getPiece(e8) instanceof Queen,
                "Queen replaces White Pawn"
        );

        TestUtils.assertEquals(
                "wQ", whiteBoard.getPiece(e8).toString(),
                "Promoted Queen has correct color"
        );

        // Black promotes to Knight.
        Board blackBoard = new Board();
        Position a2 = new Position(1, 0);
        Position a1 = new Position(0, 0);

        blackBoard.setPiece(
                a2, new Pawn(Color.BLACK, a2)
        );

        TestUtils.assertEquals(
                true,
                SpecialMoveHandler.executePromotion(
                        blackBoard, new Move(a2, a1, 'N'),
                        Color.BLACK
                ),
                "Black promotion accepted"
        );

        TestUtils.assertEquals(
                true, blackBoard.getPiece(a1) instanceof Knight,
                "Knight replaces Black Pawn"
        );

        // White promotion with diagonal capture.
        Board captureBoard = new Board();
        Position d7 = new Position(6, 3);

        captureBoard.setPiece(
                d7, new Pawn(Color.WHITE, d7)
        );
        captureBoard.setPiece(
                e8, new Rook(Color.BLACK, e8)
        );

        TestUtils.assertEquals(
                true,
                SpecialMoveHandler.executePromotion(
                        captureBoard, new Move(d7, e8, 'B'),
                        Color.WHITE
                ),
                "Promotion capture accepted"
        );

        TestUtils.assertEquals(
                true, captureBoard.getPiece(e8) instanceof Bishop,
                "Bishop replaces Pawn after capture"
        );

        TestUtils.assertEquals(
                0, captureBoard.getPieces(Color.BLACK).size(),
                "Captured piece removed from active board"
        );

        // Invalid requests must preserve the board.
        Board invalidBoard = new Board();
        invalidBoard.setPiece(
                e7, new Pawn(Color.WHITE, e7)
        );
        String initialBoard = invalidBoard.render();

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executePromotion(
                        invalidBoard, new Move(e7, e8, 'K'),
                        Color.WHITE
                ),
                "Cannot promote to King"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executePromotion(
                        invalidBoard, new Move(e7, e8, 'Q'),
                        Color.BLACK
                ),
                "Wrong player cannot promote Pawn"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executePromotion(
                        invalidBoard, new Move(e7, e8),
                        Color.WHITE
                ),
                "Missing promotion code rejected"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executePromotion(
                        invalidBoard,
                        new Move(e7, new Position(7, 6), 'Q'),
                        Color.WHITE
                ),
                "Illegal promotion movement rejected"
        );

        TestUtils.assertEquals(
                initialBoard, invalidBoard.render(),
                "Invalid promotions preserve board state"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executePromotion(
                        invalidBoard, null, Color.WHITE
                ),
                "Null promotion move rejected"
        );

        // ============================================
        // Player-level Pawn promotion integration
        // ============================================

        // White Pawn promotion through Player.makeMove().
        Board playerBoard = new Board();

        Position g7 = new Position(6, 6);
        Position g8 = new Position(7, 6);

        playerBoard.setPiece(
                g7, new Pawn(Color.WHITE, g7)
        );

        Player whitePlayer = new Player(Color.WHITE);

        String before = playerBoard.render();

        // A Pawn must not reach the last rank without promotion.
        TestUtils.assertEquals(
                false,
                whitePlayer.makeMove(
                        playerBoard, new Move(g7, g8)
                ),
                "Pawn cannot reach final rank without promotion"
        );

        TestUtils.assertEquals(
                before,
                playerBoard.render(),
                "Missing promotion preserves board state"
        );

        // White Pawn promotes to Rook.
        TestUtils.assertEquals(
                true,
                whitePlayer.makeMove(
                        playerBoard, new Move(g7, g8, 'R')
                ),
                "Player accepts White promotion"
        );

        TestUtils.assertEquals(
                true,
                playerBoard.getPiece(g8) instanceof Rook,
                "Player promotion replaces Pawn with Rook"
        );

        // Black Pawn promotion through Player.makeMove().
        Board blackPlayerBoard = new Board();

        Position b2 = new Position(1, 1);
        Position b1 = new Position(0, 1);

        blackPlayerBoard.setPiece(
                b2, new Pawn(Color.BLACK, b2)
        );

        Player blackPlayer = new Player(Color.BLACK);

        TestUtils.assertEquals(
                true,
                blackPlayer.makeMove(
                        blackPlayerBoard, new Move(b2, b1, 'B')
                ),
                "Player accepts Black promotion"
        );

        TestUtils.assertEquals(
                true,
                blackPlayerBoard.getPiece(b1) instanceof Bishop,
                "Player promotion replaces Black Pawn with Bishop"
        );

    }
}
