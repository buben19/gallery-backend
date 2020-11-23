#!/bin/sh
exec docker run --rm --privileged ng sysctl -w fs.inotify.max_user_watches=524288
