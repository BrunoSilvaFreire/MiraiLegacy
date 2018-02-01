#!/bin/bash

PID=mirai.pid

start() {
    if [ -f ${PID} ]
    then
        echo
        echo "Mirai is started @ $( cat ${PID} )"
    else
        if nohup java -jar mirai.jar 2>&1 &
        then
            pid=$!
            echo "Mirai starting @ $pid"
            echo ${pid} >${PID}
        else
            echo "An error has occoured"
            rm ${PID}
        fi
    fi
}

stop() {
    if [ -f ${PID} ]
    then
        pid=$(cat ${PID})
        kill -9 ${pid}
        rm ${PID}
        echo "Mirai was stopped @ ${pid}!"
    else
        echo "Mirai is not running! Pid file not found"
    fi
}

$1
if [ $? = 127 ]
then
    echo "Error: '$1' is not a known subcommand."
    exit 1
fi