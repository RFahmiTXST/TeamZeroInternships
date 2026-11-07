# Run from the repository root only AFTER the merged GUIController exposes
# addCaptureListener(CaptureListener). This preserves existing controller code.
$ErrorActionPreference = 'Stop'
$controllerPath = Join-Path (Get-Location) 'src/gui/GUIController.java'
$framePath = Join-Path (Get-Location) 'src/gui/ChessFrame.java'
$runnerPath = Join-Path (Get-Location) 'test/TestRunner.java'
if (-not (Test-Path $controllerPath)) { throw 'GUIController.java not found. Run from project root.' }
if (-not (Test-Path $framePath)) { throw 'ChessFrame.java not found. Run from project root.' }
if (-not (Test-Path $runnerPath)) { throw 'TestRunner.java not found. Run from project root.' }
$controller = [System.IO.File]::ReadAllText($controllerPath)
if ($controller -notmatch 'addCaptureListener\s*\(') {
    throw 'The merged controller does not expose addCaptureListener yet. Merge/reconcile Dev 1 and Dev 3 M2 PRs first; no files changed.'
}
$frame = [System.IO.File]::ReadAllText($framePath)
if (-not $frame.Contains('KingCaptureHandler')) {
    $needle = 'this.controller = new GUIController(board, boardPanel);'
    if (-not $frame.Contains($needle)) { throw 'ChessFrame constructor has changed; review manually. No files changed.' }
    $insert = $needle + "`r`n" +
       '        // Dev 4 M2: show winner and exit only when a King is captured.' + "`r`n" +
       '        KingCaptureHandler kingCaptureHandler = new KingCaptureHandler(this);' + "`r`n" +
       '        controller.addCaptureListener(kingCaptureHandler::onPieceCaptured);'
    $frame = $frame.Replace($needle, $insert)
    [System.IO.File]::WriteAllText($framePath, $frame, [System.Text.UTF8Encoding]::new($false))
    Write-Host 'Registered KingCaptureHandler once in ChessFrame.'
} else {
    Write-Host 'ChessFrame already references KingCaptureHandler; skipped.'
}
$runner = [System.IO.File]::ReadAllText($runnerPath)
if (-not $runner.Contains('KingCaptureHandlerTest.main(args);')) {
    $needle = 'test.gui.GUIControllerTest.main(args);'
    if (-not $runner.Contains($needle)) { throw 'TestRunner changed: add KingCaptureHandlerTest manually.' }
    $runner = $runner.Replace($needle, $needle + "`r`n" + '        test.gui.KingCaptureHandlerTest.main(args);')
    [System.IO.File]::WriteAllText($runnerPath, $runner, [System.Text.UTF8Encoding]::new($false))
    Write-Host 'Registered KingCaptureHandlerTest in TestRunner.'
} else {
    Write-Host 'KingCaptureHandlerTest is already registered.'
}
Write-Host 'Integration applied. Rebuild and run full tests before committing.'
