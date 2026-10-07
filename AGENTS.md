# Working in this repository

This is a college team project: a two-player Java console chess game. Keep code
and explanations simple enough for teammates to understand and maintain.

## Start and resume

- Read `plans.md` for the current developer, step, verification, and next action.
- `TODO.md` is the shared requirements and milestone checklist. Read only the
  relevant owner's section and team rules unless the task needs more context.
- Inspect Git status before changes. Preserve existing work. Read relevant source
  files and dependencies rather than rereading the entire repository.
- Complete only the requested step. Verify it, update `plans.md`, and give a short
  summary of changes, results, and the next action. Do not append chat transcripts.
- Do not use sub-agents unless explicitly requested.

## Ownership and collaboration

- Dev 1, Rayed: `src/pieces/Piece.java`, `Color.java`, Rook, Bishop, Knight, Queen.
- Dev 2, Abid: `src/board/Board.java`, `src/board/Position.java`.
- Dev 3, Sabid: `src/core/`, `src/ui/`, `src/utils/`.
- Dev 4, Towsif: Pawn, King, `test/`, `docs/`, README, `.gitignore`, Main.
- Work on `devN/<topic>` branches; never commit directly to `main`. Make small,
  meaningful commits. Every PR requires one teammate review before merging.
- Only change the current developer's source files. Coordinate changes to another
  owner's files. Update your own TODO checkboxes and context documents as needed.
- Preserve shared public signatures. Signature changes require Rayed's approval
  through the team chat first; report the needed change rather than making it.
- M1 merge order: Dev 1, Dev 2, Dev 3, Dev 4. M2: Dev 2, Dev 1, Dev 4, Dev 3.
- Distinguish local implementation, verified tests, PR review, and merged work.
  Never mark a whole team milestone complete from one developer's progress.

## Contracts and verification

- Coordinates: row 0 = rank 1, column 0 = file A; E2 = `new Position(1, 4)`.
- A new Board is empty; `initialize()` sets up the starting pieces.
- Position permits off-board candidates; `isOnBoard()` checks bounds.
- `setPiece()` changes the grid only. `movePiece()` updates the grid and calls
  `Piece.move()`. Callers check move legality.
- Keep Javadoc for classes, methods, and attributes accurate.
- Before a PR, compile: `javac -Xlint:all -d out $(find src -name '*.java')`.
- Template run: `java -cp out Main`. Its placeholder message is expected at M0.
- Once Dev 4's test harness exists, compile and run:
  `javac -d out $(find src test -name '*.java') && java -ea -cp out TestRunner`.
- Until then, use small temporary checks for the current behavior; do not create
  a competing test framework. Record commands and outcomes in `plans.md`.
- Keep M0/M1 scoped to foundations. M2 features and optional chess rules wait.
