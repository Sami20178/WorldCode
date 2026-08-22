#!/usr/bin/env bash
set -euo pipefail

ROOT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
cd "$ROOT"

fail() { echo "FEHLER: $*" >&2; exit 1; }

[ -f ./gradlew ] || fail "gradlew wurde nicht gefunden."
[ -d ./app ] || fail "Der app-Ordner wurde nicht gefunden."
command -v java >/dev/null 2>&1 || fail "Java wird benötigt."

chmod +x ./gradlew
rm -rf releases
mkdir -p releases

echo "== WorldCode Direct APK Builder =="
echo "1/3 Projekt bereinigen..."
./gradlew --no-daemon --stacktrace clean

echo "2/3 Debug-APK bauen..."
./gradlew --no-daemon --stacktrace :app:assembleDebug

echo "3/3 APK prüfen und kopieren..."
APK="$(find "$ROOT/app/build/outputs/apk" -type f -name '*.apk' -print -quit 2>/dev/null || true)"
[ -n "$APK" ] || fail "Gradle hat keine APK erzeugt."

cp "$APK" "$ROOT/releases/WorldCode.apk"
[ -s "$ROOT/releases/WorldCode.apk" ] || fail "WorldCode.apk ist leer."

printf '\n========================================\n'
printf 'BUILD ERFOLGREICH\n'
printf 'APK: %s\n' "$ROOT/releases/WorldCode.apk"
printf 'Größe: '
wc -c < "$ROOT/releases/WorldCode.apk"
printf ' Bytes\n'
printf '========================================\n'
