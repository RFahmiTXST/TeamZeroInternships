package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A pawn moves one square straight forward, or two on its first move. It captures one square
 * diagonally forward. A pawn that reaches the far rank is promoted, entered as "E7 E8=Q".
 *
 * <p>Text representation: "wp" (white) / "bp" (black). The brief uses a lowercase 'p'.
 *
 * <p>Owner: Developer 4.
 */
public class Pawn extends Piece {

    /**
     * Creates a pawn.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    public Pawn(Color color, Position position) {
        super(color, position);
    }

    /**
     * Returns the row step for "forward" for this pawn's color.
     *
     * @return +1 for white (towards rank 8), -1 for black (towards rank 1)
     */
    public int getForwardDirection() {
        // TODO(Dev 4)
        return 0;
    }

    /**
     * Returns the row this pawn's color starts on.
     *
     * @return 1 (rank 2) for white, 6 (rank 7) for black
     */
    public int getStartRow() {
        // TODO(Dev 4)
        return -1;
    }

    /**
     * Returns the row where this pawn's color is promoted.
     *
     * @return 7 (rank 8) for white, 0 (rank 1) for black
     */
    public int getPromotionRow() {
        // TODO(Dev 4)
        return -1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Includes the one-square advance, the two-square advance from the start row (only when both
     * squares are empty), and diagonal captures of opponent pieces. Pawns never move backwards.
     * En passant is not required for Phase 1.
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 4): forward one if empty; forward two if !hasMoved() and both squares empty;
        //  forward-left / forward-right if isOpponent(board.getPiece(diagonal)).
        return moves;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'p' (lowercase, as in the brief)
     */
    @Override
    public char getSymbol() {
        return 'p';
    }
}
