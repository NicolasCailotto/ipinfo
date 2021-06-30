#pull base image
FROM openjdk:8-jdk-alpine

#maintainer
MAINTAINER cailotto.nicolas@gmail.com

#expose port 8080
EXPOSE 8080

COPY config/applicationDocker.properties ./config/application.properties

#default command
CMD java -jar ipinfo-api.jar

#copy .jar to docker image
ADD target/ipinfo-*.jar ipinfo-api.jar