FROM openjdk:8-jre-alpine3.8

RUN apk --no-cache upgrade


RUN apk add vim
RUN apk add curl
RUN apk add openssl

RUN apk update && \
    apk add --update --no-cache apache-ant bash;

# Setup JAVA_HOME, this is useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
EXPOSE 8084
EXPOSE 8083
EXPOSE 8086
EXPOSE 8125
EXPOSE 6379
EXPOSE 5672

RUN export JAVA_HOME
VOLUME /tmp
ADD target target
WORKDIR /target
COPY target .
ADD entry.sh /target
ENV PATH=/target:$PATH

RUN ["chmod", "+x", "entry.sh"]

ENTRYPOINT exec entry.sh