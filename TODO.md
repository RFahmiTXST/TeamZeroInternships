# ♟️ Console Chess: Phase 1 Team To-Do List

This is the shared plan for the four developers. Tick boxes (`- [x]`) in your own PRs as you finish tasks,
so this file always shows where the project stands.

## Legend

| Marker | Meaning |
| :--- | :--- |
| `- [ ]` | Task to do |
| 🔀 **MERGE POINT Mx** | **Stop and merge.** Open your PR into `main`, get it reviewed, and merge it. Then everyone pulls `main` before continuing. |
| ⛔ | Blocked by another developer's work. The task names who and which merge point. |
| ⭐ | Optional / stretch. The brief marks castling and promotion as optional, and check/checkmate is not a Phase 1 goal. |
| 🧪 | Test task. Phase 1 goal 8 says "test your classes often". |
| 📝 | Javadoc task. Phase 1 goals 2 and 3. |

## Owners

| Role | Name | Branch prefix | Files owned |
| :--- | :--- | :--- | :--- |
| **Developer 1**: Board Infrastructure & Display | _fill in_ | `dev1/` | `board/Board.java`, `board/Position.java` |
| **Developer 2**: Core Piece Architecture | _fill in_ | `dev2/` | `pieces/Piece.java`, `pieces/Color.java`, `Rook`, `Bishop`, `Knight`, `Queen` |
| **Developer 3**: Game Flow & Input Validation | _fill in_ | `dev3/` | `core/Game.java`, `core/Player.java`, `core/Move.java`, `ui/ConsoleUI.java`, `utils/Utils.java` |
| **Developer 4**: Special Pieces, QA & PM | _fill in_ | `dev4/` | `pieces/Pawn.java`, `pieces/King.java`, `test/`, `docs/`, `README.md`, `.gitignore`, `Main.java` |

> `Move` and `ConsoleUI` were not in the role list. They sit with Developer 3 because they are part of the
> input/console loop. `Board.display()` stays with Developer 1.

---

## Milestone overview

```text
 M0 Template ──► M1 Foundations ──► M2 Features ──► M3 Integration ──► M4 Docs & Submission
  (Dev 4)         helpers & parsing   moves, board,     full game works     Javadoc, README,
                  everyone depends on display, loop     end to end          final tag
```

| Merge point | Goal | Target date | Merge order (to avoid conflicts) | Done when |
| :--- | :--- | :--- | :--- | :--- |
| 🔀 **M0** | Template is on `main` | _fill in_ | Dev 4 | Everyone has cloned and built it |
| 🔀 **M1** | Shared helpers work | _fill in_ | Dev 2 → Dev 1 → Dev 3 → Dev 4 | The M1 smoke test passes |
| 🔀 **M2** | Every feature implemented | _fill in_ | Dev 1 → Dev 2 → Dev 4 → Dev 3 | The M2 smoke test passes: the game is playable |
| 🔀 **M3** | Integration & optional features | _fill in_ | Any order, small PRs | The M3 regression checklist passes |
| 🔀 **M4** | Javadoc generated, submission ready | _fill in_ | Dev 4 last | Tag `v1.0-phase1` is pushed |

**Why this order?** At M1, Dev 2's `Piece` helpers and Dev 1's `Board` accessors are what everyone
else's code calls, so they merge first. At M2, Dev 3 merges last because the game loop is where all
the other pieces come together.

---

## Team rules (read before writing code)

1. **Never commit directly to `main`.** Work on `devN/<topic>` (for example `dev1/board-display`) and open a PR.
   Each PR needs **one review from another developer** before it is merged.
2. **Only edit files you own.** If you need a change in someone else's file, ask them or open an issue.
   This prevents almost all merge conflicts.
3. **Shared contracts are frozen after M0.** These are the public method signatures in `Piece`, `Board`,
   `Position`, `Move` and `Color`. To change one, post in the team chat first. Then make it in its own small
   PR, merged the same day, because three other people are coding against it.
4. **Coordinate convention:** `row 0 = rank 1`, `column 0 = file A`. `"E2"` is `new Position(1, 4)`.
   See the `Position` Javadoc.
5. **`possibleMoves(Board board)` takes the board.** This differs from the brief, because a piece needs the
   board to see blockers.
6. **TODO tags:** every stub says `// TODO(Dev N)`. Find yours with `grep -rn "TODO(Dev 1" src`. Delete the
   TODO when you implement it.
