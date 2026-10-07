package core;

import board.Board;
import pieces.Color;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One of the two people playing. A player has a color and keeps track of the pieces it still has.
 *
 * <p>Owner: Developer 3.
 */
public class Player {

    /** The side this player controls. */
    private final Color color;

    /** This player's pieces that have not been captured yet. */
    private final List<Piece> availablePieces;

    /**
     * Creates a player with no pieces. {@link Game#start()} gives each player its pieces once the
     * board is set up.
     *
     * @param color the side this player controls
     */
    public Player(Color color) {
        this.color = color;
        this.availablePieces = new ArrayList<>();
    }

    /**
     * Returns the side this player controls.
     *
     * @return the player's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the pieces this player still has.
     *
     * @return a read-only view of the uncaptured pieces
     */
    public List<Piece> getAvailablePieces() {
        return Collections.unmodifiableList(availablePieces);
    }

    /**
     * Replaces this player's piece list, for example with {@code board.getPieces(color)} after setup.
     *
     * @param pieces the pieces this player now owns
     */
    public void setAvailablePieces(List<Piece> pieces) {
        // TODO(Dev 3): clear availablePieces and add all of pieces.
    }

    /**
     * Removes a piece that the opponent has captured.
     *
     * @param piece the captured piece
     */
    public void removePiece(Piece piece) {
        // TODO(Dev 3)
    }

    /**
     * Tries to carry out a move for this player on the board.
     *
     * <p>Phase 1 checks that the starting square holds one of this player's own pieces. The stretch
     * goal is to also check that the destination is in that piece's
     * {@link Piece#possibleMoves(Board) possibleMoves}.
     *
     * @param board the board to move on
     * @param move  the parsed move
     * @return {@code true} if the move was made, {@code false} if it was rejected
     */
    public boolean makeMove(Board board, Move move) {
        // TODO(Dev 3): look up board.getPiece(move.getFrom()); reject if null or not this player's color;
        //  (stretch) reject if !piece.possibleMoves(board).contains(move.getTo());
        //  otherwise board.movePiece(from, to) and return true.
        return false;
    }
}
