package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Represents a Bishop in chess.
 * 
 * Developer: Dev 1 (Rayed)
 */
public class Bishop extends Piece {

    public Bishop(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Bishop (diagonal).
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
     * Returns the symbol for the Bishop ('B').
     * 
     * @return 'B'
     * TODO(Dev 1): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return ' ';
    }
}

