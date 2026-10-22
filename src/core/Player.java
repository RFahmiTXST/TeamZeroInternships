package core;

import pieces.Color;
import pieces.Piece;
import board.Board;
import board.Position;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a player in the game.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Player {
    private Color color;
    private List<Piece> availablePieces;

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
     * squares, same-square moves, friendly captures, and special moves.
     * Requires the destination to appear in possibleMoves(board).
     * King safety is not enforced until the team's check integration is ready.
     * 
     * @param board The current board.
     * @param move The requested move.
     * @return true if the move passes validation and is applied to Board
     */
    public boolean makeMove(Board board, Move move) {
        if (board == null || move == null || move.isCastling()
                || move.getPromotionPiece() != '\0') {
            return false;
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
        board.movePiece(from, to);
        return true;
    }

    public Color getColor() {
        return color;
    }
}

