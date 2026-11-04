# Build and test the Phase 2 code on Windows PowerShell (JDK 17+).
$ErrorActionPreference = 'Stop'
Set-Location (Split-Path -Parent $PSScriptRoot)
New-Item -ItemType Directory -Force -Path 'out' | Out-Null
$sources = @(Get-ChildItem -Path '.\src', '.\test' -Recurse -Filter '*.java' |
    ForEach-Object { $_.FullName })
if ($sources.Count -eq 0) { throw 'No Java sources found.' }
& javac --release 17 -Xlint:all -d out $sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed (exit $LASTEXITCODE)." }
& java '-Djava.awt.headless=true' -ea -cp out TestRunner
if ($LASTEXITCODE -ne 0) { throw "Tests failed (exit $LASTEXITCODE)." }
Write-Host 'Phase 2 M1 Java compilation and tests passed.'
