package utils;

import board.Position;
import core.Move;
import java.util.Locale;

/**
 * Utility functions for parsing and validating moves.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Utils {

    /**
     * Normalizes player input by removing leading and trailing whitespace,
     * replacing multiple whitespace characters with a single space,
     * and converting all letters to uppercase.
     *
     * @param input The raw move input entered by the player.
     * @return The normalized input string.
     * @throws IllegalArgumentException if the input is null.
     */
    public static String normalize(String input) {
        if (input == null) {
            throw new IllegalArgumentException(
                "Move input cannot be null."
            );
        }
    
        return input.trim()
                    .replaceAll("\\s+", " ")
                    .toUpperCase(Locale.ROOT);
    }

    /**
     * Checks if a move follows the basic chess format (e.g., "E2 E4").
     * This validates input format, not whether the move is legal in chess.
     *
     * @param normalizedInput The normalized move string.
     * @return True if the format is valid, false otherwise.
     */
    public static boolean isValidMoveFormat(String normalizedInput) {
        if (normalizedInput == null) {
            return false;
        }
    
        return normalizedInput.matches(
            "^[A-H][1-8] [A-H][1-8]$"
        );
    }

    /**
     * Converts a chessboard coordinate into a Position object.
     * For example, "E2" becomes row 1 and column 4.
     * Coordinates must be uppercase and within the A1-H8 range.
     *
     * @param posStr The uppercase chess coordinate to convert.
     * @return A Position object containing the row and column.
     * @throws IllegalArgumentException if the coordinate is null,
     *         incorrectly formatted, or outside the chessboard.
     */
    public static Position parsePosition(String posStr) {
        if (posStr == null ||
            !posStr.matches("^[A-H][1-8]$")) {
    
            throw new IllegalArgumentException(
                "Invalid board position: " + posStr
            );
        }
    
        int column = posStr.charAt(0) - 'A';
        int row = posStr.charAt(1) - '1';
    
        return new Position(row, column);
    }

    /**
     * Converts a chess move string into a Move object.
     * For example, "E2 E4" creates a move from E2 to E4.
     * The input is normalized and validated before parsing.
     * Only basic coordinate moves are supported in Phase One.
     *
     * @param moveStr The raw move input entered by the player.
     * @return A Move object containing the starting and ending positions.
     * @throws IllegalArgumentException if the input is null,
     *         incorrectly formatted, or contains invalid coordinates.
     */
    public static Move parseMove(String moveStr) {
        String normalized = normalize(moveStr);
    
        if (!isValidMoveFormat(normalized)) {
            throw new IllegalArgumentException(
                "Invalid move format: " + normalized
            );
        }
    
        String[] coordinates = normalized.split(" ");
    
        Position from = parsePosition(coordinates[0]);
        Position to = parsePosition(coordinates[1]);
    
        return new Move(from, to);
    }
}

