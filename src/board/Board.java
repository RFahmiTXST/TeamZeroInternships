package board;

import pieces.Color;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The chessboard: an 8x8 grid of squares plus the list of pieces captured so far.
 *
 * <p>Each element of the grid holds a reference to a {@link Piece}, or {@code null} when the
 * square is empty. The grid is indexed as {@code squares[row][column]} using the convention
 * described in {@link Position}.
 *
 * <p>A new {@code Board} is empty. Call {@link #initialize()} to set up the starting position.
 * An empty board is handy in tests, where you can place pieces with {@link #setPiece(Position, Piece)}.
 *
 * <p>Owner: Developer 2.
 */
public class Board {

    /** Number of rows and columns on the board. */
    public static final int SIZE = Position.BOARD_SIZE;

    /** The 8x8 grid. {@code squares[row][column]} is the piece on that square, or {@code null}. */
    private final Piece[][] squares;

    /** Every piece that has been captured, in the order it was captured. */
    private final List<Piece> capturedPieces;

    /**
     * Creates an empty board with no pieces on it.
     */
    public Board() {
        this.squares = new Piece[SIZE][SIZE];
        this.capturedPieces = new ArrayList<>();
    }

    /**
     * Clears the board and places all 32 pieces on their standard starting squares.
     *
     * <p>White occupies ranks 1-2 and Black ranks 7-8. Queens start on the D file and kings on the E file.
     */
    public void initialize() {
        // TODO(Dev 2): clear squares and capturedPieces, then place:
        //  row 0: white R N B Q K B N R   row 1: white pawns
        //  row 6: black pawns             row 7: black R N B Q K B N R
        //  Build each piece with its own Position, e.g. new Rook(Color.WHITE, new Position(0, 0)).
    }

    /**
     * Returns the piece on a square.
     *
     * @param position the square to look at
     * @return the piece there, or {@code null} if the square is empty or off the board
     */
    public Piece getPiece(Position position) {
        // TODO(Dev 2): return squares[row][column]; return null when !position.isOnBoard().
        return null;
    }

    /**
     * Puts a piece on a square, replacing whatever was there. Passing {@code null} empties the square.
     *
     * <p>This does not update the piece's own position; the caller makes sure they match.
     * It is mainly used by {@link #initialize()}, by pawn promotion and by tests.
     *
     * @param position the square to fill
     * @param piece    the piece to place, or {@code null} to clear the square
     */
    public void setPiece(Position position, Piece piece) {
        // TODO(Dev 2): store the piece in squares[row][column].
    }

    /**
     * Tells whether a square has no piece on it.
     *
     * @param position the square to check
     * @return {@code true} if the square is on the board and empty
     */
    public boolean isEmpty(Position position) {
        // TODO(Dev 2): on the board and getPiece(position) == null.
        return false;
    }

    /**
     * Moves the piece on {@code from} to {@code to}. If an opponent piece is on {@code to}, it is
     * captured and added to the captured list.
     *
     * <p>This method does not check whether the move is legal; callers do that first.
     *
     * @param from the square the piece is moving from; must hold a piece
     * @param to   the destination square
     * @return the captured piece, or {@code null} if nothing was captured
     * @throws IllegalArgumentException if {@code from} is empty
     */
    public Piece movePiece(Position from, Position to) {
        // TODO(Dev 2): look up the moving piece (throw if null), remember the piece on 'to' as
        //  captured (add it to capturedPieces), put the moving piece on 'to', clear 'from',
        //  call piece.move(to), and return the captured piece.
        return null;
    }

    /**
     * Returns the pieces captured so far.
     *
     * @return a read-only view of the captured pieces, in capture order
     */
    public List<Piece> getCapturedPieces() {
        return Collections.unmodifiableList(capturedPieces);
    }

    /**
     * Returns every piece of one color that is still on the board.
     *
     * @param color the side whose pieces to collect
     * @return a new list of that side's pieces
     */
    public List<Piece> getPieces(Color color) {
        List<Piece> result = new ArrayList<>();
        // TODO(Dev 2): scan all 64 squares and add every non-null piece of the given color.
        return result;
    }

    /**
     * Tells whether the king of the given color is attacked.
     *
     * <p>Not required for Phase 1. It returns {@code false} until it is implemented.
     *
     * @param color the side to test
     * @return {@code true} if that side's king is in check
     */
    public boolean isCheck(Color color) {
        // TODO(Dev 2 + Dev 4, stretch): find the king, then see whether any opponent piece's
        //  possibleMoves(this) contains the king's position.
        return false;
    }

    /**
     * Tells whether the given color is checkmated.
     *
     * <p>Not required for Phase 1. It returns {@code false} until it is implemented.
     *
     * @param color the side to test
     * @return {@code true} if that side is in check and has no move that escapes it
     */
    public boolean isCheckmate(Color color) {
        // TODO(stretch / Phase 2)
        return false;
    }

    /**
     * Tells whether the given color is stalemated (not in check, but with no legal move).
     *
     * <p>Not required for Phase 1. It returns {@code false} until it is implemented.
     *
     * @param color the side to test
     * @return {@code true} if that side is stalemated
     */
    public boolean isStalemate(Color color) {
        // TODO(stretch / Phase 2)
        return false;
    }

    /**
     * Builds the text picture of the board that {@link #display()} prints.
     *
     * <p>File letters A-H go along the top and rank numbers 8 down to 1 go down the left side.
     * Occupied squares show the piece, such as "wK". Empty dark squares show "##" and empty light
     * squares show two spaces, following the sample in the project brief.
     *
     * @return the board drawing, with one line per rank
     */
    public String render() {
        // TODO(Dev 2): use a StringBuilder. Header "   A  B  C  D  E  F  G  H", then for row 7 down to 0:
        //  (row + 1) + " " + each cell + " ". A square is dark when (row + column) % 2 == 0 (A1 is dark).
        //  Returning a String (instead of printing directly) lets us test the output exactly.
        return "[Board.render() is not implemented yet]";
    }

    /**
     * Prints the current board to the console.
     */
    public void display() {
        System.out.println(render());
    }
}