package gui;

/**
 * Immutable description of a completed GUI move, independent of mutable pieces.
 * The move number counts individual moves, including unrestricted same-color moves.
 * @param number one-based move number
 * @param side color of the piece actually moved
 * @param piece piece type
 * @param from original square in chess notation
 * @param to destination square in chess notation
 * @param captured captured color and type, or empty text for an ordinary move
 */
public record HistoryEntry(int number, String side, String piece,
                           String from, String to, String captured) { }
