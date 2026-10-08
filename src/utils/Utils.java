package utils;

import board.Position;
import core.Move;

/**
 * Utility functions for parsing and validating moves.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Utils {

    /**
     * Normalizes a string by trimming, collapsing spaces, and converting to uppercase.
     * 
     * @param input Raw input string.
     * @return Normalized string.
     * TODO(Dev 3): Implement normalize()
     */
    public static String normalize(String input) {
        return null;
    }

    /**
     * Checks if a move string matches standard formats (e.g., "E2 E4", "O-O", "E7 E8=Q").
     * 
     * @param normalizedInput The normalized move string.
     * @return True if valid format.
     * TODO(Dev 3): Implement isValidMoveFormat() with regex
     */
    public static boolean isValidMoveFormat(String normalizedInput) {
        return false;
    }

    /**
     * Parses a string like "E2" into a Position object.
     * 
     * @param posStr The square coordinate string.
     * @return Position object.
     * TODO(Dev 3): Implement parsePosition()
     */
    public static Position parsePosition(String posStr) {
        return null;
    }

    /**
     * Parses a full move string into a Move object.
     * 
     * @param moveStr The valid, normalized move string.
     * @return Parsed Move object.
     * TODO(Dev 3): Implement parseMove()
     */
    public static Move parseMove(String moveStr) {
        return null;
    }
}

