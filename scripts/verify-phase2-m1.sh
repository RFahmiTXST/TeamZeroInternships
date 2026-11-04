#!/usr/bin/env bash
# Build and test the Phase 2 code in bash (JDK 17+).
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")/.."
mkdir -p out
mapfile -d '' sources < <(find src test -type f -name '*.java' -print0)
if (( ${#sources[@]} == 0 )); then
  echo 'No Java sources found.' >&2
  exit 1
fi
javac --release 17 -Xlint:all -d out "${sources[@]}"
java -Djava.awt.headless=true -ea -cp out TestRunner
echo 'Phase 2 M1 Java compilation and tests passed.'
