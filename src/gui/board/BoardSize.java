package gui.board;

/**
 * Enumeration of preset chessboard grid dimensions for visual scaling.
 *
 * <p>Enables responsive board scaling across small, medium, and large displays
 * while maintaining pixel-perfect piece scaling and coordinate alignment.</p>
 *
 * @author Dev 1 (Rayed)
 */
public enum BoardSize {

    /** Compact board grid optimized for small screens (480x480 pixels). */
    SMALL("Small (480x480)", 480),

    /** Standard balanced board grid (640x640 pixels). */
    MEDIUM("Medium (640x640)", 640),

    /** Expanded board grid optimized for high-DPI displays (800x800 pixels). */
    LARGE("Large (800x800)", 800);

    /** User-facing display label. */
    private final String displayName;

    /** Target grid dimension in pixels (width and height). */
    private final int dimension;

    /**
     * Constructs a BoardSize option.
     *
     * @param displayName user-facing label
     * @param dimension width and height in pixels
     */
    BoardSize(String displayName, int dimension) {
        this.displayName = displayName;
        this.dimension = dimension;
    }

    /**
     * Gets the human-readable display label.
     *
     * @return display name string
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the pixel dimension of the 8x8 chessboard grid.
     *
     * @return grid dimension in pixels
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Finds a BoardSize by its case-insensitive display name.
     *
     * @param name display name string
     * @return matching BoardSize, or null if not found
     */
    public static BoardSize fromDisplayName(String name) {
        if (name == null) {
            return null;
        }
        for (BoardSize size : values()) {
            if (size.displayName.equalsIgnoreCase(name.trim())) {
                return size;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
