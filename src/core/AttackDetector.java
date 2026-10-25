
package core;

import board.Board;
import board.Position;
import pieces.Bishop;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**
 * Detects squares attacked by chess pieces.
 * Used when validating castling.
 *
 * Developer: Dev 4
 */
public final class AttackDetector {
    /** Relative row and column offsets for Knight attacks. */
    private static final int[][] KNIGHT_OFFSETS = {
        {1, 2}, {2, 1}, {-1, 2}, {-2, 1},
        {1, -2}, {2, -1}, {-1, -2}, {-2, -1}
    };

    /** Directions used to detect straight-line attacks. */
    private static final int[][] STRAIGHT = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    /** Directions used to detect diagonal attacks. */
    private static final int[][] DIAGONAL = {
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    /** Prevents utility class construction. */
    private AttackDetector() { }

    /**
     * Checks whether a square is attacked by a given color.
     *
     * @param board current chessboard
     * @param target square being checked
     * @param attacker color of attacking pieces
     * @return true if the square is under attack
     */
    public static boolean isSquareAttacked(
            Board board, Position target, Color attacker) {

        if (board == null || target == null
                || attacker == null || !target.isOnBoard()) {
            return false;
        }

        int row = target.getRow();
        int col = target.getColumn();

        // 1. Pawn attacks
        int pawnRow = row - (
                attacker == Color.WHITE ? 1 : -1
        );

        for (int offset : new int[]{-1, 1}) {
            Piece piece = at(
                    board, pawnRow, col + offset
            );

            if (piece instanceof Pawn
                    && piece.getColor() == attacker) {
                return true;
            }
        }

        // 2. Knight attacks
        for (int[] offset : KNIGHT_OFFSETS) {
            Piece piece = at(
                    board,
                    row + offset[0],
                    col + offset[1]
            );

            if (piece instanceof Knight
                    && piece.getColor() == attacker) {
                return true;
            }
        }

        // 3. King attacks
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dr == 0 && dc == 0) {
                    continue;
                }

                Piece piece = at(
                        board, row + dr, col + dc
                );

                if (piece instanceof King
                        && piece.getColor() == attacker) {
                    return true;
                }
            }
        }

        // 4. Rook, Bishop, and Queen attacks
        return attackedBySlidingPiece(
                board, target, attacker,
                STRAIGHT, false
        ) || attackedBySlidingPiece(
                board, target, attacker,
                DIAGONAL, true
        );
    }

    /**
     * Returns the piece at a square, or null if off-board.
     *
     * @param board current chessboard
     * @param row row index
     * @param col column index
     * @return the piece or null
     */
    private static Piece at(
            Board board, int row, int col) {

        Position square = new Position(row, col);

        if (!square.isOnBoard()) {
            return null;
        }

        return board.getPiece(square);
    }

    /**
     * Checks straight or diagonal sliding attacks.
     *
     * @param board current chessboard
     * @param target square being examined
     * @param attacker attacking color
     * @param directions directions to scan
     * @param diagonal true for diagonal directions
     * @return true if an eligible sliding piece attacks target
     */
    private static boolean attackedBySlidingPiece(
            Board board,
            Position target,
            Color attacker,
            int[][] directions,
            boolean diagonal) {

        for (int[] direction : directions) {

            int row = target.getRow() + direction[0];
            int col = target.getColumn() + direction[1];

            while (new Position(row, col).isOnBoard()) {

                Piece piece = at(board, row, col);

                if (piece != null) {

                    if (piece.getColor() == attacker
                            && (piece instanceof Queen
                            || (diagonal
                            && piece instanceof Bishop)
                            || (!diagonal
                            && piece instanceof Rook))) {
                        return true;
                    }

                    // Pieces block attacks behind them.
                    break;
                }

                row += direction[0];
                col += direction[1];
            }
        }

        return false;
    }
}
