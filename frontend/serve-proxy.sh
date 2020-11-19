#!/bin/sh
exec ./ember.sh server --proxy http://172.17.0.1:8080 $@
