package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A knight moves in an "L": two squares one way and one square at a right angle.
 * It is the only piece that jumps over others.
 *
 * <p>Text representation: "wN" (white) / "bN" (black).
 *
 * <p>Owner: Developer 1.
 */
public class Knight extends Piece {

    /**
     * Creates a knight.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    public Knight(Color color, Position position) {
        super(color, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 1): try the 8 L-shaped offsets ({2, 1}, {1, 2}, ...) and keep each square
        //  where canMoveTo(board, target) is true.
        return moves;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'N'
     */
    @Override
    public char getSymbol() {
        return 'N';
    }
}