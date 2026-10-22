
package core;

import board.Board;
import board.Position;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;

/**
 * Handles special chess moves.
 *
 * Developer: Dev 4
 */
public final class SpecialMoveHandler {

    /** Prevents utility class construction. */
    private SpecialMoveHandler() { }

    /**
     * Validates and executes a Pawn promotion.
     *
     * Invalid requests return false without changing
     * the board.
     *
     * @param board current chessboard
     * @param move requested promotion move
     * @param playerColor active player's color
     * @return true if promotion succeeds
     */
    public static boolean executePromotion(
            Board board, Move move, Color playerColor) {

        if (board == null || move == null
                || playerColor == null || move.isCastling()) {
            return false;
        }

        Position from = move.getFrom();
        Position to = move.getTo();

        if (from == null || to == null
                || !from.isOnBoard() || !to.isOnBoard()
                || from.equals(to)) {
            return false;
        }

        char code = Character.toUpperCase(
                move.getPromotionPiece()
        );

        if (code != 'Q' && code != 'R'
                && code != 'B' && code != 'N') {
            return false;
        }

        Piece piece = board.getPiece(from);

        if (!(piece instanceof Pawn)
                || piece.getColor() != playerColor
                || !from.equals(piece.getPosition())) {
            return false;
        }

        Pawn pawn = (Pawn) piece;

        if (to.getRow() != pawn.getPromotionRow()) {
            return false;
        }

        if (!pawn.possibleMoves(board).contains(to)) {
            return false;
        }

        // All normal promotion checks have passed.
        board.movePiece(from, to);

        Piece promoted = pawn.createPromotionPiece(code);
        board.setPiece(to, promoted);

        return true;
    }
}
