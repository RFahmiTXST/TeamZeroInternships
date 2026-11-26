package gui;

/**
 * Immutable description of a completed GUI move, independent of mutable pieces.
 * The move number counts individual moves.
 * @param number one-based move number
 * @param side color of the piece actually moved
 * @param piece piece type
 * @param from original square in chess notation
 * @param to destination square in chess notation
 * @param captured captured color and type, or empty text for an ordinary move
 * @param givesCheck whether this move put the opposing king in check
 */
public record HistoryEntry(int number, String side, String piece,
                           String from, String to, String captured, boolean givesCheck) {
    /**
     * Constructs an entry without a recorded check marker.
     * @param number one-based move number
     * @param side moving side
     * @param piece moving piece type
     * @param from source square
     * @param to destination square
     * @param captured captured piece description
     */
    public HistoryEntry(int number, String side, String piece, String from, String to, String captured) {
        this(number, side, piece, from, to, captured, false);
    }

    /**
     * Formats the move for the history table, which also lists its source square.
     * @return piece or pawn capture prefix, destination, and optional check marker
     */
    public String notation() {
        String prefix = switch (piece) {
            case "Pawn" -> captured.isEmpty() ? "" : from.substring(0, 1).toLowerCase(java.util.Locale.ROOT);
            case "Knight" -> "N";
            default -> piece.substring(0, 1);
        };
        return prefix + (captured.isEmpty() ? "" : "x") + to.toLowerCase(java.util.Locale.ROOT)
                + (givesCheck ? "+" : "");
    }
}
