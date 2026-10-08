package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Represents a Queen in chess.
 * 
 * Developer: Dev 1 (Rayed)
 */
public class Queen extends Piece {

    public Queen(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Queen (all 8 directions).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     * TODO(Dev 1): Implement possibleMoves() using slide()
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        return null;
    }

    /**
     * Returns the symbol for the Queen ('Q').
     * 
     * @return 'Q'
     * TODO(Dev 1): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return ' ';
    }
}

