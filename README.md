# 📉 Team Zero Internships

<p align="center">
  <img src="https://img.shields.io/badge/Internships_Acquired-0-red?style=for-the-badge" alt="Internships: 0">
  <img src="https://img.shields.io/badge/Rejections_Received-404-orange?style=for-the-badge" alt="Rejections: 404">
  <img src="https://img.shields.io/badge/LeetCode_Mediums-500%2B-blue?style=for-the-badge" alt="LeetCode">
  <img src="https://img.shields.io/badge/Copium_Level-Maximum-brightgreen?style=for-the-badge" alt="Copium">
</p>

> *"Status 404: Internship Not Found. But our code still compiles."*

---

## 👥 The Unemployed Dream Team

| Team Member | Role | Defensive Line in Interviews | Application Status | Fuel Source |
| :--- | :--- | :--- | :--- | :--- |
| **Rayed Fahmi** | **Dev 1**: Core Piece Architecture | *"I know array indexing, so I know 0."* | Under Review (Forever) | Cold Brew |
| **Abid Ahnaf Khan** | **Dev 2**: Board Infrastructure & Display | *"I can fix bugs, just not HR's hiring algorithm."* | Ghosted x50 | Espresso |
| **Sabid Mahmud** | **Dev 3**: Game Flow & Input Validation | *"Graph traversal algorithms can't find my offer letter."* | Recruiter Screened | Energy Drinks |
| **Towsif Hassan** | **Dev 4**: Special Pieces, QA & Project Management | *"My resume formatting is pixel-perfect, I promise."* | Applied 2m ago | Matcha |

---

## ♟️ Current Project: Console Chess (Phase 1)

A two-player chess game that runs in the console, written in Java. The board is drawn as ASCII text
and moves are typed in standard notation such as `E2 E4`.

* 📋 **Team to-do list & merge checkpoints:** [`PHASE1_PLAN.md`](docs/PHASE1_PLAN.md)
* 📚 **API Documentation:** Generate Javadoc locally and open `out/javadoc/index.html`.

### Who owns what

| Dev | Name | Focus | Files owned |
| :---: | :--- | :--- | :--- |
| **1** | **Rayed Fahmi** | Abstract `Piece` blueprint and the Rook, Bishop, Knight and Queen moves | `pieces/Piece.java`, `pieces/Color.java`, `Rook`, `Bishop`, `Knight`, `Queen` |
| **2** | **Abid Ahnaf Khan** | 8x8 board, starting setup, ASCII display with A-H / 1-8 labels | `board/Board.java`, `board/Position.java` |
| **3** | **Sabid Mahmud** | Console game loop, turn handling, parsing and validating moves like `E2 E4` | `core/Game.java`, `core/Player.java`, `core/Move.java`, `ui/ConsoleUI.java`, `utils/Utils.java` |
| **4** | **Towsif Hassan** | Pawn and King moves, castling and promotion, testing, Javadoc, GitHub management | `pieces/Pawn.java`, `pieces/King.java`, `test/`, `docs/`, `README.md` |

### Project structure

```text
src/
├── board/    Board (8x8 grid, display), Position (row/column square)
├── pieces/   Piece (abstract), Color, Pawn, Rook, Knight, Bishop, Queen, King
├── core/     Game (main loop), Player, Move
├── ui/       ConsoleUI (all console input/output)
├── utils/    Utils (move-format validation & parsing)
└── Main.java entry point
docs/         team plan, QA report, and AI usage logs
out/javadoc/  generated API documentation (not committed)
```

### Build & run (JDK 17+)

```bash
# compile everything into out/
javac -d out $(find src -name "*.java")

# play
java -cp out Main
```

### Running Tests (Windows PowerShell)

The project includes a Java-based QA testing framework for verifying
chess piece behavior.

**Requirements:** JDK 17 or newer.

