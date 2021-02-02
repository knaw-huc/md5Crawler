FROM alpine:3.13.1

RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh \
    openjdk8 maven

RUN mkdir -p /app/dir

WORKDIR /app

RUN git clone https://github.com/knaw-huc/md5Crawler.git
RUN cd  md5Crawler && \
    mvn package && \
    cp target/md5Crawler.jar /app/

ENTRYPOINT ["java","-jar", "md5Crawler.jar"] 

