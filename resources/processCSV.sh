#!/bin/bash

read -p 'Input CSV path: ' inputPath
read -p 'Provide the complete path of the folder of job Descrptions : ' folderPath

IFS=","

PARENT_GROUP_FOLDER=$folderPath/Groups
mkdir -p $PARENT_GROUP_FOLDER


cat $inputPath | while read f1 f2 f3
do
    currentDir=$PARENT_GROUP_FOLDER/$f1
    mkdir -p $currentDir
    for entry in "$folderPath"/*.txt
    do
	if grep -q "$f1" "$entry"; then
	    cp $entry $currentDir
	fi
    done
done

for entry in $PARENT_GROUP_FOLDER/*
do
    echo $entry
    if test "$(ls -A "$entry")"; then
	echo "Not Empty"
    else
	rm -rf $entry
    fi
done