From the project root, compile the source and test files:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$sources = (Get-ChildItem -Path .\src,.\test -Recurse -Filter *.java).FullName
javac --release 17 -d out $sources
```

Run the tests after successful compilation:

```powershell
java -ea -cp out TestRunner
```

The test runner displays passing and failing assertions.


Current test coverage:

- Pawn movement, captures, blocking, and boundary conditions
- King movement, captures, and board boundaries
- Rook, Bishop, Queen, and Knight movement
- Input normalization, validation, and move parsing
- Player input, ownership, and piece movement validation
- Console messages, input, and board-render delegation
- Game initialization, turn alternation, invalid-input retries, captures, restart, and clean exit

**Latest verified results:**

- Shared test suites: 352 passed, 0 failed
- Utils test suite: 28 passed, 0 failed
- Move notation tests: 8 passed, 0 failed
- Total: 388 assertions passed, 0 failed
- Process exit code: 0

The interactive game now initializes the board, starts with White, displays
each turn, and accepts moves such as `E2 E4`. A move must match the selected
piece's movement pattern. Invalid input, opponent-owned pieces, blocked moves,
and friendly captures are rejected without changing turns. Type `quit` or
`exit` to stop; end-of-input also exits cleanly.

This is a Phase 1 movement demo, not a complete chess rules engine.
Board check detection, castling, and promotion are implemented. Castling
checks attacked squares, but ordinary moves do not yet enforce king safety.
Checkmate/stalemate and en passant remain unimplemented. The game currently
ends on quit, exit, or end-of-input.



### Generate the Javadoc

Requires JDK 17 or newer.

From the project root, generate API documentation using:

```powershell
javadoc -private -Xdoclint:all -encoding UTF-8 `
    -d out/javadoc `
    -sourcepath src `
    -subpackages board:pieces:core:ui:utils `
    src/Main.java
```

If `javadoc` is not recognized on Windows, use the full path
to `javadoc.exe` inside your installed JDK.

For example, with Oracle JDK 25:

```powershell
& "C:\Program Files\Java\jdk-25\bin\javadoc.exe" `
    -private -Xdoclint:all -encoding UTF-8 `
    -d out/javadoc `
    -sourcepath src `
    -subpackages board:pieces:core:ui:utils `
    src/Main.java
```

Open the generated documentation:

```powershell
Start-Process ".\out\javadoc\index.html"
```

The final M4 Javadoc audit completed with 0 warnings and 0 errors.

See [Milestone 4 QA report](docs/M4_QA_SABID.md) for validation and remaining release tasks.




### Move notation

| Input | Meaning |
| :--- | :--- |
| `E2 E4` | Move the piece on E2 to E4. A capture is detected automatically. |
| `quit` / `exit` | End the game (case-insensitive). |
| `O-O` / `O-O-O` | Castle kingside / queenside when permitted. |
| `E7 E8=Q` | Promote a pawn; Q, R, B, and N are supported. |

---

## ⚡ Tech Stack & Tools

