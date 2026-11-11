# Dev 4 Phase 2 M4 automated QA: Java 17-compatible build, regression suite,
# and strict Javadoc. Run in Windows PowerShell from the repository root.
$ErrorActionPreference = 'Stop'
$repo = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
Set-Location $repo

New-Item -ItemType Directory -Force -Path 'out' | Out-Null
$sources = @(Get-ChildItem -Path '.\src','.\test' -Recurse -File -Filter '*.java' | ForEach-Object { $_.FullName })
if ($sources.Count -eq 0) { throw 'No Java source files found.' }

Write-Host "`n[1/3] Java 17-compatible source and test compilation" -ForegroundColor Cyan
& javac --release 17 -Xlint:all -d out $sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed with exit code $LASTEXITCODE." }
Write-Host 'COMPILATION_PASS' -ForegroundColor Green

Write-Host "`n[2/3] Headless regression suite" -ForegroundColor Cyan
$originalJavaOptions = [Environment]::GetEnvironmentVariable('JAVA_TOOL_OPTIONS','Process')
$env:JAVA_TOOL_OPTIONS = '-Djava.awt.headless=true'
try {
    & java -ea -cp out TestRunner | Tee-Object -FilePath 'out/phase2_m4_regression.txt'
    $testExit = $LASTEXITCODE
} finally {
    if ($null -eq $originalJavaOptions) {
        Remove-Item Env:JAVA_TOOL_OPTIONS -ErrorAction SilentlyContinue
    } else {
        $env:JAVA_TOOL_OPTIONS = $originalJavaOptions
    }
}
if ($testExit -ne 0) { throw "Regression suite failed with exit code $testExit." }
Write-Host 'REGRESSION_PASS' -ForegroundColor Green

Write-Host "`n[3/3] Strict Javadoc across gui, board, pieces, core, ui and utils" -ForegroundColor Cyan
$javadoc = $null
$cmd = Get-Command javadoc -ErrorAction SilentlyContinue
if ($null -ne $cmd) { $javadoc = $cmd.Source }
if ($null -eq $javadoc -and $env:JAVA_HOME) {
    $candidate = Join-Path $env:JAVA_HOME 'bin/javadoc.exe'
    if (Test-Path $candidate) { $javadoc = $candidate }
}
if ($null -eq $javadoc) {
    $candidate = 'C:\Program Files\Java\jdk-25\bin\javadoc.exe'
    if (Test-Path $candidate) { $javadoc = $candidate }
}
if ($null -eq $javadoc) { throw 'javadoc not found. Set JAVA_HOME to your JDK installation.' }

& $javadoc -private -Xdoclint:all -Werror -encoding UTF-8 `
    -d out/javadoc -sourcepath src `
    -subpackages 'gui:board:pieces:core:ui:utils' src/Main.java
if ($LASTEXITCODE -ne 0) { throw "Strict Javadoc failed with exit code $LASTEXITCODE." }
if (-not (Test-Path 'out/javadoc/index.html')) { throw 'Javadoc index.html was not generated.' }
Write-Host 'JAVADOC_PASS' -ForegroundColor Green
Write-Host "`nPHASE2_M4_AUTOMATED_QA_PASS" -ForegroundColor Green
Write-Host 'Review out/phase2_m4_regression.txt for counts and open out/javadoc/index.html.'
