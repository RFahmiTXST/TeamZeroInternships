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
integration tests, organize regression testing, and
investigate Git merge conflicts between team contributions.

I reviewed and executed the suggested tests and verified
their results locally.

### Implementation

- Added BoardIntegrationTest.java with 27 assertions.
- Tested board initialization and starting piece placement.
- Verified legal Pawn movement and rejected illegal moves.
- Tested piece positions, moved flags, and board-state preservation.
- Verified captures and board reinitialization.
- Registered the integration tests in TestRunner.

### Peer Review and Integration QA

- Independently tested Dev 1's M3 PR #10: 150 assertions passed.
- Independently tested Dev 3's M3 PR #9: 173 assertions passed.
- Tested a temporary combined integration of PR #9 and PR #10:
  175 assertions passed.
- Reviewed and approved PR #10, which was merged into dev.
- Identified and reported PR #9's merge conflicts for Dev 3
  to resolve independently.

### Final Dev 4 M3 Verification

- Shared assertions: 155 passed
- Utils assertions: 22 passed
- Total: 177 passed, 0 failed
- Exit code: 0

All tests compiled using Java 17 with -Xlint:all.

### Scope and Limitations

The final 177-assertion result applies to my Dev 4
feature branch, which includes merged PR #10 but does not
yet include Dev 3's unmerged game-loop changes.

Castling, Pawn promotion, and check detection are optional
features that I did not implement during M3.
