package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Knight in chess.
 * 
 * Developer: Dev 1 (Rayed)
 */
public class Knight extends Piece {

    /**
     * Constructs a Knight with the specified color and initial position.
     *
     * @param color the color of the piece
     * @param position the initial position on the board
     */
    public Knight(Color color, Position position) {
        super(color, position);
    }

    /**
     * Calculates possible moves for the Knight (L-shapes).
     * 
     * @param board The current board state.
     * @return List of legal moves.
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int[][] offsets = {
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2},
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1}
        };

        for (int[] offset : offsets) {
            Position target = new Position(
                this.position.getRow() + offset[0],
                this.position.getColumn() + offset[1]
            );
            if (canMoveTo(board, target)) {
                moves.add(target);
            }
        }
        return moves;
    }

    /**
     * Returns the symbol for the Knight ('N').
     * 
     * @return 'N'
     */
    @Override
    public char getSymbol() {
        return 'N';
    }
}
