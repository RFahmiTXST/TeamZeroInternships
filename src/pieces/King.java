package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * The king moves one square in any direction. It can also castle, entered as "O-O" for kingside
 * or "O-O-O" for queenside. Castling is optional in Phase 1.
 *
 * <p>Text representation: "wK" (white) / "bK" (black).
 *
 * <p>Owner: Developer 4.
 */
public class King extends Piece {

    /**
     * Creates a king.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    public King(Color color, Position position) {
        super(color, position);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Lists the up-to-8 neighbouring squares the king can land on. Phase 1 does not require
     * removing squares that are attacked by the opponent.
     */
    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 4): try the 8 neighbouring offsets and keep those where canMoveTo(board, target).
        return moves;
    }

    /**
     * Tells whether kingside castling ("O-O") is currently allowed. This is optional for Phase 1.
     *
     * @param board the current board
     * @return {@code true} if neither the king nor the H-file rook has moved and the F and G squares are empty
     */
    public boolean canCastleKingside(Board board) {
        // TODO(Dev 4, optional)
        return false;
    }

    /**
     * Tells whether queenside castling ("O-O-O") is currently allowed. This is optional for Phase 1.
     *
     * @param board the current board
     * @return {@code true} if neither the king nor the A-file rook has moved and the B, C and D squares are empty
     */
    public boolean canCastleQueenside(Board board) {
        // TODO(Dev 4, optional)
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'K'
     */
    @Override
    public char getSymbol() {
        return 'K';
    }
}
