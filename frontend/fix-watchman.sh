#!/bin/sh
. ./vars
exec docker run --rm --privileged danlynn/ember-cli:${image_tag} sysctl -w fs.inotify.max_user_watches=524288
