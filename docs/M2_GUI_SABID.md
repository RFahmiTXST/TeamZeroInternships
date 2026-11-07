# Sabid: Phase 2 Milestone 2

Build using the README command, then run `java -cp out Main`.
Click E2 then E5. Drag B1 to F6 and watch the floating piece. Move D1 to D7
to capture a black pawn. Drop outside the board or press Escape during a drag:
the piece stays at its origin. Resize and repeat.

GUIController owns selection and movement; BoardMouseHandler maps events from
squares and their labels. All GUI moves go through GUIController.movePiece.
Movement intentionally ignores chess legality and turns. Friendly captures,
empty sources and same-square moves are rejected. Board.movePiece records
opposing captures and returns the captured piece for future endgame integration.

Teammates can add turn checks and post-move notification at that shared move
method. Reuse the existing listener rather than installing duplicate movement
listeners. SquarePanel highlighting and BoardPanel refresh are reused.
Turns and the king-capture popup/exit remain teammates' tasks; history is M3.

Verified Java 17-compatible compilation, 884 headless / 894 desktop assertions
and strict source Javadoc. New tests cover real label events, ghost cleanup,
resize, captures, invalid input, reset, replacement and Escape cancellation.
