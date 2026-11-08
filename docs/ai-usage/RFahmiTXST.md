# AI Usage Log: Rayed Fahmi (Dev 1)

## Milestone 1: Project Setup & Core Abstractions

**Task / Method:** Initial project planning and setup.\
**Prompt / Question Asked:** "Suppose you are a senior software enginner. I want you to design the to-do list (PHASE1_PLAN.md) for 4 developers to collaborate on based on the rubric... setup the full starting package for this project (basically the first task for Dev 1)."\
**Code / Idea Used:** The AI generated the `PHASE1_PLAN.md` strictly following the rubric's Github branch and PR constraints. It then created the directory structure and generated the template Java files with precise `TODO(Dev X)` tags and Javadoc stubs for all teammates.\
**Result / Modification:** The project structure was successfully initialized locally, and the AI helped commit and push the `main` and `dev` branches to the GitHub repository.\

**Task / Method:** Implementing core Piece logic (`Piece.java`, `Color.java`).\
**Prompt / Question Asked:** "Carefully read through the method implementations I did and make reasonable (small & industry style) commits. Don't push it to cloud."\
**Code / Idea Used:** The AI made appropriate commits for `Color.getPrefix()`, `Piece.toString()`, `Piece.move()`, `Piece.isOpponent()`, `Piece.canMoveTo()`, and the complex `Piece.slide()` logic.\
**Result / Modification:** The logic was implemented perfectly within scope. The AI committed the changes cleanly and the developer pushed them to `feature/RFahmiTXST` branch after verifying.\

## Milestone 2: Board Infrastructure & Piece Movements

**Task / Method:** Implementing piece movement logic for Rook, Bishop, Queen, and Knight (`Rook.java`, `Bishop.java`, `Queen.java`, `Knight.java`, `Piece.java`, `Color.java`).\
**Prompt / Question Asked:** "Suppose you are an expert Java software developer. Task: Complete the milestone 2 for the Dev 1 (Rayed) mentioned in PHASE1_PLAN.md on the Git branch 'feature/RFahmiTXST'... verify if the implementation was correct and there's no bugs, errors or issues. Then mark the tasks complete in PHASE1_PLAN.md... Lastly, add the updates in RFahmiTXST.md..."\
**Code / Idea Used:** Implemented `possibleMoves(Board)` for `Rook` (using straight `slide()` directions), `Bishop` (using diagonal `slide()` directions), and `Queen` (combining all 8 sliding directions). For `Knight`, implemented `possibleMoves(Board)` using the 8 L-shaped jump offsets `{±1,±2}` and `{±2,±1}` checked against `canMoveTo(Board, Position)`. Implemented all piece symbols ('R', 'B', 'Q', 'N') and full Javadoc documentation for `Piece.java` and `Color.java`. Developed a unit test suite (`PieceMovementTest.java`) with 36 assertions testing open-board moves, corner moves, jump mechanics, friendly blocking, and enemy capture rules.\
**Result / Modification:** Code compiled cleanly on JDK 17 with `-Xlint:all` and zero warnings. All 36 assertions in `PieceMovementTest` passed, alongside all regression suites (`PawnTest` and `UtilsTest`). Tasks were marked complete in `PHASE1_PLAN.md`.\

**Task / Method:** Organizing industry-grade Git commits for Milestone 2.\
**Prompt / Question Asked:** "Suppose you are a dev-ops expert with substantial experience managing Git repositories. Task: Make industry grade (short and reasonable) commits for the milestone 2 code changes for Dev 1 (Rayed)... Make sure not to push the commit to the remote repository, I'll verify and push it myself."\
**Code / Idea Used:** Applied Conventional Commits standards to structure atomic, focused commits separating feature additions (`feat`), documentation (`docs`), and test cases (`test`).\
**Result / Modification:** Created structured, traceable commits on `feature/RFahmiTXST` without pushing to the remote repository, ready for review.\

## Milestone 3: Game Loop, Turn Flow & Integration

