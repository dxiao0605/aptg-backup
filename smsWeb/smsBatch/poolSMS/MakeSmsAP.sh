#!/bin/bash

lib=./smsAP_lib
src=./smsSend.java
cplst="./"

for entry in "$lib"/*.jar
do
  echo "$entry"
  cplst=$cplst":"$entry
done

echo $cplst

javac -cp $cplst $src


src=./smsDR.java

javac -cp $cplst $src

src=./smsQuerySm.java

javac -cp $cplst $src

exit;