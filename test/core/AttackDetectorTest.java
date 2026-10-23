
package core;

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
import test.TestUtils;

/**
 * Tests chess square attack detection for castling.
 *
 * Developer: Dev 4
 */
public final class AttackDetectorTest {

    private AttackDetectorTest() { }

    /** Adds a piece at its declared position. */
    private static void place(Board board, Piece piece) {
        board.setPiece(piece.getPosition(), piece);
    }

    /** Checks the attack detection result. */
    private static void check(
            boolean expected,
            Board board,
            Position target,
            Color attacker,
            String message) {

        TestUtils.assertEquals(
                expected,
                AttackDetector.isSquareAttacked(
                        board, target, attacker),
                message
        );
    }

    /** Runs all attack detection tests. */
    public static void main(String[] args) {

        System.out.println(
                "Running attack detection tests...\n");

        Position d4 = new Position(3, 3);

        // Empty board.
        Board empty = new Board();

        check(false, empty, d4, Color.WHITE,
                "Empty board has no White attacks");

        check(false, empty, d4, Color.BLACK,
                "Empty board has no Black attacks");

        // Invalid arguments.
        check(false, null, d4, Color.WHITE,
                "Null board rejected");

        check(false, empty, null, Color.WHITE,
                "Null target rejected");

        check(false, empty, new Position(8, 3),
                Color.WHITE, "Off-board target rejected");

        check(false, empty, d4, null,
                "Null attacker rejected");

        // White Pawn attacks diagonally, not forward.
        Board whitePawn = new Board();

        place(whitePawn, new Pawn(
                Color.WHITE, new Position(2, 2)));

        check(true, whitePawn, d4, Color.WHITE,
                "White Pawn attacks diagonally");

        check(false, whitePawn, new Position(3, 2),
                Color.WHITE,
                "White Pawn does not attack forward");

        check(false, whitePawn, d4, Color.BLACK,
                "Wrong attacker color rejected");

        place(whitePawn, new Knight(
                Color.WHITE, d4));

        check(true, whitePawn, d4, Color.WHITE,
                "Pawn attacks occupied friendly square");

        // Black Pawn attacks toward lower rows.
        Board blackPawn = new Board();

        place(blackPawn, new Pawn(
                Color.BLACK, new Position(4, 4)));

        check(true, blackPawn, d4, Color.BLACK,
                "Black Pawn attacks diagonally");

        check(false, blackPawn, new Position(3, 4),
                Color.BLACK,
                "Black Pawn does not attack forward");

        // Knight attacks are not blocked.
        Board knight = new Board();

        place(knight, new Knight(
                Color.WHITE, new Position(2, 1)));

        place(knight, new Pawn(
                Color.WHITE, new Position(2, 2)));

        check(true, knight, d4, Color.WHITE,
                "Knight jumps over pieces");

        check(false, knight, new Position(3, 2),
                Color.WHITE,
                "Knight does not attack adjacent diagonal");

        // King attacks adjacent squares.
        Board king = new Board();

        place(king, new King(
                Color.BLACK, new Position(2, 2)));

        check(true, king, d4, Color.BLACK,
                "King attacks adjacent square");

        check(false, king, new Position(4, 4),
                Color.BLACK,
                "King cannot attack two squares away");

        // Rook attacks straight lines unless blocked.
        Board rook = new Board();

        place(rook, new Rook(
                Color.WHITE, new Position(0, 3)));

        check(true, rook, d4, Color.WHITE,
                "Rook attacks open file");

        place(rook, new Pawn(
                Color.WHITE, new Position(1, 3)));

        check(false, rook, d4, Color.WHITE,
                "Rook attack blocked by piece");

        // Bishop attacks diagonals unless blocked.
        Board bishop = new Board();

        place(bishop, new Bishop(
                Color.WHITE, new Position(0, 0)));

        check(true, bishop, d4, Color.WHITE,
                "Bishop attacks open diagonal");

        place(bishop, new Pawn(
                Color.BLACK, new Position(1, 1)));

        check(false, bishop, d4, Color.WHITE,
                "Bishop attack blocked by piece");

        // Queen attacks straight and diagonal lines.
        Board straightQueen = new Board();

        place(straightQueen, new Queen(
                Color.BLACK, new Position(7, 3)));

        check(true, straightQueen, d4, Color.BLACK,
                "Queen attacks along file");

        Board diagonalQueen = new Board();

        place(diagonalQueen, new Queen(
                Color.BLACK, new Position(0, 0)));

        check(true, diagonalQueen, d4, Color.BLACK,
                "Queen attacks diagonally");
    }
}
