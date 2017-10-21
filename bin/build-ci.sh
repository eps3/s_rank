#!/bin/bash
set -e
CURRENT_FILE_DIR="$(cd "$(dirname "$0")";pwd)"
cd $CURRENT_FILE_DIR && cd ../

function build_ci() {
    if [ $# == 1 ] ; then
       command=$1
    else
        echo "command: build, test, deploy" && exit 1
    fi
    if [[ $command = 'build' ]]; then
        echo "build escaped"
    elif [[ $command = 'test' ]]; then
        sbt test
    elif [[ $command = 'deploy' ]]; then
        sbt dist
    else
        echo 'unrecognized command $command' && exit 1
    fi
}
build_ci $1