7. **Small, frequent commits** with clear messages ("Implement Board.render with rank/file labels").
   Phase 1 goal 7 requires multiple commits, and each of us should have several.
8. **Before every PR:** `javac -Xlint:all -d out $(find src -name "*.java")` must compile with no errors,
   and every new method or attribute must have Javadoc.
9. **After every merge point:** `git checkout main && git pull origin main`, then on your branch
   `git merge main`.

---

## 👤 Developer 1: Board Infrastructure & Display

### Phase A: before M1 (unblocks Dev 2 and Dev 4)

- [ ] Clone the repo, create `dev1/board-core`, and build and run the template.
- [ ] `Position.toString()`: return the file letter `'A' + column` followed by `row + 1`.
  - [ ] 🧪 `new Position(1, 4)` gives `"E2"`, `(0, 0)` gives `"A1"`, `(7, 7)` gives `"H8"`, `(7, 3)` gives `"D8"`.
- [ ] `Board.getPiece(Position)`: return `squares[row][column]`, or `null` if `!position.isOnBoard()`.
- [ ] `Board.setPiece(Position, Piece)`: store the piece (or `null`) in the grid. Ignore or throw for off-board squares.
- [ ] `Board.isEmpty(Position)`: `true` only if the square is on the board and has no piece.
- [ ] 🧪 Empty board: `getPiece` returns `null` everywhere; `setPiece` then `getPiece` returns the same object;
  off-board `getPiece` returns `null`.
- [ ] 📝 Update Javadoc if behaviour differs from the template comments.

### 🔀 MERGE POINT M1: open PR `dev1/board-core` → `main` (merge 2nd, after Dev 2)

### Phase B: M1 → M2

- [ ] `Board.initialize()`:
  - [ ] Clear all 64 squares and `capturedPieces` first, so calling it twice is safe.
  - [ ] Row 0 (rank 1), White: `R N B Q K B N R`. The Queen is on **D1** (column 3) and the King on **E1** (column 4).
  - [ ] Row 1: 8 white pawns. Row 6: 8 black pawns.
  - [ ] Row 7 (rank 8), Black: `R N B Q K B N R`. The Queen is on **D8** and the King on **E8**.
  - [ ] Create each piece with its own `Position`, for example `new Rook(Color.WHITE, new Position(0, 0))`.
- [ ] `Board.render()`. It must match the brief's sample exactly:
  ```text
     A  B  C  D  E  F  G  H
  8 bR bN bB bQ bK bB bN bR
  7 bp bp bp bp bp bp bp bp
  6    ##    ##    ##    ##
  5 ##    ##    ##    ##
  4    ##    ##    ##    ##
  3 ##    ##    ##    ##
  2 wp wp wp wp wp wp wp wp
  1 wR wN wB wQ wK wB wN wR
  ```
  - [ ] File letters **A-H on top**, rank numbers **8 down to 1 on the left**. Both are required by the brief.
  - [ ] A square is dark when `(row + column) % 2 == 0` (A1 is dark). Empty dark squares show `##`; empty light squares show two spaces.
  - [ ] Occupied squares use `piece.toString()`. ⛔ Needs Dev 2's `Piece.toString()` from M1.
  - [ ] Use a `StringBuilder` and return a `String`. `display()` already prints it.
  - [ ] Decide whether lines keep trailing spaces (for example after an empty light square on H) and make the 🧪 test match. Either choice is fine.
- [ ] `Board.movePiece(from, to)`:
  - [ ] Throw `IllegalArgumentException` if `from` is empty.
  - [ ] Add any piece on `to` to `capturedPieces` and return it (otherwise return `null`).
  - [ ] Put the moving piece on `to`, clear `from`, and call `piece.move(to)`. ⛔ Needs Dev 2's `Piece.move` from M1.
- [ ] `Board.getPieces(Color)`: scan the grid and collect that color's pieces. Dev 3 uses this to fill each `Player`.
- [ ] 🧪 After `initialize()`: 32 pieces in total, 16 per color; E1 is `wK`, D8 is `bQ`, A2 is `wp`; rows 2-5 are empty.
- [ ] 🧪 `render()` equals the expected string above, character for character. This is the test most likely to catch mistakes.
- [ ] 🧪 `movePiece(E2, E4)`: E2 is empty, E4 holds `wp`, and the pawn's `getPosition()` is E4.
- [ ] 🧪 A capture returns the captured piece and adds it to `getCapturedPieces()`.

