package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A bishop moves any number of squares diagonally and cannot jump over pieces.
 *
 * <p>Text representation: "wB" (white) / "bB" (black).
 *
 * <p>Owner: Developer 2.
 */
public class Bishop extends Piece {

    /**
     * Creates a bishop.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    public Bishop(Color color, Position position) {
        super(color, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 2): declare the 4 diagonal directions and return slide(board, DIRECTIONS).
        return moves;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'B'
     */
    @Override
    public char getSymbol() {
        return 'B';
    }
}
