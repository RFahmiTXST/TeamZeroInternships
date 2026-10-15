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

    /**
     * Constructs a Bishop with the specified color and initial position.
     *
     * @param color the color of the piece
     * @param position the initial position on the board
     */
    public Bishop(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Bishop (diagonal).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return slide(board, directions);
    }

    /**
     * Returns the symbol for the Bishop ('B').
     * 
     * @return 'B'
     */
    @Override
    public char getSymbol() {
        return 'B';
    }
}
