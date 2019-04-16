FROM openjdk:11
MAINTAINER shaoze.wang shaoze.wang@BKJK.COM
WORKDIR /opt
ADD ./target/influx-proxy.jar /opt/app.jar
EXPOSE 8080
EXPOSE 8081
CMD ["/bin/bash", "-c", "java ${JAVA_OPTS} -jar /opt/app.jar"]