
package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Represents a Pawn in chess.
 *
 * Developer: Dev 4 (Towsif)
 */
public class Pawn extends Piece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    // ============================================
    // STEP 2: PAWN HELPER METHODS
    // ============================================

    /**
     * Returns the direction in which the pawn moves.
     *
     * @return +1 for white, -1 for black
     */
    public int getForwardDirection() {
        return getColor() == Color.WHITE ? 1 : -1;
    }

    /**w
     * Returns the pawn's initial row.
     *
     * @return row 1 for white, row 6 for black
     */
    public int getStartRow() {
        return getColor() == Color.WHITE ? 1 : 6;
    }

    /**
     * Returns the row where pawn promotion occurs.
     *
     * @return row 7 for white, row 0 for black
     */
    public int getPromotionRow() {
        return getColor() == Color.WHITE ? 7 : 0;
    }

    /**
     * Calculates possible moves for the Pawn.
     *
     * @param board The current board state.
     * @return List of legal moves.
     * TODO(Dev 4): Implement possibleMoves() handling 1/2 steps and diagonal captures
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        return null;
    }

    /**
     * Returns the symbol for the Pawn ('p').
     *
     * @return 'p'
     * TODO(Dev 4): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return 'p';
    }
}
