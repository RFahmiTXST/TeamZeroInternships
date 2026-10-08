# ♟️ Console Chess: Phase 1 Team Plan

This is the central planning document for Phase 1 of the Console Chess project. It outlines the team guidelines, branching strategy, member responsibilities, and our four development milestones.

## 👥 Team Members & Feature Branches

All development must take place on your designated feature branch. Never commit directly to `main` or `dev`.

| Developer | Role | Focus | Branch Name |
| :--- | :--- | :--- | :--- |
| **Rayed Fahmi** | **Dev 1** | Core Piece Architecture | `feature/RFahmiTXST` |
| **Abid Ahnaf Khan** | **Dev 2** | Board Infrastructure & Display | `feature/<username>` |
| **Sabid Mahmud** | **Dev 3** | Game Flow & Input Validation | `feature/<username>` |
| **Towsif Hassan** | **Dev 4** | Special Pieces, QA & PM | `feature/<username>` |

## 📜 Team Guidelines & Workflow (Crucial)

1. **Git Workflow:** 
   - We maintain a `main` branch (production-ready) and a `dev` branch (integration).
   - Work on your `feature/<name>` branch.
   - Open a Pull Request (PR) from your feature branch into `dev`.
   - **Requirement:** Every member must open at least one PR into `dev` and review at least one teammate's PR.
   - Do NOT delete your remote branches; keep them until grading is complete.
2. **AI Usage Logs:**
   - Every member must maintain a log of their AI usage in `docs/ai-usage/<github-username>.md`.
   - Record what you asked the AI, what you used/changed, and the feedback/result.
3. **Commit Incrementally:**
   - Commit your work regularly. Milestones must be traceable to meaningful commits. Avoid massive code dumps at the end.
4. **Javadoc & Clean Code (OOP):**
   - Ensure all classes, interfaces, methods, and attributes are documented using Javadoc.
   - Avoid hardcoding, minimize coupling, and keep methods small and testable.

---

## 🎯 Milestones

### 🔀 Milestone 1: Project Setup & Core Abstractions
**Goal:** Establish the project repository, base classes, and core structure so all developers can work in parallel.

- **Dev 1 (Rayed - Repo Owner):**
  - [ ] Set up the initial GitHub repository with `main` and `dev` branches, add teammates, and manage branch pulling and PR merges.
  - [ ] Define `Piece.toString()`: returns `"" + color.getPrefix() + getSymbol()` (e.g., `"wQ"`, `"bN"`).
  - [ ] Define `Piece.move(Position)`: update `position` and set `moved = true`.
  - [ ] Define `Piece.isOpponent(Piece)`: checks `other != null && other.getColor() != color`.
  - [ ] Define `Piece.canMoveTo(Board, Position)`: verify target is on the board and is either empty or holds an opponent.
  - [ ] Define `Piece.slide(Board, int[][] directions)`: logic for sliding pieces (Rook, Bishop, Queen).
- **Dev 2 (Abid):**
  - [ ] Define `Position.toString()`: return file letter (`'A' + column`) followed by `row + 1` (e.g., `"E2"`).
  - [ ] Implement `Board` core methods: `getPiece(Position)`, `setPiece(Position, Piece)`, and `isEmpty(Position)`.
- **Dev 3 (Sabid):**
  - [ ] Define `Utils.normalize(String)`: trim, collapse spaces, convert to uppercase.
  - [ ] Define `Utils.isValidMoveFormat(String)`: regex for basic move formats `^[A-H][1-8] [A-H][1-8]$`. (Optional: castling `O-O` and promotion `=Q`).
  - [ ] Define `Utils.parsePosition(String)` and `Utils.parseMove(String)`.
- **Dev 4 (Towsif - PM):**
  - [ ] Create the directory structure (`src/board`, `src/pieces`, `src/core`, `src/ui`, `src/utils`), and `docs/ai-usage/` folder.
  - [ ] Setup a dependency-free test folder (`test/`) with a `TestRunner.java` to execute simple assertion tests.

🧪 **Manual Tests for M1:**
- Compile the project successfully: `javac -d out $(find src -name "*.java")`.
- Instantiate a `Position` and assert `toString()` formats correctly (e.g., column 4, row 1 returns `"E2"`).
- Test that `Utils.isValidMoveFormat` accurately validates `"E2 E4"` as true and `"E9 E4"` as false.

### 🔀 Milestone 2: Board Infrastructure & Piece Movements
**Goal:** Populate the board with pieces and ensure pieces know their legal movement patterns.

