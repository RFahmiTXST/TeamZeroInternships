# Abid's M0 and M1 plan

Developer: Abid Ahnaf Khan (Dev 2). Branch: `dev2/board-core`.
Scope: board foundations only; team requirements remain in `TODO.md`.

## Current state

- Baseline: `b693a06` (M0 template), verified against GitHub main on 2026-10-07.
- Clean working tree before setup; M1 changes now exist in Board and Position.
- Git identity is configured as `Abid90220`; GitHub repository permissions include
  pull and push. No push was needed to verify access.
- Java/Javac 21 are installed (README requires JDK 17+).
- M0 local onboarding is complete. Team-wide M0 is recorded in TODO.md; other
  teammates' local onboarding has not been independently verified.
- The four Dev 2 M1 methods are implemented and locally verified. Dev 1's helpers and Dev 4's test harness are
  not implemented in this baseline. PR #2 is open; no merge has been performed.
- Commits: `c3c5b37` (context setup), `6f9bcc9` (M1 implementation).
- Branch pushed to origin. PR: https://github.com/RFahmiTXST/TeamZeroInternships/pull/2
  targeting main; teammate review and Dev 1's M1 merge are pending.

## Steps

- [x] 1. M0 onboarding: verify baseline, Git access, build/run; create work branch
  and lightweight context files.
- [x] 2. Implement `Position.toString()`, `Board.getPiece()`, `Board.setPiece()`,
  and `Board.isEmpty()` with accurate Javadoc, preserving public signatures.
- [x] 3. Verify M1 behavior, update Abid's TODO checkboxes, and prepare a concise
  PR description and reviewable diff. Make small meaningful commits when requested.
- [x] 4a. Commit, push, and open the M1 PR against main (PR #2).
- [ ] 4b. Get one teammate review and merge after Dev 1. Record PR/merge status
  separately from local completion.

## M1 decisions and acceptance checks

- `Position.toString()`: `(1,4)` = E2, `(0,0)` = A1, `(7,7)` = H8,
  `(7,3)` = D8. Preserve existing bounds, equality, and hash-code behavior.
- `getPiece()`: return the stored object, or null for empty/off-board squares.
- `setPiece()`: store a piece or null; choose IllegalArgumentException for
  off-board squares and document that behavior. Do not update the piece's position.
- `isEmpty()`: true only for on-board squares containing no piece.
- Check all 64 empty squares, placement/readback identity, removal with null,
  and off-board bounds on each side. Compile without warnings.
- Use temporary dependency-free checks until Dev 4's shared harness is available;
  coordinate permanent board tests with Dev 4.
- M1 does not include initialize, render, movement/capture, or chess rules.

## Latest verification

2026-10-07:

```sh
javac -Xlint:all -d /private/tmp/teamzero-m1-check src/Main.java src/board/*.java src/core/*.java src/pieces/*.java src/ui/*.java src/utils/*.java /private/tmp/teamzero-m1-check/M1Check.java
java -ea -cp /private/tmp/teamzero-m1-check M1Check
javadoc -quiet -private -Xdoclint:all -d /private/tmp/teamzero-m1-javadoc -sourcepath src board
git diff --check
```

Passed: compilation and board Javadoc produced no warnings/errors; 275 temporary
checks passed for notation, all 64 squares, placement/replacement/removal,
grid-only updates, off-board behavior, and equality. Diff whitespace check passed.
Temporary check source is outside the repository; permanent tests await Dev 4's
harness. M0 build/run and read-only GitHub checks passed in the previous step.

## Next action and resume prompt

Diff reviewed: four required methods match the contracts, signatures are unchanged,
and no other developer's source files were modified. Abid's Phase A checkboxes in
TODO.md are complete locally. Team M1 remains incomplete.

Next: wait for one teammate review and Dev 1's M1 merge; address review feedback
when requested. Do not merge before those requirements are satisfied. After the
PR merges, update local main and begin M2 on a new branch when requested.

## Prepared PR

Title: `Implement M1 board accessors and chess coordinate notation`

Body:

```markdown
Implements Abid's M1 board foundations: positions now display chess notation
(for example E2), and board accessors support piece placement, removal, and
empty-square checks. Off-board reads return null, off-board squares are not
empty, and off-board writes throw IllegalArgumentException. Public signatures
are unchanged; setPiece updates only the grid.

Adds lightweight AGENTS.md/plans.md context files and marks Dev 2's Phase A
tasks complete locally.

Validation: full source compilation with -Xlint:all and board Javadoc with
-Xdoclint:all passed without warnings. A temporary dependency-free check passed
275 assertions covering notation, all 64 squares, placement/replacement/removal,
and boundary behavior. Permanent tests await Dev 4's shared harness.

Merge second at M1, after Dev 1, with one teammate review. This completes only
Dev 2's M1 work; M2 board setup/display/movement remain pending.
```

```text
I'm Abid Ahnaf Khan, Developer 2. Follow AGENTS.md and read plans.md.
Complete the next unfinished step for my M0/M1 work only. Read relevant files,
keep changes simple, and verify the behavior. Update plans.md with results and
the next action. Stop at the step boundary and give a short summary.
```
