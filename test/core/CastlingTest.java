
package core;

import board.Board;
import board.Position;
import pieces.Color;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Rook;
import test.TestUtils;
import utils.Utils;

/**
 * Tests kingside and queenside castling.
 *
 * Developer: Dev 4
 */
public final class CastlingTest {

    private CastlingTest() { }

    /** Returns a square on the player's home rank. */
    private static Position home(Color color, int col) {
        int row = color == Color.WHITE ? 0 : 7;
        return new Position(row, col);
    }

    /** Creates a board with an unmoved King and Rook. */
    private static Board setup(
            Color color, boolean kingside) {

        Board board = new Board();

        Position kingSquare = home(color, 4);
        Position rookSquare =
                home(color, kingside ? 7 : 0);

        board.setPiece(
                kingSquare, new King(color, kingSquare));

        board.setPiece(
                rookSquare, new Rook(color, rookSquare));

        return board;
    }

    /** Verifies a successful castle and both piece movements. */
    private static void verifySuccess(
            Color color, boolean kingside) {

        Board board = setup(color, kingside);

        int rookColumn = kingside ? 7 : 0;
        int kingDestination = kingside ? 6 : 2;
        int rookDestination = kingside ? 5 : 3;

        Position kingFrom = home(color, 4);
        Position rookFrom = home(color, rookColumn);
        Position kingTo = home(color, kingDestination);
        Position rookTo = home(color, rookDestination);

        Piece king = board.getPiece(kingFrom);
        Piece rook = board.getPiece(rookFrom);

        String label = color + (kingside
                ? " kingside" : " queenside");

        TestUtils.assertEquals(
                true,
                SpecialMoveHandler.executeCastling(
                        board, Move.castle(kingside), color),
                label + " castling accepted"
        );

        TestUtils.assertEquals(
                king, board.getPiece(kingTo),
                label + " King reaches destination"
        );

        TestUtils.assertEquals(
                rook, board.getPiece(rookTo),
                label + " Rook reaches destination"
        );

        TestUtils.assertEquals(
                null, board.getPiece(kingFrom),
                label + " King source cleared"
        );

        TestUtils.assertEquals(
                null, board.getPiece(rookFrom),
                label + " Rook source cleared"
        );

        TestUtils.assertEquals(
                true, king.hasMoved(),
                label + " King marked moved"
        );

        TestUtils.assertEquals(
                true, rook.hasMoved(),
                label + " Rook marked moved"
        );

        TestUtils.assertEquals(
                kingTo, king.getPosition(),
                label + " King position updated"
        );

        TestUtils.assertEquals(
                rookTo, rook.getPosition(),
                label + " Rook position updated"
        );
    }

    /** Verifies rejection without board modification. */
    private static void verifyRejected(
            Board board,
            Color color,
            boolean kingside,
            String label) {

        String before = board.render();

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executeCastling(
                        board, Move.castle(kingside), color),
                label + " rejected"
        );

