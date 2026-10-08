package core;

import board.Board;
import pieces.Color;
import ui.ConsoleUI;

/**
 * The main game controller that manages the loop, turns, and game state.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Game {
    private Board board;
    private Player playerWhite;
    private Player playerBlack;
    private Color currentTurn;
    private ConsoleUI ui;
    private boolean running;

    public Game(ConsoleUI ui) {
        this.board = new Board();
        this.playerWhite = new Player(Color.WHITE);
        this.playerBlack = new Player(Color.BLACK);
        this.ui = ui;
    }

    /**
     * Initializes the board and starts the game loop.
     * 
     * TODO(Dev 3): Implement start()
     */
    public void start() {
    }

    /**
     * The main interactive loop for playing the game.
     * 
     * TODO(Dev 3): Implement play() to alternate turns and prompt players.
     */
    public void play() {
    }

    /**
     * Ends the game and displays the result.
     * 
     * @param result Message describing how the game ended.
     * TODO(Dev 3): Implement end()
     */
    public void end(String result) {
    }

    /**
     * Switches the active turn.
     * 
     * TODO(Dev 3): Implement switchTurn()
     */
    private void switchTurn() {
    }
}

