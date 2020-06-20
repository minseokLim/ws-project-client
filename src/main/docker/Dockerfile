FROM    openjdk:8-jre-alpine
RUN     apk update && apk upgrade && apk add netcat-openbsd && apk add tzdata
RUN     ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN     mkdir -p /usr/local/clientsvr
ADD     @project.build.finalName@.jar /usr/local/clientsvr/
ADD     run.sh run.sh
RUN     chmod +x run.sh
CMD     ./run.sh