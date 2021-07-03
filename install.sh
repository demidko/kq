#!/usr/bin/env sh

./gradlew clean build
mv ./build/libs/*-all.jar /usr/local/bin/analyze.jar
echo "java -jar /usr/local/bin/analyze.jar \$@" > /usr/local/bin/analyze
chmod a+x /usr/local/bin/analyze
