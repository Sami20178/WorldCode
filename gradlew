#!/bin/sh

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)

if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

if ! command -v "$JAVACMD" >/dev/null 2>&1; then
  echo "ERROR: JAVA_HOME is not set and no 'java' command could be found." >&2
  exit 1
fi

GRADLE_VERSION=8.11.1
DIST="$HOME/.gradle/wrapper/dists/gradle-$GRADLE_VERSION-bin"
ZIP="$HOME/.gradle/wrapper/dists/gradle-$GRADLE_VERSION-bin.zip"
URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$DIST/gradle-$GRADLE_VERSION/bin/gradle" ]; then
  mkdir -p "$DIST"
  if command -v curl >/dev/null 2>&1; then curl -fsSL "$URL" -o "$ZIP"; else wget -q "$URL" -O "$ZIP"; fi
  unzip -q -o "$ZIP" -d "$DIST"
fi

exec "$DIST/gradle-$GRADLE_VERSION/bin/gradle" "$@"