### 🔀 MERGE POINT M2: open PR `dev1/board-display` → `main` (merge 1st)

### Phase C: M2 → M3

- [ ] Run the full game with Dev 3 after M2 and fix any display problems, such as alignment or a missing blank line.
- [ ] ⭐ Show captured pieces under the board, for example `Captured: bp wN`.
- [ ] ⭐ `findKing(Color)` helper, then `isCheck(Color)`: true when any opponent piece's `possibleMoves(this)`
  contains the king's square. Pair with Dev 4 on this.
- [ ] ⭐ `isCheckmate` / `isStalemate`. These are probably Phase 2 work and can stay as stubs.
- [ ] 📝 Every attribute and method in `board/` has Javadoc, and `-Xdoclint:all` shows no warnings for `board/`.

### 🔀 MERGE POINT M3

---

## 👤 Developer 2: Core Piece Architecture

### Phase A: before M1 (Dev 1, 3 and 4 code against your `Piece` class)

- [ ] Clone the repo, create `dev2/piece-base`, and build and run the template.
- [ ] Review the `Piece` contract (fields, `possibleMoves(Board)`, `getSymbol()`, `canMoveTo`, `slide`).
  Raise any signature changes in the team chat **before M1**, because three other people depend on them.
- [ ] `Piece.toString()`: `"" + color.getPrefix() + getSymbol()` gives `"wQ"`, `"bN"`, `"wp"`.
- [ ] `Piece.move(Position)`: update `position` and set `moved = true`.
- [ ] `Piece.isOpponent(Piece)`: `other != null && other.getColor() != color`.
- [ ] `Piece.canMoveTo(Board, Position)`: the target is on the board **and** is either empty or holds an opponent.
  ⛔ Needs Dev 1's `isEmpty`/`getPiece` to test. Write it against the contract now and test after M1.
- [ ] `Piece.slide(Board, int[][] directions)`: walk each direction one square at a time; add empty squares;
  on an occupied square, add it only if it holds an opponent, then stop that direction; stop at the board edge.
- [ ] 🧪 `toString()` for all 12 color/piece combinations matches the brief: `wp bp wR bR wN bN wB bB wQ bQ wK bK`.

### 🔀 MERGE POINT M1: open PR `dev2/piece-base` → `main` (merge 1st). Dev 4 needs `canMoveTo` for King and Pawn.

### Phase B: M1 → M2

- [ ] `Rook.possibleMoves`: `slide` with `{{1,0},{-1,0},{0,1},{0,-1}}`.
- [ ] `Bishop.possibleMoves`: `slide` with `{{1,1},{1,-1},{-1,1},{-1,-1}}`.
- [ ] `Queen.possibleMoves`: `slide` with all 8 directions.
- [ ] `Knight.possibleMoves`: the 8 offsets `{±1,±2}` and `{±2,±1}`, keeping each square where `canMoveTo` is true. Knights jump, so do not use `slide`.
- [ ] Store each direction table as a `private static final int[][]` constant with Javadoc.
- [ ] 🧪 Empty board, piece on **D4**: Rook gives 14 moves, Bishop 13, Queen 27, Knight 8.
- [ ] 🧪 Corner: Knight on A1 gives exactly B3 and C2; Rook on A1 gives 14.
- [ ] 🧪 Starting position: Knight on B1 gives exactly A3 and C3; Rook, Bishop and Queen give 0 moves.
- [ ] 🧪 Blocking: a friendly piece in the path stops the slide **before** that square; an opponent piece stops it **on** that square (capture).

### 🔀 MERGE POINT M2: open PR `dev2/pieces-moves` → `main` (merge 2nd)

### Phase C: M2 → M3

- [ ] Pair with Dev 3 on **move legality**: `Player.makeMove` checks that `move.getTo()` is in
  `piece.possibleMoves(board)`. Strongly recommended, even though Phase 1 only requires a format check.
- [ ] ⭐ Help Dev 4 with promotion: write a small factory, for example `Piece.create(char symbol, Color, Position)`,
  that turns `'Q'` into `new Queen(...)`.
- [ ] 📝 Every class, method and attribute in your files has Javadoc, including the direction constants.

### 🔀 MERGE POINT M3

---

## 👤 Developer 3: Game Flow & Input Validation

### Phase A: before M1 (needs nothing from the others)

