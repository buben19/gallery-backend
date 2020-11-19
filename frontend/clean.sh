#!/bin/sh
echo "Removing temporary files..."
rm -rf bower_components/ node_modules/ dist/ tmp/
echo "Done."
exit $?
