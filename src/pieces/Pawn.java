
package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pawn in chess.
 *
 * Developer: Dev 4 (Towsif)
 */
public class Pawn extends Piece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    // ============================================
    // STEP 2: PAWN HELPER METHODS
    // ============================================

    /**
     * Returns the direction in which the pawn moves.
     *
     * @return +1 for white, -1 for black
     */
    public int getForwardDirection() {
        return getColor() == Color.WHITE ? 1 : -1;
    }

    /**
     * Returns the pawn's initial row.
     *
     * @return row 1 for white, row 6 for black
     */
    public int getStartRow() {
        return getColor() == Color.WHITE ? 1 : 6;
    }

    /**
     * Returns the row where pawn promotion occurs.
     *
     * @return row 7 for white, row 0 for black
     */
    public int getPromotionRow() {
        return getColor() == Color.WHITE ? 7 : 0;
    }

    /**
     * Calculates possible moves for the Pawn.
     *
     * @param board The current board state.
     * @return List of legal moves.
     * TODO(Dev 4): Implement possibleMoves() handling 1/2 steps and diagonal captures
     */

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int row = getPosition().getRow();
        int col = getPosition().getColumn();
        int direction = getForwardDirection();

        // 1. Move one square forward
        Position oneStep = new Position(row + direction, col);

        if (oneStep.isOnBoard() && board.isEmpty(oneStep)) {
            moves.add(oneStep);

            // 2. Move two squares from starting position
            if (row == getStartRow() && !hasMoved()) {
                Position twoSteps =
                        new Position(row + 2 * direction, col);

                if (twoSteps.isOnBoard() && board.isEmpty(twoSteps)) {
                    moves.add(twoSteps);
                }
            }
        }

        // 3. Capture diagonally to the left or right
        for (int colOffset : new int[]{-1, 1}) {
            Position diagonal =
                    new Position(row + direction, col + colOffset);

            if (diagonal.isOnBoard()) {
                Piece target = board.getPiece(diagonal);

                if (isOpponent(target)) {
                    moves.add(diagonal);
                }
            }
        }

        return moves;
    }


    /**
     * Returns the symbol for the Pawn ('p').
     *
     * @return 'p'
     * TODO(Dev 4): Implement getSymbol()
     */
    @Override
    public char getSymbol() {
        return 'p';
    }
    
    /**
     * Creates a replacement piece when this Pawn reaches
     * its promotion rank.
     *
     * Supported promotion pieces:
     * Q = Queen, R = Rook, B = Bishop, N = Knight.
     *
     * The caller is responsible for replacing the Pawn
     * on the Board after validating and executing the move.
     *
     * @param pieceCode character identifying the new piece
     * @return replacement chess piece
     * @throws IllegalStateException if the Pawn has not
     *         reached its promotion rank
     * @throws IllegalArgumentException for an invalid piece
     */
    public Piece createPromotionPiece(char pieceCode) {

        if (getPosition().getRow() != getPromotionRow()) {
            throw new IllegalStateException(
                    "Pawn has not reached the promotion rank."
            );
        }

        char code = Character.toUpperCase(pieceCode);

        switch (code) {
            case 'Q':
                return new Queen(getColor(), getPosition());

            case 'R':
                return new Rook(getColor(), getPosition());

            case 'B':
                return new Bishop(getColor(), getPosition());

            case 'N':
                return new Knight(getColor(), getPosition());

            default:
                throw new IllegalArgumentException(
                        "Invalid promotion piece: " + pieceCode
                );
        }
    }

}
