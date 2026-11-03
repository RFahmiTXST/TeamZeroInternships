package gui.pieces;

import pieces.Color;
import pieces.Piece;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages visual icon assets and procedural rendering for all chess piece types.
 *
 * <p>Provides resolution-independent, visually distinct graphics for Pawns, Rooks,
 * Knights, Bishops, Queens, and Kings for both White and Black sides. Supports in-memory
 * caching to ensure high-performance rendering across board repaints and resizing.</p>
 *
 * @author Dev 1 (Rayed)
 */
public final class PieceIconManager {

    /** Default icon dimension in pixels. */
    public static final int DEFAULT_SIZE = 64;

    /** In-memory cache for rendered piece icons indexed by "COLOR_SYMBOL_SIZE". */
    private static final Map<String, ImageIcon> ICON_CACHE = new ConcurrentHashMap<>();

    /** White piece primary fill color (ivory white). */
    private static final java.awt.Color WHITE_PIECE_FILL = new java.awt.Color(252, 252, 252);
    /** White piece secondary shading color. */
    private static final java.awt.Color WHITE_PIECE_SHADE = new java.awt.Color(220, 224, 230);
    /** White piece border/stroke color. */
    private static final java.awt.Color WHITE_PIECE_STROKE = new java.awt.Color(35, 38, 42);

    /** Black piece primary fill color (charcoal/onyx). */
    private static final java.awt.Color BLACK_PIECE_FILL = new java.awt.Color(42, 45, 52);
    /** Black piece secondary highlight color. */
    private static final java.awt.Color BLACK_PIECE_SHADE = new java.awt.Color(68, 73, 82);
    /** Black piece border/stroke color. */
    private static final java.awt.Color BLACK_PIECE_STROKE = new java.awt.Color(18, 20, 24);
    /** Black piece inner highlight stroke. */
    private static final java.awt.Color BLACK_PIECE_ACCENT = new java.awt.Color(200, 205, 215, 180);

    /** Drop shadow color. */
    private static final java.awt.Color SHADOW_COLOR = new java.awt.Color(0, 0, 0, 45);

    /** Private constructor to prevent instantiation of utility manager. */
    private PieceIconManager() { }

    /**
     * Retrieves the visual icon for a domain Piece at the default size.
     *
     * @param piece the chess piece domain object; if null, returns null
     * @return the rendered ImageIcon, or null if piece is null
     */
    public static Icon getPieceIcon(Piece piece) {
        return getPieceIcon(piece, DEFAULT_SIZE);
    }

    /**
     * Retrieves the visual icon for a domain Piece scaled to the specified size.
     *
     * @param piece the chess piece domain object; if null, returns null
     * @param size icon width and height in pixels
     * @return the rendered ImageIcon, or null if piece is null
     */
    public static Icon getPieceIcon(Piece piece, int size) {
        if (piece == null) {
            return null;
        }
        return getPieceIcon(piece.getColor(), piece.getSymbol(), size);
    }

    /**
     * Retrieves the visual icon for a given color and piece symbol at default size.
     *
     * @param color the side color (WHITE or BLACK)
     * @param symbol the piece symbol character ('P', 'R', 'N', 'B', 'Q', 'K')
     * @return the rendered ImageIcon
     */
    public static Icon getPieceIcon(Color color, char symbol) {
        return getPieceIcon(color, symbol, DEFAULT_SIZE);
    }

    /**
     * Retrieves the visual icon for a given color, piece symbol, and target dimension.
     *
     * @param color the side color (WHITE or BLACK)
     * @param symbol the piece symbol character ('P', 'R', 'N', 'B', 'Q', 'K')
     * @param size icon width and height in pixels (must be positive)
     * @return the rendered ImageIcon
     */
    public static Icon getPieceIcon(Color color, char symbol, int size) {
        if (color == null || size <= 0) {
            return null;
        }
        char normalizedSymbol = Character.toUpperCase(symbol);
        String key = color.name() + "_" + normalizedSymbol + "_" + size;

        return ICON_CACHE.computeIfAbsent(key, k -> new ImageIcon(renderPieceImage(color, normalizedSymbol, size)));
    }

    /**
     * Clears all cached piece icons to reclaim memory or reload styles.
     */
    public static void clearCache() {
        ICON_CACHE.clear();
    }

