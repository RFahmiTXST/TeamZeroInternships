package gui;

import board.Board;
import board.Position;
import pieces.Bishop;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Writes and reads a versioned, human-readable .chess game file.
 * No Java object deserialization or external libraries are used.
 *
 * <p>Each file contains piece positions and moved flags, captured pieces,
 * active turn, and the chronological GUI move history. Loading creates an
 * independent Board and does not mutate the current session.</p>
 *
 * @author Dev 4 (Towsif)
 */
public final class GameFileService {
    /** File format version. */
    private static final String FORMAT = "TEAMZERO_CHESS_V1";
    /** Maximum supported size of an input file. */
    private static final long MAX_FILE_BYTES = 2L * 1024 * 1024;
    /** Upper limit to avoid allocating unbounded history from malformed files. */
    private static final int MAX_HISTORY = 20000;
    /** Maximum number of captured pieces in a game. */
    private static final int MAX_CAPTURED = 64;

    /** Prevents instantiation. */
    private GameFileService() { }

    /**
     * Immutable references to the data being saved or restored. The Board
     * itself remains mutable, so capture and write this state on the Swing EDT.
     *
     * @param board the board model, including recorded captures
     * @param turn player whose turn is next
     * @param history ordered history entries
     */
    public record GameState(Board board, Color turn, List<HistoryEntry> history) {
        /** Checks inputs and copies history. */
        public GameState {
            Objects.requireNonNull(board, "board");
            Objects.requireNonNull(turn, "turn");
            history = List.copyOf(Objects.requireNonNull(history, "history"));
        }
    }

    /**
     * Captures state from a GUI controller; call on the Swing EDT.
     *
     * @param controller active GUI controller
     * @return state ready for saving
     */
    public static GameState snapshot(GUIController controller) {
        Objects.requireNonNull(controller, "controller");
        return new GameState(controller.getBoard(), controller.getCurrentTurn(),
                controller.getHistory());
    }

    /**
     * Saves the current state to a file, using a temporary file and replacement
     * to avoid leaving a partially written destination after a write failure.
     *
     * @param destination path to the .chess file
     * @param state state to serialize
     * @throws IOException if the file cannot be written
     */
    public static void save(Path destination, GameState state) throws IOException {
        Objects.requireNonNull(destination, "destination");
        Objects.requireNonNull(state, "state");
        Properties data = toProperties(state);
        Path target = destination.toAbsolutePath().normalize();
        Path temp = Files.createTempFile(target.getParent(), ".teamzero-", ".tmp");
        try {
            try (Writer writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                data.store(writer, "Team Zero Chess save file");
            }
            try {
                Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    /**
     * Parses a saved game without modifying the running game. The caller must
     * only replace the current session after this method succeeds.
     *
     * @param source path to a previously saved .chess file
     * @return fully parsed game state
     * @throws IOException if the file is missing, unreadable, or invalid
     */
    public static GameState load(Path source) throws IOException {
        Objects.requireNonNull(source, "source");
        if (Files.size(source) > MAX_FILE_BYTES) {
            throw new IOException("Save file exceeds the supported size limit.");
        }
        Properties data = new Properties();
        try (Reader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8)) {
            data.load(reader);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Malformed save file.", exception);
        }
        if (!FORMAT.equals(data.getProperty("format"))) {
            throw new IOException("Unsupported or invalid chess save format.");
        }

        Color turn = parseColor(required(data, "turn"));
        Board board = new Board();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String value = required(data, "square." + row + "." + col);
                if (!".".equals(value)) {
                    Piece piece = parsePiece(value);
                    if (piece.getPosition().getRow() != row
                            || piece.getPosition().getColumn() != col) {
                        throw new IOException("Piece position does not match its board square.");
                    }
                    board.setPiece(new Position(row, col), piece);
                }
            }
        }

        int captures = parseCount(required(data, "captured.count"), MAX_CAPTURED);
        for (int i = 0; i < captures; i++) {
            board.recordCapture(parsePiece(required(data, "captured." + i)));
        }
        int historyCount = parseCount(required(data, "history.count"), MAX_HISTORY);
        List<HistoryEntry> history = new ArrayList<>();
        for (int i = 0; i < historyCount; i++) {
            history.add(parseHistory(required(data, "history." + i), i + 1));
        }
        return new GameState(board, turn, history);
    }

