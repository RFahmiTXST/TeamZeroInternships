
package core;

import board.Board;
import board.Position;
import pieces.Bishop;
import pieces.Color;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import test.TestUtils;
import pieces.King;

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

        TestUtils.assertEquals(
                true,
                playerBoard.getPiece(g8).hasMoved(),
                "Promoted Rook has no castling rights"
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

    /**
     * Validates and executes kingside or queenside castling.
     *
     * Rules:
     * - King and Rook must occupy their original squares.
     * - Neither piece may have moved.
     * - Squares between King and Rook must be empty.
     * - King cannot start in, pass through, or end in check.
     *
     * @param board current board
     * @param move castling move
     * @param playerColor player attempting to castle
     * @return true if castling succeeds
     */
    public static boolean executeCastling(
            Board board, Move move, Color playerColor) {

        if (board == null || move == null
                || playerColor == null || !move.isCastling()
                || move.getPromotionPiece() != '\0') {
            return false;
        }

        boolean kingside = move.isKingsideCastle();

        int row = playerColor == Color.WHITE ? 0 : 7;
        int direction = kingside ? 1 : -1;

        int rookColumn = kingside ? 7 : 0;
        int kingDestinationColumn = kingside ? 6 : 2;
        int rookDestinationColumn = kingside ? 5 : 3;

        Position kingFrom = new Position(row, 4);
        Position rookFrom = new Position(row, rookColumn);

        Position kingTo =
                new Position(row, kingDestinationColumn);

        Position rookTo =
                new Position(row, rookDestinationColumn);

        Piece king = board.getPiece(kingFrom);
        Piece rook = board.getPiece(rookFrom);

        // Verify the King is eligible.
        if (!(king instanceof King)
                || king.getColor() != playerColor
                || king.hasMoved()
                || !kingFrom.equals(king.getPosition())) {
            return false;
        }

        // Verify the Rook is eligible.
        if (!(rook instanceof Rook)
                || rook.getColor() != playerColor
                || rook.hasMoved()
                || !rookFrom.equals(rook.getPosition())) {
            return false;
        }

        // Every square between the King and Rook
        // must be empty.
        for (int col = 4 + direction;
                col != rookColumn;
                col += direction) {

            Position square = new Position(row, col);

            if (!board.isEmpty(square)) {
                return false;
            }
        }

        Color opponent = playerColor == Color.WHITE
                ? Color.BLACK : Color.WHITE;

        // King cannot start in check, pass through
        // an attacked square, or land in check.
        for (int step = 0; step <= 2; step++) {

            Position kingSquare = new Position(
                    row,
                    4 + direction * step
            );

            if (AttackDetector.isSquareAttacked(
                    board, kingSquare, opponent)) {
                return false;
            }
        }

        // All checks passed. Execute both movements.
        board.movePiece(kingFrom, kingTo);
        board.movePiece(rookFrom, rookTo);

        return true;
    }
}