        TestUtils.assertEquals(
                before, board.render(),
                label + " preserves board"
        );
    }

    /**
     * Runs all castling tests.
     *
     * @param args unused arguments
     */

    /**
     * Verifies castling through Player.makeMove().
     */

    private static void verifyPlayerCastling(
            Color color, boolean kingside) {

        Board board = setup(color, kingside);
        Player player = new Player(color);

        Position kingFrom = home(color, 4);
        Position rookFrom = home(color, kingside ? 7 : 0);

        Position kingTo = home(color, kingside ? 6 : 2);
        Position rookTo = home(color, kingside ? 5 : 3);

        String label = color
                + (kingside ? " kingside" : " queenside");

        TestUtils.assertEquals(
                true,
                player.makeMove(
                        board, Move.castle(kingside)
                ),
                label + " accepted through Player"
        );

        TestUtils.assertEquals(
                true,
                board.getPiece(kingTo) instanceof King,
                label + " King moved correctly"
        );

        TestUtils.assertEquals(
                true,
                board.getPiece(rookTo) instanceof Rook,
                label + " Rook moved correctly"
        );

        TestUtils.assertEquals(
                null,
                board.getPiece(kingFrom),
                label + " original King square cleared"
        );

        TestUtils.assertEquals(
                null,
                board.getPiece(rookFrom),
                label + " original Rook square cleared"
        );
    }

    public static void main(String[] args) {

        System.out.println("Running castling tests...\n");

        // Both directions, both players.
        verifySuccess(Color.WHITE, true);
        verifySuccess(Color.WHITE, false);
        verifySuccess(Color.BLACK, true);
        verifySuccess(Color.BLACK, false);

        // Kingside path is blocked.
        Board blockedKing = setup(Color.WHITE, true);
        Position f1 = home(Color.WHITE, 5);
        blockedKing.setPiece(
                f1, new Knight(Color.WHITE, f1));

        verifyRejected(
                blockedKing, Color.WHITE, true,
                "Blocked kingside path"
        );

        // Queenside path is blocked.
        Board blockedQueen = setup(Color.WHITE, false);
        Position b1 = home(Color.WHITE, 1);
        blockedQueen.setPiece(
                b1, new Knight(Color.WHITE, b1));

        verifyRejected(
                blockedQueen, Color.WHITE, false,
                "Blocked queenside path"
        );

        // King has previously moved.
        Board movedKing = setup(Color.WHITE, true);
        Piece king = movedKing.getPiece(
                home(Color.WHITE, 4));

        king.move(home(Color.WHITE, 4));

        verifyRejected(
                movedKing, Color.WHITE, true,
                "Previously moved King"
        );

        // Rook has previously moved.
        Board movedRook = setup(Color.WHITE, true);
        Piece rook = movedRook.getPiece(
                home(Color.WHITE, 7));

        rook.move(home(Color.WHITE, 7));

        verifyRejected(
                movedRook, Color.WHITE, true,
                "Previously moved Rook"
        );

        // Wrong player cannot castle.
        verifyRejected(
                setup(Color.WHITE, true),
                Color.BLACK, true,
                "Wrong player"
        );

        // King cannot castle while in check.
        Board inCheck = setup(Color.WHITE, true);
        Position e8 = new Position(7, 4);

        inCheck.setPiece(
                e8, new Rook(Color.BLACK, e8));

        verifyRejected(
                inCheck, Color.WHITE, true,
                "King currently in check"
        );

        // King cannot castle through an attacked square.
        Board throughCheck = setup(Color.WHITE, true);
        Position f8 = new Position(7, 5);

        throughCheck.setPiece(
                f8, new Rook(Color.BLACK, f8));

        verifyRejected(
                throughCheck, Color.WHITE, true,
                "King passes through check"
        );

        // King cannot castle into check.
        Board intoCheck = setup(Color.WHITE, true);
        Position g8 = new Position(7, 6);

        intoCheck.setPiece(
                g8, new Rook(Color.BLACK, g8));

        verifyRejected(
                intoCheck, Color.WHITE, true,
                "King destination attacked"
        );

        // Pawn attacks on empty transit squares matter.
        Board pawnAttack = setup(Color.WHITE, true);
        Position g2 = new Position(1, 6);

        pawnAttack.setPiece(
                g2, new Pawn(Color.BLACK, g2));

        verifyRejected(
                pawnAttack, Color.WHITE, true,
                "Pawn attacks castling path"
        );

        // Black cannot castle out of check.
        Board blackInCheck = setup(Color.BLACK, true);
        Position whiteE1 = home(Color.WHITE, 4);

        blackInCheck.setPiece(
                whiteE1,
                new Rook(Color.WHITE, whiteE1));

        verifyRejected(
                blackInCheck, Color.BLACK, true,
                "Black King currently in check"
        );

        // Black cannot castle through an attacked square.
        Board blackThroughCheck =
                setup(Color.BLACK, true);

        Position whiteF1 = home(Color.WHITE, 5);

        blackThroughCheck.setPiece(
                whiteF1,
                new Rook(Color.WHITE, whiteF1));

        verifyRejected(
                blackThroughCheck, Color.BLACK, true,
                "Black King passes through check"
        );

        // Queenside destination square is attacked.
        Board queenIntoCheck =
                setup(Color.WHITE, false);

        Position c8 = new Position(7, 2);

        queenIntoCheck.setPiece(
                c8, new Rook(Color.BLACK, c8));

        verifyRejected(
                queenIntoCheck, Color.WHITE, false,
                "Queenside destination attacked"
        );

        // Attacks on B1 alone do not prevent castling.
        Board attackedB1 = setup(Color.WHITE, false);
        Position b8 = new Position(7, 1);

        attackedB1.setPiece(
                b8, new Rook(Color.BLACK, b8));

        TestUtils.assertEquals(
                true,
                SpecialMoveHandler.executeCastling(
                        attackedB1,
                        Move.castle(false),
                        Color.WHITE),
                "Attack on B1 does not prohibit castling"
        );

        // Ordinary moves cannot use the castling handler.
        Board invalidMove = setup(Color.WHITE, true);

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executeCastling(
                        invalidMove,
                        new Move(
                                home(Color.WHITE, 4),
                                home(Color.WHITE, 6)),
                        Color.WHITE),
                "Ordinary move rejected by castling handler"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executeCastling(
                        null, Move.castle(true), Color.WHITE),
                "Null board rejected"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executeCastling(
                        invalidMove, null, Color.WHITE),
                "Null castling move rejected"
        );

        TestUtils.assertEquals(
                false,
                SpecialMoveHandler.executeCastling(
                        invalidMove, Move.castle(true), null),
                "Null player color rejected"
        );


        // ======================================
        // Player-level Castling integration
        // ======================================

        verifyPlayerCastling(Color.WHITE, true);
        verifyPlayerCastling(Color.WHITE, false);

        verifyPlayerCastling(Color.BLACK, true);
        verifyPlayerCastling(Color.BLACK, false);

        // Invalid castling through Player must not
        // modify the board.
        Board playerBlocked = setup(Color.WHITE, true);

        Position blockedF1 = home(Color.WHITE, 5);

        playerBlocked.setPiece(
                blockedF1,
                new Knight(Color.WHITE, blockedF1)
        );

        String beforeBlocked = playerBlocked.render();

        Player blockedPlayer = new Player(Color.WHITE);

        TestUtils.assertEquals(
                false,
                blockedPlayer.makeMove(
                        playerBlocked,
                        Move.castle(true)
                ),
                "Player rejects blocked castling"
        );

        TestUtils.assertEquals(
                beforeBlocked,
                playerBlocked.render(),
                "Rejected Player castling preserves board"
        );

        // ======================================
        // Special move notation integration
        // ======================================

        // Test White kingside castling: O-O
        Board parsedKingside = setup(Color.WHITE, true);

        TestUtils.assertEquals(
                true,
                new Player(Color.WHITE).makeMove(
                        parsedKingside,
                        Utils.parseMove("O-O")
                ),
                "O-O executes through parser and Player"
        );

        TestUtils.assertEquals(
                true,
                parsedKingside.getPiece(
                        home(Color.WHITE, 6)) instanceof King,
                "Parsed kingside castling moves King to G1"
        );

        // Test Black queenside castling: O-O-O
        Board parsedQueenside = setup(Color.BLACK, false);

        TestUtils.assertEquals(
                true,
                new Player(Color.BLACK).makeMove(
                        parsedQueenside,
                        Utils.parseMove("O-O-O")
                ),
                "O-O-O executes through parser and Player"
        );

        TestUtils.assertEquals(
                true,
                parsedQueenside.getPiece(
                        home(Color.BLACK, 2)) instanceof King,
                "Parsed queenside castling moves King to C8"
        );

        // Test Pawn Promotion: E7 E8=Q
        Board parsedPromotion = new Board();

        Position promotionFrom = new Position(6, 4);
        Position promotionTo = new Position(7, 4);

        parsedPromotion.setPiece(
                promotionFrom,
                new Pawn(Color.WHITE, promotionFrom)
        );

        TestUtils.assertEquals(
                true,
                new Player(Color.WHITE).makeMove(
                        parsedPromotion,
                        Utils.parseMove("E7 E8=Q")
                ),
                "Promotion notation executes through parser and Player"
        );

        TestUtils.assertEquals(
                "wQ",
                parsedPromotion.getPiece(promotionTo).toString(),
                "Parsed promotion creates White Queen"
        );

    }
}
