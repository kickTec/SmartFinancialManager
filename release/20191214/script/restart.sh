#!/bin/sh

pid=`ps -ef|grep "SmartFinancialManager"|grep -v grep|grep -v kill|awk '{print $2}'`
echo pid:$pid
if [ $pid ]; then
        echo 'Start stop process!'
        kill -9 $pid
        echo 'Stop process end!'
fi

sleep 1
echo 'start execute!'
java -jar SmartFinancialManager-0.0.1-SNAPSHOT.jar --spring.config.location=application-prod.properties >/logs/info.log 2>/logs/error.log &
echo 'start success!'

