package core;

import board.Board;
import board.Position;
import pieces.Color;
import pieces.Pawn;
import pieces.Piece;
import test.TestUtils;

/** Tests player validation and delegation to Board. */
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
        TestUtils.assertEquals(false, player.makeMove(board, valid), "Pawn cannot capture straight ahead");
        board.setPiece(to, null);
        Position capture = new Position(2, 5);
        board.setPiece(capture, new Pawn(Color.BLACK, capture));
        TestUtils.assertEquals(true, player.makeMove(board, new Move(from, capture)), "Enemy capture delegated");
        TestUtils.assertEquals(true, new Player(Color.BLACK).makeMove(board,
                new Move(capture, from)), "Black can capture white");
        TestUtils.assertEquals(3, board.calls, "Exactly three accepted moves delegated");
        Board real = new Board();
        real.initialize();
        String before = real.render();
        TestUtils.assertEquals(false, player.makeMove(real, new Move(from, new Position(4, 4))), "Pawn cannot move three squares");
        TestUtils.assertEquals(false, player.makeMove(real, new Move(new Position(0, 2), new Position(2, 4))), "Blocked bishop rejected");
        TestUtils.assertEquals(before, real.render(), "Illegal movement preserves real board");
        Piece pawn = real.getPiece(from);
        TestUtils.assertEquals(true, player.makeMove(real, valid), "Real pawn move accepted");
        TestUtils.assertEquals(null, real.getPiece(from), "Real source cleared");
        TestUtils.assertEquals(pawn, real.getPiece(to), "Real destination populated");
        TestUtils.assertEquals(to, pawn.getPosition(), "Real piece position updated");
        TestUtils.assertEquals(true, pawn.hasMoved(), "Real piece marked moved");
        TestUtils.assertEquals(false, player.makeMove(real, new Move(to, new Position(5, 4))), "Moved pawn cannot double-step");
        TestUtils.assertEquals(true, player.makeMove(real, new Move(new Position(0, 6), new Position(2, 5))), "Knight jumps over pawns");
    }
}
