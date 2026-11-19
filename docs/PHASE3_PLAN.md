# ♟️ Integrated GUI Chess Game: Phase 3 Team Plan

This is the central planning and architectural specification document for **Phase 3** of the Chess project. It establishes our team guidelines, Git branching strategy, member responsibilities, Model-View-Controller (MVC) integration architecture, rule enforcement engine, Extra Credit roadmap (including our AI Chess Player), and four development milestones.

---

## 👥 Team Members & Feature Branches

All development must occur strictly on designated feature branches. No developer may commit directly to `main` or `dev`.

| Developer | Role | Focus | Branch Name |
| :--- | :--- | :--- | :--- |
| **Rayed Fahmi** | **Dev 1** | Core Integration Architecture, Legal Move Simulation, AI Chess Player & UML Design | `feature/RFahmiTXST` |
| **Abid Ahnaf Khan** | **Dev 2** | Board Infrastructure, Dynamic Move Hints, Visual Check Highlighting & View Sync | `feature/ajv163` |
| **Sabid Mahmud** | **Dev 3** | Turn Enforcement, Move Input Validation, Notation/History Sync & Event Refinements | `feature/sabidmahmud01` |
| **Towsif Hassan** | **Dev 4 (QA / PM)** | Checkmate/Stalemate Engine, Special Rules GUI Integration, Regression QA & Javadoc | `feature/cyber-kuro` |

---

## 📜 Team Guidelines & Engineering Workflow (Crucial)

To qualify for maximum marks and the **Extra Credit A (+2 points)** for Git/AI process quality, all four developers must adhere strictly to the following standards:

1. **Git Branching & Pull Request Workflow:**
   - `main`: Production-ready release branch. Only updated via fast-forward or squash merge from `dev` upon milestone verification.
   - `dev`: Active integration branch. All feature branches branch from and target `dev`.
   - `feature/<github-username>`: Isolated working branch for each developer.
   - **PR Requirement:** Every developer must open at least one well-documented Pull Request targeting `dev` for each milestone, conduct peer reviews on teammate PRs, and resolve all review comments prior to merging.
   - Never delete remote feature branches; keep them intact until final course grading is completed.

2. **Incremental Commit Flow (Extra Credit A Requirement):**
   - Commits must reflect real, granular development progress across design, implementation, testing, debugging, and refinement.
   - **Prohibited:** Giant monolithic code drops at the end of milestones.
   - Follow standard conventional commit prefixes: `feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`.
   - Ensure commit messages are self-explanatory and descriptive (e.g., `feat(controller): implement king safety move simulation to reject moves exposing check`).

3. **AI Usage Logs (Extra Credit A Requirement):**
   - Every developer must maintain their continuous AI log in `docs/ai-usage/<github-username>.md`.
   - Each entry must document:
     - **Task / Method:** Component or algorithm worked on.
     - **Prompt / Question Asked:** Verbatim or concise summary of the prompt.
     - **Code / Idea Used:** What the AI suggested and what logic was adopted.
     - **Result / Modification:** How the suggestion was adapted, refactored, tested, or debugged.

4. **Clean Object-Oriented Programming (OOP) & Decoupling:**
   - **Zero Domain Coupling:** The core game rules (`board`, `pieces`, `core`, `ai`, `utils`) must remain **100% decoupled** from Swing and AWT. Zero `java.awt.*` or `javax.swing.*` imports inside domain classes.
   - **Single Responsibility Principle (SRP):** Keep classes modular. `GUIController` acts as mediator; `Board` represents state; `Piece` defines movement vectors; `AttackDetector` analyzes threat lines; `SpecialMoveHandler` encapsulates castling, en passant, and promotion; `ChessAI` encapsulates opponent decision logic.

