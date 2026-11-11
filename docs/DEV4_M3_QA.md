# Phase 2 M3 — Dev 4 QA checklist

Scope: File menu (Save Game, Load Game), Game menu (New Game), board/turn/history/capture restoration.

## Automated checks

- [x] Compile all src/ and test/ with `javac --release 17`.
- [x] Headless `TestRunner` passes with 0 failures and exit code 0.
- [x] `GameFileServiceTest` passes, including round-trip data and invalid-save preservation.
- [x] Strict source Javadoc passes with 0 warnings and exit code 0.

## Manual GUI checks

- [x] New Game appears in Game menu; Save/Load appear in File menu.
- [x] Make moves including a capture; Save Game produces a `.chess` file.
- [x] Make further moves; Load Game restores exact pieces, current turn, captured list and history table.
- [x] Cancel Save and Cancel Load without changing the current game.
- [x] Load a corrupted `.chess` file; error dialog appears and current board is unchanged.
- [x] New Game asks for confirmation. No leaves board unchanged; Yes restores standard 32-piece setup and clears captured list/history.
- [x] Settings dialog and board-size presets still work.
- [x] King capture still shows the winner dialog and exits on dismissal.
- [ ] Coordinate with Dev 2: when Undo is merged, ensure loading clears stale Undo records and resets the Undo button; test rack/history/turn together.

## Test results

- Compiler exit code: 0
- TestRunner shared assertions: 1094 passed, 0 failed
- Utils assertions: 28 passed, 0 failed
- Total assertions: 1122 passed, 0 failed
- Javadoc exit code: 0
- Javadoc warnings: 0
- Manual save/load result: PASS
- Reviewer: Pending
