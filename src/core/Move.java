package core;

import board.Position;

/**
 * One move a player has entered, already parsed from text such as "E2 E4", "E7 E8=Q" or "O-O".
 *
 * <p>A {@code Move} only describes what the player asked for. It does not check whether the move
 * is legal. {@link utils.Utils#parseMove(String)} creates moves, and {@link Player} and
 * {@link Game} carry them out.
 *
 * <p>Owner: Developer 3.
 */
public class Move {

    /**
     * The kinds of move a player can enter.
     */
    public enum Type {
        /** An ordinary move or capture, such as "E2 E4". */
        NORMAL,
        /** A pawn move to the last rank with a chosen piece, such as "E7 E8=Q". */
        PROMOTION,
        /** Kingside castling, "O-O". */
        CASTLE_KINGSIDE,
        /** Queenside castling, "O-O-O". */
        CASTLE_QUEENSIDE
    }

    /** Value of {@link #promotionSymbol} when the move is not a promotion. */
    public static final char NO_PROMOTION = '\0';

    /** The starting square, or {@code null} for castling moves. */
    private final Position from;

    /** The destination square, or {@code null} for castling moves. */
    private final Position to;

    /** The kind of move. */
    private final Type type;

    /** The piece letter a pawn is promoted to ('Q', 'R', 'B' or 'N'), or {@link #NO_PROMOTION}. */
    private final char promotionSymbol;

    /**
     * Creates an ordinary move from one square to another.
     *
     * @param from the starting square
     * @param to   the destination square
     */
    public Move(Position from, Position to) {
        this(from, to, Type.NORMAL, NO_PROMOTION);
    }

    /**
     * Creates a pawn-promotion move.
     *
     * @param from            the starting square
     * @param to              the destination square on the last rank
     * @param promotionSymbol the piece letter to promote to ('Q', 'R', 'B' or 'N')
     */
    public Move(Position from, Position to, char promotionSymbol) {
        this(from, to, Type.PROMOTION, promotionSymbol);
    }

    /**
     * Shared constructor used by the public constructors and {@link #castle(boolean)}.
     *
     * @param from            the starting square, or {@code null}
     * @param to              the destination square, or {@code null}
     * @param type            the kind of move
     * @param promotionSymbol the promotion letter, or {@link #NO_PROMOTION}
     */
    private Move(Position from, Position to, Type type, char promotionSymbol) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.promotionSymbol = promotionSymbol;
    }

    /**
     * Creates a castling move. The squares are left {@code null} because they depend on which
     * side is moving. The game fills them in when it carries out the move.
     *
     * @param kingside {@code true} for "O-O", {@code false} for "O-O-O"
     * @return the castling move
     */
    public static Move castle(boolean kingside) {
        return new Move(null, null, kingside ? Type.CASTLE_KINGSIDE : Type.CASTLE_QUEENSIDE, NO_PROMOTION);
    }

    /**
     * Returns the starting square.
     *
     * @return the starting square, or {@code null} for castling
     */
    public Position getFrom() {
        return from;
    }

    /**
     * Returns the destination square.
     *
     * @return the destination square, or {@code null} for castling
     */
    public Position getTo() {
        return to;
    }

    /**
     * Returns the kind of move.
     *
     * @return the move type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the piece letter for a promotion.
     *
     * @return 'Q', 'R', 'B' or 'N', or {@link #NO_PROMOTION} if this is not a promotion
     */
    public char getPromotionSymbol() {
        return promotionSymbol;
    }

    /**
     * Tells whether this is a castling move.
     *
     * @return {@code true} for kingside or queenside castling
     */
    public boolean isCastling() {
        return type == Type.CASTLE_KINGSIDE || type == Type.CASTLE_QUEENSIDE;
    }

    /**
     * Returns the move in the notation players type, such as "E2 E4", "E7 E8=Q" or "O-O".
     *
     * @return the move as text
     */
    @Override
    public String toString() {
        // TODO(Dev 3): format according to type (depends on Position.toString() from Dev 2).
        return type + " " + from + " -> " + to;
    }
}