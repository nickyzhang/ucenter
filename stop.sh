#!/bin/bash

jar='bigdata-gateway-0.0.1-SNAPSHOT.jar'

pidfile=bigdata-gateway.pid

echo 'start to stop app '${jar}

#pids=`ps -ef|grep ${app}|grep -v grep|awk '{print $2}'`
pids=`cat ${pidfile}`

echo 'start to kill process '${pids}

kill -15 ${pids}