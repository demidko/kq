#/bin/sh

OUT_FILE=/usr/local/bin/est.jar

curl -o $OUT_FILE https://dl.dropboxusercontent.com/u/16254496/est.jar
echo "java -jar /Users/Admin/Projects/Tools/Analyze/build/libs/Analyze-all.jar \$@" > /usr/local/bin/test475
chmod a+x /usr/local/bin/test475