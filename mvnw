#!/bin/sh
# Maven Wrapper script for Unix/Linux/macOS

set -e

MAVEN_WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"
MAVEN_WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"

if [ -f "$MAVEN_WRAPPER_JAR" ]; then
  exec java -jar "$MAVEN_WRAPPER_JAR" "$@"
fi

# Fallback: download mvnw jar if not present
DOWNLOAD_URL=$(grep -E '^wrapperUrl=' "$MAVEN_WRAPPER_PROPERTIES" | cut -d= -f2-)
if [ -n "$DOWNLOAD_URL" ]; then
  echo "Downloading Maven Wrapper..."
  mkdir -p .mvn/wrapper
  if command -v curl > /dev/null; then
    curl -fsSL "$DOWNLOAD_URL" -o "$MAVEN_WRAPPER_JAR"
  elif command -v wget > /dev/null; then
    wget -q "$DOWNLOAD_URL" -O "$MAVEN_WRAPPER_JAR"
  else
    echo "ERROR: Neither curl nor wget found." >&2
    exit 1
  fi
  exec java -jar "$MAVEN_WRAPPER_JAR" "$@"
fi

exec mvn "$@"
