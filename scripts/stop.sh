#!/bin/sh
cat pidfile|xargs kill -9
rm pidfile
