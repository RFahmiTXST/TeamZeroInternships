package pieces;

import board.Board;
import board.Position;
import java.util.List;
import java.util.ArrayList;

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
        List<Position> moves = new ArrayList<>();

        int row = getPosition().getRow();
        int col = getPosition().getColumn();

        // Check all eight neighboring squares.
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {

                // The King cannot remain on its current square.
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                Position target = new Position(
                        row + rowOffset,
                        col + colOffset
                );

                // Reject off-board squares and friendly pieces.
                if (canMoveTo(board, target)) {
                    moves.add(target);
                }
            }
        }

        return moves;
    }


    /**
     * Returns the symbol for the King ('K').
     * 
     * @return 'K'
     * TODO(Dev 4): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return 'K';
    }
}

