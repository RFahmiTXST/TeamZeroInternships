# ♟️ GUI Chess: Phase 2 Team Plan

This is the central planning document for Phase 2 of the Chess project. It outlines the team guidelines, branching strategy, member responsibilities, GUI architecture using Java Swing, extra features roadmap, and our four development milestones.

---

## 👥 Team Members & Feature Branches

All development must take place on your designated feature branch. Never commit directly to `main` or `dev`.

| Developer | Role | Focus | Branch Name |
| :--- | :--- | :--- | :--- |
| **Rayed Fahmi** | **Dev 1** | GUI Architecture, Piece Graphics & Customization Settings | `feature/RFahmiTXST` |
| **Abid Ahnaf Khan** | **Dev 2** | Board Infrastructure, Grid Layout & Visual Styling | `feature/ajv163` |
| **Sabid Mahmud** | **Dev 3** | Mouse Event Handling, Piece Movement & Captures | `feature/sabidmahmud01` |
| **Towsif Hassan** | **Dev 4** | Extra Features (Menu/History/Undo), Endgame & QA | `feature/cyber-kuro` |

---

## 📜 Team Guidelines & Workflow (Crucial)

1. **Git Workflow:**
   - We maintain a `main` branch (production-ready) and a `dev` branch (integration).
   - Work on your assigned `feature/<github-username>` branch.
   - Regularly pull `origin/dev` into your branch to stay synchronized and prevent merge conflicts.
   - Open a Pull Request (PR) from your feature branch into `dev`.
   - **Requirement:** Every member must open PRs into `dev` and review teammates' PRs before merging.
   - Do NOT delete remote feature branches; keep them until final semester grading is complete.
2. **AI Usage Logs:**
   - Every member must continue maintaining their personal log in `docs/ai-usage/<github-username>.md`.
   - Log the date, task, prompts given to the AI, code adopted or adapted, and review/debugging findings.
3. **Commit Incrementally:**
   - Commit code frequently with clear, conventional commit messages (`feat:`, `fix:`, `docs:`, `test:`). Avoid massive end-of-phase code drops.
4. **Javadoc & Clean OOP (Swing Architecture):**
   - Strictly follow the Model-View-Controller (MVC) or Model-View separation: keep core board/piece models cleanly separated from Swing UI components (`src/gui/`).
   - All Swing UI updates must execute on the Event Dispatch Thread (EDT) via `SwingUtilities.invokeLater()`.
   - Ensure all classes, fields, and methods have comprehensive Javadoc comments with `@param`, `@return`, and `@throws` where applicable. Target **0 warnings and 0 errors** under `-private -Xdoclint:all -Werror`.

---

## 🎨 Extra GUI Features Strategy (Targeting Maximum Bonus)

The project rubric awards **20%** for implementing at least two extra GUI features, and **+5 extra points** for each additional feature implemented. Our team will implement **all three official extra features** to secure the full 20% plus 5 bonus points:

1. **Feature 1: Menu Bar with Game Controls (`JMenuBar`)**
   - `New Game`: Resets the board, pieces, move history, and all game state back to starting configuration.
   - `Save Game`: Prompts user with a `JFileChooser` to save the current board layout, active player, and move history to a file (serialization or structured text).
   - `Load Game`: Prompts user with a `JFileChooser` to restore a previously saved game state seamlessly.
2. **Feature 2: Settings Window for Customization (`JDialog`)**
   - Theme Customization: Select different board color schemes (e.g., Classic Wood, Modern Slate, Forest Emerald, High Contrast).
   - Piece Style: Toggle between classic icons, minimalist vectors, or unicode chess font styles.
   - Board Size: Dynamically adjust board scale (Small ~480px, Medium ~640px, Large ~800px).
   - Real-time `Apply` button to update the view without resetting active gameplay.
3. **Feature 3: Game History Panel with Undo Button (`JPanel`)**
   - Real-time Move History: Displays sequential moves with turn number, piece name, origin square, destination square, and capture notations.
   - Captured Pieces Display: Visual rack showing captured White and Black pieces off the board.
   - `Undo` Button: Reverts the last move made, restoring any captured piece back to its square and updating the move history stack.

---

## 🎯 Milestones

