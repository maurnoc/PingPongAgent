#!/bin/bash
javac MainAgent.java
java MainAgent

if [ $# -eq 1 ]

then
    java -cp bin MainAgent $1  #~/Documents/Agent/bin/MainAgent $1

else
    echo "Error"
    exit 1

fi
