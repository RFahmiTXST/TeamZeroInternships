
package core;

import board.Board;
import board.Position;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import pieces.King;
import pieces.Rook;

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

        // Promotion occurs through a Pawn move.
        // The new piece must not acquire castling rights.
        promoted.move(to);


        board.setPiece(to, promoted);

        return true;
    }

    /**
     * Validates and executes kingside or queenside castling.
     *
     * The King and Rook must be on their starting squares,
     * neither piece may have moved, the path must be clear,
     * and the King must not castle out of, through, or into check.
     *
     * @param board current chessboard
     * @param move requested castling move
     * @param playerColor color of the player attempting to castle
     * @return true if castling succeeds
     */
    public static boolean executeCastling(
            Board board, Move move, Color playerColor) {

        // 1. Validate inputs.
        if (board == null || move == null
                || playerColor == null
                || !move.isCastling()
                || move.getPromotionPiece() != '\0') {
            return false;
        }

        // 2. Determine the starting and destination squares.
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

        // 3. Validate King eligibility.
        if (!(king instanceof King)
                || king.getColor() != playerColor
                || king.hasMoved()
                || !kingFrom.equals(king.getPosition())) {
            return false;
        }

        // 4. Validate Rook eligibility.
        if (!(rook instanceof Rook)
                || rook.getColor() != playerColor
                || rook.hasMoved()
                || !rookFrom.equals(rook.getPosition())) {
            return false;
        }

        // 5. Check that every square between King and Rook
        // is empty.
        for (int col = 4 + direction;
                col != rookColumn;
                col += direction) {

            Position square = new Position(row, col);

            if (!board.isEmpty(square)) {
                return false;
            }
        }

        // 6. Determine the opposing color.
        Color opponent = playerColor == Color.WHITE
                ? Color.BLACK : Color.WHITE;

        // 7. The King cannot start in check, pass through
        // check, or land on an attacked square.
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

        // 8. All validations passed.
        // Move the King and Rook to their new squares.
        board.movePiece(kingFrom, kingTo);
        board.movePiece(rookFrom, rookTo);

        return true;
    }
}
