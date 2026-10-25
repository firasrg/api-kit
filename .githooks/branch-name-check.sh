#!/bin/sh
case "$1" in main|release/*|dependabot/*) exit 0 ;; esac

if ! printf '%s\n' "$1" | grep -Eq '^(feat|fix|docs|config|perf|test|build|ci|chore)/[a-z0-9]+(-[a-z0-9]+)*$'; then
  cat >&2 <<EOF
Invalid branch name '$1'. Expected: <type>/<kebab-case-description>
Example: feat/criteria-specification
EOF
  exit 1
fi