5. **Swing Thread Safety & Event Dispatch Thread (EDT) Hygiene:**
   - All Swing component mutations, repaints, and UI events must execute strictly on the EDT via `SwingUtilities.invokeLater()` or `SwingUtilities.invokeAndWait()`.
   - State-mutating methods in `GUIController` must enforce EDT execution via `requireEventDispatchThread()` assertions.
   - **AI Concurrency:** Complex AI calculations (Minimax / Alpha-Beta) must run on a background thread (`SwingWorker` or executor service) so the UI remains 100% responsive without stuttering or freezing.

6. **Strict Javadoc Hygiene:**
   - 100% of all classes, interfaces, constructors, methods, and constants must contain comprehensive Javadoc comments with `@param`, `@return`, and `@throws`.
   - Must achieve **0 warnings and 0 errors** under the compiler audit command:
     ```powershell
     javadoc -private -Xdoclint:all -Werror -d out/javadoc (Get-ChildItem -Path .\src -Recurse -Filter *.java).FullName
     ```

---

## 🎨 Strategic Extra Credit Roadmap (Targeting Maximum Points)

Phase 3 offers two distinct extra credit opportunities. Our team strategy targets **both** categories to secure maximum course points:

### Extra Credit A: Git/AI Process and README Quality (Up to +2 Points to Project Portion)
- **Granular Commit Discipline:** All milestones traceable through descriptive, incremental commits from all 4 team members.
- **Exemplary AI Logs:** Complete, chronological prompt-and-adaptation records in `docs/ai-usage/`.
- **Publication-Grade README:** A polished, professional repository `README.md` featuring architecture diagrams, system specifications, compilation/run instructions, visual feature previews, and a complete capabilities matrix.

### Extra Credit B: Advanced Features — AI Chess Player (Up to +3 Points to Final Grade)
- **Feature Selection:** Implement a fully functional, integrated **AI Chess Player (Computer Opponent)** capable of playing legal, competitive chess against a human player in real time.
- **Engine Architecture (`src/ai/`):**
  - `ChessAI` (Interface): Defines `Move decideMove(Board board, Color aiColor)`.
  - `EvaluationEngine`: Computes board positional scores using:
    - Standard material weights: Pawn = 100, Knight = 320, Bishop = 330, Rook = 500, Queen = 900, King = 20,000.
    - Piece-Square Tables (PST) rewarding center control, pawn advancement, knight centralization, and king safety.
  - `MinimaxAI`: Implements Minimax with **Alpha-Beta Pruning** to search several plies ahead efficiently.
  - `RandomAI`: Baseline fallback picking uniformly among legal moves.
  - **Difficulty Modes:**
    - *Novice (Level 1):* Random / 1-ply immediate evaluation.
    - *Intermediate (Level 2):* 2-ply Minimax with positional heuristics.
    - *Advanced (Level 3):* 3-to-4 ply Alpha-Beta search with depth optimization.
- **GUI Integration:**
  - Game mode selector in menu bar (`Game -> Play vs AI (White)`, `Game -> Play vs AI (Black)`, `Game -> Player vs Player`).
  - Asynchronous background execution (`SwingWorker`) showing an animated "AI is thinking..." status indicator without blocking the UI.
  - Auto-execution of AI move upon completion, dynamically updating the board, history log, captured pieces, and turn.

---

## 🏗️ Phase 3 Integration Architecture: Bridging Backend & GUI

In Phase 1, we developed the domain rules (piece movement vectors, attack detection, console game loop). In Phase 2, we built the Swing GUI shell (unrestricted movement demo, King capture endgame, customization settings, history table, and persistence). 

**Phase 3 connects the brain to the body:**

