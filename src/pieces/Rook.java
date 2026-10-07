package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A rook moves any number of squares along a rank or a file and cannot jump over pieces.
 *
 * <p>Text representation: "wR" (white) / "bR" (black).
 *
 * <p>Owner: Developer 2.
 */
public class Rook extends Piece {

    /**
     * Creates a rook.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    public Rook(Color color, Position position) {
        super(color, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 2): declare the 4 orthogonal directions and return slide(board, DIRECTIONS).
        return moves;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'R'
     */
    @Override
    public char getSymbol() {
        return 'R';
    }
}