- [ ] Clone the repo, create `dev3/input-parsing`, and build and run the template.
- [ ] `Utils.normalize`: trim, collapse repeated spaces, convert to upper case; return `null` for `null` input.
- [ ] `Utils.isValidMoveFormat`. This is the Phase 1 **basic validation** requirement:
  - [ ] Required: `^[A-H][1-8] [A-H][1-8]$` (after normalising).
  - [ ] ⭐ Promotion: `^[A-H][1-8] [A-H][1-8]=[QRBN]$`.
  - [ ] ⭐ Castling: `O-O`, `O-O-O`. The brief uses the letter O, but also accept the zeros `0-0` / `0-0-0` that people often type.
  - [ ] Store the regexes as `private static final Pattern` constants with Javadoc.
- [ ] `Utils.parsePosition("E2")`: return `new Position(1, 4)`; throw `IllegalArgumentException` for bad input.
- [ ] `Utils.parseMove`: return `Move.castle(...)`, `new Move(from, to, 'Q')`, or `new Move(from, to)`.
- [ ] `Move.toString()`: format the move back into notation (`E2 E4`, `E7 E8=Q`, `O-O`). ⛔ Needs Dev 1's `Position.toString()` from M1.
- [ ] 🧪 Validation table. Every row must behave as listed:

  | Input | Valid? |
  | :--- | :--- |
  | `E2 E4`, `e2 e4`, `  E2   E4 ` | ✅ |
  | `E7 E8=Q`, `O-O`, `O-O-O`, `0-0` | ✅ (optional formats) |
  | `E2E4`, `E2-E4`, `E9 E4`, `I2 E4`, `E2 E4 E5`, `E2`, empty string, `null` | ❌ |

### 🔀 MERGE POINT M1: open PR `dev3/input-parsing` → `main` (merge 3rd)

### Phase B: M1 → M2

- [ ] `ConsoleUI`:
  - [ ] `showWelcome()`: title, how to type a move, the optional `O-O` and `E7 E8=Q` formats, and how to quit.
  - [ ] `showBoard(board)`: call `board.display()`.
  - [ ] `showTurn(color)`: for example `White's turn.`
  - [ ] `promptMove(color)`: for example `White, enter your move (e.g. E2 E4): `. Return `null` when
    `!scanner.hasNextLine()`, so an end-of-input (Ctrl+D) does not crash the game.
- [ ] `Player`: `setAvailablePieces`, `removePiece`, and `makeMove(board, move)`:
  - [ ] Reject the move if there is no piece on `from` ("No piece on E3").
  - [ ] Reject the move if the piece belongs to the opponent ("That is not your piece").
  - [ ] Otherwise call `board.movePiece(from, to)` and return `true`.
  - [ ] Decide how specific error messages reach `Game`. For example, throw an `IllegalArgumentException`
    with the message and let `Game` catch it and call `ui.showError`. Document the choice in Javadoc.
- [ ] `Game.start()`: `board.initialize()`; give each player `board.getPieces(color)`; set the turn to WHITE;
  set `running = true`; show the welcome message; call `play()`.
- [ ] `Game.play()`. This is the **main interface loop** from Phase 1 goal 6. On each turn:
  - [ ] Show the board, then whose turn it is, then prompt for a move.
  - [ ] Treat `quit` / `exit`, or `null` input, as the end of the game: call `end("Game ended by ...")`.
  - [ ] Show an error for an invalid format and **prompt the same player again** without switching turns.
  - [ ] On a valid move, call `makeMove`. On success, remove any captured piece from the opponent and call `switchTurn()`.
  - [ ] ⭐ After each move, call `board.isCheckmate` / `isStalemate` and end the game if either is true. They return `false` until implemented.
- [ ] `Game.end(result)`: stop the loop, show the final board, and print the result.
- [ ] 🧪 Scripted game, with no typing needed: `new Game(new ConsoleUI(new Scanner("E2 E4\nE7 E5\nquit\n"))).start()`.
- ⛔ Full end-to-end testing needs Dev 1's `initialize`/`render`/`movePiece` (M2). Before then, test against an
  empty `Board` that you fill with `setPiece`.

### 🔀 MERGE POINT M2: open PR `dev3/game-loop` → `main` (merge **last**, then run the M2 smoke test together)

### Phase C: M2 → M3

- [ ] Pair with Dev 2 on **move legality**: reject a destination that is not in `possibleMoves`, with the error "Illegal move for that piece".
- [ ] ⭐ Castling: when `move.isCastling()`, find the king and rook squares for the current color, check
  `king.canCastleKingside(board)` / `canCastleQueenside(board)`, then move both pieces. Pair with Dev 4.
