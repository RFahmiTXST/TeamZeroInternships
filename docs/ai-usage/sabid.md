# AI Usage Log: Sabid Mahmud (Dev 3)

## Milestone 1 — Utility Methods

**Task / Method:** Implement input validation and parsing methods in `Utils.java`.

**Prompt / Question Asked:** Asked ChatGPT to explain and help implement `normalize()`, `isValidMoveFormat()`, `parsePosition()`, and `parseMove()`, with readable Javadoc comments.

**Code / Idea Used:** Used regular expressions to validate chess moves, normalized input by removing extra whitespace and converting it to uppercase, and converted chess coordinates into `Position` and `Move` objects.

**Result / Modification:** Reviewed and implemented the methods in `Utils.java`, then committed the changes.

## Milestone 1 — Utility Testing

**Task / Method:** Create and run tests for `Utils.java`.

**Prompt / Question Asked:** Asked ChatGPT to help create a standalone Java test suite and explain how to compile and run it using VS Code.

**Code / Idea Used:** Created `UtilsTest.java` with 22 tests covering valid inputs, invalid inputs, and edge cases.

**Result / Modification:** Successfully compiled the Java files using JDK 21 and ran the tests. All 22 tests passed with 0 failures. Committed the test file separately.



## Milestone 2 — Console Interface Implementation

**Task / Method:** Implement console interaction methods in `ConsoleUI.java`.

**Prompt / Question Asked:** Asked ChatGPT to help plan, implement, explain, and test `showWelcome()`, `showBoard()`, `showTurn()`, `promptMove()`, and `showError()`.

**Code / Idea Used:** Used Java console output methods to display game instructions, player turns, and errors. Used `Scanner.nextLine()` to read moves and `Board.render()` to display the chessboard.

**Result / Modification:** Implemented all five methods, successfully compiled the project using JDK 21, and manually tested `showWelcome()`, `showTurn()`, `promptMove()`, and `showError()` using JShell. Committed and pushed the changes to `feature/sabidmahmud01` in commit `fbb9c4f`.

**Pending Work:** `showBoard()` still requires integration testing after Abid implements `Board.render()`. `Player.makeMove()` also remains unfinished because `Board.movePiece()` has not yet been implemented.
# 2026-10-20 — Complete Dev 3 Milestone 2

**Request:** Finish my part after reviewing Abid's Board implementation.

**Assistance used:** Codex inspected the latest feature branch and Phase 1
plan, retained the existing ConsoleUI implementation, and implemented
Player.makeMove() with M2 ownership and coordinate checks, same-square and
friendly-capture rejection, and delegation to Board.movePiece(). Unsupported
castling/promotion requests are rejected. Full movement legality and the
game loop remain M3 work.

**Tests:** Added permanent Player and ConsoleUI tests and registered them,
UtilsTest, and PieceMovementTest in TestRunner. Compilation with Java 17
compatibility and -Xlint:all passed. The runner passed 94 assertions (72
shared plus 22 Utils). A separate, uncommitted audit compiled against Abid's
Board at 8ffa8ad and passed eight Player/Board/UI integration checks; the
94 regression assertions also passed against that Board implementation.

**Status:** Local feature-branch changes only. No merge or push performed.
Board PR #6 remains an integration dependency. Updated the M2 checklist and
README to describe verified coverage and remaining integration limits.

## 2026-10-21 — Milestone 3 game loop and movement integration

**Request:** "Ok pull everything, let's go to milestone 3".

**Assistance used:** Codex fetched all remote branches and verified that PRs
#6, #7, and #8 had merged into dev. The clean local feature branch was
fast-forwarded to dev at d7d0478 without conflicts. Implemented Game.start(),
play(), end(), and turn switching; invalid input or movement retries the same
player. Player.makeMove() now checks possibleMoves(board) before mutating
the board. ConsoleUI handles EOF and displays the end message.

**Verification:** Java 17 compilation with -Xlint:all passed. All 173
assertions passed (151 shared and 22 Utils), including real game integration
for turn order, invalid moves, captures, restart, quit/exit, and EOF. The Main
entry point also passed a scripted E2 E4, E7 E5, invalid input, G1 F3, quit
smoke test. No external test dependencies were added.

**Scope:** Dev 3's M3 game flow and piece movement validation are implemented.
Check detection/king safety and optional castling/promotion remain team work.
Updated the checklist and README. Changes remain local pending review; no
M3 push or remote merge was performed.

## 2026-10-23 — Resolve M3 integration conflicts

**Request:** Pull the team's latest changes, resolve PR #9 conflicts, verify,
commit, and push the feature branch.

**Assistance used:** Codex fetched all branches and merged origin/dev at
 e6faede into feature/sabidmahmud01. Accepted dev's Player.java and PlayerTest.java,
preserving Dev 1's legality checks and Dev 4's castling/promotion delegation.
Verified the automatically merged TestRunner and plan retained all team work
and GameTest. Registered BoardCheckTest and added game-loop castling checks.
Updated stale README and integration notes.

**Verification:** Java 17 compilation with -Xlint:all passed. The combined
runner passed 352 shared and 28 Utils assertions (380 total), with no failures.
Ordinary-move king safety remains outside this resolution's scope.

