package core;

import board.Board;
import pieces.Color;
import ui.ConsoleUI;
import utils.Utils;

/**
 * The main game controller that manages the loop, turns, and game state.
 * 
 * Developer: Dev 3 (Sabid)
 */
public class Game {
    /** Board shared by both players. */
    private final Board board;
    /** White player. */
    private final Player playerWhite;
    /** Black player. */
    private final Player playerBlack;
    /** Side that must supply the next valid move. */
    private Color currentTurn;
    /** Console input and output. */
    private final ConsoleUI ui;
    /** Whether the game loop should continue. */
    private boolean running;

    /**
     * Creates a new chess game.
     *
     * @param ui console interface for input and output
     */
    public Game(ConsoleUI ui) {
        this.board = new Board();
        this.playerWhite = new Player(Color.WHITE);
        this.playerBlack = new Player(Color.BLACK);
        this.ui = ui;
    }

    /**
     * Initializes the board and starts the game loop.
     * 
     */
    public void start() {
        board.initialize();
        currentTurn = Color.WHITE;
        running = true;
        ui.showWelcome();
        play();
    }

    /**
     * The main interactive loop for playing the game.
     * 
     * Invalid input or movement keeps the same player's turn.
     * Call start() to initialize a new game before entering this loop.
     */
    public void play() {
        while (running) {
            ui.showBoard(board);
            ui.showTurn(currentTurn);
            String input = ui.promptMove(currentTurn);
            if (input == null) {
                end("Game ended: input closed.");
                continue;
            }
            String normalized = Utils.normalize(input);
            if (normalized.equals("QUIT") || normalized.equals("EXIT")) {
                end("Game ended by player.");
                continue;
            }
            if (!Utils.isValidMoveFormat(normalized)) {
                ui.showError("Enter a move such as E2 E4, or quit/exit.");
                continue;
            }
            Move move = Utils.parseMove(normalized);
            Player player = currentTurn == Color.WHITE ? playerWhite : playerBlack;
            if (!player.makeMove(board, move)) {
                ui.showError("Invalid move for this player or piece. Try again.");
                continue;
            }
            switchTurn();
        }
    }

    /**
     * Ends the game and displays the result.
     * 
     * @param result Message describing how the game ended.
     */
    public void end(String result) {
        running = false;
        ui.showResult(result);
    }

    /**
     * Switches the active turn.
     * 
     */
    private void switchTurn() {
        currentTurn = currentTurn == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}