```
+-----------------------------------------------------------------------------------+
|                            PRESENTATION LAYER (Swing GUI)                         |
|  ChessFrame | BoardPanel | SquarePanel | CapturedPiecesPanel | HistoryPanel      |
|  - Renders 8x8 grid & procedural vector piece icons                                |
|  - Paints visual move hints (dots on valid squares, rings on capturable enemies)   |
|  - Displays Check warning glow on checked King & Checkmate modal on endgame        |
+-----------------------------------------+-----------------------------------------+
                                          |
                      Dispatches Mouse / Action Events
                                          v
+-----------------------------------------------------------------------------------+
|                       CONTROLLER LAYER (Mediator & State Machine)                  |
|  GUIController | BoardMouseHandler | GameMenuActions | DialogHelper                |
|  - Enforces active turn (White / Black)                                           |
|  - Queries legal moves for selected piece: board.getLegalMoves(pos, turn)         |
|  - Simulates candidate moves: rejects moves that leave/expose friendly King in check|
|  - Triggers asynchronous AI turn when playing vs computer                          |
|  - Notifies Observers (BoardPanel, HistoryPanel, StatusListener) on state changes |
+-----------------------------------------+-----------------------------------------+
                                          |
                      Manipulates & Queries Domain Models
                                          v
+-----------------------------------------------------------------------------------+
|                       DOMAIN MODEL LAYER (Pure Business Logic)                    |
|  Board | Position | Piece (Pawn, Rook, Knight, Bishop, Queen, King) | Color | Move |
|  - possibleMoves(board): raw geometric movement paths and unblocked rays          |
|  - getLegalMoves(position, color): filter moves where !simulatedBoard.isCheck(color)|
|  - isCheck(color): King under attack check                                         |
|  - isCheckmate(color): in check AND zero legal moves remaining                    |
|  - isStalemate(color): NOT in check AND zero legal moves remaining (Draw)          |
|  - 100% DECOUPLED: ZERO AWT/Swing imports                                          |
+------------------------------------+--------------+-------------------------------+
                                     |              |
                      Delegates To   |              | Evaluates Positional Moves
                                     v              v
+------------------------------------+----+   +-----+-------------------------------+
|      SPECIAL MOVE SERVICES LAYER        |   |       AI CHESS ENGINE (src/ai/)     |
|  AttackDetector | SpecialMoveHandler    |   |  ChessAI | MinimaxAI | Evaluation   |
|  - Castling (path clear, no check)      |   |  - Alpha-Beta Pruning               |
|  - Pawn Promotion (Q, R, B, N)          |   |  - Piece-Square Tables (PST)        |
|  - En Passant capture validation        |   |  - Background SwingWorker execution |
+-----------------------------------------+   +-------------------------------------+
```

### Core Rule Invariants for Phase 3:
1. **Legal Move Filtering & King Safety:**
   - A player can only select and move pieces of their own color.
   - A move is legal **if and only if**:
     1. The destination square is within the piece's standard geometric movement capabilities (`possibleMoves(board)`).
     2. Path clearance rules are satisfied (Rooks, Bishops, Queens blocked by obstacles; Knights jump).
     3. Executing the move does **NOT** leave or expose the friendly King to attack (`!testBoard.isCheck(activeColor)`). Pinned pieces cannot move outside the pin axis.
2. **End of Simplified King Capture:**
   - In Phase 2, capturing a King was allowed and abruptly ended the game.
   - In Phase 3, **a King can never be captured**. An attempt to move into check or capture a King is illegal and rejected.
3. **Check & Checkmate Endgame:**
   - **Check:** When a player's King is targeted by one or more opposing pieces, the GUI must visually indicate check (e.g., status bar warning and red radial glow on the checked King square).
   - **Checkmate:** Occurs when a player's King is in check, and **no legal move exists** across all of that player's pieces that can resolve the check. The game halts immediately and declares the victor.
   - **Stalemate (Draw):** Occurs when a player is **not in check**, but has **zero legal moves** on their turn.

---

## 🎯 Development Milestones

