package core;

import pieces.Color;
import pieces.Piece;
import board.Board;
import board.Position;
import java.util.List;
import java.util.ArrayList;
import pieces.Pawn;

/**
 * Represents a player in the game.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Player {
    /** Side controlled by this player. */
    private Color color;
    /** Optional piece list supplied by the caller; Board is authoritative for moves. */
    private List<Piece> availablePieces;

    /** Creates a player.
     * @param color owning side
     */
    public Player(Color color) {
        this.color = color;
        this.availablePieces = new ArrayList<>();
    }

    /**
     * Sets the player's pieces currently on the board.
     * 
     * @param pieces List of active pieces.
     */
    public void setAvailablePieces(List<Piece> pieces) {
        this.availablePieces = pieces;
    }

    /**
     * Applies a move matching this player's piece movement pattern.
     * Rejects missing/off-board coordinates, empty or opponent-owned source
     * squares, same-square moves, and friendly captures.
     * Castling and promotion are delegated to SpecialMoveHandler.
     * Enforces move legality by requiring the destination to appear in
     * the piece's possibleMoves(board) list (Milestone 3 Dev 1).
     * Ordinary-move king safety is not yet enforced.
     * 
     * @param board The current board.
     * @param move The requested move.
     * @return true if the move passes validation and is applied to Board
     */
    public boolean makeMove(Board board, Move move) {

        if (board == null || move == null) {
            return false;
        }

        // Handle kingside and queenside castling.
        if (move.isCastling()) {
            return SpecialMoveHandler.executeCastling(
                    board, move, color
            );
        }

        // Handle Pawn Promotion.
        if (move.getPromotionPiece() != '\0') {
            return SpecialMoveHandler.executePromotion(
                    board, move, color
            );
        }
        Position from = move.getFrom();
        Position to = move.getTo();
        if (from == null || to == null || !from.isOnBoard() || !to.isOnBoard()
                || from.equals(to)) {
            return false;
        }
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != color) {
            return false;
        }
        Piece target = board.getPiece(to);
        if (target != null && target.getColor() == color) {
            return false;
        }

        List<Position> possibleMoves = piece.possibleMoves(board);

        if (possibleMoves == null || !possibleMoves.contains(to)) {
            return false;
        }

        // A Pawn reaching the final rank must promote.
        // Do not leave an ordinary Pawn on the promotion square.
        if (piece instanceof Pawn
                && to.getRow() == ((Pawn) piece).getPromotionRow()) {
            return false;
        }

        board.movePiece(from, to);
        return true;

    }

    /** Returns this player's color.
     * @return this player's side */
    public Color getColor() {
        return color;
    }
}
