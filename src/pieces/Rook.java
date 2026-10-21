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

    /**
     * Constructs a Rook with the specified color and initial position.
     *
     * @param color the color of the piece
     * @param position the initial position on the board
     */
    public Rook(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Rook (vertical and horizontal).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return slide(board, directions);
    }

    /**
     * Returns the symbol for the Rook ('R').
     * 
     * @return 'R'
     */
    @Override
    public char getSymbol() {
        return 'R';
    }
}
