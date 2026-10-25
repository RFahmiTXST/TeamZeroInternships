package board;

/**
 * Represents a square on the chessboard (row and column).
 * Coordinate convention: row 0 = rank 1, column 0 = file A.
 * 
 * Developer: Dev 2 (Abid)
 */
public class Position {
    /** Number of ranks and files on a chessboard. */
    private static final int BOARD_SIZE = 8;

    /** Zero-based row, where 0 represents rank 1. */
    private int row;
    /** Zero-based column, where 0 represents file A. */
    private int column;

    /**
     * Creates a position, including off-board candidates for move generation.
     *
     * @param row zero-based row
     * @param column zero-based column
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row index.
     * @return zero-based row
     */
    public int getRow() { return row; }

    /**
     * Returns the column index.
     * @return zero-based column
     */
    public int getColumn() { return column; }

    /**
     * Converts the coordinates to standard chess notation (e.g., "E2").
     * 
     * @return String representation of the position.
     */
    @Override
    public String toString() {
        return "" + (char) ('A' + column) + (row + 1);
    }

    /**
     * Checks if the current coordinates lie within the 8x8 board boundaries.
     * 
     * @return True if the position is on the board.
     */
    public boolean isOnBoard() {
        return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
    }
    
    /**
     * Compares positions by row and column.
     * @param obj object to compare
     * @return true if both positions represent the same coordinates
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.column == other.column;
    }

    /**
     * Returns a hash code consistent with coordinate equality.
     * @return hash code for this position
     */
    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}