    /**
     * Renders a piece image procedurally using Java2D vector paths.
     *
     * @param color the piece color
     * @param symbol normalized uppercase piece symbol
     * @param size target pixel dimension
     * @return a high-resolution, anti-aliased BufferedImage
     */
    public static BufferedImage renderPieceImage(Color color, char symbol, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Enable highest-quality anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        boolean isWhite = (color == Color.WHITE);
        java.awt.Color fillColor = isWhite ? WHITE_PIECE_FILL : BLACK_PIECE_FILL;
        java.awt.Color shadeColor = isWhite ? WHITE_PIECE_SHADE : BLACK_PIECE_SHADE;
        java.awt.Color strokeColor = isWhite ? WHITE_PIECE_STROKE : BLACK_PIECE_STROKE;

        float strokeWidth = Math.max(1.5f, size * 0.035f);
        BasicStroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        // Draw procedural piece shape
        switch (symbol) {
            case 'P':
                drawPawn(g2, size, fillColor, shadeColor, strokeColor, stroke, isWhite);
                break;
            case 'R':
                drawRook(g2, size, fillColor, shadeColor, strokeColor, stroke, isWhite);
                break;
            case 'N':
                drawKnight(g2, size, fillColor, shadeColor, strokeColor, stroke, isWhite);
                break;
            case 'B':
                drawBishop(g2, size, fillColor, shadeColor, strokeColor, stroke, isWhite);
                break;
            case 'Q':
                drawQueen(g2, size, fillColor, shadeColor, strokeColor, stroke, isWhite);
                break;
            case 'K':
                drawKing(g2, size, fillColor, shadeColor, strokeColor, stroke, isWhite);
                break;
            default:
                drawFallback(g2, size, color, symbol);
                break;
        }

        g2.dispose();
        return image;
    }

    /**
     * Draws a procedural Pawn.
     *
     * @param g2 graphics context
     * @param size icon dimension in pixels
     * @param fill primary body fill color
     * @param shade secondary accent/shading color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     * @param isWhite true if White piece, false if Black
     */
    private static void drawPawn(Graphics2D g2, int size, java.awt.Color fill, java.awt.Color shade,
                                 java.awt.Color strokeColor, BasicStroke stroke, boolean isWhite) {
        double s = size;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new Ellipse2D.Double(s * 0.22, s * 0.84, s * 0.56, s * 0.10));

        // Pedestal base
        RoundRectangle2D base = new RoundRectangle2D.Double(s * 0.25, s * 0.78, s * 0.50, s * 0.10, s * 0.04, s * 0.04);
        fillAndStroke(g2, base, fill, strokeColor, stroke);

        // Body / neck
        Path2D.Double body = new Path2D.Double();
        body.moveTo(s * 0.32, s * 0.78);
        body.curveTo(s * 0.36, s * 0.60, s * 0.40, s * 0.48, s * 0.42, s * 0.42);
        body.lineTo(s * 0.58, s * 0.42);
        body.curveTo(s * 0.60, s * 0.48, s * 0.64, s * 0.60, s * 0.68, s * 0.78);
        body.closePath();
        fillAndStroke(g2, body, fill, strokeColor, stroke);

        // Collar ring
        RoundRectangle2D collar = new RoundRectangle2D.Double(s * 0.37, s * 0.38, s * 0.26, s * 0.06, s * 0.03, s * 0.03);
        fillAndStroke(g2, collar, shade, strokeColor, stroke);

        // Head sphere
        Ellipse2D.Double head = new Ellipse2D.Double(s * 0.35, s * 0.16, s * 0.30, s * 0.30);
        fillAndStroke(g2, head, fill, strokeColor, stroke);

