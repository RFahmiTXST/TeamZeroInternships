package gui;

import gui.dialogs.DialogHelper;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/**
 * Provides the Phase 2 M3 New, Save, and Load game menu actions.
 * File chooser interaction stays in the Swing UI, while file encoding stays
 * in GameFileService and board/history updates stay in GUIController.
 *
 * @author Dev 4 (Towsif)
 */
public final class GameMenuActions {
    /** Main application window used for dialogs. */
    private final ChessFrame frame;
    /** Existing controller that owns the current board and move history. */
    private final GUIController controller;

    /**
     * Creates game menu actions for an initialized frame.
     *
     * @param frame the chess application window
     * @param controller controller owning the live game state
     */
    public GameMenuActions(ChessFrame frame, GUIController controller) {
        this.frame = Objects.requireNonNull(frame, "frame");
        this.controller = Objects.requireNonNull(controller, "controller");
    }

    /**
     * Creates the File menu with Save Game and Load Game choices.
     *
     * @return populated Swing file menu
     */
    public JMenu createFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save Game...");
        save.addActionListener(event -> saveGame());
        file.add(save);
        JMenuItem load = new JMenuItem("Load Game...");
        load.addActionListener(event -> loadGame());
        file.add(load);
        return file;
    }

    /**
     * Creates the New Game menu item for the existing Game menu.
     *
     * @return item wired to the confirmation and reset action
     */
    public JMenuItem createNewGameItem() {
        JMenuItem item = new JMenuItem("New Game");
        item.addActionListener(event -> newGame());
        return item;
    }

    /** Confirms and resets the board, turn, captured pieces and history. */
    public void newGame() {
        if (DialogHelper.showConfirmDialog(frame, "New Game",
                "Start a new game? Current unsaved progress will be lost.")) {
            controller.startNewGame();
        }
    }

    /** Opens a save file chooser and writes the current game to a .chess file. */
    public void saveGame() {
        JFileChooser chooser = createChooser();
        chooser.setDialogTitle("Save Chess Game");
        if (chooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;

        Path file = chooser.getSelectedFile().toPath();
        if (!file.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".chess")) {
            file = file.resolveSibling(file.getFileName() + ".chess");
        }
        if (Files.exists(file)) {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Replace the existing file?\n" + file,
                    "Confirm Overwrite", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
        }
        try {
            GameFileService.save(file, GameFileService.snapshot(controller));
            DialogHelper.showInfoDialog(frame, "Game Saved",
                    "Game saved successfully.\n" + file);
        } catch (IOException | SecurityException exception) {
            DialogHelper.showErrorDialog(frame, "Save Failed", exception.getMessage());
        }
    }

    /**
     * Opens an existing save file. An invalid file leaves the running game
     * untouched, because parsing finishes before restoreSession is called.
     */
    public void loadGame() {
        JFileChooser chooser = createChooser();
        chooser.setDialogTitle("Load Chess Game");
        if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        try {
            GameFileService.GameState saved = GameFileService.load(
                    chooser.getSelectedFile().toPath());
            controller.restoreSession(saved.board(), saved.turn(), saved.history());
            DialogHelper.showInfoDialog(frame, "Game Loaded",
                    "Game restored successfully.");
        } catch (IOException | SecurityException exception) {
            DialogHelper.showErrorDialog(frame, "Load Failed", exception.getMessage());
        }
    }

    /**
     * Returns a file chooser limited to .chess saves.
     * @return configured file chooser
     */
    private JFileChooser createChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Team Zero Chess (*.chess)", "chess"));
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }
}
