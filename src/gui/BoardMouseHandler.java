package gui;

import board.Position;
import gui.board.BoardPanel;
import gui.board.SquarePanel;
import gui.pieces.PieceIconManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/** Translates mouse gestures into controller moves, including events on piece labels. */
final class BoardMouseHandler extends MouseAdapter {
    /** Controller owning the model and selection. */
    private final GUIController controller;
    /** Coordinate space used for hit testing. */
    private final BoardPanel panel;
    /** Press location in panel coordinates, or null when idle. */
    private Point pressed;
    /** Square where the gesture began. */
    private Position origin;
    /** Whether the pointer crossed the drag threshold. */
    private boolean dragging;
    /** Floating icon; never receives pointer events. */
    private JLabel ghost;

    /**
     * Installs one shared adapter and an Escape cancellation action.
     * @param controller owner of this input adapter
     */
    BoardMouseHandler(GUIController controller) {
        this.controller = controller;
        panel = controller.getBoardPanel();
        install(panel);
        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "cancelMove");
        panel.getActionMap().put("cancelMove", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) {
                controller.cancelSelection();
            }
        });
        panel.addHierarchyListener(event -> {
            if (!panel.isShowing()) controller.cancelSelection();
        });
    }

    /**
     * Registers on children because Swing mouse events do not bubble.
     * @param component component whose existing descendants are registered
     */
    private void install(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) install(child);
        }
    }

    @Override public void mousePressed(MouseEvent event) {
        if (!SwingUtilities.isLeftMouseButton(event)) return;
        cancel();
        pressed = point(event);
        origin = square(pressed);
    }

    @Override public void mouseDragged(MouseEvent event) {
        if (pressed == null) return;
        Point location = point(event);
        if (!dragging && pressed.distance(location) >= 5) {
            dragging = true;
            controller.selectSquare(origin);
            if (origin != null && controller.getBoard().getPiece(origin) != null) {
                createGhost();
            }
        }
        if (ghost != null) {
            Point p = SwingUtilities.convertPoint(panel, location, ghost.getParent());
            ghost.setLocation(p.x - ghost.getWidth() / 2, p.y - ghost.getHeight() / 2);
        }
    }

    @Override public void mouseReleased(MouseEvent event) {
        if (!SwingUtilities.isLeftMouseButton(event) || pressed == null) return;
        Position destination = square(point(event));
        Position source = origin;
        boolean wasDragging = dragging;
        cancel();
        if (wasDragging) controller.movePiece(source, destination);
        else if (java.util.Objects.equals(source, destination)) controller.clickSquare(destination);
        else controller.cancelSelection();
    }

    /** Removes transient drag state; the board model is unchanged. */
    void cancel() {
        if (ghost != null) {
            Container parent = ghost.getParent();
            parent.remove(ghost);
            parent.repaint();
            ghost = null;
        }
        pressed = null;
        origin = null;
        dragging = false;
    }

    /** Creates a floating copy of the source icon when attached to a window. */
    private void createGhost() {
        JRootPane root = SwingUtilities.getRootPane(panel);
        if (root == null) return;
        int size = Math.max(1, panel.getSquarePanel(origin).getWidth() - 12);
        ghost = new JLabel(PieceIconManager.getPieceIcon(controller.getBoard().getPiece(origin), size)) {
            @Override public boolean contains(int x, int y) { return false; }
        };
        ghost.setSize(size, size);
        root.getLayeredPane().add(ghost, JLayeredPane.DRAG_LAYER);
    }

    /**
     * Converts label or square mouse coordinates to board coordinates.
     * @param event mouse event
     * @return point relative to the board view
     */
    private Point point(MouseEvent event) {
        return SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), panel);
    }

    /**
     * Resolves squares after layout, including resized boards and label children.
     * @param point pointer location in board coordinates
     * @return square coordinate, or null outside the playable grid
     */
    private Position square(Point point) {
        Component hit = SwingUtilities.getDeepestComponentAt(panel, point.x, point.y);
        while (hit != null && hit != panel) {
            if (hit instanceof SquarePanel) return ((SquarePanel) hit).getPosition();
            hit = hit.getParent();
        }
        return null;
    }
}