![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
![C++](https://img.shields.io/badge/C++-00599C?style=flat-square&logo=c%2B%2B&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white)
![LinkedIn](https://img.shields.io/badge/LinkedIn_Easy_Apply-0A66C2?style=flat-square&logo=linkedin&logoColor=white)

---

## 🎭 Team Lore & Fun Facts

* 📉 **The Name Origin:** We named ourselves after our combined sum of internship offer letters.
* 👻 **Ghosting Speedrun:** Record time for getting ghosted after a "Great chat!" email is 14 minutes.
* 🎯 **Ultimate Goal:** Make this team name completely inaccurate before the semester ends.
* 💬 **Motto:** *"Entry-level position requiring 5+ years of experience in technologies invented 2 years ago."*
* 📊 **Live Tracker:**
  ```text
  [Applications Sent] ====> 1,200
  [Interviews]         ==> 4
  [Offers]             => 0
  ```

<!-- DEV4_PHASE2_README_START -->
## Phase 2 — Swing Chess GUI

Phase 2 adds a desktop GUI using Java Swing while retaining the Phase 1 console game. The desktop window opens by default on a machine with a graphical display. The GUI demonstrates the features assigned in `docs/PHASE2_PLAN.md`; it is **not a full tournament-rules chess engine**.

### Requirements and quick start

- JDK **17 or newer** with both `java` and `javac` accessible in the terminal.
- A desktop environment for the Swing GUI. A headless environment runs the console fallback.
- No external Java libraries, assets, or network connection are required to launch the GUI.

**Windows PowerShell** — run from the repository root:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$sources = (Get-ChildItem -Path .\src,.\test -Recurse -Filter *.java).FullName
javac --release 17 -d out $sources
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
java -cp out Main
```

If `javac` is missing from PATH but JDK 25 is installed at the known Windows location, use:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-25'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

**Bash (Linux or Git Bash with a working JDK)**:

```bash
mkdir -p out
find src test -name '*.java' -print0 | xargs -0 javac --release 17 -d out
java -cp out Main
```

For the original terminal game, use `java -cp out Main --console`. WSL requires a JDK installed in its Linux environment unless you intentionally use Git Bash with the Windows JDK.

### GUI features and controls

| Feature | How to use it |
| --- | --- |
| Board and pieces | Standard 8×8 starting setup, colored squares, file/rank labels, and scalable piece icons |
| Click-to-move | Click a piece to select it, then click a destination square |
| Drag-and-drop | Press and drag a piece to its destination; Escape cancels a pending drag |
| Captures and turn | Capturing an opponent removes its piece; the status area displays the active turn |
| King capture | Capturing a King displays a modal winner message; dismissing the message closes the application |
| New Game | **Game → New Game** asks for confirmation before clearing the current session |
| Save and Load | **File → Save Game / Load Game** uses versioned `.chess` files to preserve the board, captured pieces, turn, moved flags, and move history |
| Appearance | **Game → Settings** or **Options → Appearance & Settings** changes themes, piece styles and board size |
| Move history | The right sidebar lists accepted moves, captures, source and destination squares |
| Captured pieces | The sidebar displays captured white and black pieces |
| Undo | The **Undo** button reverses the latest reversible move and updates the board, capture display, turn and history |

Phase 2 intentionally allows unrestricted destination moves rather than enforcing all standard chess rules. Invalid sources, off-board moves, same-square moves, and friendly captures are rejected. Undo is cleared on New Game and Load Game so moves from another session cannot be replayed.

### Running automated tests and Javadoc

**Windows PowerShell**:

```powershell
$env:JAVA_TOOL_OPTIONS = '-Djava.awt.headless=true'
try {
    java -ea -cp out TestRunner
} finally {
    Remove-Item Env:JAVA_TOOL_OPTIONS -ErrorAction SilentlyContinue
}
```

The project contains the Dev 4 M4 verification script `scripts/verify-dev4-phase2-m4.ps1`. It compiles source and test files with `--release 17`, runs the entire headless test suite, and generates Javadoc with strict validation.

To generate the source documentation manually in Windows PowerShell:

```powershell
javadoc -private -Xdoclint:all -Werror -encoding UTF-8 `
    -d out/javadoc -sourcepath src `
    -subpackages gui:board:pieces:core:ui:utils src/Main.java
Start-Process '.\out\javadoc\index.html'
```

If `javadoc` is not on PATH, replace it with the full path to `javadoc.exe` in the installed JDK. Generated contents of `out/` are build artifacts, not repository source files.

### Phase 2 screenshots and submission evidence

Capture the current running GUI, save the images in `docs/screenshots/`, and add the images to this section **after** they exist. Required evidence:

- `phase2_01_initial_board.png` — default chessboard and 32 starting pieces.
- `phase2_02_moves_captures.png` — moved pieces and a captured-pieces rack with a history entry.
- `phase2_03_king_capture_winner.png` — winner popup visible before clicking OK.
- `phase2_04_game_menus.png` — File and Game menus showing New/Save/Load commands.
- `phase2_05_save_load.png` — Save/Load workflow with restored board and history.
- `phase2_06_settings.png` — settings dialog and a changed board style or size.
- `phase2_07_history_undo.png` — move-history table and Undo control.

Once captured, insert Markdown image links such as `![Phase 2 initial board](docs/screenshots/phase2_01_initial_board.png)` here. Do not add broken image links before the files are available.

The team-wide final screenshot report and delivery PDF are tracked in [`docs/PHASE2_REPORT_CHECKLIST.md`](docs/PHASE2_REPORT_CHECKLIST.md). Final submission and the `dev` → `main` release are coordinated by Dev 1.
<!-- DEV4_PHASE2_README_END -->
