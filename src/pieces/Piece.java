package pieces;

import board.Board;
import board.Position;
import java.util.List;

/**
 * Abstract blueprint for all chess pieces.
 * 
 * Developer: Dev 1 (Rayed)
 */
public abstract class Piece {
    protected Color color;
    protected Position position;
    protected boolean moved;

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.moved = false;
    }

    /**
     * Calculates all possible legal moves for this piece.
     * 
     * @param board The current state of the board.
     * @return List of possible positions this piece can move to.
     * TODO(Dev 1, Dev 4): Implement possibleMoves logic in subclasses
     */
    public abstract List<Position> possibleMoves(Board board);

    /**
     * Gets the character symbol of the piece (e.g., 'Q', 'N', 'p').
     * 
     * @return The piece symbol.
     * TODO(Dev 1, Dev 4): Implement getSymbol() in subclasses
     */
    public abstract char getSymbol();

    /**
     * Returns the string representation of the piece (e.g., "wQ", "bp").
     * 
     * @return String representation of the piece.
     * TODO(Dev 1): Implement toString()
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Moves the piece to a new position on the board.
     * 
     * @param newPosition The destination square.
     * TODO(Dev 1): Implement move() to update position and set moved = true
     */
    public void move(Position newPosition) {
    }

    /**
     * Checks if a given piece is an opponent.
     * 
     * @param other The piece to check against.
     * @return True if the piece belongs to the opponent color.
     * TODO(Dev 1): Implement isOpponent()
     */
    public boolean isOpponent(Piece other) {
        return false;
    }

    /**
     * Checks if a target position is valid for this piece to move to (empty or holds an opponent).
     * 
     * @param board The current board state.
     * @param target The target position to check.
     * @return True if the move is valid.
     * TODO(Dev 1): Implement canMoveTo()
     */
    public boolean canMoveTo(Board board, Position target) {
        return false;
    }

    /**
     * Helper method for sliding pieces (Rook, Bishop, Queen) to calculate continuous movement across the board.
     * 
     * @param board The current board state.
     * @param directions A 2D array of movement direction vectors.
     * @return List of valid positions in the given directions.
     * TODO(Dev 1): Implement slide() logic
     */
    protected List<Position> slide(Board board, int[][] directions) {
        return null;
    }

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public boolean hasMoved() { return moved; }
}

