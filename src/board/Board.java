package board;

import pieces.Piece;
import pieces.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the 8x8 chessboard and handles piece placement and movement.
 * 
 * Developer: Dev 2 (Abid)
 */
public class Board {
    /** Number of ranks and files on a chessboard. */
    private static final int SIZE = 8;

    /** Pieces indexed by row and column; null indicates an empty square. */
    private Piece[][] squares;
    /** Pieces captured during the game, in capture order. */
    private List<Piece> capturedPieces;

    /** Creates an empty board with no captured pieces. */
    public Board() {
        squares = new Piece[SIZE][SIZE];
        capturedPieces = new ArrayList<>();
    }

    /**
     * Retrieves the piece at the given position.
     * 
     * @param position The position to check.
     * @return The Piece at the position, or null if empty/off-board.
     */
    public Piece getPiece(Position position) {
        if (!position.isOnBoard()) {
            return null;
        }
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Places a piece at the specified position on the board.
     * Passing null clears the square. This changes only the grid, not the piece's
     * own position; callers must keep the two consistent.
     * 
     * @param position The target square.
     * @param piece The piece to place, or null to clear the square.
     * @throws IllegalArgumentException if the position is off the board
     */
    public void setPiece(Position position, Piece piece) {
        if (!position.isOnBoard()) {
            throw new IllegalArgumentException("Position is off the board: " + position);
        }
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Checks if a square is completely empty.
     * 
     * @param position The square to check.
     * @return True if the square is empty and valid.
     */
    public boolean isEmpty(Position position) {
        return position.isOnBoard() && getPiece(position) == null;
    }

    /**
     * Initializes the board with the standard 32 chess pieces in their starting positions.
     * 
     * TODO(Dev 2): Implement initialize()
     */
    public void initialize() {
    }

    /**
     * Renders the board into a formatted string with coordinates for console display.
     * 
     * @return The text-based board state.
     * TODO(Dev 2): Implement render()
     */
    public String render() {
        return null;
    }

    /**
     * Prints the board to the console directly.
     * 
     * TODO(Dev 2): Implement display() by calling System.out.println(render())
     */
    public void display() {
    }

    /**
     * Moves a piece from one square to another, handling captures.
     * 
     * @param from The starting square.
     * @param to The destination square.
     * @return The captured piece, or null if no capture occurred.
     * TODO(Dev 2): Implement movePiece()
     */
    public Piece movePiece(Position from, Position to) {
        return null;
    }

    /**
     * Gets a list of all active pieces of a specific color.
     * 
     * @param color The color to look for.
     * @return List of pieces.
     * TODO(Dev 2): Implement getPieces()
     */
    public List<Piece> getPieces(Color color) {
        return null;
    }

    /**
     * Checks if a given color's king is currently under attack (in check).
     * 
     * @param color The color to check.
     * @return True if in check.
     * TODO(Dev 2, Dev 4): Implement isCheck()
     */
    public boolean isCheck(Color color) {
        return false;
    }
}

