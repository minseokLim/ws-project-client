#!/bin/sh

echo "********************************************************"
echo "Starting Client Server Service"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Dspring.profiles.active=$PROFILE \
     -jar /usr/local/clientsvr/@project.build.finalName@.jar
