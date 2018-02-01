#!/bin/bash
if [ -f $1 ]
then
    rm -rf $1
fi

mkdir $1
mkdir $1/plugins
echo "Copying files to $1"

cp mirai.sh $1/mirai.sh
cp output/bot/libs/bot-all.jar $1/mirai.jar

for D in output/plugins/*; do
    if [ -d "${D}" ]; then
        projectName=${D#output/plugins/}
        fileName=${projectName}-all.jar
        filePath="output/plugins/${projectName}/libs/${fileName}"
        if [ ! -f ${filePath} ]
        then
            fileName=${projectName}.jar
             filePath="output/plugins/${projectName}/libs/${fileName}"
        fi
        echo "Copying plugin ${projectName} with file ${fileName}"
        cp  ${filePath} "$1/plugins/${projectName}.jar"  # your processing here
    fi
done
chmod u+x $1/mirai.sh
(cd $1; exec $1/mirai.sh start)