### 🔀 Milestone 1: Swing GUI Shell, Board Grid & Piece Visuals (Static Display - 30%)
**Goal:** Construct the foundational Swing GUI application, render an 8x8 chessboard with alternating light and dark squares, and display all 32 chess pieces on their initial starting squares.

- **Dev 1 (Rayed - Repo Owner / Architecture):**
  - [x] Set up the GUI package architecture (`src/gui/`, `src/gui/board/`, `src/gui/pieces/`, `src/gui/dialogs/`) with initial component scaffolding.
  - [x] Create the main application window `ChessFrame` (`JFrame`) with appropriate layout manager (`BorderLayout`), minimum dimensions, and clean window closing behavior.
  - [x] Implement `PieceIconManager` / `PieceView`: load or procedurally render visually distinct graphics for all 6 piece types (`Pawn`, `Rook`, `Knight`, `Bishop`, `Queen`, `King`) for both `White` and `Black`.
  - [x] Provide GUI entry point in `src/Main.java` allowing users to launch either the new Swing GUI (default) or the Phase 1 Console game.
- **Dev 2 (Abid - Board Infrastructure & Layout):**
  - [x] Refine `BoardPanel` layout and `SquarePanel` visual styling, including coordinate label typography and square feedback borders.
  - [x] Keep the board and its squares square during window resizing; align coordinate labels and resize piece icons.
  - [x] Show square hover and focus indicators while preserving explicit selection and highlights.
  - [x] Add `test/gui/BoardPanelTest.java` for mapping, layout, theme, model refresh, and feedback.
- **Dev 3 (Sabid - Game State Bridge & Initial Placement):**
  - [x] Implement `GUIController` to act as the mediator between the underlying `Board` / `Piece` data model and the Swing UI.
  - [x] Populate the initial visual board: place all 32 pieces in their standard starting positions (White on ranks 1 & 2, Black on ranks 7 & 8).
  - [x] Ensure all initial component construction and board painting adhere to Swing thread-safety guidelines (`SwingUtilities.invokeLater`).
- **Dev 4 (Towsif - QA & Setup):**
  - [x] Set up Phase 2 test scaffolding in `test/gui/` to verify headless component initialization and state-to-view mapping.
  - [x] Create Phase 2 documentation structure and screenshot checklist (`docs/PHASE2_REPORT_CHECKLIST.md`).
  - [x] Verify clean build scripts on PowerShell and bash (`javac` commands compiling both `src` and `test` under Java 17+).

🧪 **Manual Tests for M1:**
- Launch the application: verify a polished window opens displaying an 8x8 board with clearly alternating light and dark squares.
- Verify file coordinates (`A` through `H`) and rank coordinates (`1` through `8`) are cleanly legible.
- Inspect all 32 starting pieces: confirm Pawns, Rooks, Knights, Bishops, Queens, and Kings are in their exact standard starting squares and visually distinct.

---

### 🔀 Milestone 2: Mouse Event Handling, Piece Movement & Captures (30% + 10%)
**Goal:** Allow users to move pieces using mouse interactions (both click-and-move and drag-and-drop), execute captures when landing on opponent squares, and trigger the endgame winner pop-up upon King capture.

- **Dev 3 (Sabid - Mouse Event Handling & Move Execution):**
  - [x] Implement mouse event listeners on `SquarePanel` / `BoardPanel`:
    - **Click-and-Move:** Click a piece to select it (with visual feedback), then click a destination square to move the piece. Clicking an invalid target or empty square deselects.
    - **Drag-and-Drop:** Mouse press initiates drag with visual floating icon/ghost cursor, mouse drag tracks cursor, and mouse release drops piece onto target square.
  - [x] Implement unrestricted piece movement: as required by Phase 2 instructions, movement validation is not required—any piece can move to any valid square on the board.
  - [x] Update piece coordinates in the underlying `Board` and refresh the visual squares on both origin and destination.
- **Dev 2 (Abid - Visual Feedback & Board Dynamic Refresh):**
  - [ ] Implement visual highlight for currently selected square (e.g., subtle highlighted border or semi-transparent colored overlay).
  - [ ] Implement smooth drag-and-drop feedback (dragged piece icon follows mouse pointer).
  - [ ] Implement board re-rendering logic: immediately clear the piece icon from the source square and draw it on the destination square.
