# Phase 2 M4 - Sabid event-handling QA

Verified 2026-11-10 against dev 4f635e3, including merged PRs #27, #28 and #29.
Sabid's assigned event QA and personal AI log are complete for this integration.

## Verified behavior

Standalone test/gui/SabidM4EventTest.java provides 142 deterministic checks:
- Forty back-to-back mouse moves record exactly one history entry each. Repeated Undo restores the original position, moved flag and empty table.
- Drops beyond all four board edges at three window proportions preserve model/history, clean up the drag ghost and create no Undo records.
- Escape, New Game and board replacement during dragging cancel stale releases and permit subsequent valid gestures.
- Loading through the existing restoreSession API during selection or dragging cancels the old gesture, restores history/capture/turn state, clears prior Undo records and permits a new move. Undo of that new move stops at the loaded session boundary.

Java 17-targeted compilation passed. All 142 new checks passed in both headless and desktop modes. Existing TestRunner passed 1,170 headless and 1,240 desktop assertions (including 28 utility assertions each). Existing Save/Load tests cover file round-trip, malformed files and rack refresh; this suite focuses on Sabid's event boundary, without duplicating file handling.

No production-code fix was needed. Automated Swing events do not constitute manual physical mouse or multi-monitor testing.

## Running Sabid's suite

Compile all src and test Java files using the repository build steps, then run:

```text
java -ea -Djava.awt.headless=true -cp out test.gui.SabidM4EventTest
java -ea -Djava.awt.headless=false -cp out test.gui.SabidM4EventTest
```

The standalone suite throws on failure. It deliberately avoids changing shared TestRunner.

## Scope and handoff

Changes are limited to this report, SabidM4EventTest and Sabid's AI log. No production files, teammate logs, README, shared plan or architecture review are changed. The shared plan's Sabid M4 checkbox has not been edited to avoid overlapping Rayed's pending plan changes; this report records completion.

PR #28 is merged; the prior Save/Load integration blocker is resolved. Rayed's M4 PR #30 remains separate. Rayed owns architecture and final release; Towsif owns final Javadoc/README; Abid owns component peer review/log. Shared submission screenshots, PDF and peer evaluations still need final team coordination. This report does not claim the entire team's M4 delivery is complete.

These three paths do not overlap PR #30's current paths. Merge checks against current dev and Rayed's branch must be repeated if either advances. Future edits cannot be guaranteed conflict-free.
