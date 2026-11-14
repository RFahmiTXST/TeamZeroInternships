package gui.board;

import java.awt.Color;
import java.util.Objects;

/**
 * Enumeration of available visual board color themes and styles.
 *
 * <p>Each theme defines a cohesive pair of background colors for light and dark
 * chessboard squares, facilitating dynamic customization without interrupting
 * the active game state.</p>
 *
 * @author Dev 1 (Rayed)
 */
public enum BoardTheme {

    /** Classic birch and walnut wood theme. */
    CLASSIC_WOOD("Classic Wood", new Color(240, 217, 181), new Color(181, 136, 99)),

    /** Modern sleek silver and slate gray theme. */
    MODERN_GRAY("Modern Gray", new Color(222, 224, 226), new Color(140, 146, 154)),

    /** Soft ice and slate blue theme. */
    SLATE_BLUE("Slate Blue", new Color(225, 232, 240), new Color(106, 135, 156)),

    /** Traditional tournament buff and forest green theme. */
    FOREST_GREEN("Forest Green", new Color(238, 238, 210), new Color(118, 150, 86));

    /** User-friendly display label. */
    private final String displayName;

    /** Background color for alternating light squares. */
    private final Color lightColor;

    /** Background color for alternating dark squares. */
    private final Color darkColor;

    /**
     * Constructs a BoardTheme with the given display name and square colors.
     *
     * @param displayName user-facing name of the theme
     * @param lightColor background color for light squares
     * @param darkColor background color for dark squares
     */
    BoardTheme(String displayName, Color lightColor, Color darkColor) {
        this.displayName = displayName;
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }

    /**
     * Gets the human-readable display name of this theme.
     *
     * @return display name string
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the light square background color.
     *
     * @return light square Color
     */
    public Color getLightColor() {
        return lightColor;
    }

    /**
     * Gets the dark square background color.
     *
     * @return dark square Color
     */
    public Color getDarkColor() {
        return darkColor;
    }

    /**
     * Finds a BoardTheme by its case-insensitive display name.
     *
     * @param name display name string
     * @return matching BoardTheme, or null if not found
     */
    public static BoardTheme fromDisplayName(String name) {
        if (name == null) {
            return null;
        }
        for (BoardTheme theme : values()) {
            if (theme.displayName.equalsIgnoreCase(name.trim())) {
                return theme;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
