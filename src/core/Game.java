package core;

import board.Board;
import pieces.Color;
import ui.ConsoleUI;

/**
 * Controls one game of chess: owns the board and both players, keeps track of whose turn it is,
 * and runs the main loop.
 *
 * <p>Owner: Developer 3.
 */
public class Game {

    /** The board being played on. */
    private final Board board;

    /** The player with the white pieces. */
    private final Player whitePlayer;

    /** The player with the black pieces. */
    private final Player blackPlayer;

    /** The console used to show output and read moves. */
    private final ConsoleUI ui;

    /** The side whose turn it is. */
    private Color currentTurn;

    /** {@code true} while the main loop should keep asking for moves. */
    private boolean running;

    /**
     * Creates a game that reads from and writes to the standard console.
     */
    public Game() {
        this(new ConsoleUI());
    }

    /**
     * Creates a game that uses the given console. Tests can pass in a {@link ConsoleUI} that reads
     * from a prepared list of moves.
     *
     * @param ui the console to use
     */
    public Game(ConsoleUI ui) {
        this.board = new Board();
        this.whitePlayer = new Player(Color.WHITE);
        this.blackPlayer = new Player(Color.BLACK);
        this.ui = ui;
        this.currentTurn = Color.WHITE;
        this.running = false;
    }

    /**
     * Sets up a new game and starts the main loop.
     */
    public void start() {
        // TODO(Dev 3): board.initialize(); give each player board.getPieces(color); currentTurn = WHITE;
        //  running = true; ui.showWelcome(); play();
        ui.showMessage("Chess template is wired up. Game.start() is not implemented yet.");
    }

    /**
     * The main loop. Each turn it shows the board, says whose turn it is, asks for a move, checks the
     * move's format, carries it out, and switches turns. Invalid input shows an error and asks again.
     */
    public void play() {
        // TODO(Dev 3): while (running) {
        //    ui.showBoard(board); ui.showTurn(currentTurn);
        //    String input = ui.promptMove(currentTurn);
        //    null (end of input) or "quit" -> end(...)
        //    !Utils.isValidMoveFormat(input) -> ui.showError(...) and continue
        //    Move move = Utils.parseMove(input);
        //    getCurrentPlayer().makeMove(board, move) ? (update the opponent's pieces on capture, switchTurn())
        //                                              : ui.showError(...)
        //    (stretch) board.isCheckmate / isStalemate -> end(...)
        //  }
    }

    /**
     * Ends the game and announces the result.
     *
     * @param result the message to show, such as "White wins by checkmate." or "Draw by stalemate."
     */
    public void end(String result) {
        // TODO(Dev 3): running = false; show the final board and the result.
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return the white or black player
     */
    public Player getCurrentPlayer() {
        return currentTurn == Color.WHITE ? whitePlayer : blackPlayer;
    }

    /**
     * Returns the side whose turn it is.
     *
     * @return the current turn's color
     */
    public Color getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Returns the board, which is useful in tests.
     *
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gives the turn to the other side.
     */
    private void switchTurn() {
        currentTurn = currentTurn.opposite();
    }
}
