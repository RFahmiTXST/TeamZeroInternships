package board;

import pieces.Piece;
import pieces.Color;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;
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
     */
    public void initialize() {
        squares = new Piece[SIZE][SIZE];
        capturedPieces.clear();

        placeBackRank(Color.WHITE, 0);
        placePawns(Color.WHITE, 1);
        placePawns(Color.BLACK, 6);
        placeBackRank(Color.BLACK, 7);
    }

    /**
     * Places a complete back rank for one side.
     *
     * @param color the side whose pieces are being placed
     * @param row the back-rank row
     */
    private void placeBackRank(Color color, int row) {
        setPiece(new Position(row, 0), new Rook(color, new Position(row, 0)));
        setPiece(new Position(row, 1), new Knight(color, new Position(row, 1)));
        setPiece(new Position(row, 2), new Bishop(color, new Position(row, 2)));
        setPiece(new Position(row, 3), new Queen(color, new Position(row, 3)));
        setPiece(new Position(row, 4), new King(color, new Position(row, 4)));
        setPiece(new Position(row, 5), new Bishop(color, new Position(row, 5)));
        setPiece(new Position(row, 6), new Knight(color, new Position(row, 6)));
        setPiece(new Position(row, 7), new Rook(color, new Position(row, 7)));
    }

    /**
     * Places eight pawns for one side.
     *
     * @param color the side whose pawns are being placed
     * @param row the pawn row
     */
    private void placePawns(Color color, int row) {
        for (int column = 0; column < SIZE; column++) {
            Position position = new Position(row, column);
            setPiece(position, new Pawn(color, position));
        }
    }

    /**
     * Renders the board into a formatted string with coordinates for console display.
     * 
     * @return The text-based board state, with rank and file labels.
     */
    public String render() {
        StringBuilder rendered = new StringBuilder();
        rendered.append("    A   B   C   D   E   F   G   H\n");
        for (int row = SIZE - 1; row >= 0; row--) {
            rendered.append(row + 1).append(" |");
            for (int column = 0; column < SIZE; column++) {
                Piece piece = squares[row][column];
                rendered.append(' ')
                        .append(piece == null ? "--" : piece.toString())
                        .append(" |");
            }
            rendered.append('\n');
        }
        rendered.append("    A   B   C   D   E   F   G   H");
        return rendered.toString();
    }

    /**
     * Prints the board to the console directly.
     * 
     */
    public void display() {
        System.out.println(render());
    }

    /**
     * Moves a piece from one square to another, handling captures.
     * 
     * @param from The starting square.
     * @param to The destination square.
     * @return The captured piece, or null if no capture occurred.
     * @throws IllegalArgumentException if either position is invalid or the
     *         starting square is empty
     */
    public Piece movePiece(Position from, Position to) {
        if (from == null || to == null || !from.isOnBoard() || !to.isOnBoard()) {
            throw new IllegalArgumentException("Move positions must be on the board");
        }

        Piece movingPiece = getPiece(from);
        if (movingPiece == null) {
            throw new IllegalArgumentException("No piece at starting position: " + from);
        }

        Piece capturedPiece = getPiece(to);
        if (capturedPiece != null) {
            capturedPieces.add(capturedPiece);
        }
        squares[from.getRow()][from.getColumn()] = null;
        squares[to.getRow()][to.getColumn()] = movingPiece;
        movingPiece.move(to);
        return capturedPiece;
    }

    /**
     * Gets a list of all active pieces of a specific color.
     * 
     * @param color The color to look for.
     * @return a new list containing the active pieces of the requested color
     */
    public List<Piece> getPieces(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Piece piece = squares[row][column];
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Checks if a given color's king is currently under attack (in check).
     * 
     * @param color The color to check.
     * @return true if the color's king is targeted by an opposing piece;
     *         false when it is not targeted or the king is absent
     */
    public boolean isCheck(Color color) {
        if (color == null) {
            return false;
        }

        Position kingPosition = null;
        for (Piece piece : getPieces(color)) {
            if (piece instanceof King) {
                kingPosition = piece.getPosition();
                break;
            }
        }

        if (kingPosition == null) {
            return false;
        }

        Color opponent = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        for (Piece opponentPiece : getPieces(opponent)) {
            List<Position> possibleMoves = opponentPiece.possibleMoves(this);
            if (possibleMoves != null && possibleMoves.contains(kingPosition)) {
                return true;
            }
        }
        return false;
    }
}

