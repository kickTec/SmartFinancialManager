#!/bin/sh
JAVA_OPTS="-XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=32m -Xms64m -Xmx64m -Xmn64m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC"

log_dir=/home/kenick/smartFinancial-manager/logs
cd /home/kenick/smartFinancial-manager
echo 'start execute!'
java -jar  SmartFinancialManager-0.0.1-SNAPSHOT.jar --spring.config.location=application-prod.properties >"${log_dir}/info.log" 2>"${log_dir}/error.log"  &
echo 'start execute end!'
