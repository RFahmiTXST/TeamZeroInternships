package pieces;

/**
 * The two sides in a chess game.
 *
 * <p>Owner: Developer 2.
 */
public enum Color {

    /** The white side, which moves first. */
    WHITE('w', "White"),

    /** The black side. */
    BLACK('b', "Black");

    /** Single-letter prefix used in a piece's text representation ('w' or 'b'). */
    private final char prefix;

    /** Name to show to players, such as "White". */
    private final String displayName;

    /**
     * Creates a color constant.
     *
     * @param prefix      the letter that starts this color's piece codes
     * @param displayName the name to show to players
     */
    Color(char prefix, String displayName) {
        this.prefix = prefix;
        this.displayName = displayName;
    }

    /**
     * Returns the letter that starts this color's piece codes.
     *
     * @return 'w' for white, 'b' for black
     */
    public char getPrefix() {
        return prefix;
    }

    /**
     * Returns the name to show to players.
     *
     * @return "White" or "Black"
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the other side.
     *
     * @return {@link #BLACK} for white and {@link #WHITE} for black
     */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
