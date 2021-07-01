# syntax = docker/dockerfile:experimental
FROM gradle:jdk16 as builder
WORKDIR /project
COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
RUN --mount=type=cache,target=./.gradle gradle clean build

FROM openjdk:17-buster as backend
WORKDIR /opt
COPY --from=builder /project/build/libs/*-all.jar /analyze.jar
ENTRYPOINT ["java", "-jar", "/analyze.jar"]