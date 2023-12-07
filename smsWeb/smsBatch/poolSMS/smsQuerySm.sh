#!/bin/bash

#java -jar smsAP.jar &


home=/home/smsAP/poolSMS
cd $home

lib=./smsAP_lib
src=smsQuerySm
cplst="./"

for entry in "$lib"/*.jar
do
  echo "$entry"
  cplst=$cplst":"$entry
done

echo $cplst

java -cp $cplst $src

exit;


exit 0