**Task / Method:** Assisting Dev 3 in integrating move legality (Player.java, PlayerTest.java).\
**Prompt / Question Asked:** "Suppose you are an expert Java software developer. Task: Verify the milestone 3 tasks for dev 1 (Rayed) mentioned in PHASE1_PLAN.md were correctly implemented and there's no bugs, errors or issues.. Then mark the tasks complete in PHASE1_PLAN.md... Lastly, add the updates in RFahmiTXST.md..."\
**Code / Idea Used:** Verified integration of piece movement legality in Player.makeMove() by querying piece.possibleMoves(board) and rejecting any requested destination not contained in that list. Evaluated the expanded unit tests in PlayerTest.java. Now, it can verify illegal moves across piece types (pawns moving three squares or capturing straight, blocked bishops, rooks moving diagonally, knights moving straight).\
**Result / Modification:** Code compiled cleanly under JDK 17 with -Xlint:all and zero warnings. All 150 test assertions passed across the central test runner (TestRunner) with zero failures. Dev 1's Milestone 3 task was marked complete in PHASE1_PLAN.md.\

**Task / Method:** Organizing industry-grade Git commits for Milestone 3.\
**Prompt / Question Asked:** "Suppose you are a dev-ops expert with substantial experience managing Git repositories. Task: Create industry grade (short and reasonable) commits for the milestone 3 code changes for Dev 1 (Rayed)... Make sure not to push the commit to the remote repository, I'll verify and push it myself."\
**Code / Idea Used:** Applied Conventional Commits standards to structure atomic, focused commits separating feature additions (`feat`), documentation (`docs`), and test cases (`test`).\
**Result / Modification:** Created structured, traceable commits on `feature/RFahmiTXST` without pushing to the remote repository, ready for review.\


## Milestone 4: QA, Documentation & Final Delivery

**Task / Method:** Final peer review of merged codebase on `dev` and preparing final repository delivery.\
**Prompt / Question Asked:** "Suppose you are an expert Java software developer. Task: Verify the completion of milestone 4 tasks for dev 1 (Rayed) mentioned in PHASE1_PLAN.md.. Then mark the tasks complete in PHASE1_PLAN.md... Lastly, add the updates in RFahmiTXST.md..."\
**Code / Idea Used:** Conducted an architectural and code-quality peer review of all merged packages (`board`, `core`, `pieces`, `ui`, `utils`). Verified low coupling and high cohesion across components (Board state isolation, Player move delegation, UI abstraction, and special move decoupling via AttackDetector and SpecialMoveHandler). Ran full test suite, verified manual game flows, and audited Javadoc coverage with `-Xdoclint:all`. Prepared the final PR management structure and delivery checklist for merging `dev` into `main`.\
**Result / Modification:** Validated zero regressions across the codebase. Updated Dev 1's tasks in `PHASE1_PLAN.md` and personal AI usage log. Documented peer review findings and identified minor private Javadoc doclint warnings for Dev 4's M4 task.\


---

# Phase 2: Java Swing Graphical User Interface (GUI)

## Milestone 1: Swing GUI Shell, Board Grid & Piece Visuals

**Task / Method:** Establishing Phase 2 Swing GUI architecture, procedural piece rendering, main window frame, and launch integration (`gui/ChessFrame.java`, `gui/pieces/PieceIconManager.java`, `gui/pieces/PieceView.java`, `gui/board/BoardPanel.java`, `gui/board/SquarePanel.java`, `gui/dialogs/DialogHelper.java`, `Main.java`, `test/gui/PieceIconManagerTest.java`).\
**Prompt / Question Asked:** "Suppose you are an expert Java software developer. Task: Verify that the milestone 1 tasks for dev 1 (Rayed) mentioned in PHASE2_PLAN.md were correctly implemented and there's no bugs, errors or issues."\
**Code / Idea Used:** Verified the design and implemented of the GUI package architecture under `src/gui/`, `src/gui/pieces/`, `src/gui/board/`, and `src/gui/dialogs/`. Built `PieceIconManager`. The developer delivered high-performance, resolution-independent procedural Java2D vector rendering for all 6 piece types (`Pawn`, `Rook`, `Knight`, `Bishop`, `Queen`, `King`) across both `White` and `Black` with caching and multi-resolution scaling support. The developer created `PieceView` for UI metadata encapsulation. The developer created `ChessFrame` (`JFrame`) with `BorderLayout`, status footer, menu bar skeleton, and proper window closing behavior. The developer built `BoardPanel` with 8x8 `GridLayout`, alternating light/dark square colors (`SquarePanel`), and coordinate border labels. The developer updated `Main.java` to launch the Swing GUI by default on the Event Dispatch Thread while supporting `--console` / `-c` terminal mode and headless fallback. Developed `PieceIconManagerTest` with 32 assertions and registered it in `TestRunner`.\
**Result / Modification:** Code compiled cleanly under Java 17+ and JDK 26. All 448 automated test assertions (420 shared + 28 Utils) passed with zero failures. Complete Javadoc audit with `-private -Xdoclint:all -Werror` generated zero warnings and zero errors across all packages.

