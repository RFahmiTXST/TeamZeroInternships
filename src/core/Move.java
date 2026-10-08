package core;

import board.Position;

/**
 * Represents a single move command, including starting square, ending square, and special flags.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Move {
    private Position from;
    private Position to;
    private char promotionPiece;
    private boolean isCastling;
    private boolean isKingsideCastle;

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    public Move(Position from, Position to, char promotionPiece) {
        this.from = from;
        this.to = to;
        this.promotionPiece = promotionPiece;
    }

    public static Move castle(boolean kingside) {
        Move m = new Move(null, null);
        m.isCastling = true;
        m.isKingsideCastle = kingside;
        return m;
    }

    public Position getFrom() { return from; }
    public Position getTo() { return to; }
    public char getPromotionPiece() { return promotionPiece; }
    public boolean isCastling() { return isCastling; }
    public boolean isKingsideCastle() { return isKingsideCastle; }
    
    /**
     * Reconstructs the move string from this object (e.g., "E2 E4").
     * 
     * @return Move string notation.
     * TODO(Dev 3): Implement toString()
     */
    @Override
    public String toString() {
        return null;
    }
}