- [ ] ⭐ Promotion: when the type is `PROMOTION`, check that the piece is a pawn arriving on its promotion row,
  then replace it using `board.setPiece` and the factory Dev 2 writes. Pair with Dev 4.
- [ ] ⭐ `help` and `resign` commands.
- [ ] 📝 Every class, method and attribute in `core/`, `ui/` and `utils/` has Javadoc.

### 🔀 MERGE POINT M3

---

## 👤 Developer 4: Special Pieces, QA & Project Management

### Phase 0: M0 (project setup)

- [x] Create the packages `board`, `pieces`, `core`, `ui` and `utils`, and one class file for every entity.
  This covers Phase 1 goal 1 and is done by the template commit.
- [x] Java `.gitignore`, README build instructions and the `docs/` folder. Done in the template.
- [ ] Review the template, then merge branch `claude/relaxed-davinci-72sq14` into `main`.
- [ ] Add all 3 teammates as collaborators. Turn on **branch protection** for `main`: require a PR and 1 approval.
- [ ] Fill in the **Owners** table and the **Target dates** above.
- [ ] ⭐ Create one GitHub Issue per major task in this file and put them on a GitHub Project board.
- [ ] Post the **Team rules** above in the team chat.

### 🔀 MERGE POINT M0: template on `main`; every developer clones it and runs `java -cp out Main`

### Phase A: before M1

- [ ] Create `dev4/test-setup` and set up a **dependency-free test folder** so everyone tests the same way:
  - [ ] `test/` mirrors `src/`, for example `test/board/BoardTest.java` in `package board`. Each test class has a `main`
    method and uses a small `TestUtils.assertEquals(expected, actual, message)` helper.
  - [ ] `test/TestRunner.java` calls every test class and prints the totals passed and failed.
  - [ ] Compile and run with `javac -d out $(find src test -name "*.java") && java -ea -cp out TestRunner`.
  - [ ] Add these commands to the README.
- [ ] `Pawn.getForwardDirection()`: +1 for white, -1 for black. `getStartRow()`: 1 / 6. `getPromotionRow()`: 7 / 0.
- [ ] 🧪 Tests for those three helpers.
- [ ] Review every M1 PR. As PM, you are the second reviewer when the assigned reviewer is busy.

### 🔀 MERGE POINT M1: merge `dev4/test-setup` → `main` (merge last), then run the **M1 smoke test** below

### Phase B: M1 → M2

- [ ] `Pawn.possibleMoves(board)`:
  - [ ] One square forward if it is empty.
  - [ ] Two squares forward if `!hasMoved()` (or the pawn is on its start row) **and both** squares are empty.
  - [ ] One square diagonally forward, left or right, only if it holds an opponent (`isOpponent`).
  - [ ] Never backwards or sideways; never captures straight ahead.
  - [ ] Moves onto the last rank are still listed; promotion happens when the move is carried out (M3).
  - [ ] ⭐ En passant is out of scope for Phase 1. Note it in Javadoc.
- [ ] `King.possibleMoves(board)`: the 8 neighbouring squares where `canMoveTo` is true. Phase 1 does not filter out attacked squares.
- [ ] ⭐ `King.canCastleKingside` / `canCastleQueenside`: the king has not moved; the rook on H (or A) is a `Rook`
  of the same color that has not moved; F and G (or B, C and D) are empty. Not moving through check is a Phase 2 rule.
- [ ] 🧪 Pawn: white E2 gives E3 and E4; after it moves, only one step; blocked straight ahead gives 0 moves;
  captures diagonally only an opponent; a black pawn on E7 gives E6 and E5.
- [ ] 🧪 King: E1 in the starting position gives 0 moves; D4 on an empty board gives 8; A1 gives 3.
- [ ] 🧪 Write integration tests for the other developers' merged code, such as the `render()` string and the Utils
  validation table, if they have not been written yet.

### 🔀 MERGE POINT M2: merge `dev4/pawn-king` → `main` (merge 3rd, before Dev 3), then lead the **M2 smoke test**

### Phase C: M2 → M3 (integration & optional features)

- [ ] ⭐ Castling with Dev 3: wire `O-O` / `O-O-O` through `Game`, moving both king and rook.
- [ ] ⭐ Promotion with Dev 3 and Dev 2: `E7 E8=Q` replaces the pawn with the chosen piece.
- [ ] ⭐ Check/checkmate with Dev 1: `isCheck` built on `King` and `possibleMoves`.
- [ ] 🧪 Run the **M3 regression checklist** below on a fresh clone. File a GitHub Issue for each bug and assign it to the file's owner.

