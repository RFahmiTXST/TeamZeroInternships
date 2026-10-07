package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Blueprint for every chess piece.
 *
 * <p>A piece knows its {@link Color}, the {@link Position} it stands on, and whether it has moved
 * yet. The last one matters for the pawn's two-square first move and for castling. Each subclass
 * supplies its own movement rules through {@link #possibleMoves(Board)} and its letter through
 * {@link #getSymbol()}.
 *
 * <p><b>Change from the brief:</b> the brief lists {@code possibleMoves()} with no parameters.
 * Here it takes the {@link Board}, because a piece cannot tell which squares are blocked or
 * capturable without seeing the board.
 *
 * <p>Owner: Developer 1. Pawn and King are owned by Developer 4.
 */
public abstract class Piece {

    /** The side this piece belongs to. It never changes. */
    private final Color color;

    /** The square this piece currently stands on. */
    private Position position;

    /** Whether this piece has moved at least once since the game started. */
    private boolean moved;

    /**
     * Creates a piece that has not moved yet.
     *
     * @param color    the side the piece belongs to
     * @param position the square the piece starts on
     */
    protected Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.moved = false;
    }

    /**
     * Returns the side this piece belongs to.
     *
     * @return the piece's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the square this piece stands on.
     *
     * @return the current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Tells whether this piece has moved since the game started.
     *
     * @return {@code true} once {@link #move(Position)} has been called
     */
    public boolean hasMoved() {
        return moved;
    }

    /**
     * Moves this piece to a new square and marks it as moved.
     *
     * <p>This updates only the piece. {@link Board#movePiece(Position, Position)} updates the grid
     * and calls this method.
     *
     * @param newPosition the square the piece now stands on
     */
    public void move(Position newPosition) {
        this.position = newPosition;
        this.moved = true;
    }

    /**
     * Tells whether another piece belongs to the other side.
     *
     * @param other the piece to compare with; may be {@code null}
     * @return {@code true} if {@code other} is not {@code null} and has a different color
     */
    public boolean isOpponent(Piece other) {
        return other != null && other.getColor() != color;
    }

    /**
     * Lists every square this piece could move to from where it stands, following its movement rules.
     *
     * <p>Phase 1 does not require removing moves that leave your own king in check.
     *
     * @param board the current board, used to see which squares are empty or occupied
     * @return the reachable squares; an empty list if the piece cannot move
     */
    public abstract List<Position> possibleMoves(Board board);

    /**
     * Returns the letter for this kind of piece: 'R', 'N', 'B', 'Q', 'K', or lowercase 'p' for pawns.
     *
     * @return the piece letter
     */
    public abstract char getSymbol();

    /**
     * Tells whether this piece may end its move on {@code target}: the square is on the board and is
     * either empty or holds an opponent piece. Used by single-step pieces (Knight, King) and by
     * {@link #slide(Board, int[][])}.
     *
     * @param board  the current board
     * @param target the square to test
     * @return {@code true} if the piece may land there
     */
    protected boolean canMoveTo(Board board, Position target) {
        // TODO(Dev 1): target.isOnBoard() && (board.isEmpty(target) || isOpponent(board.getPiece(target))).
        return false;
    }

    /**
     * Shared helper for sliding pieces (Rook, Bishop, Queen). Starting next to this piece, it walks one
     * square at a time in each direction. It stops at the board edge or at the first piece. The square
     * of an opponent piece is included, because it can be captured. The square of a friendly piece is not.
     *
     * @param board      the current board
     * @param directions row/column steps, such as {@code {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}} for a rook
     * @return every square reachable along those directions
     */
    protected List<Position> slide(Board board, int[][] directions) {
        List<Position> moves = new ArrayList<>();
        // TODO(Dev 1): for each {dRow, dCol}: step from position until off the board; add empty squares;
        //  on an occupied square add it only if it is an opponent's, then stop that direction.
        return moves;
    }

    /**
     * Returns the two-character code used on the board, such as "wQ", "bN" or "wp".
     *
     * @return the color prefix followed by the piece letter
     */
    @Override
    public String toString() {
        return "" + color.getPrefix() + getSymbol();
    }
}