package pieces;

import board.Board;
import board.Position;
import java.util.List;
import java.util.ArrayList;

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
        return "" + color.getPrefix() + getSymbol();
    }

    /**
     * Moves the piece to a new position on the board.
     * 
     * @param newPosition The destination square.
     * TODO(Dev 1): Implement move() to update position and set moved = true
     */
    public void move(Position newPosition) {
        this.position = newPosition;
        this.moved = true;
    }

    /**
     * Checks if a given piece is an opponent.
     * 
     * @param other The piece to check against.
     * @return True if the piece belongs to the opponent color.
     * TODO(Dev 1): Implement isOpponent()
     */
    public boolean isOpponent(Piece other) {
        return other != null && other.getColor() != this.color;
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
        if (target == null || !target.isOnBoard()) {
            return false;
        }
        if (board.isEmpty(target)) {
            return true;
        }
        Piece pieceAtTarget = board.getPiece(target);
        return isOpponent(pieceAtTarget);
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
        List<Position> possibleMoves = new ArrayList<>();
        for (int[] direction : directions) {
            int rowDir = direction[0];
            int colDir = direction[1];
            
            int currentRow = this.position.getRow() + rowDir;
            int currentCol = this.position.getColumn() + colDir;
            
            while (true) {
                Position target = new Position(currentRow, currentCol);
                if (!target.isOnBoard()) {
                    break;
                }
                
                if (board.isEmpty(target)) {
                    possibleMoves.add(target);
                } else {
                    Piece pieceAtTarget = board.getPiece(target);
                    if (isOpponent(pieceAtTarget)) {
                        possibleMoves.add(target);
                    }
                    break;
                }
                
                currentRow += rowDir;
                currentCol += colDir;
            }
        }
        return possibleMoves;
    }

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public boolean hasMoved() { return moved; }
}

