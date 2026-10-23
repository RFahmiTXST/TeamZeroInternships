package core;

import board.Position;

/**
 * Represents a single move command, including starting square, ending square, and special flags.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Move {
    /** Starting square, null for castling. */
    private Position from;
    /** Destination square, null for castling. */
    private Position to;
    /** Promotion symbol, or the null character for an ordinary move. */
    private char promotionPiece;
    /** Whether this request is castling. */
    private boolean isCastling;
    /** Whether castling uses the kingside rook. */
    private boolean isKingsideCastle;

    /**
     * Creates a coordinate move; validation occurs when it is applied.
     * @param from starting square
     * @param to destination square
     */
    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a promotion request.
     * @param from starting square
     * @param to destination square
     * @param promotionPiece requested promotion symbol
     */
    public Move(Position from, Position to, char promotionPiece) {
        this.from = from;
        this.to = to;
        this.promotionPiece = promotionPiece;
    }

    /**
     * Creates a castling request.
     * @param kingside true for kingside, false for queenside
     * @return castling move
     */
    public static Move castle(boolean kingside) {
        Move m = new Move(null, null);
        m.isCastling = true;
        m.isKingsideCastle = kingside;
        return m;
    }

    /** Returns the starting square.
     * @return starting square, or null for castling */
    public Position getFrom() { return from; }
    /** Returns the destination square.
     * @return destination square, or null for castling */
    public Position getTo() { return to; }
    /** Returns the requested promotion symbol.
     * @return promotion symbol, or the null character if absent */
    public char getPromotionPiece() { return promotionPiece; }
    /** Identifies a castling request.
     * @return whether this is a castling request */
    public boolean isCastling() { return isCastling; }
    /** Identifies kingside castling.
     * @return whether the castling request is kingside */
    public boolean isKingsideCastle() { return isKingsideCastle; }
    
    /**
     * Reconstructs the move string from this object (e.g., "E2 E4").
     * 
     * @return Move string notation.
     */
    @Override
    public String toString() {
        if (isCastling) {
            return isKingsideCastle ? "O-O" : "O-O-O";
        }
        String notation = from + " " + to;
        return promotionPiece == '\0' ? notation
                : notation + "=" + Character.toUpperCase(promotionPiece);
    }
}

