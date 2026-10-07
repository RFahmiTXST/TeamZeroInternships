package utils;

import board.Position;
import core.Move;

/**
 * Static helpers, mainly for checking and parsing the moves players type.
 *
 * <p>Supported input (case-insensitive, extra spaces ignored):
 * <ul>
 *   <li>{@code "E2 E4"}: move or capture from E2 to E4 (required)</li>
 *   <li>{@code "E7 E8=Q"}: pawn promotion to Q, R, B or N (optional)</li>
 *   <li>{@code "O-O"} / {@code "O-O-O"}: kingside / queenside castling. Zeros ("0-0") are accepted too (optional)</li>
 * </ul>
 *
 * <p>Owner: Developer 3.
 */
public final class Utils {

    /**
     * Not used. This class only has static methods.
     */
    private Utils() {
    }

    /**
     * Cleans up raw input: trims it, collapses repeated spaces and converts it to upper case.
     *
     * @param input the raw line typed by the player; may be {@code null}
     * @return the cleaned-up text, or {@code null} if {@code input} was {@code null}
     */
    public static String normalize(String input) {
        // TODO(Dev 3): input.trim().replaceAll("\\s+", " ").toUpperCase()
        return input;
    }

    /**
     * Basic Phase 1 check: tells whether the input has a supported move format. It does not check
     * whether the move is legal.
     *
     * @param input the raw line typed by the player; may be {@code null}
     * @return {@code true} if the input matches one of the supported formats
     */
    public static boolean isValidMoveFormat(String input) {
        // TODO(Dev 3): normalize, then match "^[A-H][1-8] [A-H][1-8]$"
        //  (optional) or "^[A-H][1-8] [A-H][1-8]=[QRBN]$" or castling "^(O-O|O-O-O|0-0|0-0-0)$".
        return false;
    }

    /**
     * Converts a square such as "E2" into a {@link Position}.
     *
     * @param square a file letter A-H followed by a rank 1-8, in either case
     * @return the matching position, such as {@code new Position(1, 4)} for "E2"
     * @throws IllegalArgumentException if {@code square} is not a valid square
     */
    public static Position parsePosition(String square) {
        // TODO(Dev 3): column = letter - 'A', row = digit - '1'; validate first.
        throw new UnsupportedOperationException("Utils.parsePosition is not implemented yet");
    }

    /**
     * Converts a line in a valid format into a {@link Move}.
     * Call {@link #isValidMoveFormat(String)} first.
     *
     * @param input the raw line typed by the player
     * @return the parsed move
     * @throws IllegalArgumentException if the input is not in a supported format
     */
    public static Move parseMove(String input) {
        // TODO(Dev 3): castling -> Move.castle(kingside); "E7 E8=Q" -> new Move(from, to, 'Q');
        //  otherwise new Move(parsePosition(parts[0]), parsePosition(parts[1])).
        throw new UnsupportedOperationException("Utils.parseMove is not implemented yet");
    }
}
