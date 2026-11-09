# Sabid - Phase 2 M3 history panel

Run `java -cp out Main` after compiling src and test. Move E2 to E5, drag B1 to
F6, then move D1 to D7. The right-hand table records one row per move, including
the captured black pawn. Same-square or friendly-piece drops add no rows.
Settings changes preserve the history and reserve room for the sidebar.
Move numbers count individual accepted moves, starting at 1. Side records the
actual moving piece, since Phase 2 permits unrestricted moves regardless of turn.

## Integration for teammates

GUIController owns the chronological history and publishes immutable snapshots
through addHistoryListener/removeHistoryListener. getHistory returns an immutable
copy; entries contain strings, not mutable Piece references. HistoryPanel is a
read-only observer and never changes the board. startNewGame and setBoard clear
history. A king-capture move is recorded before endgame callbacks.

Undo (Abid) should restore model/turn/capture state and remove the corresponding
history entry in one controller operation, then notify history observers.
Save/load (Towsif) should persist and restore history along with model state.
Those restoration APIs are intentionally not implemented in this contribution.
Avoid adding a second history list owned by the sidebar.

## Verification

Merged baseline: 1066 headless / 1119 desktop assertions.
With history: 1088 headless / 1149 desktop assertions, Java 17 build and strict
source Javadoc passed. Includes 60-row auto-scroll and all board-size presets.
No new external dependencies. M3 is not complete team-wide until undo/rack and
New/Save/Load are integrated and tested.
