package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Represents a Rook in chess.
 * 
 * Developer: Dev 1 (Rayed)
 */
public class Rook extends Piece {

    public Rook(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Rook (vertical and horizontal).
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
     * Returns the symbol for the Rook ('R').
     * 
     * @return 'R'
     * TODO(Dev 1): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return ' ';
    }
}

