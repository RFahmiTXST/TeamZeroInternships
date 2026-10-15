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

    /**
     * Constructs a Queen with the specified color and initial position.
     *
     * @param color the color of the piece
     * @param position the initial position on the board
     */
    public Queen(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Queen (all 8 directions).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };
        return slide(board, directions);
    }

    /**
     * Returns the symbol for the Queen ('Q').
     * 
     * @return 'Q'
     */
    @Override
    public char getSymbol() {
        return 'Q';
    }
}