### 🔀 MERGE POINT M3

### Phase D: M3 → M4 (documentation & submission)

- [ ] 📝 **Javadoc audit:** run
  `javadoc -private -Xdoclint:all -d /tmp/jd -sourcepath src -subpackages board:pieces:core:ui:utils src/Main.java`
  and send each warning to the file's owner. Goal: **zero warnings**, so every class, method and attribute is documented.
- [ ] 📝 **Generate the final Javadoc** into `docs/` (command in the README) and commit it. This is Phase 1 goal 3.
- [ ] ⭐ Turn on GitHub Pages (Settings → Pages → `main` / `/docs`) so the docs are available as a website.
- [ ] Update the README: features, how to play, known limitations (for example en passant), and team roles.
- [ ] Check `git shortlog -sn` on `main`: **every developer has several commits** (goal 7).
- [ ] Final check on a fresh clone: compile, run the tests, and play a short game.
- [ ] Tag the release: `git tag v1.0-phase1 && git push origin v1.0-phase1`.

### 🔀 MERGE POINT M4: final merge to `main`, tagged and submitted ✅

---

## 🧪 Smoke tests (run together right after each merge point)

### After M1

- [ ] `main` compiles with no errors.
- [ ] `new Position(1, 4).toString()` gives `E2`.
- [ ] `Utils.isValidMoveFormat("e2 e4")` gives `true`, and `Utils.isValidMoveFormat("E9 E4")` gives `false`.
- [ ] On an empty board with a piece placed by `setPiece`, `getPiece` returns it and `canMoveTo` works.
- [ ] `TestRunner` runs and all tests pass.

### After M2: first playable build 🎉

- [ ] `java -cp out Main` shows the starting board **exactly** like the sample in the brief.
- [ ] It says "White's turn" and prompts for a move.
- [ ] `E2 E4` moves the pawn, the board redraws, and it becomes Black's turn.
- [ ] `E2E4`, `Z9 Z9`, `hello` and an empty line each show an error and **prompt the same player again**.
- [ ] Moving from an empty square, or moving an opponent's piece, shows an error.
- [ ] Capture: `E2 E4`, `D7 D5`, `E4 D5` puts the white pawn on D5, and the black pawn is captured.
- [ ] `quit` ends the game cleanly. Ctrl+D does not crash it.

### After M3: regression checklist

- [ ] Everything in the M2 list still passes.
- [ ] Each piece type rejects at least one illegal move, if legality checks were added.
- [ ] ⭐ Kingside castling works after `E2 E4, E7 E5, G1 F3, B8 C6, F1 C4, G8 F6, O-O`.
- [ ] ⭐ Promotion `X7 X8=Q` produces `wQ`.
- [ ] `-Xdoclint:all` shows no warnings.

---

## ✅ Phase 1 requirement checklist (from the project brief)

| # | Requirement | Owner(s) | Merge point | Status |
| :---: | :--- | :--- | :---: | :---: |
| 1 | Packages (`pieces`, `board`, `utils`, …) and one class file per entity | Dev 4 | M0 | ✅ template |
| 2 | Javadoc on every class, method and attribute | Everyone, audited by Dev 4 | M4 | ☐ |
| 3 | Generated Javadoc in `docs/` | Dev 4 | M4 | ☐ |
| 4 | Abstract `Piece` plus 6 subclasses with their own movement rules | Dev 2, Dev 4 | M2 | ☐ |
| 5 | `Board`: 8x8 matrix, initialization, display | Dev 1 | M2 | ☐ |
| 6 | Console loop: prompt, parse input, basic format validation | Dev 3 | M2 | ☐ |
| 7 | GitHub repo with multiple commits | Everyone, checked by Dev 4 | M4 | ☐ |
| 8 | Classes tested often | Everyone, coordinated by Dev 4 | Every merge point | ☐ |
| ⭐ | Castling `O-O` / `O-O-O` | Dev 4 + Dev 3 | M3 | ☐ |
| ⭐ | Pawn promotion `E7 E8=Q` | Dev 4 + Dev 3 + Dev 2 | M3 | ☐ |
| ⭐ | Check / checkmate / stalemate | Dev 1 + Dev 4 | M3 / Phase 2 | ☐ |