```
+-----------------------------------------------------------------------------------+
|                               PHASE 3 MILESTONES                                  |
+-------------------------+-------------------------+-------------------------------+
| MILESTONE 1 (30%)       | MILESTONE 2 (30%)       | MILESTONE 3 (20% + EC)        |
| Core Rule Integration   | Check, Checkmate &      | Special Rules &               |
| & Legal Move Hints      | King Safety Engine      | AI Opponent Engine            |
+-------------------------+-------------------------+-------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
| MILESTONE 4 (20%): UML Class Diagram, Strict QA Audit, PDF Report & Final Merge   |
+-----------------------------------------------------------------------------------+
```

---

### 🔀 Milestone 1: Core Rule Integration, Move Validation & Visual Hints (30%)
**Goal:** Connect Phase 1 piece logic to the Phase 2 GUI. Enforce that only active player pieces can be selected, only legal destinations can be targeted, and render dynamic visual move hints on the board.

- **Dev 1 (Rayed - Repo Owner / Architecture):**
  - [x] Implement `Board.getLegalMoves(Position position, Color color)`: returns moves from `piece.possibleMoves(board)` that do not leave the friendly King in check.
  - [x] Refactor `GUIController.selectSquare(Position)`: allow selection only if the clicked square contains a piece matching `currentTurn`.
  - [x] Expose `GUIController.getLegalMovesForSelected()` so UI panels can query reachable squares.
- **Dev 2 (Abid - Board Infrastructure & Visual Feedback):**
  - [x] Implement visual move hints on `BoardPanel` / `SquarePanel`:
    - Draw a subtle translucent green circle/dot on empty legal destination squares.
    - Draw a high-contrast target ring or corner bracket on opponent squares containing capturable pieces.
  - [x] Ensure selection highlights and move hints clear cleanly upon move completion or cancellation (<kbd>Esc</kbd> / clicking outside).
  - [x] Add `test/gui/MoveHintTest.java` verifying that hints render only on strictly legal squares.
- **Dev 3 (Sabid - Input Validation & Turn Enforcement):**
  - [ ] Refactor `GUIController.movePiece(Position from, Position to)`: reject any move where `to` is not in `board.getLegalMoves(from, currentTurn)`.
  - [ ] Reject illegal click-and-move and drag-and-drop attempts with non-destructive visual/audio feedback (e.g., flash square red or status bar warning: *"Illegal move: piece cannot move there"*).
  - [ ] Ensure active turn strictly alternates between White and Black on legal moves, and never changes on rejected moves.
- **Dev 4 (Towsif - QA & Setup):**
  - [ ] Build test scaffolding in `test/core/LegalMoveTest.java` verifying:
    - Piece obstacle collisions (Rook, Bishop, Queen blocked by friendly/enemy pieces).
    - Knight jump capabilities over obstacles.
    - Pawn single/double advance and diagonal captures.
    - Turn enforcement: White cannot move Black pieces and vice-versa.
  - [ ] Ensure all M1 code compiles cleanly under `javac --release 17 -Xlint:all`.

