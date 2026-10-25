# AI Usage Log — cyber-kuro

**Role:** Developer 4 — Special Pieces, QA & Project Management

**Project:** Console Chess — Phase 1

**AI Tool:** OpenAI ChatGPT

### AI Assistance
I asked ChatGPT to help structure the framework of the project and breakdown my responsibilities and priorities. Later, I also asked for guidance on designing and implementing tests for my code.

ChatGPT explained the board's zero-based coordinate system and suggested implementations about QA tests.

The suggested testing framework uses assertion methods, tracks passing and failing assertions, and produces a test summary.

### Result
All six Pawn helper tests passed:

- White forward direction: PASS
- Black forward direction: PASS
- White starting row: PASS
- Black starting row: PASS
- White promotion row: PASS
- Black promotion row: PASS

**Total: 6 passed, 0 failed.**


## Milestone 2 — Testing and QA

### AI Assistance

I used ChatGPT to help structure the testing framework,
organize test scenarios for Pawn and King movement, and plan
the integration of the team's test suites.

### Work and Verification

I implemented and tested Pawn and King movement, including
normal moves, captures, blocking, and boundary conditions.

I also integrated the team's automated tests into the central
TestRunner and independently reviewed other developers' work.

### Result

- Pawn assertions: 34 passed
- King assertions: 15 passed
- Other piece assertions: 38 passed
- Player and ConsoleUI assertions: 28 passed
- Utils assertions: 22 passed

**Combined result: 137 passed, 0 failed.**

Additional Board QA smoke tests: 17 passed, 0 failed.


## Milestone 3 — Integration Testing and QA

### AI Assistance

I used ChatGPT to help design Board, Player, and Piece
integration tests, implement Pawn promotion and Castling,
organize regression testing.

I reviewed and executed the suggested code and tests
and verified their results locally.

### Implementation

- Added BoardIntegrationTest.java with 27 assertions.
- Tested board initialization and starting piece placement.
- Verified legal Pawn movement and rejected illegal moves.
- Tested piece positions, moved flags, and board-state preservation.
- Verified captures and board reinitialization.
- Implemented Pawn promotion to Queen, Rook, Bishop, and Knight.
- Implemented kingside and queenside Castling for both colors.
- Added AttackDetector.java to detect attacked squares.
- Integrated special moves into Player.makeMove().
- Added support for O-O, O-O-O, and E7 E8=Q notation.
- Added special-move tests and updated TestRunner.

### Peer Review and Integration QA

- Independently tested Dev 1's M3 PR #10: 150 assertions passed.
- Independently tested Dev 3's M3 PR #9: 173 assertions passed.
- Tested a temporary combined integration of PR #9 and PR #10:
  175 assertions passed.
- Reviewed and approved PR #10, which was merged into dev.
- Identified and reported PR #9's merge conflicts for Dev 3
  to resolve independently.

### Final Dev 4 M3 Verification

- Shared assertions: 315 passed
- Utils assertions: 28 passed
- Total: 343 passed, 0 failed
- Exit code: 0

All tests compiled using Java 17 with -Xlint:all.

### Scope and Limitations

The final 343-assertion result applies to my Dev 4
feature branch, which includes merged PR #10 but does
not yet include Dev 3's unmerged game-loop changes.

Pawn promotion and Castling are implemented and tested
through move parsing, Player, and Board.

Interactive game-loop integration and full King safety
validation have not yet been completed.


## Milestone 4 — Final QA and Documentation

### AI Assistance

I used ChatGPT to help investigate the Javadoc setup on
Windows, identify missing documentation.

I reviewed the changes and verified them locally.

### Work Completed

- Synchronized with the merged M3 game-loop implementation.
- Verified Castling and Pawn Promotion in the interactive game.
- Ran the complete regression suite successfully.
- Located the Javadoc tool in the Oracle JDK 25 installation.
- Resolved 29 Javadoc warnings across the Java source files.
- Generated API documentation using `-Xdoclint:all`.
- Opened and verified the generated HTML documentation.
- Updated README instructions for generating Javadocs.
- Updated the Phase 1 milestone completion checklist.

### Final Verification

- Shared assertions: 352 passed, 0 failed.
- Utils assertions: 28 passed, 0 failed.
- Total: 380 passed, 0 failed.
- Java compilation: successful with `--release 17 -Xlint:all`.
- Javadoc warnings: 0.
- Javadoc errors: 0.
- Javadoc exit code: 0.
- Regression exit code: 0.

### Scope

This milestone focused on QA and documentation.
Ordinary-move king safety, checkmate, stalemate, and
en passant remain outside the implemented Phase 1 scope.
