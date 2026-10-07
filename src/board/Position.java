package board;

/**
 * An immutable square on the chessboard, stored as zero-based row and column indices.
 *
 * <p><b>Coordinate convention (the whole team relies on this):</b>
 * <ul>
 *   <li>{@code row} 0 is rank 1 (White's back rank) and {@code row} 7 is rank 8 (Black's back rank).</li>
 *   <li>{@code column} 0 is file A and {@code column} 7 is file H.</li>
 * </ul>
 * For example, "E2" is {@code new Position(1, 4)} and "D8" is {@code new Position(7, 3)}.
 *
 * <p>The constructor does not reject off-board values, so move generators can build a candidate
 * square first and then call {@link #isOnBoard()} to discard it.
 *
 * <p>Owner: Developer 2.
 */
public final class Position {

    /** Number of rows and columns on a chessboard. */
    public static final int BOARD_SIZE = 8;

    /** Zero-based row index (0 = rank 1, 7 = rank 8). */
    private final int row;

    /** Zero-based column index (0 = file A, 7 = file H). */
    private final int column;

    /**
     * Creates a position from zero-based indices.
     *
     * @param row    zero-based row index (0 = rank 1)
     * @param column zero-based column index (0 = file A)
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the zero-based row index.
     *
     * @return the row (0 = rank 1, 7 = rank 8)
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the zero-based column index.
     *
     * @return the column (0 = file A, 7 = file H)
     */
    public int getColumn() {
        return column;
    }

    /**
     * Tells whether this position lies inside the 8x8 board.
     *
     * @return {@code true} if both row and column are between 0 and 7 inclusive
     */
    public boolean isOnBoard() {
        return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
    }

    /**
     * Returns the square in standard chess notation, such as "E2".
     *
     * @return the file letter (A-H) followed by the rank number (1-8)
     */
    @Override
    public String toString() {
        return "" + (char) ('A' + column) + (row + 1);
    }

    /**
     * Two positions are equal when they have the same row and column.
     *
     * @param other the object to compare with
     * @return {@code true} if {@code other} is a {@code Position} on the same square
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position)) {
            return false;
        }
        Position that = (Position) other;
        return row == that.row && column == that.column;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}, so positions work in sets and maps.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}
