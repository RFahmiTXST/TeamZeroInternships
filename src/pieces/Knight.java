package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Represents a Knight in chess.
 * 
 * Developer: Dev 1 (Rayed)
 */
public class Knight extends Piece {

    public Knight(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Knight (L-shapes).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     * TODO(Dev 1): Implement possibleMoves() using 8 jump offsets
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        return null;
    }

    /**
     * Returns the symbol for the Knight ('N').
     * 
     * @return 'N'
     * TODO(Dev 1): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return ' ';
    }
}

