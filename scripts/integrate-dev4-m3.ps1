# Dev 4 Phase 2 M3 integration against the merged HistoryPanel GUI.
# Run at the repository root AFTER synchronizing your feature branch with dev.
# This script preserves existing controller, menu and test registrations.
$ErrorActionPreference = 'Stop'

$controllerPath = Join-Path (Get-Location) 'src/gui/GUIController.java'
$framePath = Join-Path (Get-Location) 'src/gui/ChessFrame.java'
$runnerPath = Join-Path (Get-Location) 'test/TestRunner.java'
foreach ($path in @($controllerPath, $framePath, $runnerPath)) {
    if (-not (Test-Path $path)) { throw "Missing $path; run from the repository root." }
}
foreach ($path in @('src/gui/GameFileService.java', 'src/gui/GameMenuActions.java', 'test/gui/GameFileServiceTest.java')) {
    if (-not (Test-Path $path)) { throw "Missing $path; extract the package before integration." }
}

$controller = [System.IO.File]::ReadAllText($controllerPath)
$frame = [System.IO.File]::ReadAllText($framePath)
$runner = [System.IO.File]::ReadAllText($runnerPath)

if (-not $controller.Contains('getHistory()') -or -not $controller.Contains('addHistoryListener(')) {
    throw 'HistoryPanel APIs not found. Merge the latest dev before integrating Dev 4 M3.'
}
if (-not $controller.Contains('restoreSession(')) {
    $anchor = '    /**' + "`n" + '     * Returns a snapshot that cannot change with future moves.'
    if (-not $controller.Replace("`r`n", "`n").Contains($anchor)) {
        throw 'GUIController changed. Restore method should be integrated manually; no files changed.'
    }
}
if (-not $frame.Contains('GameMenuActions')) {
    if (-not $frame.Contains('JMenuBar menuBar = new JMenuBar();') -or
        -not $frame.Contains('JMenu gameMenu = new JMenu("Game");')) {
        throw 'ChessFrame menu structure changed; review the file before applying. No files changed.'
    }
}
if (-not $runner.Contains('GameFileServiceTest.main(args);') -and
    -not $runner.Contains('test.gui.HistoryPanelTest.main(args);')) {
    throw 'TestRunner structure changed; review it before applying. No files changed.'
}

if (-not $controller.Contains('restoreSession(')) {
    $method = @'
    /**
     * Restores a fully parsed saved game, including captured pieces, turn and history.
     * Loading does not fire capture listeners or announce a winner.
     *
     * @param restoredBoard independent Board reconstructed from the save file
     * @param restoredTurn next active player
     * @param restoredHistory chronological move records
     * @throws IllegalArgumentException if the supplied session is invalid
     * @throws IllegalStateException if called outside the Swing EDT
     */
    public void restoreSession(Board restoredBoard, Color restoredTurn,
                               List<HistoryEntry> restoredHistory) {
        requireEventDispatchThread();
        if (restoredBoard == null || restoredTurn == null || restoredHistory == null) {
            throw new IllegalArgumentException("Restored board, turn and history are required.");
        }
        List<HistoryEntry> restored = List.copyOf(restoredHistory);
        for (int i = 0; i < restored.size(); i++) {
            if (restored.get(i).number() != i + 1) {
                throw new IllegalArgumentException("Restored history numbering is invalid.");
            }
        }
        cancelSelection();
        this.board = restoredBoard;
        this.currentTurn = restoredTurn;
        List<Piece> captured = restoredBoard.getCapturedPieces();
        this.lastCapturedPiece = captured.isEmpty() ? null : captured.get(captured.size() - 1);
        history.clear();
        history.addAll(restored);
        boardPanel.clearHighlights();
        refreshBoard();
        notifyHistoryListeners();
        notifyStatusListeners();
    }

'@
    $normal = $controller.Replace("`r`n", "`n")
    $normal = $normal.Replace($anchor, $method + "`n" + $anchor)
    $controller = $normal
}
if (-not $frame.Contains('GameMenuActions')) {
    $fileMenu = 'JMenuBar menuBar = new JMenuBar();' + "`n" +
        '        GameMenuActions gameMenuActions = new GameMenuActions(this, controller);' + "`n" +
        '        menuBar.add(gameMenuActions.createFileMenu());'
    $frame = $frame.Replace('JMenuBar menuBar = new JMenuBar();', $fileMenu)
    $gameMenu = 'JMenu gameMenu = new JMenu("Game");' + "`n" +
        '        gameMenu.add(gameMenuActions.createNewGameItem());' + "`n" +
        '        gameMenu.addSeparator();'
    $frame = $frame.Replace('JMenu gameMenu = new JMenu("Game");', $gameMenu)
}
if (-not $runner.Contains('GameFileServiceTest.main(args);')) {
    $runner = $runner.Replace('test.gui.HistoryPanelTest.main(args);',
      'test.gui.HistoryPanelTest.main(args);' + "`n" + '        test.gui.GameFileServiceTest.main(args);')
}

$utf8 = [System.Text.UTF8Encoding]::new($false)
[System.IO.File]::WriteAllText($controllerPath, $controller, $utf8)
[System.IO.File]::WriteAllText($framePath, $frame, $utf8)
[System.IO.File]::WriteAllText($runnerPath, $runner, $utf8)
Write-Host 'Dev 4 Phase 2 M3 integration added to GUIController, ChessFrame and TestRunner.'
Write-Host 'Next: compile and run the complete regression suite before committing.'
