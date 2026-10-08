package board;

/**
 * Represents a square on the chessboard (row and column).
 * Coordinate convention: row 0 = rank 1, column 0 = file A.
 * 
 * Developer: Dev 2 (Abid)
 */
public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }

    /**
     * Converts the coordinates to standard chess notation (e.g., "E2").
     * 
     * @return String representation of the position.
     * TODO(Dev 2): Implement toString()
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Checks if the current coordinates lie within the 8x8 board boundaries.
     * 
     * @return True if the position is on the board.
     * TODO(Dev 2): Implement isOnBoard()
     */
    public boolean isOnBoard() {
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.column == other.column;
    }

    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}

