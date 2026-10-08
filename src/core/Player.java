package core;

import pieces.Color;
import pieces.Piece;
import board.Board;
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
     * Attempts to execute a move for this player.
     * 
     * @param board The current board.
     * @param move The requested move.
     * @return True if the move is legal and executed, false otherwise.
     * TODO(Dev 3, Dev 1): Implement makeMove() to validate and apply the move.
     */
    public boolean makeMove(Board board, Move move) {
        return false;
    }

    public Color getColor() {
        return color;
    }
}

