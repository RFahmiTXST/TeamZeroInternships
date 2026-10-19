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