- **Dev 1 (Rayed - Capture Logic & Turn State):**
  - [x] Implement piece capture handling: if a destination square contains an opponent piece, remove the captured piece from the `Board` model and remove its icon from the display.
  - [x] Ensure captured pieces disappear immediately and cleanly from the board.
  - [x] Maintain active player turn tracking (e.g., status bar showing "White's Turn" / "Black's Turn").
- **Dev 4 (Towsif - Endgame Notification & Winner Declaration - 10%):**
  - [x] Implement King capture detection: detect whenever the piece being captured is an instance of `King`.
  - [x] Display modal dialog (`JOptionPane.showMessageDialog`) declaring the winning player (e.g., *"Game Over! White captured Black's King and wins!"*).
  - [x] Ensure the game terminates cleanly upon closing or confirming the dialog (as mandated by Phase 2 instructions).

🧪 **Manual Tests for M2:**
- **Click-and-move:** Click a piece on `E2`, verify highlight appears; click `E5`, verify piece moves to `E5`.
- **Drag-and-drop:** Click and drag a Knight from `B1` to `F6`; verify the dragged icon tracks smoothly and snaps to `F6` upon release.
- **Capture:** Move a White Queen onto an occupied Black square; verify the Black piece disappears completely and White Queen occupies the square.
- **Endgame:** Move any piece onto the opposing King's square; verify a modal pop-up window appears declaring the winner, and dismissing the pop-up terminates the game.

---

### 🔀 Milestone 3: Extra GUI Features Implementation (20% + 5% Bonus)
**Goal:** Implement all three extra GUI features: Menu Bar controls (New/Save/Load), Settings Window for real-time appearance customization, and Game History Panel with an Undo button.

- **Dev 4 (Towsif - Extra Feature 1: Menu Bar & Game Controls):**
  - [ ] Implement top `JMenuBar` with `File` and `Game` menus.
  - [ ] Implement **New Game**: prompts for confirmation, then resets the `Board` to initial setup, clears move history, resets captured pieces, and repaints the board.
  - [ ] Implement **Save Game**: open `JFileChooser`, serialize or format the game state (board matrix, captured pieces, turn, move history) into a `.chess` or text file.
  - [ ] Implement **Load Game**: open `JFileChooser`, parse selected save file, restore complete board state, move history, and UI views.
- **Dev 1 (Rayed - Extra Feature 2: Settings Window for Customization):**
  - [x] Design and implement `SettingsDialog` (`JDialog`) accessible via menu bar or toolbar gear icon.
  - [x] Add **Board Background Color/Style** selector (e.g., Classic Wood, Modern Gray, Slate Blue, Forest Green).
  - [x] Add **Chess Piece Style/Color** selector (e.g., Classic vector, Minimalist, High-contrast).
  - [x] Add **Board Size** selector (e.g., Small 480x480, Medium 640x640, Large 800x800).
  - [x] Implement real-time **Apply** button: dynamically updates board colors, piece icons, and window dimensions without restarting or losing the ongoing game state.
- **Dev 3 (Sabid - Extra Feature 3: Game History Panel):**
  - [x] Create `HistoryPanel` (`JPanel`) positioned adjacent to the chessboard in a `BorderLayout.EAST` container.
  - [x] Implement a scrollable `JTable` or `JList` logging move history in real-time (turn number, piece moved, start square, destination square, capture indicator).
  - [x] Auto-scroll to the latest move as moves occur.
- **Dev 2 (Abid - Extra Feature 3: Captured Pieces Rack & Undo Action):**
  - [ ] Implement visual Captured Pieces displays in the side panel (displaying captured White and Black pieces with counts/icons).
  - [ ] Implement **Undo Button**: maintain a move history stack (Memento or Command pattern).
  - [ ] On Undo click: revert the previous move, return moved piece to origin square, restore any captured piece back to destination square, update move history table, and repaint.

