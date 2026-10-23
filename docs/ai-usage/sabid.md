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
