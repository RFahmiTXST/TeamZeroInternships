
package core;

import board.Position;

/**
 * Represents a chess move, including normal moves,
 * Pawn promotion, and Castling.
 *
 * Developer: Dev 3 (Sabid)
 */
public class Move {

    /** Starting position of the move. */
    private Position from;

    /** Destination position of the move. */
    private Position to;

    /** Piece selected for Pawn promotion, or zero if none. */
    private char promotionPiece;

    /** Whether this move represents Castling. */
    private boolean isCastling;

    /** Whether Castling is on the kingside. */
    private boolean isKingsideCastle;

    /**
     * Creates a normal chess move.
     *
     * @param from starting position
     * @param to destination position
     */
    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a move with Pawn promotion.
     *
     * @param from starting position
     * @param to destination position
     * @param promotionPiece requested promotion piece
     */
    public Move(Position from, Position to, char promotionPiece) {
        this.from = from;
        this.to = to;
        this.promotionPiece = promotionPiece;
    }

    /**
     * Creates a Castling move.
     *
     * @param kingside true for kingside, false for queenside
     * @return Castling move
     */
    public static Move castle(boolean kingside) {
        Move m = new Move(null, null);
        m.isCastling = true;
        m.isKingsideCastle = kingside;
        return m;
    }

    /**
     * Returns the starting position.
     *
     * @return starting position
     */
    public Position getFrom() { return from; }

    /**
     * Returns the destination position.
     *
     * @return destination position
     */
    public Position getTo() { return to; }

    /**
     * Returns the Pawn promotion piece code.
     *
     * @return promotion piece code, or zero if none
     */
    public char getPromotionPiece() { return promotionPiece; }

    /**
     * Checks whether this move represents Castling.
     *
     * @return true if the move is Castling
     */
    public boolean isCastling() { return isCastling; }

    /**
     * Checks whether the Castling direction is kingside.
     *
     * @return true for kingside Castling
     */
    public boolean isKingsideCastle() { return isKingsideCastle; }

    /**
     * Returns the move notation.
     *
     * @return move notation, currently not implemented
     */
    @Override
    public String toString() {
        return null;
    }
}