**Task / Method:** Organizing industry-grade Git commits for Milestone 1.\
**Prompt / Question Asked:** "Suppose you are a dev-ops expert with substantial experience managing Git repositories. Task: Create industry grade (short and reasonable) commits for the Phase 2 milestone 1 code changes for Dev 1 (Rayed)... Make sure not to push the commit to the remote repository, I'll verify and push it myself."\
**Code / Idea Used:** Applied Conventional Commits standards to structure atomic, focused commits separating Phase 2 planning and guidelines (`docs(plan)`), procedural piece icon rendering and metadata (`feat(gui)`), board layout and dialog foundations (`feat(gui)`), main window frame and entry point integration (`feat(gui)`), test coverage and test runner registration (`test(gui)`), and AI usage documentation (`docs(ai-usage)`).\
**Result / Modification:** Created structured, traceable commits on `feature/RFahmiTXST` without pushing to the remote repository, ready for review.\


## Milestone 2: Mouse Event Handling, Piece Movement & Captures

**Task / Method:** Implementing piece capture handling, clean board disappearance, and active player turn tracking (`gui/GUIController.java`, `gui/ChessFrame.java`, `gui/board/BoardPanel.java`, `board/Board.java`, `test/gui/CaptureAndTurnTest.java`, `test/TestRunner.java`).\
**Prompt / Question Asked:** "Suppose you are an expert Java software developer. Task: Verify that the milestone 2 tasks for dev 1 (Rayed) mentioned in PHASE2_PLAN.md were correctly implemented and there's no bugs, errors or issues."\
**Code / Idea Used:** Verified full piece capture handling in `GUIController.movePiece()` and `GUIController.capturePiece()`. When a piece moves onto an opponent square, the captured piece is cleanly removed from the `Board` model and stored in `capturedPieces`, its icon is immediately cleared/replaced from the display (`SquarePanel`), and registered `CaptureListener` callbacks are notified (enabling King capture detection for Dev 4). Confirmed that the system prohibits friendly piece captures to preserve board integrity. Active player turn state (`Color currentTurn`) is maintained, providing `getCurrentTurn()`, `setCurrentTurn()`, `switchTurn()`, and automatic status bar updates on `ChessFrame` via `StatusListener` ("White's Turn" / "Black's Turn"). Developer added `recordCapture()`, `getCapturedPieces()`, and `getCapturedPieces(Color)` to `Board.java`, and `updateSquare(Position)` to `BoardPanel.java`. Verified the thorough unit test suite (`CaptureAndTurnTest.java`) with 33 assertions covering turn initialization, turn alternation, capture execution, model/view piece removal, friendly fire prevention, King capture detection, game resets, and EDT thread-safety.\
**Result / Modification:** Full automated test suite passes with zero failures (940 total tests: 912 shared + 28 Utils in display mode, 924 in headless mode). Zero Javadoc warnings/errors under strict `-private -Xdoclint:all -Werror`.

**Task / Method:** Organizing industry-grade Git commits for Milestone 2.\
**Prompt / Question Asked:** "Suppose you are a dev-ops expert with substantial experience managing Git repositories. Task: Create industry grade (short and reasonable) commits for the Phase 2 milestone 2 code changes for Dev 1 (Rayed)... Make sure not to push the commit to the remote repository, I'll verify and push it myself."\
**Code / Idea Used:** Applied Conventional Commits standards to structure atomic, focused commits separating board infrastructure and capture history (`feat(board)`), GUI controller capture handling and active turn tracking (`feat(gui)`), unit test coverage and test runner registration (`test(gui)`), and Phase 2 Milestone 2 task tracking and AI usage documentation (`docs(dev1)`).\
**Result / Modification:** Created structured, traceable commits on `feature/RFahmiTXST` without pushing to the remote repository, ready for review.\