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
    /** Creates a utility instance. */
    public Utils() { }

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
     * Validates normal moves, promotion, and castling notation.
     *
     * @param normalizedInput normalized move text
     * @return true if the input follows a supported format
     */
    public static boolean isValidMoveFormat(String normalizedInput) {
        if (normalizedInput == null) {
            return false;
        }

        return normalizedInput.matches(
                "^[A-H][1-8] [A-H][1-8](=[QRBN])?$")
                || normalizedInput.equals("O-O")
                || normalizedInput.equals("O-O-O");
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
     * Parses normal moves, Pawn promotion, and castling.
     *
     * @param moveStr move entered by the player
     * @return parsed Move object
     * @throws IllegalArgumentException if the format is invalid
     */
    public static Move parseMove(String moveStr) {
        String normalized = normalize(moveStr);

        if (!isValidMoveFormat(normalized)) {
            throw new IllegalArgumentException(
                    "Invalid move format: " + normalized);
        }

        if (normalized.equals("O-O")) {
            return Move.castle(true);
        }

        if (normalized.equals("O-O-O")) {
            return Move.castle(false);
        }

        String[] coordinates = normalized.split(" ");

        Position from = parsePosition(coordinates[0]);

        String destination = coordinates[1];
        Position to = parsePosition(destination.substring(0, 2));

        if (destination.length() == 4) {
            char promotionCode = destination.charAt(3);
            return new Move(from, to, promotionCode);
        }

        return new Move(from, to);
    }

}

