#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/app/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "$(date '+%Y-%m-%d %H:%M:%S') > build 파일명: $JAR_NAME" >> /home/ubuntu/app/deploy.log

echo "$(date '+%Y-%m-%d %H:%M:%S') > build 파일 복사" >> /home/ubuntu/app/deploy.log
DEPLOY_PATH=/home/ubuntu/app/
cp $BUILD_JAR $DEPLOY_PATH

echo "$(date '+%Y-%m-%d %H:%M:%S') > 현재 실행 중인 애플리케이션 pid 확인" >> /home/ubuntu/app/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "$(date '+%Y-%m-%d %H:%M:%S') > 현재 구동 중인 애플리케이션이 없습니다." >> /home/ubuntu/app/deploy.log
else
  echo "$(date '+%Y-%m-%d %H:%M:%S') > kill -15 $CURRENT_PID" >> /home/ubuntu/app/deploy.log
  kill -9 $CURRENT_PID
  sleep 10
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "$(date '+%Y-%m-%d %H:%M:%S') > JAR 실행" >> /home/ubuntu/app/deploy.log
nohup java -Duser.timezone=Asia/Seoul -jar $DEPLOY_JAR >> /home/ubuntu/deploy.log 2>/home/ubuntu/app/deploy_err.log &
