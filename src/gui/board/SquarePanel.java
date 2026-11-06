package gui.board;

import board.Position;
import gui.pieces.PieceIconManager;
import pieces.Piece;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents a single square component on the GUI chessboard.
 *
 * <p>Handles square background coloring (alternating light/dark), piece icon presentation,
 * and selection/highlight states.</p>
 *
 * @author Dev 2 (Abid - Primary Owner) &amp; Dev 1 (Rayed - Architecture)
 */
public class SquarePanel extends JPanel {

    /** Generated serial version UID for Swing serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /** Default light square background color (classic birch wood). */
    public static final Color DEFAULT_LIGHT_COLOR = new Color(240, 217, 181);
    /** Default dark square background color (classic walnut wood). */
    public static final Color DEFAULT_DARK_COLOR = new Color(181, 136, 99);
    /** Selected square border / highlight tint. */
    public static final Color SELECTION_BORDER_COLOR = new Color(46, 204, 113);
    /** Highlighted square tint (e.g., legal target or move source). */
    public static final Color HIGHLIGHT_TINT_COLOR = new Color(241, 196, 15, 160);
    /** Muted plum hover border complements the board's wood palette. */
    private static final Color HOVER_BORDER_COLOR = new Color(112, 58, 72);

    /** Coordinate position of this square on the board. */
    private final Position position;
    /** Whether this square has a light alternating color. */
    private final boolean lightSquare;
    /** Label hosting the piece icon. */
    private final JLabel pieceLabel;

    /** Currently active light square background color. */
    private Color lightColor = DEFAULT_LIGHT_COLOR;
    /** Currently active dark square background color. */
    private Color darkColor = DEFAULT_DARK_COLOR;

    /** The piece currently occupying this square, or null if empty. */
    private Piece currentPiece;
    /** Whether this square is currently selected by the user. */
    private boolean selected;
    /** Whether this square is highlighted. */
    private boolean highlighted;
    /** Whether the pointer is over this square. */
    private boolean hovered;
    /** Whether this square has keyboard focus. */
    private boolean focused;

    /**
     * Constructs a SquarePanel at the given board position.
     *
     * @param position coordinate position of the square
     */
    public SquarePanel(Position position) {
        this.position = position;
        // In standard chess: a1 is (row 0, col 0), (0+0)%2 == 0 which is dark.
        this.lightSquare = ((position.getRow() + position.getColumn()) % 2 != 0);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(80, 80));

        pieceLabel = new JLabel();
        pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieceLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(pieceLabel, BorderLayout.CENTER);

        setFocusable(true);
        MouseAdapter mouseFeedback = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent event) {
                hovered = true;
                refreshAppearance();
            }
            @Override public void mouseExited(MouseEvent event) {
                hovered = false;
                refreshAppearance();
            }
            @Override public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
            }
        };
        addMouseListener(mouseFeedback);
        pieceLabel.addMouseListener(mouseFeedback);
        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) {
                focused = true;
                refreshAppearance();
            }
            @Override public void focusLost(FocusEvent event) {
                focused = false;
                refreshAppearance();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent event) {
                refreshAppearance();
            }
        });

        refreshAppearance();
    }

    /**
     * Gets the board coordinate position of this square.
     *
     * @return the Position object
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Checks if this square is designated as a light square.
     *
     * @return true if light square, false if dark square
     */
    public boolean isLightSquare() {
        return lightSquare;
    }

    /**
     * Gets the piece occupying this square.
     *
     * @return the Piece instance, or null if empty
     */
    public Piece getPiece() {
        return currentPiece;
    }

    /**
     * Updates the piece occupying this square and refreshes its visual icon.
     *
     * @param piece the new piece to display, or null to clear the square
     */
    public void setPiece(Piece piece) {
        this.currentPiece = piece;
        if (piece == null) {
            pieceLabel.setIcon(null);
            pieceLabel.setToolTipText(null);
        } else {
            pieceLabel.setToolTipText(piece.getColor() + " " + piece.getClass().getSimpleName() + " at " + position);
        }
        refreshAppearance();
        revalidate();
        repaint();
    }

    /**
     * Sets whether this square is selected.
     *
     * @param selected true to select, false to deselect
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        refreshAppearance();
    }

    /**
     * Checks if this square is selected.
     *
     * @return true if selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets whether this square is highlighted.
     *
     * @param highlighted true to highlight
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        refreshAppearance();
    }

    /**
     * Checks if this square is highlighted.
     *
     * @return true if highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Configures the theme colors for light and dark squares.
     *
     * @param lightColor background color for light squares
     * @param darkColor background color for dark squares
     */
    public void setSquareColors(Color lightColor, Color darkColor) {
        if (lightColor != null) {
            this.lightColor = lightColor;
        }
        if (darkColor != null) {
            this.darkColor = darkColor;
        }
        refreshAppearance();
    }

    /**
     * Refreshes the visual border, background, and icon sizing for this square.
     */
    public void refreshAppearance() {
        Color baseColor = lightSquare ? lightColor : darkColor;

        if (selected) {
            setBorder(BorderFactory.createLineBorder(SELECTION_BORDER_COLOR, 3));
            setBackground(baseColor);
        } else if (highlighted || focused) {
            setBorder(BorderFactory.createLineBorder(HIGHLIGHT_TINT_COLOR, 2));
            setBackground(baseColor);
        } else if (hovered) {
            setBorder(BorderFactory.createLineBorder(HOVER_BORDER_COLOR, 2));
            setBackground(baseColor);
        } else {
            setBorder(null);
            setBackground(baseColor);
        }

        if (currentPiece != null) {
            int side = Math.min(getWidth(), getHeight());
            int iconSize = side <= 0 ? PieceIconManager.DEFAULT_SIZE : Math.max(1, side - 12);
            pieceLabel.setIcon(PieceIconManager.getPieceIcon(currentPiece, iconSize));
        }
    }
}

