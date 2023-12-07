#!/bin/bash

home=/home/smsAP/routineSMS
cd $home

lib=./lib
src=routineSMS
cplst="./"

for entry in "$lib"/*.jar
do
  echo "$entry"
  cplst=$cplst":"$entry
done

echo $cplst

java -cp $cplst $src


log_s_time=$(date +%Y%m%d-%H%M%S)  
log=/home/smsAP/routineSMS/logs/start_smsAP.$(date +%Y%m%d)


ProcNumber=`ps -ef | grep 'smsAP.sh' | grep -v 'grep' | wc -l`
echo "----------------------Start------------------------------------------------" >>$log
echo $log_s_time >>$log
echo "smsAP.sh ProcNumber"= $ProcNumber  >>$log
  
if [ $ProcNumber -eq 0 ];then
     echo "start_smsAP" >>$log
	 /home/smsAP/poolSMS/smsAP.sh &
     #java P34_AdjustMent  >> $log
	 #java P4_To_P4  >> $log
	 #java P4_To_P4  >> $log
   else	 
	 echo Process_Exit >>  $log
  fi

  
echo "------------------------end--------------------------------------------" >>$log



ProcNumber=`ps -ef | grep 'smsDR.sh' | grep -v 'grep' | wc -l`
echo "----------------------Start------------------------------------------------" >>$log
echo $log_s_time >>$log
echo "smsDR.sh ProcNumber"= $ProcNumber  >>$log
  
if [ $ProcNumber -eq 0 ];then
     echo "start_smsDR" >>$log
	 /home/smsAP/poolSMS/smsDR.sh &
     #java P34_AdjustMent  >> $log
	 #java P4_To_P4  >> $log
	 #java P4_To_P4  >> $log
   else	 
	 echo Process_Exit >>  $log
  fi

  
echo "------------------------end--------------------------------------------" >>$log

exit;