    /**
     * Builds a properties representation from a model snapshot.
     * @param state current session data
     * @return serialized key/value fields
     * @throws IOException if the data is not representable
     */
    private static Properties toProperties(GameState state) throws IOException {
        Properties data = new Properties();
        data.setProperty("format", FORMAT);
        data.setProperty("turn", state.turn().name());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = state.board().getPiece(new Position(row, col));
                data.setProperty("square." + row + "." + col,
                        piece == null ? "." : encodePiece(piece));
            }
        }
        List<Piece> captured = state.board().getCapturedPieces();
        if (captured.size() > MAX_CAPTURED || state.history().size() > MAX_HISTORY) {
            throw new IOException("Game contains more entries than the save format supports.");
        }
        data.setProperty("captured.count", Integer.toString(captured.size()));
        for (int i = 0; i < captured.size(); i++) {
            data.setProperty("captured." + i, encodePiece(captured.get(i)));
        }
        data.setProperty("history.count", Integer.toString(state.history().size()));
        for (int i = 0; i < state.history().size(); i++) {
            data.setProperty("history." + i, encodeHistory(state.history().get(i), i + 1));
        }
        return data;
    }

    /**
     * Encodes a piece without serializing executable Java objects.
     * @param piece piece being stored
     * @return encoded color, type, coordinates and moved flag
     */
    private static String encodePiece(Piece piece) {
        Position pos = piece.getPosition();
        return piece.getColor().name() + "|" + piece.getClass().getSimpleName()
                + "|" + pos.getRow() + "|" + pos.getColumn()
                + "|" + piece.hasMoved();
    }

    /**
     * Creates a piece with its saved position and movement flag.
     * @param encoded piece fields
     * @return restored piece object
     * @throws IOException if the entry is invalid
     */
    private static Piece parsePiece(String encoded) throws IOException {
        String[] fields = encoded.split("\\|", -1);
        if (fields.length != 5) {
            throw new IOException("Invalid piece entry.");
        }
        Color color = parseColor(fields[0]);
        int row = parseCount(fields[2], 7);
        int col = parseCount(fields[3], 7);
        Position position = new Position(row, col);
        Piece piece;
        switch (fields[1]) {
            case "Pawn": piece = new Pawn(color, position); break;
            case "Rook": piece = new Rook(color, position); break;
            case "Knight": piece = new Knight(color, position); break;
            case "Bishop": piece = new Bishop(color, position); break;
            case "Queen": piece = new Queen(color, position); break;
            case "King": piece = new King(color, position); break;
            default: throw new IOException("Unknown saved piece type.");
        }
        if ("true".equals(fields[4])) {
            piece.move(position);
        } else if (!"false".equals(fields[4])) {
            throw new IOException("Invalid piece moved flag.");
        }
        return piece;
    }

    /**
     * Encodes one completed move.
     * @param entry one move-history entry
     * @param expectedNumber one-based move index
     * @return validated encoded fields
     * @throws IOException if the entry is invalid
     */
    private static String encodeHistory(HistoryEntry entry, int expectedNumber)
            throws IOException {
        if (entry == null || entry.number() != expectedNumber) {
            throw new IOException("Invalid history numbering.");
        }
        String value = entry.number() + "|" + entry.side() + "|" + entry.piece()
                + "|" + entry.from() + "|" + entry.to() + "|" + entry.captured();
        // Only simple printable values generated by GUIController are supported.
        if (value.contains("\n") || value.contains("\r") || value.contains("\t")) {
            throw new IOException("Invalid characters in history.");
        }
        parseHistory(value, expectedNumber);
        return value;
    }

    /**
     * Parses and validates a single history entry.
     * @param encoded serialized move fields
     * @param expectedNumber one-based move index
     * @return restored immutable move entry
     * @throws IOException if the fields are invalid
     */
    private static HistoryEntry parseHistory(String encoded, int expectedNumber)
            throws IOException {
        String[] fields = encoded.split("\\|", -1);
        if (fields.length != 6 || parseCount(fields[0], MAX_HISTORY) != expectedNumber) {
            throw new IOException("Invalid move-history entry.");
        }
        if (!("WHITE".equals(fields[1]) || "BLACK".equals(fields[1]))
                || !isPieceName(fields[2])
                || !fields[3].matches("[A-H][1-8]")
                || !fields[4].matches("[A-H][1-8]")) {
            throw new IOException("Invalid move-history details.");
        }
        if (!fields[5].isEmpty()) {
            String[] capture = fields[5].split(" ", -1);
            if (capture.length != 2
                    || !("WHITE".equals(capture[0]) || "BLACK".equals(capture[0]))
                    || !isPieceName(capture[1])) {
                throw new IOException("Invalid history capture description.");
            }
        }
        return new HistoryEntry(expectedNumber, fields[1], fields[2],
                fields[3], fields[4], fields[5]);
    }

    /**
     * Checks for one of the six supported piece names.
     * @param name piece name
     * @return true if supported
     */
    private static boolean isPieceName(String name) {
        return "Pawn".equals(name) || "Rook".equals(name)
                || "Knight".equals(name) || "Bishop".equals(name)
                || "Queen".equals(name) || "King".equals(name);
    }

    /**
     * Parses a known piece or turn color.
     * @param value saved color text
     * @return parsed color
     * @throws IOException if the value is unknown
     */
    private static Color parseColor(String value) throws IOException {
        if ("WHITE".equals(value)) return Color.WHITE;
        if ("BLACK".equals(value)) return Color.BLACK;
        throw new IOException("Unknown piece or turn color.");
    }

    /**
     * Parses a bounded, nonnegative decimal count or board coordinate.
     * @param value saved decimal text
     * @param max maximum permitted value
     * @return parsed number
     * @throws IOException if outside the allowed range
     */
    private static int parseCount(String value, int max) throws IOException {
        try {
            if (!value.matches("[0-9]{1,7}")) throw new NumberFormatException();
            int parsed = Integer.parseInt(value);
            if (parsed > max) throw new NumberFormatException();
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IOException("Invalid save-file number.", exception);
        }
    }

    /**
     * Retrieves a required save field.
     * @param data parsed properties
     * @param key property name
     * @return saved value
     * @throws IOException if the field is missing
     */
    private static String required(Properties data, String key) throws IOException {
        String value = data.getProperty(key);
        if (value == null) throw new IOException("Missing save field: " + key);
        return value;
    }
}
