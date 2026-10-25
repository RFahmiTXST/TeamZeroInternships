package pieces;

/**
 * Represents the color of a chess piece (White or Black).
 * 
 * Developer: Dev 1 (Rayed)
 */
public enum Color {
    /** The white player color. */
    WHITE,
    /** The black player color. */
    BLACK;

    /**
     * Gets the character prefix for this color ('w' for White, 'b' for Black).
     * 
     * @return prefix character
     */
    public char getPrefix() {
        return this == WHITE ? 'w' : 'b';
    }
}

