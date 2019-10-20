#!/bin/sh

tpid1=`ps -ef|grep "SmartFinancialManager"|grep -v grep|grep -v kill|awk '{print $2}'`
echo tpid1-$tpid1
if [ $tpid1 ]; then
        echo 'Start stop process!'
	kill -9 $tpid1
        echo 'Stop process end!'
fi
