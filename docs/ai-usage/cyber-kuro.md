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


## Current Limitations

M2 testing covers basic movement for all six chess piece
types, input parsing, Player validation, and ConsoleUI behavior.

Full game-loop integration, king safety, and advanced chess
rules have not yet been completed.