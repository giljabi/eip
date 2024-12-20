#!/bin/sh

APP_NAME=eip-1.0.jar

PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')
if [ -z "$PID" ]; then
    echo "Application is not running."
else
    echo "Killing application with PID: $PID"
    kill -9 $PID
    echo "Application terminated."
fi

echo "Start EIP application"
nohup /usr/lib/jvm/java-8-openjdk-amd64/bin/java -jar -Dspring.profiles.active=prod -Xms256m -Xmx256m -Djava.net.preferIPv4Stack=true -Duser.timezone=Asia/Seoul -Djasypt.encryptor.password=암호화키 $APP_NAME > /dev/null 2>&1 &