- **Dev 1 (Rayed):**
  - [ ] Implement `Rook.possibleMoves(Board)`: use `slide` with directions `{{1,0},{-1,0},{0,1},{0,-1}}`.
  - [ ] Implement `Bishop.possibleMoves(Board)`: use `slide` with directions `{{1,1},{1,-1},{-1,1},{-1,-1}}`.
  - [ ] Implement `Queen.possibleMoves(Board)`: use `slide` with all 8 directions.
  - [ ] Implement `Knight.possibleMoves(Board)`: check the 8 L-shaped offsets `{±1,±2}` and `{±2,±1}` using `canMoveTo`.
- **Dev 2 (Abid):**
  - [ ] Implement `Board.initialize()`: clear squares, place all 32 pieces in their starting positions (white on rows 0/1, black on rows 6/7).
  - [ ] Implement `Board.render()`: use a `StringBuilder` to render the grid with file letters A-H on top and rank numbers 8-1 on the left.
  - [ ] Implement `Board.movePiece(from, to)`: throw exception if `from` is empty, otherwise move piece, add any captured piece to `capturedPieces`, and call `Piece.move()`.
- **Dev 3 (Sabid):**
  - [ ] Build `ConsoleUI`: Implement `showWelcome()`, `showBoard(Board)`, `showTurn(Color)`, and `promptMove(Color)`.
  - [ ] Build `Player.makeMove()`: reject if no piece is on `from` or belongs to the opponent, otherwise call `board.movePiece(from, to)`.
- **Dev 4 (Towsif):**
  - [ ] Implement `Pawn.possibleMoves(Board)`: handle moving 1 square forward, 2 squares forward (if `!hasMoved`), and diagonal captures of opponents.
  - [ ] Implement `King.possibleMoves(Board)`: check the 8 neighboring squares using `canMoveTo`.

🧪 **Manual Tests for M2:**
- Call `Board.display()` and visually verify it matches the required project brief format perfectly.
- Place a single `Knight` in the middle of an empty board and manually verify `possibleMoves()` returns 8 valid positions.
- Place a `Pawn` at starting position and verify it can move 1 or 2 steps forward.

### 🔀 Milestone 3: Game Loop, Turn Flow & Integration
**Goal:** Tie the board, pieces, and player input into a playable game loop.

- **Dev 3 (Sabid):**
  - [ ] Implement `Game.play()` interface loop: alternate turns, show the board, and prompt for moves.
  - [ ] Treat `quit`/`exit` as end of game cleanly.
  - [ ] Show an error for invalid input format and reprompt the *same* player without switching turns.
- **Dev 1 (Rayed):**
  - [ ] Assist Dev 3 in integrating move legality: reject a destination if it is not in the piece's `possibleMoves(board)` list.
- **Dev 2 (Abid):**
  - [ ] Work with Dev 4 to implement `Board.isCheck(Color)`: scan if any opponent's piece targets the King's square. (Optional Phase 1 feature, required for Phase 2).
- **Dev 4 (Towsif):**
  - [ ] Optional: Integrate Castling logic (`O-O`, `O-O-O`) across `Game` and `King`.
  - [ ] Optional: Integrate Pawn Promotion (`E7 E8=Q`) across `Game` and `Pawn`.

🧪 **Manual Tests for M3:**
- Play a mock game: Type `E2 E4`. Verify White pawn moves. Type `E7 E5`. Verify Black pawn moves.
- Attempt an invalid format like `hello`. Verify the game shows an error and reprompt the *same* player.
- Execute a capture. Verify the captured piece is replaced on the board and the game doesn't crash.

### 🔀 Milestone 4: QA, Documentation & Final Delivery
**Goal:** Finalize code quality, generate Javadocs, write the README, and merge everything into `main`.

- **Dev 1 (Rayed - Repo Owner):**
  - [ ] Manage the final PR from `dev` to `main` and oversee repository delivery. 
  - [ ] Perform a final peer review of all merged code in the `dev` branch. Look for tight coupling, poor naming, or missing Javadocs.
  - [ ] Update your personal AI usage logs.
- **Dev 4 (Towsif):**
  - [ ] Run Javadoc generation tool with `-Xdoclint:all` and ensure zero warnings. 
  - [ ] Write the runnable README with dependencies, commands, and feature summaries.
- **Dev 2 (Abid) & Dev 3 (Sabid):**
  - [ ] Perform a final peer review of all merged code in the `dev` branch. 
  - [ ] Update your personal AI usage logs.
- **All Members:** 
  - [ ] Verify all tasks in the rubric are met. 
  - [ ] Submit peer contribution evaluations if required.

🧪 **Manual Tests for M4:**
- Verify `javadoc -private -d docs ...` generates successfully and `index.html` is readable.
- Clone the repository into a fresh directory, follow the README instructions blindly, and ensure the game compiles and runs perfectly.
- Ensure all feature branches are pushed and untouched for grading.
