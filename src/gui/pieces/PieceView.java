package gui.pieces;

import pieces.Color;
import pieces.Piece;

import javax.swing.Icon;

/**
 * Represents the visual presentation and metadata of a chess piece in the GUI.
 *
 * <p>Encapsulates visual styling, display labels, accessibility descriptions,
 * and icon resolution for rendering pieces across the GUI board and side panels.</p>
 *
 * @author Dev 1 (Rayed)
 */
public class PieceView {

    /** The underlying domain piece model, or null if representing an empty view. */
    private final Piece piece;

    /**
     * Constructs a PieceView wrapping the specified domain piece.
     *
     * @param piece the chess piece to represent visually; may be null for an empty square
     */
    public PieceView(Piece piece) {
        this.piece = piece;
    }

    /**
     * Gets the wrapped domain piece model.
     *
     * @return the Piece instance, or null if empty
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Checks if this view represents an active piece.
     *
     * @return true if a piece is present, false otherwise
     */
    public boolean hasPiece() {
        return piece != null;
    }

    /**
     * Gets the color of the piece.
     *
     * @return the piece Color, or null if empty
     */
    public Color getColor() {
        return piece != null ? piece.getColor() : null;
    }

    /**
     * Gets the character symbol representing the piece type.
     *
     * @return symbol character (e.g., 'K', 'Q', 'p'), or ' ' if empty
     */
    public char getSymbol() {
        return piece != null ? piece.getSymbol() : ' ';
    }

    /**
     * Returns a human-readable display name for the piece.
     *
     * @return formatted name such as "White Queen" or "Black Knight"
     */
    public String getDisplayName() {
        if (piece == null) {
            return "Empty";
        }
        String colorName = piece.getColor() == Color.WHITE ? "White" : "Black";
        char sym = Character.toUpperCase(piece.getSymbol());
        String typeName;
        switch (sym) {
            case 'K': typeName = "King"; break;
            case 'Q': typeName = "Queen"; break;
            case 'R': typeName = "Rook"; break;
            case 'B': typeName = "Bishop"; break;
            case 'N': typeName = "Knight"; break;
            case 'P': typeName = "Pawn"; break;
            default:  typeName = "Piece"; break;
        }
        return colorName + " " + typeName;
    }

    /**
     * Retrieves the visual icon for this piece at the default icon size.
     *
     * @return an Icon instance, or null if empty
     */
    public Icon getIcon() {
        return getIcon(PieceIconManager.DEFAULT_SIZE);
    }

    /**
     * Retrieves the visual icon for this piece scaled to the specified dimension.
     *
     * @param size icon width and height in pixels
     * @return an Icon instance, or null if empty
     */
    public Icon getIcon(int size) {
        if (piece == null) {
            return null;
        }
        return PieceIconManager.getPieceIcon(piece, size);
    }

    /**
     * Returns a string representation of the piece view.
     *
     * @return human-readable representation
     */
    @Override
    public String toString() {
        return getDisplayName() + (piece != null ? " at " + piece.getPosition() : "");
    }
}