🧪 **Verification & Tests for Milestone 1:**
- **Turn Restriction Test:** Click Black pawn on E7 on turn 1 (White's turn); verify selection is rejected and status bar indicates *"It is White's turn"*.
- **Move Hint Visual Test:** Click White Knight on B1; verify visual highlight dots appear on A3 and C3, and nowhere else.
- **Path Obstacle Test:** Click White Rook on A1 behind unmoved pawns; verify zero move hints appear and clicking A3 is rejected.
- **Pawn Mechanics Test:** Click White Pawn on E2; verify hints on E3 and E4. Move to E4; verify Black can move.

---

### 🔀 Milestone 2: Check, Checkmate, King Safety & Endgame Flow (30%)
**Goal:** Implement robust King safety validation, persistent visual check warnings, full checkmate detection, and replace Phase 2's King capture with a proper checkmate endgame modal.

- **Dev 1 (Rayed - King Safety & Move Simulation):**
  - [ ] Implement hypothetical move simulation:
    ```java
    public boolean wouldExposeKing(Position from, Position to, Color color);
    ```
    Creates a lightweight trial move on the board model, runs `AttackDetector.isSquareAttacked(targetKingPos, opponentColor)`, and rolls back the trial state.
  - [ ] Implement pinned piece protection: ensure pinned pieces (e.g., Bishop on diagonal between friendly King and enemy Queen) cannot move off the pin vector.
- **Dev 2 (Abid - Check Visual Feedback & King Highlighting):**
  - [ ] Implement persistent visual Check indicator on `BoardPanel`:
    - When `board.isCheck(color)` is true, render a prominent red radial glow / danger highlight on the square of the checked King.
    - Update the status bar label with a bold warning: *"Check! White/Black King is under attack!"*.
  - [ ] Ensure the check highlight clears immediately when a legal move evades, blocks, or captures the checking threat.
- **Dev 3 (Sabid - Check Evasion & History Annotation):**
  - [ ] When a player is in check, restrict selection and movement strictly to moves that resolve the check (evade King, block ray, or capture checking piece).
  - [ ] Update `HistoryPanel` notation: append standard chess symbol `+` to move notation when a move delivers check (e.g., `Qh5+`).
- **Dev 4 (Towsif - Checkmate & Stalemate Engine):**
  - [ ] Implement comprehensive `board.isCheckmate(Color color)`:
    1. Verify `board.isCheck(color)` is true.
    2. Iterate over all active pieces of `color`; compute `getLegalMoves()`. If the sum of legal moves is 0, declare **Checkmate**.
  - [ ] Implement `board.isStalemate(Color color)`:
    1. Verify `board.isCheck(color)` is false.
    2. If total legal moves for `color` is 0, declare **Stalemate (Draw)**.
  - [ ] Replace `KingCaptureHandler` with `CheckmateHandler`:
    - Displays a modal dialog: *"Checkmate! [White/Black] wins by checkmate!"*.
    - Annotate history notation with `#` (e.g., `Qxf7#`).
    - Disable further board movement while allowing game review, New Game, or Save Game.

🧪 **Verification & Tests for Milestone 2:**
- **Pin Test:** Position White King on E1, White Rook on E2, Black Queen on E7. Verify White Rook cannot move horizontally to D2 or F2 because it would expose the King to check.
- **Scholar's Mate Checkmate Test:** Execute 1. e4 e5 2. Bc4 Nc6 3. Qh5 Nf6 4. Qxf7#. Verify checkmate modal dialog appears declaring White victory, and game halts cleanly without King capture.
- **Check Evasion Test:** Place White King in check from a Black Rook. Attempt moving an unrelated pawn; verify move is rejected. Verify moving King to an unattacked square or blocking the ray succeeds.
- **Stalemate Test:** Setup known stalemate position (e.g., Black King trapped in corner with no legal moves and not in check); verify game announces Stalemate / Draw.

---

### 🔀 Milestone 3: Special Rules Integration & AI Chess Player (20% + Extra Credits)
**Goal:** Integrate full special rules (Castling, Pawn Promotion, En Passant) into the Swing GUI, and implement an autonomous AI Chess Player with Minimax and Alpha-Beta pruning for Extra Credit B.

- **Dev 1 (Rayed - AI Chess Player Engine — Extra Credit B):**
  - [ ] Design and implement package `src/ai/`:
    - `ChessAI`: Strategy interface for computer opponents.
    - `EvaluationEngine`: Computes material differential and Piece-Square Table (PST) positional values.
    - `MinimaxAI`: Implements depth-limited Minimax algorithm with **Alpha-Beta Pruning**.
    - `AITask`: Executes search asynchronously on a background worker thread (`SwingWorker`), preventing UI hangs.
  - [ ] Integrate AI game controls: menu option to play vs Computer with selectable difficulty (*Novice*, *Intermediate*, *Advanced*).
- **Dev 2 (Abid - Promotion Dialog & Special Move Visuals):**
  - [ ] Implement `PromotionDialog` (`JDialog`): when a Pawn reaches rank 8 (White) or rank 1 (Black), open a modal dialog displaying Queen, Rook, Bishop, and Knight vector buttons for player selection.
  - [ ] On piece selection, substitute the promoted piece on the board and update visual icons immediately.
  - [ ] Animate or highlight the Rook's automatic slide during castling moves.
- **Dev 3 (Sabid - Castling & En Passant GUI Wiring):**
  - [ ] Wire Kingside (`O-O`) and Queenside (`O-O-O`) castling gestures into `BoardMouseHandler`: dragging or clicking King two squares toward a qualifying Rook executes castling (moving King 2 squares and jumping Rook).
  - [ ] Wire En Passant capture: moving pawn diagonally behind an opposing double-stepped pawn removes the enemy pawn from the board and updates captured racks.
- **Dev 4 (Towsif - Special Moves Rules Verification & AI Stress QA):**
  - [ ] Verify castling conditions: King and target Rook have not moved, squares between are empty, and King does not pass through or land on a square under attack (`AttackDetector`).
  - [ ] Add `test/ai/AITest.java`: stress test AI against random and heuristic moves across 100 simulated turns to verify zero crashes, zero illegal moves, and reliable move generation within a 2-second timeout.

🧪 **Verification & Tests for Milestone 3:**
- **Castling GUI Test:** Clear squares between White King (E1) and Rook (H1). Move King to G1; verify King lands on G1 and Rook automatically relocates to F1. Verify castling is blocked if F1 is under enemy attack.
- **Pawn Promotion GUI Test:** Move White Pawn to E8; verify `PromotionDialog` pops up with Q/R/B/N options. Select Queen; verify Pawn becomes Queen on E8 with queen vector icon and history logs `E7-E8=Q`.
- **En Passant GUI Test:** Black moves pawn D7-D5. White pawn on E5 moves to D6; verify Black pawn on D5 is removed from board, added to White's captured rack, and turn toggles.
- **AI Player Match Test:** Start game as White vs AI (Black, Intermediate). Make move 1. e4; verify "AI is thinking..." appears in status bar, and within ~1 second Black automatically executes a legal reply (e.g., e5 or c5) without UI freezing.

---

### 🔀 Milestone 4: UML Class Diagram, Strict QA Audit, Documentation & Final Delivery (20%)
**Goal:** Produce a publication-grade UML Class Diagram (Req 2 - 20%), pass all unit and regression tests with 0 failures, complete strict Javadoc audit with 0 warnings, generate the Phase 3 PDF deliverable, and merge `dev` into `main`.

- **Dev 1 (Rayed - Repo Owner / Architecture Lead):**
  - [ ] Design and author the master **UML Class Diagram** (Req 2 — 20%):
    - Illustrate all domain, GUI, controller, service, and AI classes.
    - Explicitly include class names, key attributes, methods, access specifiers (`+`, `-`, `#`), and relationships (Inheritance, Realization, Association, Aggregation, Composition, Dependency) with accurate multiplicity.
    - Clearly document the architectural bridge between GUI and backend game logic.
  - [ ] Export UML Class Diagram in vector SVG and high-resolution 300+ DPI PNG format to `docs/uml/`.
  - [ ] Manage final Pull Request from `dev` to `main`, ensuring all milestone branches are merged and verified.
- **Dev 4 (Towsif - QA Lead & Documentation PM):**
  - [ ] Run the complete automated test suite (`TestRunner` + GUI suites + AI suite) verifying **100% pass rate** with 0 regressions.
  - [ ] Run strict Javadoc audit: ensure **0 warnings and 0 errors** under `-private -Xdoclint:all -Werror`.
  - [ ] Assemble and compile the **Phase 3 Submission PDF Document** including:
    - Running GUI screenshots showing Check, Checkmate, AI play, and move hints.
    - Embedded high-resolution UML Class Diagram.
    - Comprehensive notes on implemented core rules, optional rules (castling, promotion, en passant), and AI opponent.
- **Dev 2 (Abid) & Dev 3 (Sabid) - Peer Reviews & AI Usage Logs:**
  - [ ] Verify that all personal AI logs in `docs/ai-usage/` are up-to-date, detailed, and formatted to qualify for Extra Credit A.
  - [ ] Conduct final round of cross-peer code reviews on PRs entering `dev`.

🧪 **Verification & Tests for Milestone 4:**
- **Full Build Verification:** Compile clean from root with zero warnings:
  ```powershell
  javac --release 17 -Xlint:all -d out (Get-ChildItem -Path .\src,.\test -Recurse -Filter *.java).FullName
  ```
- **Regression Suite:** Run all automated assertions:
  ```powershell
  java -ea -Djava.awt.headless=true -cp out TestRunner
  ```
- **Strict Javadoc Verification:**
  ```powershell
  javadoc -private -Xdoclint:all -Werror -d out/javadoc (Get-ChildItem -Path .\src -Recurse -Filter *.java).FullName
  ```
- **PDF Deliverable Audit:** Inspect `docs/TeamZero_Phase3_Documentation.pdf` to confirm GUI screenshots, readable UML diagram, and submission notes are fully legible.

---

## 📐 UML Class Diagram Specification (Requirement 2 — 20%)

The UML Class Diagram is a core requirement worth **20 points (20%)** of the Phase 3 grade. It must communicate the complete software architecture with clarity and precision:

### Mandatory Diagram Elements:
1. **Class Names & Stereotypes:** All production classes, interfaces, records, and enums across all packages (`pieces`, `board`, `core`, `gui`, `gui.board`, `gui.pieces`, `gui.dialogs`, `ai`, `utils`).
2. **Access Specifiers:** Full notation on important fields and methods (`+` public, `-` private, `#` protected, `~` package-private).
3. **Relationships & Line Notations:**
   - **Inheritance (`--|>`):** `Pawn`, `Rook`, `Knight`, `Bishop`, `Queen`, `King` extend `Piece`. `ChessFrame` extends `JFrame`.
   - **Realization (`..|>`):** `MinimaxAI`, `RandomAI` implement `ChessAI`.
   - **Composition (`*--`):** `Board` owns 64 `Position` coordinates and an 8×8 `Piece[][]` grid. `ChessFrame` owns `BoardPanel`, `HistoryPanel`, `GUIController`.
   - **Aggregation (`o--`):** `BoardPanel` aggregates 64 `SquarePanel` instances.
   - **Association (`-->`):** `GUIController` associates with `Board`, `KingCaptureHandler` / `CheckmateHandler`, `GameFileService`.
   - **Dependency (`..>`):** `Board.getLegalMoves()` depends on `AttackDetector`.
4. **Multiplicity Labels:** Accurately placed on association lines (e.g., `Board "1" *-- "0..32" Piece`, `BoardPanel "1" o-- "64" SquarePanel`).
5. **Architectural Bridging:** Clearly annotate the separation between the **Presentation Layer (GUI)** and the **Domain Model Layer (Backend)** through the **Controller Mediator**.

---

## 📊 Phase 3 Rubric Alignment & Scoring Breakdown

| Rubric Category | Weight | Max Points | Implementation Plan & Coverage Details | Primary Owner |
| :--- | :---: | :---: | :--- | :--- |
| **Requirement 1: Implementation** | **80%** | **80** | Full integration of Phase 1 rules with Phase 2 GUI. Enforces legal piece movement, path obstacles, turn alternation, King safety simulation, persistent check warnings, checkmate detection & endgame flow, and optional special rules (promotion, castling, en passant). | All Devs |
| **Requirement 2: UML Class Diagram** | **20%** | **20** | Professional UML diagram documenting all classes, attributes, methods, access specifiers, inheritance/realization, associations, and multiplicities illustrating the GUI-backend bridge. | Dev 1 & Dev 4 |
| **Extra Credit A: Git/AI & README** | *Bonus* | **+2** *(Project)* | Incremental commit flow, high-quality commit messages, thorough AI logs in `docs/ai-usage/`, and a publication-grade `README.md`. | All Devs |
| **Extra Credit B: Advanced Features** | *Bonus* | **+3** *(Course)* | Substantial advanced feature: **AI Chess Player** (Minimax with Alpha-Beta pruning, evaluation heuristics, difficulty presets, and non-blocking asynchronous GUI integration). | Dev 1 & Dev 4 |
| **Total Target Score** | **100% Core + Bonus** | **100 + EC** | **Full 100/100 Core + Maximum Extra Credit A (+2) + Extra Credit B (+3)** | **Team Zero** |

---

## ⚠️ Technical Risk Management & Mitigation Matrix

| Technical Risk | Likelihood | Impact | Preventive Architecture & Mitigation |
| :--- | :---: | :---: | :--- |
| **GUI Freeze during AI Search** | High | High | **Mitigation:** Execute Minimax/Alpha-Beta search inside a dedicated background worker (`SwingWorker<Move, Void>`). Never perform move tree searches on the Swing Event Dispatch Thread (EDT). |
| **Move Simulation Side-Effects** | High | High | **Mitigation:** `wouldExposeKing()` uses atomic simulate-and-rollback (`board.undoMove()`) or copies a lightweight board clone to guarantee active gameplay state is never mutated during legality scans. |
| **Checkmate Calculation Lag** | Medium | Medium | **Mitigation:** Short-circuit `isCheckmate()` immediately if any legal escape move is discovered. Order moves by captures and king moves first for rapid evaluation. |
| **Undo Consistency with Special Moves** | Medium | High | **Mitigation:** Extend `UndoEntry` record to capture castling rights, en passant target squares, and promoted piece identities, ensuring 100% lossless rewinds. |
| **Headless CI Test Failures** | Medium | High | **Mitigation:** Keep all domain rules and AI engines 100% decoupled from AWT/Swing so the entire test suite runs with `-Djava.awt.headless=true`. |

---

## 📅 Timeline & Master Sprint Schedule

```
+-----------------------------------------------------------------------------------+
| SPRINT 1 (Milestone 1): Legal Move Simulation & GUI Move Hints                    |
| - Connect piece possibleMoves to GUIController                                    |
| - Implement board.getLegalMoves() & turn enforcement                              |
| - Render dynamic destination dots & capture rings on BoardPanel                   |
+-----------------------------------------------------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
| SPRINT 2 (Milestone 2): Check, Checkmate & King Safety Engine                     |
| - Implement move simulation to reject moves exposing King to check                |
| - Implement visual Check warning glow and status bar alerts                       |
| - Build isCheckmate() and isStalemate() engines; wire Checkmate modal dialog      |
+-----------------------------------------------------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
| SPRINT 3 (Milestone 3): Special Rules & AI Chess Player (Extra Credit B)          |
| - Wire PromotionDialog (Q/R/B/N) and Castling/En Passant into GUI                 |
| - Build MinimaxAI with Alpha-Beta pruning and Evaluation heuristics               |
| - Integrate background SwingWorker AI play into Game menu                         |
+-----------------------------------------------------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
| SPRINT 4 (Milestone 4): UML Diagram, Javadoc Audit & Submission Package           |
| - Author publication-grade UML Class Diagram (Req 2 - 20%)                        |
| - Pass 100% regression tests and strict Javadoc audit (0 warnings / 0 errors)     |
| - Compile Phase 3 Submission PDF; merge dev to main for release                   |
+-----------------------------------------------------------------------------------+
```
