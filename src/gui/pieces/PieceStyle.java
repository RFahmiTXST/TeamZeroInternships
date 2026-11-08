package gui.pieces;

/**
 * Enumeration of visual rendering styles for chess piece graphics.
 *
 * <p>Supports switching between traditional vector styling with gradients/shadows,
 * flat minimalist monochrome silhouettes, and ultra-readable high-contrast graphics
 * for enhanced accessibility.</p>
 *
 * @author Dev 1 (Rayed)
 */
public enum PieceStyle {

    /** Classical procedural vector graphics with gradient shading and drop shadows. */
    CLASSIC_VECTOR("Classic Vector", "Full vector detailing with realistic shading and soft drop shadows"),

    /** Flat, modern monochrome silhouette with clean lines and no drop shadows. */
    MINIMALIST("Minimalist", "Flat modern monochrome silhouette with clean crisp lines"),

    /** High-visibility bold palette with thick outlines for maximum accessibility. */
    HIGH_CONTRAST("High-Contrast", "Maximum accessibility with bold contours and vivid accents");

    /** User-facing display label. */
    private final String displayName;

    /** Descriptive summary of visual characteristics. */
    private final String description;

    /**
     * Constructs a PieceStyle option.
     *
     * @param displayName user-facing label
     * @param description visual style summary
     */
    PieceStyle(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
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
     * Gets the descriptive summary of this style.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Finds a PieceStyle by its case-insensitive display name.
     *
     * @param name display name string
     * @return matching PieceStyle, or null if not found
     */
    public static PieceStyle fromDisplayName(String name) {
        if (name == null) {
            return null;
        }
        for (PieceStyle style : values()) {
            if (style.displayName.equalsIgnoreCase(name.trim())) {
                return style;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
