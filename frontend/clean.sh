#!/bin/sh
set -e
echo "Removing temporary files..."
rm -rf node_modules dist package-lock.json
echo "Done."
exit $?
