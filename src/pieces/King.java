package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Represents a King in chess.
 * 
 * Developer: Dev 4 (Towsif)
 */
public class King extends Piece {

    public King(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the King (1 step any direction).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     * TODO(Dev 4): Implement possibleMoves() using 8 neighbor offsets
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        return null;
    }

    /**
     * Returns the symbol for the King ('K').
     * 
     * @return 'K'
     * TODO(Dev 4): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return ' ';
    }
}