        // Accent specular highlight
        drawSpecularHighlight(g2, s * 0.40, s * 0.20, s * 0.10, isWhite);
    }

    /**
     * Draws a procedural Rook (Castle).
     *
     * @param g2 graphics context
     * @param size icon dimension in pixels
     * @param fill primary body fill color
     * @param shade secondary accent/shading color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     * @param isWhite true if White piece, false if Black
     */
    private static void drawRook(Graphics2D g2, int size, java.awt.Color fill, java.awt.Color shade,
                                 java.awt.Color strokeColor, BasicStroke stroke, boolean isWhite) {
        double s = size;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new Ellipse2D.Double(s * 0.18, s * 0.84, s * 0.64, s * 0.10));

        // Base
        RoundRectangle2D base = new RoundRectangle2D.Double(s * 0.20, s * 0.76, s * 0.60, s * 0.12, s * 0.04, s * 0.04);
        fillAndStroke(g2, base, fill, strokeColor, stroke);

        // Tower body
        Path2D.Double tower = new Path2D.Double();
        tower.moveTo(s * 0.27, s * 0.76);
        tower.lineTo(s * 0.31, s * 0.38);
        tower.lineTo(s * 0.69, s * 0.38);
        tower.lineTo(s * 0.73, s * 0.76);
        tower.closePath();
        fillAndStroke(g2, tower, fill, strokeColor, stroke);

        // Crenellated battlements
        Path2D.Double parapet = new Path2D.Double();
        parapet.moveTo(s * 0.23, s * 0.38);
        parapet.lineTo(s * 0.23, s * 0.20); // left tooth
        parapet.lineTo(s * 0.33, s * 0.20);
        parapet.lineTo(s * 0.33, s * 0.27); // first notch
        parapet.lineTo(s * 0.45, s * 0.27);
        parapet.lineTo(s * 0.45, s * 0.20); // center tooth
        parapet.lineTo(s * 0.55, s * 0.20);
        parapet.lineTo(s * 0.55, s * 0.27); // second notch
        parapet.lineTo(s * 0.67, s * 0.27);
        parapet.lineTo(s * 0.67, s * 0.20); // right tooth
        parapet.lineTo(s * 0.77, s * 0.20);
        parapet.lineTo(s * 0.77, s * 0.38);
        parapet.closePath();
        fillAndStroke(g2, parapet, fill, strokeColor, stroke);

        drawSpecularHighlight(g2, s * 0.28, s * 0.42, s * 0.12, isWhite);
    }

    /**
     * Draws a procedural Knight (Horse).
     *
     * @param g2 graphics context
     * @param size icon dimension in pixels
     * @param fill primary body fill color
     * @param shade secondary accent/shading color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     * @param isWhite true if White piece, false if Black
     */
    private static void drawKnight(Graphics2D g2, int size, java.awt.Color fill, java.awt.Color shade,
                                   java.awt.Color strokeColor, BasicStroke stroke, boolean isWhite) {
        double s = size;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new Ellipse2D.Double(s * 0.18, s * 0.84, s * 0.64, s * 0.10));

        // Base
        RoundRectangle2D base = new RoundRectangle2D.Double(s * 0.22, s * 0.77, s * 0.56, s * 0.11, s * 0.04, s * 0.04);
        fillAndStroke(g2, base, fill, strokeColor, stroke);

        // Horse silhouette
        Path2D.Double horse = new Path2D.Double();
        horse.moveTo(s * 0.30, s * 0.77);
        horse.curveTo(s * 0.30, s * 0.62, s * 0.26, s * 0.52, s * 0.22, s * 0.44); // chest
        horse.lineTo(s * 0.22, s * 0.38); // muzzle bottom
        horse.lineTo(s * 0.34, s * 0.34); // muzzle top
        horse.lineTo(s * 0.38, s * 0.22); // ear front
        horse.lineTo(s * 0.45, s * 0.16); // ear tip
        horse.lineTo(s * 0.46, s * 0.25); // ear back
        horse.curveTo(s * 0.60, s * 0.27, s * 0.72, s * 0.38, s * 0.74, s * 0.60); // mane / neck
        horse.lineTo(s * 0.70, s * 0.77); // back base
        horse.closePath();
        fillAndStroke(g2, horse, fill, strokeColor, stroke);

        // Eye indicator
        g2.setColor(strokeColor);
        g2.fill(new Ellipse2D.Double(s * 0.36, s * 0.32, s * 0.06, s * 0.06));

        // Nostril indicator
        g2.fill(new Ellipse2D.Double(s * 0.25, s * 0.40, s * 0.04, s * 0.04));

        drawSpecularHighlight(g2, s * 0.48, s * 0.30, s * 0.12, isWhite);
    }

    /**
     * Draws a procedural Bishop.
     *
     * @param g2 graphics context
     * @param size icon dimension in pixels
     * @param fill primary body fill color
     * @param shade secondary accent/shading color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     * @param isWhite true if White piece, false if Black
     */
    private static void drawBishop(Graphics2D g2, int size, java.awt.Color fill, java.awt.Color shade,
                                   java.awt.Color strokeColor, BasicStroke stroke, boolean isWhite) {
        double s = size;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new Ellipse2D.Double(s * 0.20, s * 0.84, s * 0.60, s * 0.10));

        // Base
        RoundRectangle2D base = new RoundRectangle2D.Double(s * 0.22, s * 0.76, s * 0.56, s * 0.11, s * 0.04, s * 0.04);
        fillAndStroke(g2, base, fill, strokeColor, stroke);

        // Collar ring
        RoundRectangle2D collar = new RoundRectangle2D.Double(s * 0.32, s * 0.66, s * 0.36, s * 0.07, s * 0.03, s * 0.03);
        fillAndStroke(g2, collar, shade, strokeColor, stroke);

        // Mitre head
        Path2D.Double mitre = new Path2D.Double();
        mitre.moveTo(s * 0.34, s * 0.66);
        mitre.curveTo(s * 0.28, s * 0.48, s * 0.34, s * 0.30, s * 0.50, s * 0.20);
        mitre.curveTo(s * 0.66, s * 0.30, s * 0.72, s * 0.48, s * 0.66, s * 0.66);
        mitre.closePath();
        fillAndStroke(g2, mitre, fill, strokeColor, stroke);

        // Top finial sphere
        Ellipse2D.Double crossOrb = new Ellipse2D.Double(s * 0.45, s * 0.13, s * 0.10, s * 0.10);
        fillAndStroke(g2, crossOrb, fill, strokeColor, stroke);

        // Slit / cut in mitre
        g2.setColor(strokeColor);
        g2.setStroke(stroke);
        g2.drawLine((int) (s * 0.44), (int) (s * 0.32), (int) (s * 0.56), (int) (s * 0.44));

        drawSpecularHighlight(g2, s * 0.40, s * 0.35, s * 0.10, isWhite);
    }

    /**
     * Draws a procedural Queen.
     *
     * @param g2 graphics context
     * @param size icon dimension in pixels
     * @param fill primary body fill color
     * @param shade secondary accent/shading color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     * @param isWhite true if White piece, false if Black
     */
    private static void drawQueen(Graphics2D g2, int size, java.awt.Color fill, java.awt.Color shade,
                                  java.awt.Color strokeColor, BasicStroke stroke, boolean isWhite) {
        double s = size;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new Ellipse2D.Double(s * 0.16, s * 0.85, s * 0.68, s * 0.10));

        // Base
        RoundRectangle2D base = new RoundRectangle2D.Double(s * 0.20, s * 0.77, s * 0.60, s * 0.11, s * 0.04, s * 0.04);
        fillAndStroke(g2, base, fill, strokeColor, stroke);

        // Waist / Robe
        RoundRectangle2D waist = new RoundRectangle2D.Double(s * 0.28, s * 0.68, s * 0.44, s * 0.07, s * 0.03, s * 0.03);
        fillAndStroke(g2, waist, shade, strokeColor, stroke);

        // Coronet gown
        Path2D.Double crown = new Path2D.Double();
        crown.moveTo(s * 0.30, s * 0.68);
        crown.lineTo(s * 0.18, s * 0.30); // left wing peak
        crown.lineTo(s * 0.34, s * 0.44); // valley 1
        crown.lineTo(s * 0.50, s * 0.26); // center peak
        crown.lineTo(s * 0.66, s * 0.44); // valley 2
        crown.lineTo(s * 0.82, s * 0.30); // right wing peak
        crown.lineTo(s * 0.70, s * 0.68);
        crown.closePath();
        fillAndStroke(g2, crown, fill, strokeColor, stroke);

        // Jewels on 3 crown peaks
        double r = s * 0.05;
        fillAndStroke(g2, new Ellipse2D.Double(s * 0.18 - r, s * 0.30 - r, r * 2, r * 2), shade, strokeColor, stroke);
        fillAndStroke(g2, new Ellipse2D.Double(s * 0.50 - r, s * 0.26 - r, r * 2, r * 2), shade, strokeColor, stroke);
        fillAndStroke(g2, new Ellipse2D.Double(s * 0.82 - r, s * 0.30 - r, r * 2, r * 2), shade, strokeColor, stroke);

        drawSpecularHighlight(g2, s * 0.42, s * 0.46, s * 0.15, isWhite);
    }

    /**
     * Draws a procedural King.
     *
     * @param g2 graphics context
     * @param size icon dimension in pixels
     * @param fill primary body fill color
     * @param shade secondary accent/shading color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     * @param isWhite true if White piece, false if Black
     */
    private static void drawKing(Graphics2D g2, int size, java.awt.Color fill, java.awt.Color shade,
                                 java.awt.Color strokeColor, BasicStroke stroke, boolean isWhite) {
        double s = size;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new Ellipse2D.Double(s * 0.16, s * 0.85, s * 0.68, s * 0.10));

        // Base
        RoundRectangle2D base = new RoundRectangle2D.Double(s * 0.20, s * 0.77, s * 0.60, s * 0.11, s * 0.04, s * 0.04);
        fillAndStroke(g2, base, fill, strokeColor, stroke);

        // Waist
        RoundRectangle2D waist = new RoundRectangle2D.Double(s * 0.28, s * 0.68, s * 0.44, s * 0.07, s * 0.03, s * 0.03);
        fillAndStroke(g2, waist, shade, strokeColor, stroke);

        // Arched dome crown
        Path2D.Double crown = new Path2D.Double();
        crown.moveTo(s * 0.30, s * 0.68);
        crown.curveTo(s * 0.18, s * 0.48, s * 0.24, s * 0.32, s * 0.50, s * 0.30);
        crown.curveTo(s * 0.76, s * 0.32, s * 0.82, s * 0.48, s * 0.70, s * 0.68);
        crown.closePath();
        fillAndStroke(g2, crown, fill, strokeColor, stroke);

        // Royal Cross on top
        Path2D.Double cross = new Path2D.Double();
        double cx = s * 0.50;
        double cy = s * 0.20;
        double w = s * 0.04;
        double h = s * 0.16;
        double barW = s * 0.14;
        double barH = s * 0.04;

        cross.append(new RoundRectangle2D.Double(cx - w / 2, cy - h / 2, w, h, 2, 2), false);
        cross.append(new RoundRectangle2D.Double(cx - barW / 2, cy - barH / 2 - s * 0.02, barW, barH, 2, 2), false);
        fillAndStroke(g2, cross, isWhite ? WHITE_PIECE_FILL : BLACK_PIECE_FILL, strokeColor, stroke);

        drawSpecularHighlight(g2, s * 0.40, s * 0.42, s * 0.14, isWhite);
    }

    /**
     * Fallback renderer when an unexpected character symbol is encountered.
     *
     * @param g2 graphics context
     * @param size dimension in pixels
     * @param color piece color
     * @param symbol piece character symbol
     */
    private static void drawFallback(Graphics2D g2, int size, Color color, char symbol) {
        g2.setColor(color == Color.WHITE ? WHITE_PIECE_FILL : BLACK_PIECE_FILL);
        g2.fillOval(size / 6, size / 6, (size * 2) / 3, (size * 2) / 3);
        g2.setColor(color == Color.WHITE ? WHITE_PIECE_STROKE : WHITE_PIECE_FILL);
        g2.setFont(new Font("SansSerif", Font.BOLD, (int) (size * 0.45)));
        FontMetrics fm = g2.getFontMetrics();
        String text = String.valueOf(symbol);
        int tx = (size - fm.stringWidth(text)) / 2;
        int ty = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, tx, ty);
    }

    /**
     * Helper to fill a shape and stroke its outline.
     *
     * @param g2 graphics context
     * @param shape shape geometry to render
     * @param fill fill color
     * @param strokeColor outline stroke color
     * @param stroke stroke style
     */
    private static void fillAndStroke(Graphics2D g2, java.awt.Shape shape, java.awt.Color fill,
                                      java.awt.Color strokeColor, BasicStroke stroke) {
        g2.setColor(fill);
        g2.fill(shape);
        g2.setColor(strokeColor);
        g2.setStroke(stroke);
        g2.draw(shape);
    }

    /**
     * Draws subtle specular lighting for 3D depth.
     *
     * @param g2 graphics context
     * @param x top-left X coordinate of specular ellipse
     * @param y top-left Y coordinate of specular ellipse
     * @param radius dimension scale
     * @param isWhite true if White piece, false if Black
     */
    private static void drawSpecularHighlight(Graphics2D g2, double x, double y, double radius, boolean isWhite) {
        g2.setColor(isWhite ? new java.awt.Color(255, 255, 255, 140) : BLACK_PIECE_ACCENT);
        g2.fill(new Ellipse2D.Double(x, y, radius, radius * 0.6));
    }
}
