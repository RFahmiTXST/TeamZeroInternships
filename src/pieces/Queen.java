package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A queen moves any number of squares along a rank, file or diagonal, so it combines a
 * rook and a bishop.
 *
 * <p>Text representation: "wQ" (white) / "bQ" (black).
 *
 * <p>Owner: Developer 1.
 */
public class Queen extends Piece {

    /**
     * Creates a queen.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    public Queen(Color color, Position position) {
        super(color, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 1): declare all 8 directions and return slide(board, DIRECTIONS).
        return moves;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'Q'
     */
    @Override
    public char getSymbol() {
        return 'Q';
    }
}