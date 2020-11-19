#!/bin/sh
. ./vars
exec docker run -ti --rm --user $(id -u) -v $(pwd):/myapp -p 4200:4200 -p 49152:49152 danlynn/ember-cli:${image_tag} $@
