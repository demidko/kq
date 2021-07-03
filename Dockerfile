# syntax = docker/dockerfile:experimental
FROM gradle:jdk16 as builder
WORKDIR /project
COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
RUN --mount=type=cache,target=./.gradle gradle clean build

FROM openjdk:17-buster as backend
COPY --from=builder /project/build/libs/*-all.jar /analyze.jar
RUN echo "java -jar /analyze.jar \$@" > /usr/local/bin/analyze
RUN chmod a+x /usr/local/bin/analyze
ENTRYPOINT ["/analyze"]