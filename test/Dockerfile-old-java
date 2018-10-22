FROM openjdk:8-alpine AS builder
RUN mkdir /src
COPY ./gradlew /src/
COPY ./gradle/ /src/gradle/
COPY ./build.gradle /src/
COPY ./settings.gradle /src/
WORKDIR /src/
RUN ./gradlew
COPY ./src/ /src/src/
RUN ./gradlew shadowJar

FROM ubuntu:bionic
RUN apt-get update && apt-get install -y --allow-downgrades openjdk-8-jre-headless=8u162-b12-1 openjdk-8-jdk-headless=8u162-b12-1
COPY --from=builder /src/build/libs/jvm-timezone-dst-all.jar /jvm-timezone-dst-all.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-cp","/jvm-timezone-dst-all.jar","gkawamoto.tests.TimezoneDST"]