🧪 **Manual Tests for M3:**
- **Undo Test:** Make 4 moves including 1 capture; press `Undo` twice; verify the board accurately rewinds the moves and restores the captured piece.
- **Save/Load Test:** Make several moves, save game as `test_save.chess`, make random moves, load `test_save.chess`; verify the exact board layout and history are restored.
- **Settings Test:** Open Settings window, switch theme from Classic Wood to Modern Gray, select Large size, click `Apply`; verify immediate visual changes without resetting the board.
- **New Game Test:** Play moves, click `New Game`; verify board returns to starting layout and history clears.

---

### 🔀 Milestone 4: QA, Final Documentation, Screenshots & Delivery (10%)
**Goal:** Complete code review and refactoring, run Javadoc audit with 0 warnings, capture all required screenshots, assemble the submission PDF, and perform final dev-to-main merge.

- **Dev 1 (Rayed - Repo Owner):**
  - [ ] Perform architectural review of all merged GUI code: ensure clean OOP, proper encapsulation, no code duplication, and strict separation between UI and domain models.
  - [ ] Manage the final Phase 2 Pull Request from `dev` to `main`.
  - [ ] Update personal AI usage log in `docs/ai-usage/RFahmiTXST.md`.
- **Dev 4 (Towsif - QA Lead & Documentation):**
  - [ ] Execute Javadoc generator with `-private -Xdoclint:all -Werror` across all packages (`gui`, `board`, `pieces`, `core`, `ui`, `utils`); ensure **0 warnings and 0 errors**.
  - [ ] Update project `README.md` with Phase 2 GUI launch instructions, feature descriptions, and screenshots.
  - [ ] Update personal AI usage log in `docs/ai-usage/cyber-kuro.md`.
- **Dev 2 (Abid) & Dev 3 (Sabid):**
  - [ ] Dev 2: Update personal AI usage log in `docs/ai-usage/ajv163.md` and complete peer review of GUI components.
  - [ ] Dev 3: Update personal AI usage log in `docs/ai-usage/sabid.md` and verify event handling edge cases (rapid clicks, dragging off-screen, etc.).
- **All Members (Submission Deliverables Preparation):**
  - [ ] Capture the required submission screenshots:
    1. Screenshot of the GUI at the start of the game.
    2. Screenshot of moving several pieces, with pieces getting captured.
    3. Screenshot of capturing the King, displaying the pop-up window declaring the winner.
    4. Screenshots of Extra Features: Menu bar (New/Save/Load in action), Settings customization window with dynamic theme changes, and History Panel with Undo button.
  - [ ] Compile and review the final submission document (`Phase2_Submission_TeamZero.pdf`) containing all required screenshots and design explanations.
  - [ ] Complete and submit peer contribution evaluations if required by course rubric.

🧪 **Manual Tests for M4:**
- Verify clean compilation from terminal: `javac -d out $(Get-ChildItem -Recurse -Filter *.java).FullName`.
- Launch application blindly following `README.md` instructions and verify GUI operates without errors or console warnings.
- Verify Javadoc HTML files in `out/javadoc/index.html` render cleanly with zero broken tags or missing descriptions.

---

## 📊 Phase 2 Grading Criteria & Coverage Breakdown

| Component | Weight | Implementation Details in Plan | Primary Owner |
| :--- | :---: | :--- | :--- |
| **GUI Functionality** | **30%** | 8x8 Swing chessboard, alternating colors, coordinate labels, 32 distinct pieces on starting positions (Milestone 1). | Dev 1 & Dev 2 |
| **Piece Movement & Captures** | **30%** | Click-and-move, drag-and-drop, unrestricted movement to any square, opponent piece removal upon capture (Milestone 2). | Dev 2 & Dev 3 |
| **Endgame Notification** | **10%** | King capture detection, modal pop-up declaring winner, clean application termination (Milestone 2). | Dev 4 |
| **Extra Features** | **20% (+5% Bonus)** | Menu Bar (New/Save/Load), Settings Window (Themes/Sizes/Apply), History Panel + Undo Button (Milestone 3). | All Devs |
| **Code Organization & Docs** | **10%** | OOP design, 0-warning Javadocs, Git workflow, AI usage logs, screenshot PDF report (Milestone 4). | All Devs |
| **Total Potential Score** | **105% / 100%** | Full credit across all rubric categories + 5 bonus points for implementing a 3rd extra feature. | **Team Zero** |

