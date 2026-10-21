package core;

import board.Board;
import board.Position;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import test.TestUtils;

/** Tests the M2 player boundary independently of Board's pending PR. */
public final class PlayerTest {
    private PlayerTest() { }

    /** Board spy that records delegation without supplying chess behavior. */
    private static final class RecordingBoard extends Board {
        private int calls;
        private Position from;
        private Position to;

        @Override
        public Piece movePiece(Position source, Position destination) {
            calls++;
            from = source;
            to = destination;
            return getPiece(destination);
        }
    }

    /** Runs rejection and delegation checks. @param args unused */
    public static void main(String[] args) {
        RecordingBoard board = new RecordingBoard();
        Position from = new Position(1, 4), to = new Position(3, 4);
        Piece white = new Pawn(Color.WHITE, from);
        board.setPiece(from, white);
        Player player = new Player(Color.WHITE);
        Move valid = new Move(from, to);
        TestUtils.assertEquals(false, player.makeMove(null, valid), "Null board rejected");
        Move[] invalid = {null, new Move(null, to), new Move(from, null),
            new Move(new Position(-1, 0), to), new Move(from, new Position(8, 0)),
            new Move(from, from), new Move(to, from), Move.castle(true),
            new Move(from, to, 'Q')};
        for (int i = 0; i < invalid.length; i++) {
            TestUtils.assertEquals(false, player.makeMove(board, invalid[i]), "Invalid move " + i);
        }
        TestUtils.assertEquals(false, new Player(Color.BLACK).makeMove(board, valid), "Opponent source rejected");
        board.setPiece(to, new Pawn(Color.WHITE, to));
        TestUtils.assertEquals(false, player.makeMove(board, valid), "Friendly capture rejected");
        TestUtils.assertEquals(0, board.calls, "Rejected moves never delegated");
        TestUtils.assertEquals(white, board.getPiece(from), "Rejected moves preserve source");
        TestUtils.assertEquals(false, white.hasMoved(), "Rejected moves preserve piece state");
        board.setPiece(to, null);
        TestUtils.assertEquals(true, player.makeMove(board, valid), "Own piece move delegated");
        TestUtils.assertEquals(from, board.from, "Correct source delegated");
        TestUtils.assertEquals(to, board.to, "Correct destination delegated");
        board.setPiece(to, new Pawn(Color.BLACK, to));
        TestUtils.assertEquals(true, player.makeMove(board, valid), "Enemy capture delegated");
        TestUtils.assertEquals(true, new Player(Color.BLACK).makeMove(board,
                new Move(to, from)), "Black can capture white");
        TestUtils.assertEquals(3, board.calls, "Exactly three accepted moves delegated");
    }
}
