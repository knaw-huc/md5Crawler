FROM adoptopenjdk/openjdk8:alpine-jre
COPY out/artifacts/md5Crawler_jar md5Crawler.jar
ENTRYPOINT java -jar md5Crawler.jar -d .

