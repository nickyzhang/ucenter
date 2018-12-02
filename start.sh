#!/bin/bash

jar='bigdata-gateway-0.0.1-SNAPSHOT.jar'

pidfile=bigdata-gateway.pid

echo 'start to start app '${jar}

# nohup java -cp .:${jar} -Dtest=true ${app} 2>&1 >/dev/null & echo $! > app.pid

nohup java -server -Dfile.encoding=UTF-8 -Xmx2048m -Xms512m -Xmn512m -XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCApplicationConcurrentTime -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -Xloggc:log/gc.log -XX:-OmitStackTraceInFastThrow -jar ${jar} 2>&1 >/dev/null & echo $! > ${pidfile}