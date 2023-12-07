#!/bin/bash

lib=./lib
src=./routineSMS.java
cplst="./"

for entry in "$lib"/*.jar
do
  echo "$entry"
  cplst=$cplst":"$entry
done

echo $cplst

javac -cp $cplst $src

exit;