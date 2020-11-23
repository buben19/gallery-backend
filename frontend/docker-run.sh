#!/bin/sh
exec docker run -ti --rm --user $(id -u) -v $(pwd):/app -p 4200:4200 ng $@
