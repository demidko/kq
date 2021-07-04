#!/usr/bin/env sh

./gradlew clean build
mv ./build/libs/*-all.jar /usr/local/bin/kq.jar
echo "java -jar /usr/local/bin/kq.jar \$@" > /usr/local/bin/kq
chmod a+x /usr/local/bin/kq
