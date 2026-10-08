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
        return ' ';
    }
}