## 2026-10-23 — Milestone 4 final review and documentation

**Request:** "Pull everything and do milestone 4".

**Assistance used:** Codex fetched all remote branches and fast-forwarded the
clean feature checkout to dev 929830a after PR #9 merged. Reviewed merged
gameplay, Board, piece movement, parsing, special moves, and regression tests.
Completed Move.toString() and added eight notation tests. Corrected missing
and malformed source Javadocs and removed obsolete implementation TODOs.
Updated README build/documentation instructions, the Dev 3 checklist, and
docs/M4_QA_SABID.md with verification and remaining release requirements.

**Validation:** Java 17-compatible compilation with -Xlint:all passed; all
388 assertions passed (360 shared, 28 Utils). Source Javadoc generated with
-private -Xdoclint:all -Werror and zero warnings. A clean source copy passed
the documented build, test, and Javadoc commands and a scripted game covering
invalid input, turns, castling, and quit. Generated overview and Move HTML
were checked. Javadoc was invoked by its installed JDK path because the
Windows Java shortcut directory does not expose javadoc on PATH.

**Limits:** Ordinary-move king safety, checkmate/stalemate, and en passant
remain documented limitations. Original rubric and peer-evaluation forms are
not in the checkout, so they have not been marked complete. The final release
PR remains Rayed's responsibility. M4 changes are local pending review;
no M4 push or dev-to-main merge was performed.

## 2026-11-04 — Phase 2 Milestone 1 controller

**Request:** "Complete my Milestone 1 first."

**Assistance adopted:** Codex implemented GUIController to connect Board and
BoardPanel, initialize all 32 pieces, refresh the view, and replace an existing
board without resetting it. ChessFrame delegates model access and refresh to
this controller. Mutation is restricted to Swing's EDT; Main already launches
through invokeLater. Existing GUI tests now also execute on the EDT.

**Verification:** Java 17-compatible compilation passed. Full desktop test run:
807 shared + 28 Utils assertions (835 total); headless: 797 + 28 (825 total).
Controller checks cover all starting squares, reset after capture, supplied and
replacement models, refresh, invalid arguments, and rejected non-EDT updates.
Strict source Javadoc (-private -Xdoclint:all -Werror) passed without warnings.
The compiler retains seven existing Swing serialization/constructor warnings.

**Scope:** M1 only. Mouse movement, capture events, and history remain later
milestones. Abid's pending board-layout PR was not merged. No push performed.


## 2026-11-06 - Phase 2 M2 mouse controls

Request: self-review and merge M1, then implement my M2 contribution.
Codex assisted the review; this was not independent peer approval. PR #21 was
merged after integration passed 855 headless / 865 desktop assertions and
strict source Javadoc.

Codex implemented click selection and unrestricted moves in GUIController;
BoardMouseHandler translates events from squares and piece labels, adds a
floating drag icon, handles resized coordinates, and cancels on Escape,
outside drops, reset and model replacement. Existing Board.movePiece records
opposing captures. Friendly captures and same-square moves are rejected.
The status bar now describes controls instead of claiming turn tracking.

Validation: Java 17-compatible build passed; 884 headless / 894 desktop
assertions passed, including 28 Utils assertions each. MouseMovementTest adds
29 checks using actual component events. Strict source Javadoc passed with
-private -Xdoclint:all -Werror. User verified the GUI manually and authorized committing, pushing and opening a PR into dev. This contribution does not merge M2 into dev.
Teammates still own turns and the king-capture popup/termination; M3 history
is not included. Only Sabid's M2 checklist entries were checked.


## 2026-11-08 - Phase 2 M3 move history

User requested fetching team changes, verifying completion and starting M3.
Verified merged dev 2bcbe23: Rayed's capture/turn PR #23, Towsif's endgame PR #24,
Abid's feedback verification PR #25 and Rayed's settings PR #26 are merged.
Baseline passed 1066 headless / 1119 desktop assertions. Seven existing Swing
compiler warnings remain; these are not a new regression. Abid's M2 checklist
is stale even though the implementation and PR #25 verification are present.

Codex implemented immutable HistoryEntry snapshots, controller history observers,
and a read-only scrollable HistoryPanel in the frame's EAST region. Accepted
moves record actual piece color/type, origin, destination and capture; rejected
moves do not create entries. Reset/replacement clears history. Entries are
published before capture callbacks so winning moves appear before the modal
endgame dialog. Window sizing reserves sidebar space for all settings presets.

Final verification: Java 17-targeted compilation, 1088 headless / 1149 desktop
assertions, and strict source Javadoc passed. New checks cover immutable
snapshots, captures, event ordering, reset/replacement, listener lifecycle,
read-only cells, 60-row scrolling, frame wiring and all board-size presets.
This is automated verification; no new manual live-GUI confirmation is claimed.

Scope: Sabid's history panel only. Undo/captured rack remain Abid's work;
New/Save/Load remain Towsif's work. Future load/undo must restore controller
history together with board state; setBoard currently clears history rather
than pretending to recover it. User authorized committing and pushing after checking conflicts. Latest dev was 2bcbe23 and matched the local base; all six source/test files matched the verified build. M3 is not merged into dev.
