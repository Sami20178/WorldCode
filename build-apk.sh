#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

if [ ! -f ./gradlew ]; then
  echo "FEHLER: gradlew wurde nicht gefunden."
  exit 1
fi

chmod +x ./gradlew

echo "== WorldCode APK Builder =="
echo "Bereinige Projekt..."
./gradlew --no-daemon clean

echo "Baue APK..."
./gradlew --no-daemon :app:assembleDebug

APK="$(find app/build/outputs/apk -type f -name '*.apk' | head -n 1)"
if [ -z "$APK" ]; then
  echo "FEHLER: Keine APK wurde erstellt."
  exit 1
fi

mkdir -p releases
cp "$APK" releases/WorldCode.apk

echo ""
echo "========================================"
echo "APK erfolgreich erstellt!"
echo "releases/WorldCode.apk"
echo "========================================"
