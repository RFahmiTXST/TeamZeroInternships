# Phase 2 Report and Screenshot Checklist

This checklist tracks the evidence required by `docs/PHASE2_PLAN.md`.
Do not mark items complete until the actual screenshot or test evidence exists.

## Milestone 1: Static Swing chessboard

- [ ] GUI launches using `java -cp out Main` on a desktop with a display.
- [ ] Board shows an 8x8 grid with alternating colors.
- [ ] File labels A through H and rank labels 1 through 8 are readable.
- [ ] All 32 pieces appear in the standard starting positions.
- [ ] All six piece types are visually distinguishable for both colors.
- [ ] Save a screenshot as `screenshots/phase2_m1_initial_board.png`.
- [ ] Run `scripts/verify-phase2-m1.ps1` on Windows; record result.
- [ ] Run `scripts/verify-phase2-m1.sh` using bash; record result.

## Milestone 2: Movement, captures, and winner dialog

- [ ] Screenshot after click-to-move interaction.
- [ ] Screenshot demonstrating drag-and-drop movement.
- [ ] Screenshot after capturing an opponent's piece.
- [ ] Screenshot of the King-capture winner dialog before closing it.

## Milestone 3: Extra GUI features

- [ ] Screenshot of File/Game menu and New Game functionality.
- [ ] Screenshot showing Save Game and Load Game workflow.
- [ ] Screenshot showing the Settings window and applied theme changes.
- [ ] Screenshot showing move history, captured pieces, and Undo.

## Milestone 4: Final verification and submission

- [ ] Compile all source and test files from a clean checkout.
- [ ] Run the full automated test suite without failures.
- [ ] Generate Javadoc with `-private -Xdoclint:all -Werror`, zero warnings.
- [ ] Verify `out/javadoc/index.html` opens correctly.
- [ ] Update README with GUI build, run, and test instructions.
- [ ] Compile screenshots and design explanations into `Phase2_Submission_TeamZero.pdf`.
- [ ] Compare final evidence with the original assignment rubric.
- [ ] Complete peer contribution evaluations if required.

## Evidence log

| Date | Milestone | Evidence file / command | Result | Reviewer |
| --- | --- | --- | --- | --- |
| | | | | |

Generated build output under `out/` is not committed. Keep screenshots
in a shared, team-agreed location and avoid including local machine paths.
